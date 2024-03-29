/* released under bsd licence
 * see LICENCE file or http://www.opensource.org/licenses/bsd-license.php for details
 * Institute of Applied Simulation (ZHAW)
 * Author Thomas Niederberger
 */
package ch.zhaw.ias.dito.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.table.AbstractTableModel;

import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.netbeans.validation.api.ui.ValidationGroup;

import ch.zhaw.ias.dito.QuestionType;
import ch.zhaw.ias.dito.Utils;
import ch.zhaw.ias.dito.config.Question;
import ch.zhaw.ias.dito.config.QuestionConfig;
import ch.zhaw.ias.dito.config.TableColumn;
import ch.zhaw.ias.dito.ui.resource.AppConfig;
import ch.zhaw.ias.dito.ui.resource.Translation;
import ch.zhaw.ias.dito.ui.util.BlockPlotPanel;
import ch.zhaw.ias.dito.ui.util.HelpArea;
import ch.zhaw.ias.dito.ui.util.HistogramFrame;
import ch.zhaw.ias.dito.ui.util.NumberComparator;

public class QuestionPanel extends DitoPanel implements ActionListener {
  private JCheckBox activateScale = new JCheckBox(Translation.INSTANCE.get("s2.cb.activateScale"));
  private JCheckBox activateAutoscale = new JCheckBox(Translation.INSTANCE.get("s2.cb.activateAutoscale"));
  private JCheckBox activateDistanceWeight = new JCheckBox(Translation.INSTANCE.get("s2.cb.activateDistanceWeight"));
  private JCheckBox activateQuestionWeight = new JCheckBox(Translation.INSTANCE.get("s2.cb.activateQuestionWeight"));
  private JXTable table;
  private JScrollPane sp;
  private List<Question> questions;
  private JButton correlationButton;
  
