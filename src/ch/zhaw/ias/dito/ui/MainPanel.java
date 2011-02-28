package ch.zhaw.ias.dito.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTitledPanel;
import org.netbeans.validation.api.ui.ValidationPanel;

import ch.zhaw.ias.dito.ui.resource.Translation;

public class MainPanel extends JXTitledPanel implements ActionListener {
  private MainFrame mainFrame;
  
  private JXButton nextButton = new JXButton(Translation.INSTANCE.get("misc.next"));
  private JXButton previousButton = new JXButton(Translation.INSTANCE.get("misc.previous"));
  
  private DitoPanel currentMainPanel;
  private ValidationPanel validationPanel = new ValidationPanel();
  
  public MainPanel(MainFrame mainFrame) {
    this.mainFrame = mainFrame;
    setTitle(Translation.INSTANCE.get("main.main"));
    JXPanel buttonPanel = new JXPanel();
    buttonPanel.setLayout(new FlowLayout());
    buttonPanel.add(previousButton);
    buttonPanel.add(nextButton);
    
    this.add(buttonPanel, BorderLayout.SOUTH);
    add(validationPanel, BorderLayout.CENTER);

    nextButton.addActionListener(this);
    previousButton.addActionListener(this);
  }
  
  public void switchTo(ScreenEnum e) {
    currentMainPanel = e.getPanel(validationPanel.getValidationGroup());    
    nextButton.setVisible(currentMainPanel.hasNext());
    previousButton.setVisible(currentMainPanel.hasPrevious());
    validationPanel.setInnerComponent(currentMainPanel);
    
    setTitle(Translation.INSTANCE.get(e.getTitleKey()));
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
    mainFrame.switchTo(nextScreen);
  }
}
