package ch.zhaw.ias.dito.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.xml.bind.JAXBException;

import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXStatusBar;
import org.netbeans.validation.api.ui.ValidationPanel;

import ch.zhaw.ias.dito.DistanceAlgorithm;
import ch.zhaw.ias.dito.Matrix;
import ch.zhaw.ias.dito.config.DitoConfiguration;
import ch.zhaw.ias.dito.ui.resource.Translation;

import com.jgoodies.looks.windows.WindowsLookAndFeel;

public class MainFrame extends JXFrame implements ActionListener {
  private ValidationPanel validationPanel = new ValidationPanel();
  private TopPanel topPanel;
  private JLabel statusLabel;
  private JProgressBar progressBar; 
  private DitoPanel currentMainPanel;
  private ScreenEnum currentScreen;
  
  private JXButton nextButton = new JXButton(Translation.INSTANCE.get("misc.next"));
  private JXButton previousButton = new JXButton(Translation.INSTANCE.get("misc.previous"));
    
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

    JXPanel buttonPanel = new JXPanel();
    buttonPanel.setLayout(new FlowLayout());
    buttonPanel.add(previousButton);
    buttonPanel.add(nextButton);
    nextButton.addActionListener(this);
    previousButton.addActionListener(this);
    
    topPanel = new TopPanel(this);
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
    this.add(validationPanel, BorderLayout.CENTER);
    
    this.setStatusBar(bar);
    this.setTitle(Translation.INSTANCE.get("misc.title"));
    this.setSize(500, 500);
    this.setDefaultCloseOperation(JXFrame.EXIT_ON_CLOSE);
    this.setVisible(true);
    
    switchTo(ScreenEnum.INPUT);
  }

  @Override
  public void actionPerformed(ActionEvent e) {   
    if (e.getSource() == nextButton || e.getSource() == previousButton) { 
      switchMainPanel(e.getSource() == previousButton);
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
    currentMainPanel = e.getPanel(validationPanel.getValidationGroup());
    topPanel.switchProcessImage(e);
    nextButton.setVisible(currentMainPanel.hasNext());
    previousButton.setVisible(currentMainPanel.hasPrevious());
    this.currentScreen = e;
    validationPanel.setInnerComponent(currentMainPanel);
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

  public void reload() {
    switchTo(currentScreen);
  }
}
