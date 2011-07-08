package ch.zhaw.ias.dito.ui.resource;

import java.awt.Color;
import java.util.ResourceBundle;

import javax.swing.UIManager;

public class AppConfig {
  public static final String BUILD_DATE;
  public static final String BUILD_NUMBER;
  public static final Color ACTIVE;
  
  
  static {

    ResourceBundle versionBundle = ResourceBundle.getBundle("ch.zhaw.ias.dito.ui.resource.version");
    BUILD_DATE = versionBundle.getString("buildDate");
    BUILD_NUMBER = versionBundle.getString("buildNum");

    Color c = UIManager.getColor("info");
    if (c != null) {
      ACTIVE = c;
    } else {
      ACTIVE = Color.LIGHT_GRAY;
    }
  }
}
