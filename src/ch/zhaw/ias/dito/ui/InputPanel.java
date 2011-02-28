package ch.zhaw.ias.dito.ui;

import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXTextField;
import org.netbeans.validation.api.builtin.Validators;
import org.netbeans.validation.api.ui.ValidationGroup;

import ch.zhaw.ias.dito.config.Input;
import ch.zhaw.ias.dito.ui.resource.Translation;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.debug.FormDebugPanel;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class InputPanel extends DitoPanel {
  private JXTextField filePath = new JXTextField();
  private JXButton browseButton;
  private JXTextField separator = new JXTextField();
  private JXTextField questions = new JXTextField();
  
  public InputPanel(ValidationGroup validationGroup) {
    super(ScreenEnum.INPUT, null, ScreenEnum.QUESTION, validationGroup);
    FormLayout layout = new FormLayout("pref, 4dlu, 50dlu, 4dlu, min", 
    "pref, pref, 10px, pref, 10px, pref, 10px, pref, 10px, pref");
    layout.setRowGroups(new int[][]{{2, 4, 6}}); 
    CellConstraints cc = new CellConstraints();
    DefaultFormBuilder fb = new DefaultFormBuilder(layout, Translation.INSTANCE.getBundle(), new FormDebugPanel());
    //fb.setDefaultDialogBorder();
    
    fb.appendI15dSeparator("s1.lb.file");
    browseButton = new JXButton(Translation.INSTANCE.get("s1.bu.browse"));
    filePath.setName(Translation.INSTANCE.get("s1.lb.file"));
    fb.appendI15d("s1.lb.file", filePath, browseButton);
    //fb.add();
    fb.nextLine();
    fb.nextLine();
    fb.appendI15d("s1.lb.separator", separator);
    fb.nextLine();
    fb.nextLine();
    fb.appendI15d("s1.lb.question", questions);
    fb.nextLine();
    
    validationGroup.add(filePath, Validators.FILE_MUST_BE_FILE, Validators.FILE_MUST_EXIST);
    validationGroup.add(separator, Validators.REQUIRE_NON_EMPTY_STRING);
    Input i = Config.INSTANCE.getDitoConfig().getInput();
    filePath.setText(i.getFilename());
    separator.setText(Character.toString(i.getSeparator()));
    
    this.add(fb.getPanel());
  }
}
