package ch.zhaw.ias.dito.ui.resource;

import java.awt.Color;

import javax.swing.UIManager;

public class AppConfig {
  public static final Color ACTIVE;
  
  static {
    //Color c = UIManager.getColor("Button.focus");
    //Color c = UIManager.getColor("textHighlight");
    Color c = UIManager.getColor("info");
    if (c != null) {
      ACTIVE = c;
    } else {
      ACTIVE = Color.LIGHT_GRAY;
    }
  }
}
