package ch.zhaw.ias.dito.ui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.SimpleHistogramBin;
import org.jfree.data.statistics.SimpleHistogramDataset;
import org.netbeans.validation.api.ui.ValidationGroup;

import ch.zhaw.ias.dito.DVector;
import ch.zhaw.ias.dito.Matrix;
import ch.zhaw.ias.dito.MdsHelper;
import ch.zhaw.ias.dito.ui.resource.Translation;
import ch.zhaw.ias.dito.ui.util.BlockPlotPanel;
import ch.zhaw.ias.dito.ui.util.MdsXYDataset;
import ch.zhaw.ias.dito.ui.util.SingleHistogramPanel;

public class AnalysisPanel extends DitoPanel {
  //private Matrix distanceMatrix;
  private JTabbedPane tabs = new JTabbedPane();
  
  public AnalysisPanel() {
    super(ScreenEnum.ANALYSIS, ScreenEnum.METHOD, ScreenEnum.OUTPUT);
    Matrix distanceMatrix = Config.INSTANCE.getDistanceMatrix();
    
    String title = Translation.INSTANCE.get("misc.graphic.histogram");
    JFreeChart chart = createHistogramChart(title, distanceMatrix);
    tabs.addTab(title, new ChartPanel(chart));
    
    title = Translation.INSTANCE.get("misc.graphic.singleHistogram");
    tabs.addTab(title, new SingleHistogramPanel(distanceMatrix));
    
    //those analysis tools are only supported for datasets smaller than 500
    if (distanceMatrix.getColCount() <= 500) {
      title = Translation.INSTANCE.get("misc.graphic.block");
      tabs.addTab(title, new BlockPlotPanel(distanceMatrix));
      
      title = Translation.INSTANCE.get("misc.graphic.mds");
      tabs.addTab(title, new ChartPanel(createMdsChart(title, distanceMatrix)));
    } else {
      String tooBig = Translation.INSTANCE.get("misc.graphic.bigDataset");
      title = Translation.INSTANCE.get("misc.graphic.block");
      title += " [" + tooBig + "]";
      tabs.addTab(title, new JPanel());
      tabs.setEnabledAt(2, false);
      
      title = Translation.INSTANCE.get("misc.graphic.mds");
      title += " [" + tooBig + "]";
      tabs.addTab(title, new JPanel());
      tabs.setEnabledAt(3, false);
    }
    
    this.setLayout(new BorderLayout());
    this.add(tabs, BorderLayout.CENTER);
  }
  
  @Override
  public void saveToModel() {
    // nothing to be saved
  }
    
  private JFreeChart createHistogramChart(String title, Matrix distanceMatrix) {
    int NUM_OF_BINS = 50;
    SimpleHistogramDataset dataset = new SimpleHistogramDataset("");
    dataset.setAdjustForBinSize(false);
    double min = distanceMatrix.extremum(false);
    double max = distanceMatrix.extremum(true);
    double spacing = (max-min)/NUM_OF_BINS;
    double currentBound = min;

    for (int i = 0; i < NUM_OF_BINS-1; i++) {
      dataset.addBin(new SimpleHistogramBin(currentBound, (currentBound + spacing), true, false));
      currentBound += spacing;
    }
    //ensure that the maximum is included and not lost because of numerical problems
    dataset.addBin(new SimpleHistogramBin(currentBound, max, true, true));
    for (int i = 0; i < distanceMatrix.getColCount(); i++) {
      DVector v = distanceMatrix.col(i);
      dataset.addObservations(v.getValues());
    }
    return ChartFactory.createHistogram(title, Translation.INSTANCE.get("misc.graphic.distance"), Translation.INSTANCE.get("misc.graphic.frequency"), dataset, PlotOrientation.VERTICAL, false, true, false);
  }
  
  private JFreeChart createMdsChart(String title, Matrix distanceMatrix) {
    double[][] values = MdsHelper.getMds(distanceMatrix);
    //return ChartFactory.createHistogram(
    return ChartFactory.createScatterPlot(title, null, null, new MdsXYDataset(values), PlotOrientation.VERTICAL, false, true, false);
  }  
}
