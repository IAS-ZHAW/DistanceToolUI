package ch.zhaw.ias.dito.ui;

import java.io.File;
import java.util.List;

import javax.swing.DefaultComboBoxModel;

import org.jdesktop.swingx.JXComboBox;
import org.netbeans.validation.api.ui.ValidationGroup;

import ch.zhaw.ias.dito.DistanceAlgorithm;
import ch.zhaw.ias.dito.config.DitoConfiguration;
import ch.zhaw.ias.dito.config.Method;
import ch.zhaw.ias.dito.dist.DistanceMethodEnum;

public class MethodPanel extends DitoPanel {
  private JXComboBox methods;
  private MethodComboModel comboModel;
  
  public MethodPanel(ValidationGroup validationGroup) {
    super(ScreenEnum.METHOD, ScreenEnum.QUESTION, ScreenEnum.ANALYSIS, validationGroup);
    comboModel = new MethodComboModel(DistanceMethodEnum.getList(false));
    methods = new JXComboBox(comboModel);
    Method m = Config.INSTANCE.getDitoConfig().getMethod();
    methods.setSelectedItem(DistanceMethodEnum.get(m.getName()));
    this.add(methods);
  }
  
  @Override
  public void saveToModel() {
    Method m = Config.INSTANCE.getDitoConfig().getMethod();
    m.setName(comboModel.getSelectedMethod().getName());
    
    DitoConfiguration config = Config.INSTANCE.getDitoConfig();
    DistanceAlgorithm algo = new DistanceAlgorithm(config);
    Config.INSTANCE.setDistanceMatrix(algo.doIt());
  }
  
  static class MethodComboModel extends DefaultComboBoxModel {
    private List<DistanceMethodEnum> methods;
    
    public MethodComboModel(List<DistanceMethodEnum> methods) {
      this.methods = methods;
    }
    
    @Override
    public int getSize() {
      return methods.size();
    }
    
    @Override
    public Object getElementAt(int index) {
      return methods.get(index);
    }
    
    public DistanceMethodEnum getSelectedMethod() {
      return (DistanceMethodEnum) getSelectedItem();
    }
  }
}
