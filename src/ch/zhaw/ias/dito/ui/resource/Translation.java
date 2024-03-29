/* released under bsd licence
 * see LICENCE file or http://www.opensource.org/licenses/bsd-license.php for details
 * Institute of Applied Simulation (ZHAW)
 * Author Thomas Niederberger
 */
package ch.zhaw.ias.dito.ui.resource;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public enum Translation {
  INSTANCE;
  
  private final ResourceBundle bundle;
  
  Translation() {
    bundle = ResourceBundle.getBundle("ch.zhaw.ias.dito.ui.resource.uiTexts");
  }
  
  public ResourceBundle getBundle() {
    return bundle;
  }
  
  public String get(String key) {
    try {
      return bundle.getString(key);
    } catch (MissingResourceException e) {
      //in case resource can't be found -> return key
      return key;
    }
  }
}
