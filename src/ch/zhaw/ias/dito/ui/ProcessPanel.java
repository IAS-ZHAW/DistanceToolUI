package ch.zhaw.ias.dito.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;

import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTitledPanel;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import ch.zhaw.ias.dito.config.ConfigProperty;
import ch.zhaw.ias.dito.config.DitoConfiguration;
import ch.zhaw.ias.dito.ui.resource.Translation;

public class ProcessPanel extends JXTitledPanel {
  private JXLabel imageLabel = new JXLabel();
  
  public ProcessPanel() {
    setTitle(Translation.INSTANCE.get("main.process"));
    
    imageLabel.setBackground(Color.red);
    FormLayout layout = new FormLayout("5dlu, pref:grow, 5dlu", 
    "5dlu, fill:pref, fill:pref:grow, fill:pref, fill:pref:grow, fill:pref, fill:pref:grow, fill:pref, 5dlu");
    int[][] rowGroups = new int[][] {{2, 4, 6, 8}, {3, 5, 7}};
    layout.setRowGroups(rowGroups);
    CellConstraints cc = new CellConstraints();
    PanelBuilder pb = new PanelBuilder(layout);
    pb.add(new ProcessStepPanel(1, ConfigProperty.INPUT_FILENAME, ConfigProperty.INPUT_SEPARATOR), cc.xy(2, 2));
    pb.add(new ArrowPanel(), cc.xy(2, 3));
    pb.add(new ProcessStepPanel(2), cc.xy(2, 4));
    pb.add(new ArrowPanel(), cc.xy(2, 5));
    pb.add(new ProcessStepPanel(3, ConfigProperty.METHOD_NAME), cc.xy(2, 6));
    pb.add(new ArrowPanel(), cc.xy(2, 7));
    pb.add(new ProcessStepPanel(4, ConfigProperty.OUTPUT_FILENAME, ConfigProperty.OUTPUT_SEPARATOR, ConfigProperty.OUTPUT_PRECISION), cc.xy(2, 8));
    
    this.add(pb.getPanel());
  }
  
  public void switchProcessImage(ScreenEnum screen) {
    URL url = Translation.class.getResource("process-screen" + screen.getScreenId() + ".png");
    ImageIcon image = new ImageIcon(url);
    imageLabel.setBackground(Color.red);
    //imageLabel.setIcon(image);
  }
  
  static class ProcessStepPanel extends JXTitledPanel {
    private JXLabel titleLabel;
    private Map<String, JXLabel> labels = new HashMap<String, JXLabel>();
    
    public ProcessStepPanel(int id, ConfigProperty... properties) {
      this.setTitle("Prozessschritt " + id);
      this.setTitleFont(getTitleFont().deriveFont((float) 20));
      //this.add(titleLabel);
      JXPanel contentPanel = new JXPanel();
      contentPanel.setLayout(new GridLayout(properties.length, 2));
      for (int i = 0; i < properties.length; i++) {
        contentPanel.add(new JXLabel(Translation.INSTANCE.get(properties[i].getKey())));
        contentPanel.add(new JXLabel("dummy"));
      }
      contentPanel.setBackground(Color.LIGHT_GRAY);
      this.setContentContainer(contentPanel);
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
