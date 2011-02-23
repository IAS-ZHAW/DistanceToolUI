package ch.zhaw.ias.dito.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.xml.bind.JAXBException;

import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXStatusBar;

import ch.zhaw.ias.dito.DistanceAlgorithm;
import ch.zhaw.ias.dito.Matrix;
import ch.zhaw.ias.dito.config.DitoConfiguration;
import ch.zhaw.ias.dito.ui.resource.Translation;

import com.jgoodies.looks.windows.WindowsLookAndFeel;

public class MainFrame extends JXFrame implements ActionListener {
  private static final String IMAGE_PATH = "C:/java-workspace/DistanceToolUI/src/ch/zhaw/ias/dito/ui/resource/";
  private JLabel statusLabel;
  private JProgressBar progressBar; 
  private DitoPanel currentMainPanel;
  private ScreenEnum currentScreen;
  
  private JXLabel imageLabel = new JXLabel();
  private JXButton nextButton = new JXButton(Translation.INSTANCE.get("misc.next"));
  private JXButton previousButton = new JXButton(Translation.INSTANCE.get("misc.previous"));
  
  private JXButton newProject = new JXButton(Translation.INSTANCE.get("toolbar.new"));
  private JXButton openProject = new JXButton(Translation.INSTANCE.get("toolbar.open"));
  private JXButton saveProject = new JXButton(Translation.INSTANCE.get("toolbar.save"));
  private JXButton saveAsProject = new JXButton(Translation.INSTANCE.get("toolbar.saveAs"));
  
  private String inputPath = "c:/java-workspace/DistanceToolCore/testdata/irisFlower.dito";
  private String outputPath = "c:/java-workspace/DistanceToolCore/testdata/irisFlower-output.dito";
  
  public MainFrame() {
    /*try {
      DitoConfiguration config = DitoConfiguration.loadFromFile(inputPath);
      Config.INSTANCE.setDitoConfig(config);
    } catch (Exception e) {
      throw new Error("We're doomed!!!!");
    }*/
    Config.INSTANCE.setDitoConfig(DitoConfiguration.createEmpty());
    JXPanel topPanel = new JXPanel();
    
    topPanel.setLayout(new FlowLayout());
    JXPanel toolbarPanel = new JXPanel();
    toolbarPanel.setLayout(new GridLayout(2, 2));
    toolbarPanel.add(newProject);
    toolbarPanel.add(openProject);
    toolbarPanel.add(saveProject);
    toolbarPanel.add(saveAsProject);
    topPanel.add(imageLabel);
    topPanel.add(toolbarPanel);
    
    JXPanel buttonPanel = new JXPanel();
    buttonPanel.setLayout(new FlowLayout());
    buttonPanel.add(previousButton);
    buttonPanel.add(nextButton);
    nextButton.addActionListener(this);
    previousButton.addActionListener(this);
    openProject.addActionListener(this);
    
    this.add(topPanel, BorderLayout.NORTH);
    this.add(buttonPanel, BorderLayout.SOUTH);
        
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
    this.setSize(500, 500);
    this.setDefaultCloseOperation(JXFrame.EXIT_ON_CLOSE);
    this.setVisible(true);
    
    switchTo(ScreenEnum.INPUT);
  }

  @Override
  public void actionPerformed(ActionEvent e) {   
    if (e.getSource() == openProject) {
      JFileChooser fileChooser = new JFileChooser();
      int returnVal = fileChooser.showOpenDialog(this);

      if (returnVal == JFileChooser.APPROVE_OPTION) {
          File file = fileChooser.getSelectedFile();
          try {
            DitoConfiguration config = DitoConfiguration.loadFromFile(file);
            Config.INSTANCE.setDitoConfig(config);
            switchTo(currentScreen);
          } catch (Exception e2) {
            throw new Error("We're doomed!!!!", e2);
          }
          //This is where a real application would open the file.
          //log.append("Opening: " + file.getName() + "." + newline);
      } else {
          //log.append("Open command cancelled by user." + newline);
      }
    } else if (e.getSource() == nextButton || e.getSource() == previousButton) { 
      switchMainPanel(e.getSource() == previousButton);
    } else {
      try {
        //System.out.println("reading input-file: " + config.getInput().getFilename());
        DitoConfiguration config = Config.INSTANCE.getDitoConfig();
        DitoConfiguration.saveToFile(outputPath, config);
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
      } catch (JAXBException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }
    }
  }
  
  public void switchMainPanel(boolean previous) {
    ScreenEnum nextScreen;
    if (previous == true) {
      nextScreen = currentMainPanel.getPrevious();
    } else {
      nextScreen = currentMainPanel.getNext();
    }
    switchTo(nextScreen);
  }
  
  public void switchTo(ScreenEnum e) {
    if (currentMainPanel != null) {
      this.remove(currentMainPanel); 
    }
    currentMainPanel = e.getPanel();
    ImageIcon image = new ImageIcon(IMAGE_PATH + "process-screen" + currentMainPanel.getScreenEnum().getScreenId() + ".png");
    imageLabel.setIcon(image);
    nextButton.setVisible(currentMainPanel.hasNext());
    previousButton.setVisible(currentMainPanel.hasPrevious());
    this.currentScreen = e;
    this.add(currentMainPanel, BorderLayout.CENTER);
    this.validate();
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
