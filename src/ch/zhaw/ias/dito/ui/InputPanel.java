package ch.zhaw.ias.dito.ui;

import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTextField;

public class InputPanel extends DitoPanel {
  private JXTextField filePath = new JXTextField();
  private JXTextField separator = new JXTextField();
  private JXTextField questions = new JXTextField();
  public InputPanel() {
    super(ScreenEnum.INPUT, null, ScreenEnum.QUESTION);
    /*FormLayout layout = new FormLayout("pref, 4dlu, 50dlu, 4dlu, min", 
    "pref, pref, 10px, pref, 10px, pref, 10px, pref, 10px, pref");
    layout.setRowGroups(new int[][]{{1, 3, 5}}); 
    ResourceBundle bundle = ResourceBundle.getBundle("ch.zhaw.ias.dito.ui.resource.uiTexts");
    CellConstraints cc = new CellConstraints();
    DefaultFormBuilder fb = new DefaultFormBuilder(layout, bundle);
    fb.setDefaultDialogBorder();
    
    fb.appendI15dSeparator("s1.lb.file");
    fb.appendI15d("s1.lb.file", filePath);
    fb.nextLine();
    fb.appendI15d("s1.lb.separator", separator);
    fb.nextLine();
    fb.appendI15d("s1.lb.question", questions);
    fb.nextLine();
    fb.appendI15d("s1.lb.survey", surveys);    
    /*fb.appendadd(new JXLabel("Datei"), cc.xy(1, 1));
    fb.add(filePath, cc.xy(3, 1));
    fb.add(new JXLabel("Trennzeichen"), cc.xy(1, 3));
    fb.add(separator, cc.xy(3, 3));
    fb.add(new JXLabel("Fragen"), cc.xy(1, 5));
    fb.add(separator, cc.xy(3, 5));
    fb.add(new JXLabel("Fragebogen"), cc.xy(1, 7));
    fb.add(separator, cc.xy(3, 7));
    
    this.setLayout(new BorderLayout());
    this.add(fb.getPanel(), BorderLayout.CENTER);*/
  }
}
