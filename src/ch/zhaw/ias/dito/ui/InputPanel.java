package ch.zhaw.ias.dito.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JCheckBox;
import javax.swing.JFileChooser;

import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXTextField;
import org.netbeans.validation.api.builtin.Validators;
import org.netbeans.validation.api.ui.ValidationGroup;

import ch.zhaw.ias.dito.config.ConfigProperty;
import ch.zhaw.ias.dito.config.DitoConfiguration;
import ch.zhaw.ias.dito.config.Input;
import ch.zhaw.ias.dito.config.PropertyGuardian;
import ch.zhaw.ias.dito.ui.resource.Translation;
import ch.zhaw.ias.dito.ui.util.ExtensionFileFilter;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.debug.FormDebugPanel;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class InputPanel extends DitoPanel implements ActionListener {
  private JXTextField filePath = new JXTextField();
  private JXButton browseButton;
  private JXTextField separator = new JXTextField();
  private JXTextField surveyFrom = new JXTextField();
  private JXTextField surveyTo = new JXTextField();
  private JCheckBox columnTitles = new JCheckBox(Translation.INSTANCE.get("s1.cb.firstRow"));
  private JCheckBox allSurveys = new JCheckBox(Translation.INSTANCE.get("s1.cb.allSurveys"));
  
  public InputPanel(ValidationGroup validationGroup) {
    super(ScreenEnum.INPUT, null, ScreenEnum.QUESTION, validationGroup);
    
    FormLayout layout = new FormLayout("max(50dlu; pref), 5dlu, max(100dlu; pref), 5dlu, max(50dlu; pref), 5dlu, max(100dlu; pref), 5dlu, max(50dlu; pref)", 
    "pref, 2dlu, pref, 2dlu, pref, 2dlu, pref, 10dlu, pref, 2dlu, pref, 2dlu, pref, 2dlu, pref");
    //layout.setRowGroups(new int[][]{{2, 4, 6}}); 
    CellConstraints cc = new CellConstraints();
    DefaultFormBuilder fb = new DefaultFormBuilder(layout, Translation.INSTANCE.getBundle());
    
    browseButton = new JXButton(Translation.INSTANCE.get("s1.bu.browse"));
    filePath.setName(Translation.INSTANCE.get("s1.lb.file"));

    fb.addI15dSeparator("s1.title.file", cc.xyw(1, 1, 9));
    fb.addI15dLabel("s1.lb.file", cc.xy(1, 3));
    fb.add(filePath, cc.xyw(3, 3, 5));
    fb.add(browseButton, cc.xy(9, 3));
    fb.addI15dLabel("s1.lb.separator", cc.xy(1, 5));
    fb.add(separator, cc.xy(7, 5));
    fb.add(columnTitles, cc.xyw(3, 7, 5));
    
    fb.addI15dSeparator("s1.title.data", cc.xyw(1, 9, 9));
    fb.addI15dLabel("s1.lb.survey", cc.xy(1, 11));
    fb.add(surveyFrom, cc.xy(3, 11));
    fb.addI15dLabel("s1.lb.to", cc.xy(5, 11));
    fb.add(surveyTo, cc.xy(7, 11));
    fb.add(allSurveys, cc.xy(3, 13));
    fb.addI15dLabel("s1.lb.question", cc.xy(1, 15));    
    
    validationGroup.add(filePath, Validators.FILE_MUST_BE_FILE, Validators.FILE_MUST_EXIST);
    validationGroup.add(separator, Validators.REQUIRE_NON_EMPTY_STRING);
    Input i = Config.INSTANCE.getDitoConfig().getInput();
    filePath.setText(i.getFilename());
    separator.setText(Character.toString(i.getSeparator()));
    
    browseButton.addActionListener(this);
    this.setLayout(new BorderLayout());
    this.add(fb.getPanel(), BorderLayout.CENTER);
  }
  
  @Override
  public void saveToModel() {
    Input i = Config.INSTANCE.getDitoConfig().getInput();
    i.setFilename(filePath.getText());
    i.setSeparator(separator.getText().charAt(0));
    
    PropertyGuardian guardian = Config.INSTANCE.getPropertyGuardian();
    guardian.propertyChanged(ConfigProperty.INPUT_FILENAME, "", i.getFile().getName());
    guardian.propertyChanged(ConfigProperty.INPUT_SEPARATOR, "", i.getSeparator());
  }
  
  @Override
  public void actionPerformed(ActionEvent e) {
    JFileChooser fileChooser = new JFileChooser(filePath.getText());
    fileChooser.setFileFilter(ExtensionFileFilter.CSV);
    int returnVal = fileChooser.showOpenDialog(this);
    
    if (returnVal == JFileChooser.APPROVE_OPTION) {
        File file = fileChooser.getSelectedFile();
        filePath.setText(file.getAbsolutePath());
    }
  }
}
