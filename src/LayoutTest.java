import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXTitledPanel;

import ch.zhaw.ias.dito.ui.resource.Translation;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.debug.FormDebugPanel;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;


public class LayoutTest extends JXFrame {
  public LayoutTest() {
    setSize(500, 500);
    
    FormLayout layout = new FormLayout("100dlu, pref:grow", 
    "pref:grow");
    CellConstraints cc = new CellConstraints();
    this.setLayout(layout);
    JPanel panel = new JXTitledPanel("Toolbar");
    panel.setBackground(Color.GREEN);
    //this.getContentPane().setBackground(Color.blue);
    this.getContentPane().add(panel, cc.xy(1, 1));
    this.getContentPane().add(new JXTitledPanel("Hilfe"), cc.xy(2, 1));
    //this.add(new JXTitledPanel("Prozessablauf"), cc.xy(2, 1));
    //this.add(new JXTitledPanel("Hauptpanel"), cc.xy(2, 2));
    //DefaultFormBuilder fb = new DefaultFormBuilder(layout);
    //fb.setDefaultDialogBorder();
    
    //fb.appendI15dSeparator("s1.lb.file");
    //fb.add();
    //fb.nextLine();
    //fb.nextLine();
    //setLayout(new GridLayout(2, 2, 10, 10));
    //this.setLayout(layout);
    /*fb.add(new JXTitledPanel("Toolbar"), cc.rc(1, 1));
    fb.add(new JXTitledPanel("Hilfe"), cc.rc(1, 2));
    fb.add(new JXTitledPanel("Prozessablauf"), cc.rc(2, 1));
    fb.add(new JXTitledPanel("Hauptpanel"), cc.rc(2, 2));*/
    //this.add(fb.getPanel());
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
  }
  
  public static void main(String... strings) {
    new LayoutTest();
  }
}
