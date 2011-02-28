package ch.zhaw.ias.dito.ui;

import java.io.IOException;
import java.net.URL;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import org.jdesktop.swingx.JXTitledPanel;

import ch.zhaw.ias.dito.ui.resource.Translation;

public class HelpPanel extends JXTitledPanel {
  private JTextPane textPane = new JTextPane();
  
  public HelpPanel() {
    setTitle(Translation.INSTANCE.get("main.help"));
    textPane.setEditable(false);
    JScrollPane js = new JScrollPane(textPane);

    this.add(js);
  }
  
  public void switchTo(ScreenEnum e) {
    int screenId = e.getScreenId();
    try {
      URL url = Translation.class.getResource("help-s" + screenId + ".html");
      textPane.setPage(url);
    } catch (IOException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
  }
}
