package ch.zhaw.ias.dito.ui;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.netbeans.validation.api.ui.ValidationGroup;

public enum ScreenEnum {
  INPUT(1, InputPanel.class), 
  QUESTION(2, QuestionPanel.class), 
  OUTPUT(3, OutputPanel.class), 
  ANALYSIS(4, AnalysisPanel.class);
  
  private int screenId;
  private Class panelClass;
  
  private ScreenEnum(int screenId, Class panelClass) {
    this.screenId = screenId;
    this.panelClass = panelClass;
  }
  
  public int getScreenId() {
    return screenId;
  }
  
  public DitoPanel getPanel(ValidationGroup validationGroup) {
    try {
      Constructor<DitoPanel> c = panelClass.getConstructor(ValidationGroup.class);
      return (DitoPanel) c.newInstance(validationGroup);
      //return (DitoPanel) panelClass.newInstance();
    } catch (Exception e) {
      throw new Error("this should never happen", e);
    }
  }
}
