package ch.zhaw.ias.dito.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTitledPanel;
import org.netbeans.validation.api.ui.ValidationPanel;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.debug.FormDebugPanel;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import ch.zhaw.ias.dito.ui.resource.Translation;

public class MainPanel extends JXPanel implements ActionListener, ChangeListener {
  private MainFrame mainFrame;
  
  private JXButton nextButton = new JXButton(Translation.INSTANCE.get("misc.next"));
  private JXButton previousButton = new JXButton(Translation.INSTANCE.get("misc.previous"));
  
  private DitoPanel currentMainPanel;
  private ValidationPanel validationPanel = new ValidationPanel();
  
  public MainPanel(MainFrame mainFrame) {
    this.mainFrame = mainFrame;
    //setTitle(Translation.INSTANCE.get("main.main"));
    setBorder(BorderFactory.createEtchedBorder());
    
    
    FormLayout layout = new FormLayout("pref:grow(0.2), fill:pref:grow(0.6), pref:grow(0.2)", 
    "50dlu, fill:pref:grow(0.6), fill:pref:grow(0.4), fill:pref");
    //layout.setRowGroups(new int[][]{{2, 4, 6}}); 
    CellConstraints cc = new CellConstraints();
    //DefaultFormBuilder fb = new DefaultFormBuilder(layout, Translation.INSTANCE.getBundle(), new FormDebugPanel());
    
    JXPanel buttonPanel = new JXPanel();
    buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.add(previousButton);
    buttonPanel.add(nextButton);
    validationPanel.addChangeListener(this);
    
    this.setLayout(layout);
    this.add(validationPanel, cc.xy(2, 2));
    this.add(buttonPanel, cc.xy(2, 4));
    

    nextButton.addActionListener(this);
    previousButton.addActionListener(this);
  }
  
  public void switchTo(ScreenEnum e) {
    currentMainPanel = e.getPanel(validationPanel.getValidationGroup());    
    nextButton.setEnabled(currentMainPanel.hasNext());
    previousButton.setEnabled(currentMainPanel.hasPrevious());
    validationPanel.setInnerComponent(currentMainPanel);
    //setTitle(Translation.INSTANCE.get(e.getTitleKey()));
  }
  
  @Override
  public void stateChanged(ChangeEvent e) {
    nextButton.setEnabled(!validationPanel.isProblem());
  }

  @Override
  public void actionPerformed(ActionEvent e) {   
    if (e.getSource() == nextButton || e.getSource() == previousButton) { 
      currentMainPanel.saveToModel();
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
