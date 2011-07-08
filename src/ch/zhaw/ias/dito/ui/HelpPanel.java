package ch.zhaw.ias.dito.ui;

import java.awt.Dimension;
import java.io.IOException;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import org.jdesktop.swingx.JXTitledPanel;

import ch.zhaw.ias.dito.ui.resource.Translation;
import ch.zhaw.ias.dito.ui.util.HelpArea;

public class HelpPanel extends JXTitledPanel implements HelpArea {
  private JEditorPane textPane = new JTextPane();
  
  public HelpPanel() {
    setTitle(Translation.INSTANCE.get("main.help"));
    setBorder(BorderFactory.createEtchedBorder());
    textPane.setEditable(false);
    textPane.setPreferredSize(new Dimension(this.getWidth(), textPane.getHeight()));
    JScrollPane js = new JScrollPane(textPane);
    js.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
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
    
  @Override
  public void showHelpTopic(String key) {
    textPane.scrollToReference(key);
    
  }
}