  public QuestionPanel(HelpArea helpArea) {
    super(ScreenEnum.QUESTION, ScreenEnum.INPUT, ScreenEnum.METHOD);
    questions = Config.INSTANCE.getDitoConfig().getQuestions();
    QuestionTableModel model = new QuestionTableModel(questions);
    
    table = new JXTable(model);
    table.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
          JXTable target = (JXTable)e.getSource();
          int viewIndex = target.getSelectedRow();
          int modelIndex = table.convertRowIndexToModel(viewIndex);
          Question q = questions.get(modelIndex);
          new HistogramFrame(q);
        }      
      }
    });
    table.setColumnControlVisible(true);
    for (TableColumn col : TableColumn.values()) {
      table.packColumn(col.getId(), 5, 100);
    }
    
    NumberComparator comparator = new NumberComparator();
    table.getColumnExt(TableColumn.DISTANCE_WEIGHT.getId()).setComparator(comparator);
    table.getColumnExt(TableColumn.MAX.getId()).setComparator(comparator);
    table.getColumnExt(TableColumn.MIN.getId()).setComparator(comparator);
    table.getColumnExt(TableColumn.NUMBER.getId()).setComparator(comparator);
    table.getColumnExt(TableColumn.SCALING.getId()).setComparator(comparator);
    table.getColumnExt(TableColumn.QUESTION_WEIGHT.getId()).setComparator(comparator);

    JComboBox comboBox = new JComboBox();
    comboBox.addItem(QuestionType.NOMINAL);
    comboBox.addItem(QuestionType.ORDINAL);
    comboBox.addItem(QuestionType.METRIC);
    comboBox.addItem(QuestionType.BINARY);
    table.setDefaultEditor(QuestionType.class, new DefaultCellEditor(comboBox));

    sp = new JScrollPane(table);
    this.setLayout(new BorderLayout());
    
    table.addHighlighter(new ColorHighlighter(HighlightPredicate.EDITABLE, AppConfig.ACTIVE, Color.BLACK));
    table.addHighlighter(new ColorHighlighter(new HighlightPredicate() {
      
      @Override
      public boolean isHighlighted(Component arg0, ComponentAdapter ca) {
        Question q = questions.get(ca.row);
        return q.getData().isConstant();
      }
    }, Color.ORANGE, Color.BLACK, Color.ORANGE, Color.BLACK));

    updateCheckboxes();
    
    JXPanel checkboxPanel = new JXPanel();
    BoxLayout bl = new BoxLayout(checkboxPanel, BoxLayout.Y_AXIS);
    checkboxPanel.setLayout(bl);
    checkboxPanel.add(activateScale);
    checkboxPanel.add(activateAutoscale);
    checkboxPanel.add(activateDistanceWeight);
    checkboxPanel.add(activateQuestionWeight);
    correlationButton = new JButton(Translation.INSTANCE.get("s2.bu.correlationCoef"));
    correlationButton.addActionListener(this);
    checkboxPanel.add(correlationButton);
    this.add(sp, BorderLayout.CENTER);
    this.add(checkboxPanel, BorderLayout.SOUTH);
  }
  
  private void updateCheckboxes() {
    QuestionConfig qc = Config.INSTANCE.getDitoConfig().getQuestionConfig();
    activateScale.setSelected(qc.isEnableScale());
    activateScale.addActionListener(this);
    activateAutoscale.setSelected(qc.isEnableAutoScale());
    activateAutoscale.addActionListener(this);
    activateDistanceWeight.setSelected(qc.isEnableDistanceWeight());
    activateDistanceWeight.addActionListener(this);
    activateQuestionWeight.setSelected(qc.isEnableQuestionWeight());
    activateQuestionWeight.addActionListener(this);
    activateAutoscale.setEnabled(activateScale.isSelected());
  }

  class QuestionTableModel extends AbstractTableModel {
    private List<Question> questions;
    public QuestionTableModel(List<Question> questions) {
      this.questions = questions;
    }
    
    @Override
    public int getColumnCount() {
      return TableColumn.values().length;
    }
    
    @Override
    public int getRowCount() {
      return questions.size();
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex) {
      TableColumn column = TableColumn.getById(columnIndex);
      if (column == TableColumn.TYPE) {
        return QuestionType.class;
      } else {
        return super.getColumnClass(columnIndex);
      }
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
      Question q = questions.get(rowIndex);
      TableColumn column = TableColumn.getById(columnIndex);
      Object value = q.getValue(column);
      return value;
    }
    
    @Override
    public String getColumnName(int column) {
      return Translation.INSTANCE.get("s2.title." + column);
    }
            
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
      TableColumn column = TableColumn.getById(columnIndex);
      if (column == TableColumn.DISTANCE_WEIGHT) {
        return column.isEditable() && activateDistanceWeight.isSelected();  
      }
      if (column == TableColumn.QUESTION_WEIGHT) {
        return column.isEditable() && activateQuestionWeight.isSelected();  
      }
      if (column == TableColumn.SCALING) {
        return column.isEditable() && activateScale.isSelected() && (!activateAutoscale.isSelected());  
      }
      if (column == TableColumn.EXCLUDE) {
        Question q = questions.get(rowIndex);
        QuestionType type = q.getQuestionType();
        return column.isEditable() && type != QuestionType.BINARY;
      }
      if (column == TableColumn.TYPE) {
        Question q = questions.get(rowIndex);
        QuestionType type = (QuestionType) q.getValue(column);
        return type == QuestionType.NOMINAL || type == QuestionType.ORDINAL;
      }
      return column.isEditable();
    }
    
    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
      Question q = questions.get(rowIndex);
      TableColumn column = TableColumn.getById(columnIndex);
      if (column == TableColumn.EXCLUDE) {
        double[] values = Utils.splitToDouble(value.toString());
        q.setValue(column, values);
      } else {
        q.setValue(column, value);
      }
    }
  }
  
  @Override
  public void saveToModel() {
    QuestionConfig qc = Config.INSTANCE.getDitoConfig().getQuestionConfig();
    qc.setEnableScale(activateScale.isSelected());
    qc.setEnableAutoScale(activateAutoscale.isSelected());
    qc.setEnableDistanceWeight(activateDistanceWeight.isSelected());
    qc.setEnableQuestionWeight(activateQuestionWeight.isSelected());
  }
  
  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == correlationButton) {
      BlockPlotPanel blockPanel = new BlockPlotPanel(Config.INSTANCE.getDitoConfig().getData().correlationCoeffs(), -1.0, 1.0);
      JFrame frame = new JFrame(Translation.INSTANCE.get("s2.title.correlationCoef"));
      frame.add(blockPanel);
      frame.setSize(400, 400);
      frame.setVisible(true);
    } else {
      activateAutoscale.setEnabled(activateScale.isSelected());
      table.updateUI();
    }
  }
}
