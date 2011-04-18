package ch.zhaw.ias.dito.ui;

import java.awt.event.ActionEvent;

import org.jdesktop.swingx.JXPanel;
import org.netbeans.validation.api.ui.ValidationGroup;

public abstract class DitoPanel extends JXPanel { 
  private ValidationGroup validationGroup;
  private ScreenEnum screenEnum;
  private ScreenEnum previous;
  private ScreenEnum next;
  
  public DitoPanel(ScreenEnum screenEnum, ScreenEnum previous, ScreenEnum next) {
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
  
  /**
   * Saves the current values entered in the UI to the data model.
   * Designed to be overwritten.  
   */
  public abstract void saveToModel();
  
  /**
   * Default implementation. Only executes a callback to the mainpanel.
   * Designed to be overwritten. Can be useful for long calculations. 
   * As soon as calculations are finished super.swichtPanel(e) should be called.
   */
  public void calculate(ActionEvent e, MainPanel mp) {
    mp.switchPanel(e);
  }
}
