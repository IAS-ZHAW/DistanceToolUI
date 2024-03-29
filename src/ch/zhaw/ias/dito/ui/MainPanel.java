/* released under bsd licence
 * see LICENCE file or http://www.opensource.org/licenses/bsd-license.php for details
 * Institute of Applied Simulation (ZHAW)
 * Author Thomas Niederberger
 */
package ch.zhaw.ias.dito.ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXPanel;
import org.netbeans.validation.api.ui.ValidationPanel;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import ch.zhaw.ias.dito.ui.resource.Translation;
import ch.zhaw.ias.dito.ui.util.HelpArea;

public class MainPanel extends JXPanel implements ActionListener, ChangeListener {
  private MainFrame mainFrame;
  private DitoPanel currentMainPanel;
  private ValidationPanel validationPanel = new ValidationPanel();
  
  private JXButton nextButton = new JXButton(Translation.INSTANCE.get("misc.next"));
  private JXButton previousButton = new JXButton(Translation.INSTANCE.get("misc.previous"));
    
  public MainPanel(MainFrame mainFrame) {
    this.mainFrame = mainFrame;
    //setTitle(Translation.INSTANCE.get("main.main"));
    setBorder(BorderFactory.createEtchedBorder());
    
    FormLayout layout = new FormLayout("fill:20dlu, fill:0:grow, fill:20dlu", 
      "20dlu, fill:min:grow(0.8), fill:0:grow(0.2), fill:20dlu");
    //layout.setRowGroups(new int[][]{{2, 4, 6}}); 
    CellConstraints cc = new CellConstraints();
    //DefaultFormBuilder fb = new DefaultFormBuilder(layout, Translation.INSTANCE.getBundle(), new FormDebugPanel());
    
    JXPanel buttonPanel = new JXPanel();
    buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
    nextButton.setPreferredSize(new Dimension(100, 25));
    previousButton.setPreferredSize(new Dimension(100,  25));
    
    buttonPanel.add(previousButton);
    buttonPanel.add(nextButton);
    validationPanel.addChangeListener(this);
    
    this.setLayout(layout);
    this.add(validationPanel, cc.xy(2, 2));
    this.add(buttonPanel, cc.xy(2, 4));
    
    nextButton.addActionListener(this);
    previousButton.addActionListener(this);
  }
  
  public void switchTo(ScreenEnum e, HelpArea helpArea) {
    currentMainPanel = e.getPanel(helpArea);//validationPanel.getValidationGroup());
    nextButton.setEnabled(currentMainPanel.hasNext());
    previousButton.setEnabled(currentMainPanel.hasPrevious());
    validationPanel.setInnerComponent(currentMainPanel);
  }
  
  @Override
  public void stateChanged(ChangeEvent e) {
    nextButton.setEnabled(!validationPanel.isProblem());
  }

  public void switchPanel(ActionEvent e) {
    setProcessState(false);
    if (e.getSource() == nextButton || e.getSource() == previousButton) { 
      switchMainPanel(e.getSource() == previousButton);
    } 
  }
  
  @Override
  public void actionPerformed(ActionEvent e) {   
    if (e.getSource() == nextButton || e.getSource() == previousButton) {
      mainFrame.setProcessState(true);
      currentMainPanel.saveToModel();
      if (e.getSource() == nextButton) {
        // calculations are only executed when next is clicked
        currentMainPanel.calculate(e, this);
      } else {
        switchPanel(e);
      }
    } 
  }
  
  public void save() {
    currentMainPanel.saveToModel();
  }
  
  public void setProcessState(boolean active) {
    mainFrame.setProcessState(active);
  }
  
  private void switchMainPanel(boolean previous) {
    ScreenEnum nextScreen;
    if (previous == true) {
      nextScreen = currentMainPanel.getPrevious();
    } else {
      nextScreen = currentMainPanel.getNext();
    }
    mainFrame.switchTo(nextScreen);
  }
}
