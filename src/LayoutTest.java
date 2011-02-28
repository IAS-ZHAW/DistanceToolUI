import javax.swing.JFrame;

import org.jdesktop.swingx.JXTitledPanel;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;


public class LayoutTest extends JFrame {
  public LayoutTest() {
    setSize(500, 500);
    
    FormLayout layout = new FormLayout("100dlu, pref:grow", 
        "fill:100dlu, fill:pref:grow");
    CellConstraints cc = new CellConstraints();
    this.getContentPane().setLayout(layout);

    this.add(new JXTitledPanel("Toolbar"), cc.xy(1, 1));
    this.add(new JXTitledPanel("Hilfe"), cc.xy(2, 1));
    this.add(new JXTitledPanel("Prozessablauf"), cc.xy(1, 2));
    this.add(new JXTitledPanel("Hauptpanel"), cc.xy(2, 2));
    
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
  }
  
  public static void main(String... strings) {
    new LayoutTest();
  }
}
