package ch.zhaw.ias.dito.ui;

import org.netbeans.validation.api.ui.ValidationGroup;

public class AnalysisPanel extends DitoPanel {
  
  public AnalysisPanel(ValidationGroup validationGroup) {
    super(ScreenEnum.ANALYSIS, ScreenEnum.OUTPUT, null, validationGroup);
  }
}
