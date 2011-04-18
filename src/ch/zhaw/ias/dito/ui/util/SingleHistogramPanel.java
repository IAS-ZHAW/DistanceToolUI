package ch.zhaw.ias.dito.ui.util;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jdesktop.swingx.JXButton;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.SimpleHistogramBin;
import org.jfree.data.statistics.SimpleHistogramDataset;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import ch.zhaw.ias.dito.DVector;
import ch.zhaw.ias.dito.Matrix;
import ch.zhaw.ias.dito.ui.resource.Translation;

public class SingleHistogramPanel extends JPanel implements ChangeListener {
  private Matrix m;
  private ChartPanel chartPanel;
  private JFreeChart chart;
  private JSlider slider;
  private JSpinner spinner;

  private SimpleHistogramDataset dataset;

  public SingleHistogramPanel(Matrix m) {
    super(new BorderLayout());
    this.m = m;
    this.chart = createChart();
    this.chartPanel = new ChartPanel(this.chart);

    Border border = BorderFactory.createCompoundBorder(
        BorderFactory.createEmptyBorder(4, 4, 4, 4),
        BorderFactory.createEtchedBorder());
    this.chartPanel.setBorder(border);
    add(this.chartPanel, BorderLayout.CENTER);

    JPanel dashboard = new JPanel(new BorderLayout());
    dashboard.setBorder(BorderFactory.createEmptyBorder(0, 4, 4, 4));

    this.spinner = new JSpinner(new SpinnerNumberModel(0, 0, m.getColCount()-1, 1));
    spinner.addChangeListener(this);
    this.slider = new JSlider(0, m.getColCount()-1, 0);
    slider.setPaintLabels(true);
    
    slider.setMajorTickSpacing(Math.max(50, 10 * Math.round(m.getColCount()/100)));
    slider.setPaintTicks(true);
    this.slider.addChangeListener(this);
    
    FormLayout layout = new FormLayout("fill:0:g, max(20dlu; pref)", "top:pref"); 
    CellConstraints cc = new CellConstraints();
    DefaultFormBuilder fb = new DefaultFormBuilder(layout, Translation.INSTANCE.getBundle());
    
    fb.add(slider, cc.xy(1, 1));
    fb.add(spinner, cc.xy(2, 1));
    
    dashboard.add(fb.getPanel(), BorderLayout.CENTER);
    add(dashboard, BorderLayout.SOUTH);
    switchColumn(0);
  }

  private JFreeChart createChart() {
    dataset = createDataset();
    JFreeChart chart = ChartFactory.createHistogram("titel", Translation.INSTANCE.get("misc.graphic.value"), Translation.INSTANCE.get("misc.graphic.frequency"), dataset, PlotOrientation.VERTICAL, false, true, false);
    return chart;
  }

  private SimpleHistogramDataset createDataset() {
    this.dataset = new SimpleHistogramDataset("");
    dataset.setAdjustForBinSize(false);
    return dataset;
  }

  public void stateChanged(ChangeEvent e) {
    if (e.getSource() == slider) {
      int value = this.slider.getValue();
      spinner.setValue(value);
      switchColumn(value);
    } else {
      int value = (Integer) this.spinner.getValue();
      slider.setValue(value);
      switchColumn(value);      
    }
  }
  
  private void switchColumn(int column) {
    chart.setTitle(Translation.INSTANCE.get("misc.graphic.singleHistogramColumn") + " " + column);
    DVector data = m.col(column);
    int numOfbins = (int) (data.length()/20) + 10;
    
    double min = data.min();
    double max = data.max();
    double spacing = (max-min)/numOfbins;
    double currentBound = min;
    dataset.removeAllBins();
    for (int i = 0; i < numOfbins-1; i++) {
      dataset.addBin(new SimpleHistogramBin(currentBound, (currentBound + spacing), true, false));
      currentBound += spacing;
    }
    //ensure that the maximum is included and not lost because of numerical problems
    dataset.addBin(new SimpleHistogramBin(currentBound, max, true, true));
    
    dataset.clearObservations();
    dataset.addObservations(data.getValues());
    dataset.seriesChanged(null);
  }
}