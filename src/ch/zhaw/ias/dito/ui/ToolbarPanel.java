package ch.zhaw.ias.dito.ui;

import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.xml.bind.JAXBException;

import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTitledPanel;

import ch.zhaw.ias.dito.config.DitoConfiguration;
import ch.zhaw.ias.dito.ui.resource.Translation;
import ch.zhaw.ias.dito.ui.util.ExtensionFileFilter;

public class ToolbarPanel extends JXTitledPanel implements ActionListener {
  private MainFrame mainFrame;
  
  private JXButton newProject = new JXButton(Translation.INSTANCE.get("toolbar.new"), new ImageIcon(Toolkit.getDefaultToolkit().getImage(Translation.class.getResource("ic-new.png"))));
  private JXButton openProject = new JXButton(Translation.INSTANCE.get("toolbar.open"), new ImageIcon(Toolkit.getDefaultToolkit().getImage(Translation.class.getResource("ic-open.png"))));
  private JXButton saveProject = new JXButton(Translation.INSTANCE.get("toolbar.save"), new ImageIcon(Toolkit.getDefaultToolkit().getImage(Translation.class.getResource("ic-save.png"))));
  private JXButton saveAsProject = new JXButton(Translation.INSTANCE.get("toolbar.saveAs"), new ImageIcon(Toolkit.getDefaultToolkit().getImage(Translation.class.getResource("ic-saveAs.png"))));
  
  public ToolbarPanel(MainFrame mainFrame) {
    this.mainFrame = mainFrame;
    setTitle(Translation.INSTANCE.get("main.toolbar"));
    setBorder(BorderFactory.createEtchedBorder());
    
    JXPanel toolbarPanel = new JXPanel();
    toolbarPanel.setLayout(new GridLayout(2, 2));
    toolbarPanel.add(newProject);
    toolbarPanel.add(saveProject);
    toolbarPanel.add(openProject);    
    toolbarPanel.add(saveAsProject);  
    
    newProject.addActionListener(this);
    openProject.addActionListener(this);
    saveProject.addActionListener(this);
    saveAsProject.addActionListener(this);
    
    //this.setLayout(new GridLayout(1, 2));
    this.add(toolbarPanel);
  }
  
  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == openProject) {
      checkSave();
      JFileChooser fileChooser = new JFileChooser();
      fileChooser.setFileFilter(ExtensionFileFilter.DITO);
      int returnVal = fileChooser.showOpenDialog(this);

      if (returnVal == JFileChooser.APPROVE_OPTION) {
          File file = fileChooser.getSelectedFile();
          try {
            DitoConfiguration config = DitoConfiguration.loadFromFile(file);
            config.setLocation(file.getAbsolutePath());
            config.loadMatrix();
            Config.INSTANCE.setDitoConfig(config);
            mainFrame.reload();
          } catch (Exception e2) {
            throw new Error("We're doomed!!!!", e2);
          }
      } else {
          //log.append("Open command cancelled by user." + newline);
      }
    } else if (e.getSource() == newProject) {
      checkSave();
      DitoConfiguration config = DitoConfiguration.createEmpty();
      Config.INSTANCE.setDitoConfig(config);
      mainFrame.switchTo(ScreenEnum.INPUT);
    } 
    
    if (e.getSource() == saveProject) {
      save(false);
    } else if (e.getSource() == saveAsProject) {
      save(true);
    }
  }
  
  private void save(boolean saveAs) {
    mainFrame.save();
    
    DitoConfiguration config = Config.INSTANCE.getDitoConfig();
    if (saveAs || config.hasLocation() == false) {
      JFileChooser fileChooser = new JFileChooser();
      int returnVal = fileChooser.showSaveDialog(this);
      if (returnVal == JFileChooser.APPROVE_OPTION) {
        File file = fileChooser.getSelectedFile();        
        try {
          config.setLocation(file.getAbsolutePath());
          config.save();
        } catch (JAXBException e1) {
          e1.printStackTrace();
        }
      }
    } else {
      try {          
        config.save();
      } catch (JAXBException e1) {
        e1.printStackTrace();
      }
    }
  }
  
  public void checkSave() {
    int save = JOptionPane.showConfirmDialog(null, Translation.INSTANCE.get("misc.saveConfirm"), Translation.INSTANCE.get("toolbar.save"), JOptionPane.YES_NO_OPTION);
    if (save == JOptionPane.YES_OPTION) {
      save(false);
    }
  }
}
