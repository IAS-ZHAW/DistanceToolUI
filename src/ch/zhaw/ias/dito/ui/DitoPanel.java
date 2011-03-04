package ch.zhaw.ias.dito.ui;

import java.awt.event.ActionEvent;

import org.jdesktop.swingx.JXPanel;
import org.netbeans.validation.api.ui.ValidationGroup;

public abstract class DitoPanel extends JXPanel { 
  private ValidationGroup validationGroup;
  private ScreenEnum screenEnum;
  private ScreenEnum previous;
  private ScreenEnum next;
  
  public DitoPanel(ScreenEnum screenEnum, ScreenEnum previous, ScreenEnum next, ValidationGroup validationGroup) {
    this.screenEnum = screenEnum;
    this.previous = previous;
    this.next = next;
  }
  
  public ScreenEnum getScreenEnum() {
    return screenEnum;
  }
  public ScreenEnum getNext() {
    return next;
  }
  public ScreenEnum getPrevious() {
    return previous;
  }
  
  public boolean hasNext() {
    return next != null;
  }
  public boolean hasPrevious() {
    return previous != null;
  }

  public ValidationGroup getValidationGroup() {
    return validationGroup;
  }
  
  public abstract void saveToModel();
  
  /**
   * Default implementation. 
   * Designed to be overwritten.
   */
  public void calculate(ActionEvent e, MainPanel mp) {
    mp.switchPanel(e);
  }
}
