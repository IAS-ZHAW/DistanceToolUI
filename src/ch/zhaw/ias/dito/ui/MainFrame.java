package ch.zhaw.ias.dito.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ResourceBundle;

import javax.swing.UIManager;
import javax.xml.bind.JAXBException;

import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXTextField;

import ch.zhaw.ias.dito.DistanceAlgorithm;
import ch.zhaw.ias.dito.Matrix;
import ch.zhaw.ias.dito.config.DitoConfiguration;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;
import com.jgoodies.looks.windows.WindowsButtonUI;
import com.jgoodies.looks.windows.WindowsLookAndFeel;

public class MainFrame extends JXFrame implements ActionListener {
  private DitoConfiguration config;
  private JXTextField filePath = new JXTextField();
  private JXTextField separator = new JXTextField();
  private JXTextField questions = new JXTextField();
  private JXTextField surveys = new JXTextField();
  private JXButton start = new JXButton("Kick-Ass start button");
  
  public MainFrame() {
    try {
      config = DitoConfiguration.loadFromFile("c:/java-workspace/DistanceToolCore/testdata/irisFlower.xml");//filePath.getText()); 
    } catch (Exception e) {
      throw new Error("We're doomed!!!!");
    }
    
    start.addActionListener(this);
    
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
    this.add(new TablePanel(config.getQuestions()), BorderLayout.CENTER); 
    this.add(start, BorderLayout.SOUTH);
    
    this.setSize(500, 500);
    this.setDefaultCloseOperation(JXFrame.EXIT_ON_CLOSE);
    this.setVisible(true);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    System.out.println(filePath.getText());
    
    try {
      //System.out.println("reading input-file: " + config.getInput().getFilename());
      Matrix m = Matrix.readFromFile(new File(config.getInput().getFilename()), config.getInput().getSeparator());
      DistanceAlgorithm algo = new DistanceAlgorithm(m, config);
      algo.doIt();
      algo.writeToFile();
    } catch (FileNotFoundException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    } catch (IOException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
  }
  
  public static void main(String... args) {
    try {
      UIManager.setLookAndFeel(new WindowsLookAndFeel());
      /*
       * com.jgoodies.looks.windows.WindowsLookAndFeel
       * com.jgoodies.looks.plastic.PlasticLookAndFeel
       * com.jgoodies.looks.plastic.Plastic3DLookAndFeel
       * com.jgoodies.looks.plastic.PlasticXPLookAndFeel 
       */
   } catch (Exception e) {}

    new MainFrame();
  }
}
