package ch.zhaw.ias.dito.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JProgressBar;
import javax.swing.JTextPane;

import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTitledPanel;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import ch.zhaw.ias.dito.config.ConfigProperty;
import ch.zhaw.ias.dito.config.PropertyListener;
import ch.zhaw.ias.dito.ui.resource.Translation;

public class ProcessPanel extends JXPanel {
  private ScreenEnum currentScreen;
  private Map<ScreenEnum, ProcessStepPanel> panels = new HashMap<ScreenEnum, ProcessPanel.ProcessStepPanel>();
  private JProgressBar progress = new JProgressBar();
  
  public ProcessPanel() {
    //setTitle(Translation.INSTANCE.get("main.process"));
    setBorder(BorderFactory.createEtchedBorder());
    
    FormLayout layout = new FormLayout("5dlu, pref:grow, 5dlu", 
    "5dlu, fill:pref, fill:pref:grow, fill:pref, fill:pref:grow, fill:pref, fill:pref:grow, fill:pref, fill:pref:grow, fill:pref, 5dlu, fill:20dlu, 5dlu");
    int[][] rowGroups = new int[][] {{2, 4, 6, 8}, {3, 5, 7}};
    layout.setRowGroups(rowGroups);
    CellConstraints cc = new CellConstraints();
    PanelBuilder pb = new PanelBuilder(layout);
    panels.put(ScreenEnum.INPUT, new ProcessStepPanel(ScreenEnum.INPUT, ConfigProperty.INPUT_FILENAME, ConfigProperty.INPUT_SIZE));
    panels.put(ScreenEnum.QUESTION, new ProcessStepPanel(ScreenEnum.QUESTION, ConfigProperty.QUESTION_NUMBER));
    panels.put(ScreenEnum.METHOD, new ProcessStepPanel(ScreenEnum.METHOD, ConfigProperty.METHOD_NAME));
    panels.put(ScreenEnum.ANALYSIS, new ProcessStepPanel(ScreenEnum.ANALYSIS));
    panels.put(ScreenEnum.OUTPUT, new ProcessStepPanel(ScreenEnum.OUTPUT, ConfigProperty.OUTPUT_FILENAME, ConfigProperty.OUTPUT_PRECISION));
    
    pb.add(panels.get(ScreenEnum.INPUT), cc.xy(2, 2));
    pb.add(new ArrowPanel(), cc.xy(2, 3));
    pb.add(panels.get(ScreenEnum.QUESTION), cc.xy(2, 4));
    pb.add(new ArrowPanel(), cc.xy(2, 5));
    pb.add(panels.get(ScreenEnum.METHOD), cc.xy(2, 6));
    pb.add(new ArrowPanel(), cc.xy(2, 7));
    pb.add(panels.get(ScreenEnum.ANALYSIS), cc.xy(2, 8));    
    pb.add(new ArrowPanel(), cc.xy(2, 9));
    pb.add(panels.get(ScreenEnum.OUTPUT), cc.xy(2, 10));
    
    pb.add(progress, cc.xy(2, 12));
    
    this.setLayout(new BorderLayout());
    add(pb.getPanel(), BorderLayout.CENTER);
  }
  
  public void switchTo(ScreenEnum e) {
    if (currentScreen != null) {
      panels.get(currentScreen).setHighlighted(false);
    }
    currentScreen = e;
    panels.get(currentScreen).setHighlighted(true);
  }
  
  public void setProcessState(boolean active) {
    progress.setIndeterminate(active);
  }
  
  static class ProcessStepPanel extends JXTitledPanel implements PropertyListener {
    private Map<ConfigProperty, JXLabel> labels = new HashMap<ConfigProperty, JXLabel>();
    
    public ProcessStepPanel(ScreenEnum screen, ConfigProperty... properties) {
      this.setTitle(Translation.INSTANCE.get(screen.getTitleKey()));
      this.setTitleFont(getTitleFont().deriveFont((float) 20));
      this.setBorder(null);
      
      JXPanel contentPanel = new JXPanel();
      contentPanel.setLayout(new GridLayout(properties.length, 2, 2, 2));
      contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
      for (int i = 0; i < properties.length; i++) {
        Config.INSTANCE.getPropertyGuardian().addListener(this);
        JXLabel valueLabel = new JXLabel();
        labels.put(properties[i], valueLabel);
        contentPanel.add(new JXLabel(Translation.INSTANCE.get(properties[i].getKey())));
        contentPanel.add(valueLabel);
      }
      contentPanel.setBackground(Color.LIGHT_GRAY);
      this.setContentContainer(contentPanel);
    }
    
    public void setHighlighted(boolean highlight) {
      if (highlight == true) {
        setTitleForeground(Color.WHITE);
        setBackground(Color.RED);
      } else {
        setTitleForeground(Color.BLACK);
        setBackground(Color.LIGHT_GRAY);  
      }
    }
    
    @Override
    public boolean listensTo(ConfigProperty prop) {
      return labels.containsKey(prop);
    }
    
    @Override
    public void propertyChanged(ConfigProperty prop, Object oldValue,
        Object newValue) {
      JXLabel lb = labels.get(prop);
      lb.setText(newValue.toString());
    }
  }
  
  static class ArrowPanel extends JXPanel {
    @Override
    public void paint(Graphics g1) {
      super.paint(g1);
      Graphics2D g = (Graphics2D) g1;
      int height = getHeight();
      int width = getWidth();
      int arrowW = (int) (0.75*width);
      int border = 10;
      int space = (width - arrowW) / 2;
      
      g.setColor(Color.LIGHT_GRAY);
      g.fillRect(space+arrowW/4, border, arrowW/2, height-2*border-space);
      g.fillPolygon(new int[] {space, space+arrowW/2, (int) (width-space)}, new int[] {height-border-space, height-border, height-border-space}, 3);
    }
  }
}
