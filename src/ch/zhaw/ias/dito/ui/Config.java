package ch.zhaw.ias.dito.ui;

import java.util.ConcurrentModificationException;

import javax.swing.SwingUtilities;

import ch.zhaw.ias.dito.Matrix;
import ch.zhaw.ias.dito.config.DitoConfiguration;
import ch.zhaw.ias.dito.config.PropertyGuardian;

public enum Config {
  INSTANCE;
  
  private DitoConfiguration config;
  private Matrix distanceMatrix;
  private PropertyGuardian propertyGuardian = new PropertyGuardian();
  
  public DitoConfiguration getDitoConfig() {
    return config;
  }
  
  /**
   * For thread safety reasons only the EDT is allowed to set a new Configuration
   * @param config the new configuration
   */
  public void setDitoConfig(DitoConfiguration config) {
    // this is pretty ugly, since it leads to a strong coupling between the guardian and the configuration
    // even worse it could lead to significant memory leaks when forgotten!
    if (this.config != null) {
      propertyGuardian.removeListener(this.config);
      this.config.kill();
    }
    if (SwingUtilities.isEventDispatchThread() == false) {
      throw new ConcurrentModificationException("only EDT is allowed to change the configuration for thread-safety reasons");
    }
    this.config = config;
    propertyGuardian.addListener(this.config);
  }

  public Matrix getDistanceMatrix() {
    return distanceMatrix;
  }
  
  /**
   * For thread safety reasons only the EDT is allowed to set a new distanceMatrix
   * @param distanceMatrix the new distanceMatrix
   */
  public void setDistanceMatrix(Matrix distanceMatrix) {
    if (SwingUtilities.isEventDispatchThread() == false) {
      throw new ConcurrentModificationException("only EDT is allowed to change the configuration for thread-safety reasons");
    }    
    this.distanceMatrix = distanceMatrix;
  }

  public PropertyGuardian getPropertyGuardian() {
    return propertyGuardian;
  }
}
