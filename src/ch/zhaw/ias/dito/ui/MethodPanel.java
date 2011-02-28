package ch.zhaw.ias.dito.ui;

import org.jdesktop.swingx.JXPanel;
import org.netbeans.validation.api.ui.ValidationGroup;

public class MethodPanel extends DitoPanel {
  public MethodPanel(ValidationGroup validationGroup) {
    super(ScreenEnum.METHOD, ScreenEnum.QUESTION, ScreenEnum.OUTPUT, validationGroup);
  }
}
