/* released under bsd licence
 * see LICENCE file or http://www.opensource.org/licenses/bsd-license.php for details
 * Institute of Applied Simulation (ZHAW)
 * Author Thomas Niederberger
 */
package ch.zhaw.ias.dito.ui;

import java.lang.reflect.Constructor;

import org.netbeans.validation.api.ui.ValidationGroup;

import ch.zhaw.ias.dito.ui.util.HelpArea;

public enum ScreenEnum {
  INPUT(1, InputPanel.class), 
  QUESTION(2, QuestionPanel.class),
  METHOD(3, MethodPanel.class), 
  ANALYSIS(4, AnalysisPanel.class),
  OUTPUT(5, OutputPanel.class);
  
  private int screenId;
  private Class panelClass;
  
  private ScreenEnum(int screenId, Class panelClass) {
    this.screenId = screenId;
    this.panelClass = panelClass;
  }
  
  public int getScreenId() {
    return screenId;
  }
  
  public String getTitleKey() {
    return "s" + getScreenId() + ".title";
  }
  
  public DitoPanel getPanel(HelpArea helpArea) {
    try {
      Constructor<DitoPanel> c = panelClass.getConstructor(HelpArea.class);
      return (DitoPanel) c.newInstance(helpArea);
      //return (DitoPanel) panelClass.newInstance();
    } catch (Exception e) {
      throw new Error("this should never happen", e);
    }
  }
}
