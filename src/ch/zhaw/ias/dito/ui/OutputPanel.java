package ch.zhaw.ias.dito.ui;

import org.netbeans.validation.api.ui.ValidationGroup;

public class OutputPanel extends DitoPanel {
  public OutputPanel(ValidationGroup validationGroup) {
    super(ScreenEnum.OUTPUT, ScreenEnum.QUESTION, ScreenEnum.ANALYSIS, validationGroup);
  }
  
  
}
