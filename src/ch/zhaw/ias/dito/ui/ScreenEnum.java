package ch.zhaw.ias.dito.ui;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

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
  
  public DitoPanel getPanel() {
    try {
      return (DitoPanel) panelClass.newInstance();
    } catch (Exception e) {
      throw new Error("this should never happen", e);
    }
  }
}
