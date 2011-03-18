package ch.zhaw.ias.dito.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.SwingWorker;
import javax.swing.plaf.basic.BasicBorders.RadioButtonBorder;

import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.JXRadioGroup;
import org.jdesktop.swingx.JXTextField;
import org.netbeans.validation.api.ui.ValidationGroup;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.debug.FormDebugPanel;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import ch.zhaw.ias.dito.Coding;
import ch.zhaw.ias.dito.DistanceAlgorithm;
import ch.zhaw.ias.dito.Matrix;
import ch.zhaw.ias.dito.config.ConfigProperty;
import ch.zhaw.ias.dito.config.DitoConfiguration;
import ch.zhaw.ias.dito.config.Method;
import ch.zhaw.ias.dito.config.PropertyGuardian;
import ch.zhaw.ias.dito.dist.DistanceMethodEnum;
import ch.zhaw.ias.dito.dist.UniversalBinaryDist;
import ch.zhaw.ias.dito.ui.resource.Translation;

public class MethodPanel extends DitoPanel implements ActionListener {
  private JXComboBox methods;
  private JXTextField parameter = new JXTextField();
  private JCheckBox ownDefinition = new JCheckBox(Translation.INSTANCE.get("s3.lb.ownDefinition"));
  private JXTextField createDefinition = new JXTextField();
  private JCheckBox randomSample = new JCheckBox(Translation.INSTANCE.get("s3.lb.randomSample"));
  private JXTextField sampleSize = new JXTextField();
  private JCheckBox parallel = new JCheckBox(Translation.INSTANCE.get("s3.lb.parallel"));
  private JXTextField numberOfThreads = new JXTextField();
  private MethodComboModel comboModel;
  private JXRadioGroup<Coding> codingGroup = new JXRadioGroup<Coding>(new Coding[] {Coding.REAL, Coding.BINARY});
  
  public MethodPanel(ValidationGroup validationGroup) {
    super(ScreenEnum.METHOD, ScreenEnum.QUESTION, ScreenEnum.ANALYSIS, validationGroup);
    comboModel = new MethodComboModel(DistanceMethodEnum.get(Coding.REAL));
    methods = new JXComboBox(comboModel);
    
    FormLayout layout = new FormLayout("pref, 5dlu, max(100dlu; pref), 5dlu, max(100dlu; pref), pref:grow", 
      "pref, 2dlu, pref, 2dlu, pref, 2dlu, pref, 2dlu, pref, 2dlu, pref, 2dlu, pref, 2dlu, pref, 2dlu, pref, 2dlu, pref, 2dlu, pref, fill:pref:grow");
    CellConstraints cc = new CellConstraints();
    DefaultFormBuilder fb = new DefaultFormBuilder(layout, Translation.INSTANCE.getBundle());
  
    fb.addI15dSeparator("s3.lb.method", cc.xyw(1, 1, 5));
    fb.addI15dLabel("s3.lb.coding", cc.xyw(1, 3, 3));
    fb.add(codingGroup, cc.xy(5, 3));
    codingGroup.addActionListener(this);
    
    fb.addI15dLabel("s3.lb.distance", cc.xyw(1, 5, 3));
    fb.add(methods, cc.xy(5, 5));
    
    fb.addI15dLabel("s3.lb.parameter", cc.xyw(1, 7, 3));
    fb.add(parameter, cc.xy(5, 7));
    
    fb.add(ownDefinition, cc.xyw(1, 9, 3));
    createDefinition.setText("(a+d)/(a+b+c+d)");
    fb.add(createDefinition, cc.xy(5, 9));    
    
    fb.addI15dSeparator("s3.lb.calculation", cc.xyw(1, 11, 5));
    fb.add(randomSample, cc.xyw(1, 13, 5));
    fb.addI15dLabel("s3.lb.sampleSize", cc.xy(3, 15));
    fb.add(sampleSize, cc.xy(5, 15));
    
    fb.add(parallel, cc.xyw(1, 17, 5));
    fb.addI15dLabel("s3.lb.numberOfThreads", cc.xy(3, 19));
    fb.add(numberOfThreads, cc.xy(5, 19));
    
    Method m = Config.INSTANCE.getDitoConfig().getMethod();
    codingGroup.setSelectedValue(m.getMethod().getCoding());
    methods.setSelectedItem(DistanceMethodEnum.get(m.getName()));
    
    randomSample.setSelected(m.isUseRandomSample());
    randomSample.addActionListener(this);
    sampleSize.setText(Integer.toString(m.getSampleSize()));
    
    parallel.setSelected(m.isParallel());
    parallel.addActionListener(this);
    numberOfThreads.setText(Integer.toString(m.getNumberOfThreads()));
    
    this.setLayout(new BorderLayout());
    this.add(fb.getPanel(), BorderLayout.CENTER);
  }
  
  private void updateEnabling() {
    sampleSize.setEnabled(randomSample.isSelected());
    numberOfThreads.setEnabled(parallel.isSelected());
  }
  
  @Override
  public void actionPerformed(ActionEvent e) {
    if (e == null) {
      comboModel = new MethodComboModel(DistanceMethodEnum.get(codingGroup.getSelectedValue()));
      methods.setModel(comboModel);
    } else {
      updateEnabling();
    }
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
    m.setMethod(comboModel.getSelectedMethod());
    if (comboModel.getSelectedMethod() == DistanceMethodEnum.get("Universal")) {
      UniversalBinaryDist binaryDist = (UniversalBinaryDist) comboModel.getSelectedMethod().getSpec();
      binaryDist.setExpression(createDefinition.getText());
    } 
    m.setUseRandomSample(randomSample.isSelected());
    m.setSampleSize(Integer.parseInt(sampleSize.getText()));
    
    m.setParallel(parallel.isSelected());
    m.setNumberOfThreads(Integer.parseInt(numberOfThreads.getText()));
    
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
