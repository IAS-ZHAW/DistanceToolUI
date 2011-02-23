package ch.zhaw.ias.dito.ui;

import java.util.ConcurrentModificationException;

import javax.swing.SwingUtilities;

import ch.zhaw.ias.dito.config.DitoConfiguration;

public enum Config {
  INSTANCE;
  
  private DitoConfiguration config;
  
  public DitoConfiguration getDitoConfig() {
    return config;
  }
  
  public void setDitoConfig(DitoConfiguration config) {
    if (SwingUtilities.isEventDispatchThread() == false) {
      throw new ConcurrentModificationException("only EDT is allowed to change the configuration for thread-safety reasons");
    }
    this.config = config;
  }
}
