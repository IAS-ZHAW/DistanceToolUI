package ch.zhaw.ias.dito.ui;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.xml.bind.JAXBException;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXStatusBar;
import org.jdesktop.swingx.plaf.windows.WindowsLookAndFeelAddons;

import ch.zhaw.ias.dito.config.DitoConfiguration;
import ch.zhaw.ias.dito.ui.resource.Translation;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.looks.windows.WindowsLookAndFeel;

public class MainFrame extends JXFrame {
  private ToolbarPanel toolbarPanel;
  private HelpPanel helpPanel = new HelpPanel();
  private ProcessPanel processPanel = new ProcessPanel();
  private MainPanel mainPanel = new MainPanel(this);
  
  private JLabel statusLabel;
  private JProgressBar progressBar; 
  private ScreenEnum currentScreen;
      
  private String inputPath = "c:/java-workspace/DistanceToolCore/testdata/irisFlower.dito";
  private String outputPath = "c:/java-workspace/DistanceToolCore/testdata/irisFlower-output.dito";
  
  public MainFrame() {
    //Config.INSTANCE.setDitoConfig(DitoConfiguration.createEmpty());
    //Just for testing purpose
    try {
      String testfile = "C:/java-workspace/DistanceToolCore/testdata/irisFlower.dito";
      DitoConfiguration config = DitoConfiguration.loadFromFile(testfile);
      config.setLocation(testfile);
      config.loadMatrix();
      Config.INSTANCE.setDitoConfig(config);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (JAXBException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    FormLayout layout = new FormLayout("2dlu, 150dlu, 2dlu, pref:grow, 2dlu", 
    "2dlu, fill:75dlu, 2dlu, fill:pref:grow, 2dlu");
    CellConstraints cc = new CellConstraints();
    this.getContentPane().setLayout(layout);
    
    toolbarPanel = new ToolbarPanel(this);
    
    this.add(toolbarPanel, cc.xy(2, 2));
    this.add(helpPanel, cc.xy(4, 2));
    this.add(processPanel, cc.xy(2, 4));
    this.add(mainPanel, cc.xy(4, 4));
 
    JXStatusBar bar = new JXStatusBar();
    statusLabel = new JLabel("Ready");
    JXStatusBar.Constraint c1 = new JXStatusBar.Constraint();
    c1.setFixedWidth(100);
    bar.add(statusLabel, c1);     // Fixed width of 100 with no inserts
    JXStatusBar.Constraint c2 = new JXStatusBar.Constraint(
            JXStatusBar.Constraint.ResizeBehavior.FILL); // Fill with no inserts
    progressBar = new JProgressBar();
    bar.add(progressBar, c2);            // Fill with no inserts - will use remaining space
    
    this.setStatusBar(bar);
    this.setTitle(Translation.INSTANCE.get("misc.title"));
    this.setSize(1000, 1000);
    this.setDefaultCloseOperation(JXFrame.EXIT_ON_CLOSE);
    this.setVisible(true);
    
    switchTo(ScreenEnum.INPUT);
  }
  
  public void switchTo(ScreenEnum e) {
    mainPanel.switchTo(e);
    helpPanel.switchTo(e);
    processPanel.switchTo(e);
    this.currentScreen = e;
    this.validate();
  }
  
  public void reload() {
    switchTo(currentScreen);
  }
  
  public static void main(String... args) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        try {
          UIManager.setLookAndFeel(new WindowsLookAndFeel());
          /*
           * com.jgoodies.looks.windows.WindowsLookAndFeel
           * com.jgoodies.looks.plastic.PlasticLookAndFeel
           * com.jgoodies.looks.plastic.Plastic3DLookAndFeel
           * com.jgoodies.looks.plastic.PlasticXPLookAndFeel 
           */
       } catch (Exception e) {
         
       }
       new MainFrame();
      }
  });
  }
}
