package ch.zhaw.ias.dito.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;

import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTextField;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import ch.zhaw.ias.dito.config.ConfigProperty;
import ch.zhaw.ias.dito.config.Input;
import ch.zhaw.ias.dito.config.PropertyGuardian;
import ch.zhaw.ias.dito.ui.resource.Translation;
import ch.zhaw.ias.dito.ui.util.ExtensionFileFilter;
import ch.zhaw.ias.dito.ui.util.HelpArea;
import ch.zhaw.ias.dito.ui.util.HelpLabel;
import ch.zhaw.ias.dito.ui.util.RangeSlider;
import ch.zhaw.ias.dito.util.Logger;
import ch.zhaw.ias.dito.util.Logger.LogLevel;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class InputPanel extends DitoPanel implements ActionListener, ChangeListener {
  private JXTextField filePath = new JXTextField();
  private JXButton browseButton;
  private JXTextField separator = new JXTextField();
  private JCheckBox columnTitles = new JCheckBox(Translation.INSTANCE.get("s1.cb.firstRow"));
  private JCheckBox allSurveys = new JCheckBox(Translation.INSTANCE.get("s1.cb.allSurveys"));
  private JCheckBox allQuestions = new JCheckBox(Translation.INSTANCE.get("s1.cb.allQuestions"));
  private JSpinner columnMinSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 1, 1));
  private JSpinner columnMaxSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 1, 1));
  private RangeSlider columnSlider = new RangeSlider();
  private JSpinner rowMinSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 1, 1));
  private JSpinner rowMaxSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 1, 1));  
  private RangeSlider rowSlider = new RangeSlider();
  
  private List<List<String>> data = new ArrayList<List<String>>();
  private SimpleTableModel tableModel = new SimpleTableModel();
  private JXTable visualTable;
  
  public InputPanel(HelpArea helpArea) {
    super(ScreenEnum.INPUT, null, ScreenEnum.QUESTION);
    
    FormLayout layout = new FormLayout("max(30dlu; pref), 30dlu, 5dlu, max(150dlu; pref), 5dlu, max(100dlu; pref), 5dlu, max(50dlu; pref), fill:0:g", 
    "pref, 2dlu, pref, 2dlu, pref, 10dlu, pref, 2dlu, 20dlu, 2dlu, fill:pref:grow, 2dlu, pref, 2dlu, pref, 2dlu, pref");
    //layout.setRowGroups(new int[][]{{2, 4, 6}}); 
    CellConstraints cc = new CellConstraints();
    DefaultFormBuilder fb = new DefaultFormBuilder(layout, Translation.INSTANCE.getBundle());
    
    browseButton = new JXButton(Translation.INSTANCE.get("s1.bu.browse"));
    filePath.setName(Translation.INSTANCE.get("s1.lb.file"));

    fb.addI15dSeparator("s1.title.file", cc.xyw(1, 1, 8));
    fb.add(new HelpLabel(helpArea, "s1.lb.file"), cc.xyw(1, 3, 2));
    fb.add(filePath, cc.xyw(4, 3, 3));
    fb.add(browseButton, cc.xy(8, 3));
    fb.add(new HelpLabel(helpArea, "s1.lb.separator"), cc.xyw(1, 5, 2));
    //fb.addI15dLabel("s1.lb.separator", cc.xyw(1, 5, 2));
    fb.add(separator, cc.xy(8, 5));
    
    fb.addI15dSeparator("s1.title.data", cc.xyw(1, 7, 8));

    visualTable = new JXTable(tableModel);
    visualTable.setSortable(false);
    //visualTable.setHorizontalScrollEnabled(true);
    visualTable.getTableHeader().setReorderingAllowed(false);
    visualTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    visualTable.addHighlighter(new ColorHighlighter(new HighlightPredicate() {
      
      @Override
      public boolean isHighlighted(Component renderer, ComponentAdapter adapter) {
        return isColumnSelected(adapter.column) && isRowSelected(adapter.row);
      }
    }, Color.LIGHT_GRAY, Color.BLACK, Color.LIGHT_GRAY, Color.BLACK));
    JScrollPane scrollPane = new JScrollPane(visualTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    rowSlider.setInverted(true);
    rowSlider.setOrientation(JSlider.VERTICAL);
    columnSlider.addChangeListener(this);
    rowSlider.addChangeListener(this);
    columnSlider.setMinimum(1);
    rowSlider.setMinimum(1);
    
    columnMinSpinner.addChangeListener(this);
    columnMaxSpinner.addChangeListener(this);
    rowMinSpinner.addChangeListener(this);
    rowMaxSpinner.addChangeListener(this);
    separator.addFocusListener(new FocusAdapter() {
      @Override
      public void focusLost(FocusEvent e) {
        updateTable();
      }
    });
    filePath.addFocusListener(new FocusAdapter() {
      @Override
      public void focusLost(FocusEvent e) {
        updateTable();
      }
    });    
    
    JPanel columnSliderPanel = new JPanel();
    columnSliderPanel.setLayout(new BorderLayout());
    columnMinSpinner.setPreferredSize(new Dimension(50, (int) columnMinSpinner.getPreferredSize().getHeight()));
    columnMaxSpinner.setPreferredSize(new Dimension(50, (int) columnMaxSpinner.getPreferredSize().getHeight()));
    columnSliderPanel.add(columnMinSpinner, BorderLayout.WEST);
    columnSliderPanel.add(columnSlider, BorderLayout.CENTER);
    columnSliderPanel.add(columnMaxSpinner, BorderLayout.EAST);
    fb.add(columnSliderPanel, cc.xyw(4, 9, 6));
    
    JPanel rowSliderPanel = new JPanel();
    rowSliderPanel.setLayout(new BorderLayout());
    rowSliderPanel.add(rowMinSpinner, BorderLayout.NORTH);
    rowSliderPanel.add(rowSlider, BorderLayout.CENTER);
    rowSliderPanel.add(rowMaxSpinner, BorderLayout.SOUTH);    
    fb.add(rowSliderPanel, cc.xyw(2, 11, 1));

    //set preferred size explicitly, otherwise the preferred height is too big and the screen doesn't scale its size in an appropriate way
    scrollPane.setPreferredSize(new Dimension(scrollPane.getWidth(), 100));
    fb.add(scrollPane, cc.xyw(4, 11, 6));
    fb.add(allSurveys, cc.xy(4, 13));
    fb.add(allQuestions, cc.xy(4, 15));
    fb.add(columnTitles, cc.xy(4, 17));
    
    //validationGroup.add(filePath, Validators.FILE_MUST_BE_FILE, Validators.FILE_MUST_EXIST);
    //validationGroup.add(separator, Validators.REQUIRE_NON_EMPTY_STRING);
    
    Input i = Config.INSTANCE.getDitoConfig().getInput();
    filePath.setText(i.getFilename());
    separator.setText(Character.toString(i.getSeparator()));
    allQuestions.addChangeListener(this);
    allSurveys.addChangeListener(this);
    columnTitles.addChangeListener(this);
    allQuestions.setSelected(i.isAllQuestions());
    allSurveys.setSelected(i.isAllSurveys());
    columnTitles.setSelected(i.isQuestionTitles());
    
    browseButton.addActionListener(this);
    this.setLayout(new BorderLayout());
    this.add(fb.getPanel(), BorderLayout.CENTER);
    updateTable();
    columnSlider.setValue(i.getStartQuestion());
    columnSlider.setUpperValue(i.getEndQuestion());
    rowSlider.setValue(i.getStartSurvey());
    rowSlider.setUpperValue(i.getEndSurvey());    
  }
  
  private boolean isColumnSelected(int col) {
    if (allQuestions.isSelected()) {
      return true;
    } else {
      return (columnSlider.getValue() - 1) <= col && col <= (columnSlider.getUpperValue() - 1);
    }
  }
  
  private boolean isRowSelected(int row) {
    if (allSurveys.isSelected()) {
      return true;
    } else {
      return (rowSlider.getValue() - 1) <= row && row <= (rowSlider.getUpperValue() - 1);
    }
  }
  
  @Override
  public void saveToModel() {
    Input i = Config.INSTANCE.getDitoConfig().getInput();
    i.setFilename(filePath.getText());
    i.setSeparator(separator.getText().charAt(0));
    i.setStartQuestion(columnSlider.getValue());
    i.setEndQuestion(columnSlider.getUpperValue());
    i.setStartSurvey(rowSlider.getValue());
    i.setEndSurvey(rowSlider.getUpperValue());
    i.setAllQuestions(allQuestions.isSelected());
    i.setAllSurveys(allSurveys.isSelected());
    i.setQuestionTitles(columnTitles.isSelected());
    
    PropertyGuardian guardian = Config.INSTANCE.getPropertyGuardian();
    guardian.propertyChanged(ConfigProperty.INPUT_FILENAME);
  }
  
  @Override
  public void actionPerformed(ActionEvent e) {
    JFileChooser fileChooser = new JFileChooser(filePath.getText());
    fileChooser.setFileFilter(ExtensionFileFilter.CSV_OR_TXT);
    int returnVal = fileChooser.showOpenDialog(this);
    
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      File file = fileChooser.getSelectedFile();
      filePath.setText(file.getAbsolutePath());
      updateTable();
    }
  }
  
  private void updateTable() {
    data.clear();
    File f = new File(filePath.getText());
    if (f.isDirectory() || !f.exists()) {
      return;
    }
    Logger.INSTANCE.log("Start reading file", LogLevel.INFORMATION, true);    
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new FileReader(f));
      Pattern pattern = Pattern.compile(Character.toString(separator.getText().charAt(0)));
      String line;
      while ((line = reader.readLine()) != null) {
        Scanner s = new Scanner(line);
        s.useDelimiter(pattern);
        List<String> values = new ArrayList<String>();
        while (s.hasNext()) {
          values.add(s.next());
        }
        data.add(values);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e) {
          // stupid java error handling. no one knows what to do here!
          e.printStackTrace();
        }
      }
    }
        
    tableModel.fireTableStructureChanged();
    visualTable.packAll();
    
    columnSlider.setMaximum(tableModel.getColumnCount());
    ((SpinnerNumberModel) columnMaxSpinner.getModel()).setMaximum(tableModel.getColumnCount());
    updateRowMaximum();
    
    columnSlider.setValue(1);
    rowSlider.setValue(1);
    rowSlider.setUpperValue(tableModel.getRowCount());
    columnSlider.setUpperValue(tableModel.getColumnCount());
    
    Logger.INSTANCE.log("finished reading file", LogLevel.INFORMATION, false);
  }

  private void updateRowMaximum() {
    rowSlider.setMaximum(tableModel.getRowCount());
    ((SpinnerNumberModel) rowMaxSpinner.getModel()).setMaximum(tableModel.getRowCount());
  }

  private void synchronizeSpinnersSliders(boolean sliderChanged) {
    if (sliderChanged) {
      columnMinSpinner.setValue(columnSlider.getValue());
      columnMaxSpinner.setValue(columnSlider.getUpperValue());
      rowMinSpinner.setValue(rowSlider.getValue());
      rowMaxSpinner.setValue(rowSlider.getUpperValue());
    } else {
      columnSlider.setValue((Integer) columnMinSpinner.getValue());
      columnSlider.setUpperValue((Integer) columnMaxSpinner.getValue());
      rowSlider.setValue((Integer) rowMinSpinner.getValue());
      rowSlider.setUpperValue((Integer) rowMaxSpinner.getValue());
    }
  }
  
  private void updateMinMaxSpinners() {
    ((SpinnerNumberModel) columnMinSpinner.getModel()).setMaximum((Integer) columnMaxSpinner.getValue());
    ((SpinnerNumberModel) columnMaxSpinner.getModel()).setMinimum((Integer) columnMinSpinner.getValue());

    ((SpinnerNumberModel) rowMinSpinner.getModel()).setMaximum((Integer) rowMaxSpinner.getValue());
    ((SpinnerNumberModel) rowMaxSpinner.getModel()).setMinimum((Integer) rowMinSpinner.getValue());
  }
  
  @Override
  public void stateChanged(ChangeEvent e) {
    if (e.getSource() == columnSlider || e.getSource() == rowSlider) {
      synchronizeSpinnersSliders(true);
    } else if (e.getSource() == allQuestions || e.getSource() == allSurveys) {
      activateSliders();
    } else if (e.getSource() == columnTitles) {
      updateRowMaximum(); 
      tableModel.fireTableStructureChanged();
    } else {
      synchronizeSpinnersSliders(false);
    }
    updateMinMaxSpinners();
    tableModel.fireTableDataChanged();
  }
  
  private void activateSliders() {
    columnMinSpinner.setEnabled(!allQuestions.isSelected());
    columnMaxSpinner.setEnabled(!allQuestions.isSelected());
    columnSlider.setEnabled(!allQuestions.isSelected());
    rowMinSpinner.setEnabled(!allSurveys.isSelected());
    rowMaxSpinner.setEnabled(!allSurveys.isSelected());
    rowSlider.setEnabled(!allSurveys.isSelected());
  }

  private class SimpleTableModel extends AbstractTableModel {
    
    @Override
    public int getColumnCount() {
      if (data.size() > 0) {
        return data.get(0).size();
      } else {
        return 0;
      }
    }
    
    @Override
    public int getRowCount() {
      if (columnTitles.isSelected()) {
        return data.size() - 1;
      } else {
        return data.size();  
      }
    }
        
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
      if (columnTitles.isSelected()) {
        return data.get(rowIndex + 1).get(columnIndex);
      } else {
        return data.get(rowIndex).get(columnIndex);  
      }
    }
    
    @Override
    public String getColumnName(int columnIndex) {
      if (columnTitles.isSelected()) {
        return data.get(0).get(columnIndex); 
      } else {
        return "" + columnIndex;
      }
    }
  }
}
