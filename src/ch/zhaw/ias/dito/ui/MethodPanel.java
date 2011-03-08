package ch.zhaw.ias.dito.ui;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.SwingWorker;

import org.jdesktop.swingx.JXComboBox;
import org.netbeans.validation.api.ui.ValidationGroup;

import ch.zhaw.ias.dito.DistanceAlgorithm;
import ch.zhaw.ias.dito.Matrix;
import ch.zhaw.ias.dito.config.ConfigProperty;
import ch.zhaw.ias.dito.config.DitoConfiguration;
import ch.zhaw.ias.dito.config.Method;
import ch.zhaw.ias.dito.config.PropertyGuardian;
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
  
  
  public void calculate(final ActionEvent e, final MainPanel mp) {
    
    SwingWorker<Matrix, Void> worker = new SwingWorker<Matrix, Void> () {

      @Override
      protected Matrix doInBackground() throws Exception {
        DitoConfiguration config = Config.INSTANCE.getDitoConfig();
        DistanceAlgorithm algo = new DistanceAlgorithm(config);
        return algo.doIt();
      }
      
      @Override
      protected void done() {
        try {
          Matrix m = get();
          Config.INSTANCE.setDistanceMatrix(m);
          mp.switchPanel(e);
        } catch (InterruptedException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (ExecutionException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    };
    worker.execute();
  };

  
  @Override
  public void saveToModel() {
    Method m = Config.INSTANCE.getDitoConfig().getMethod();
    m.setName(comboModel.getSelectedMethod().getName());
    PropertyGuardian guardian = Config.INSTANCE.getPropertyGuardian();
    guardian.propertyChanged(ConfigProperty.METHOD_NAME, "", m.getName());
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
