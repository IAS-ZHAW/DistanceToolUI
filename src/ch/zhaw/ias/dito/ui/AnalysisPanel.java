package ch.zhaw.ias.dito.ui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JTabbedPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.PaintScale;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.chart.title.PaintScaleLegend;
import org.jfree.data.statistics.SimpleHistogramBin;
import org.jfree.data.statistics.SimpleHistogramDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYZDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.netbeans.validation.api.ui.ValidationGroup;

import ch.zhaw.ias.dito.DVector;
import ch.zhaw.ias.dito.Matrix;
import ch.zhaw.ias.dito.ui.resource.Translation;
import ch.zhaw.ias.dito.ui.util.ColorPaintScale;
import ch.zhaw.ias.dito.ui.util.SingleHistogramPanel;
import ch.zhaw.ias.dito.ui.util.MatrixXYZDataset;

public class AnalysisPanel extends DitoPanel {
  private Matrix distanceMatrix;
  private JTabbedPane tabs = new JTabbedPane();
  
  public AnalysisPanel(ValidationGroup validationGroup) {
    super(ScreenEnum.ANALYSIS, ScreenEnum.METHOD, ScreenEnum.OUTPUT, validationGroup);
    distanceMatrix = Config.INSTANCE.getDistanceMatrix();
    MatrixXYZDataset dataset = new MatrixXYZDataset(distanceMatrix);
    
    String title = Translation.INSTANCE.get("misc.graphic.histogram");
    JFreeChart chart = createHistogramChart(title);
    tabs.addTab(title, new ChartPanel(chart));
    
    title = Translation.INSTANCE.get("misc.graphic.singleHistogram");
    tabs.addTab(title, new SingleHistogramPanel(distanceMatrix));
    
    title = Translation.INSTANCE.get("misc.graphic.block");
    chart = createColorChart(title, dataset);
    ChartPanel chartPanel = new ChartPanel(chart);
    chartPanel.setDisplayToolTips(true);
    tabs.addTab(title, chartPanel);
    this.setLayout(new BorderLayout());
    this.add(tabs, BorderLayout.CENTER);
  }
  
  @Override
  public void saveToModel() {
    // nothing to be saved
  }
  
  private JFreeChart createColorChart(String title, XYZDataset dataset) {
    NumberAxis xAxis = new NumberAxis();
    xAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
    xAxis.setLowerMargin(0.0);
    xAxis.setUpperMargin(0.0);
    NumberAxis yAxis = new NumberAxis();
    yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
    yAxis.setLowerMargin(0.0);
    yAxis.setUpperMargin(0.0);
    XYBlockRenderer renderer = new XYBlockRenderer();
    renderer.setBaseToolTipGenerator(new XYToolTipGenerator() {
      @Override
      public String generateToolTip(XYDataset dataset, int series, int item) {
        XYZDataset xyzDataset = (XYZDataset)dataset;
        double x = xyzDataset.getXValue(series, item);
        double y = xyzDataset.getYValue(series, item);
        double z = xyzDataset.getZValue(series, item);
        return ("X=" + x + ", Y=" + y + ", Z=" + z);
      }
    });
    PaintScale scale = new ColorPaintScale(distanceMatrix.extremum(false), distanceMatrix.extremum(true));
    renderer.setPaintScale(scale);
    ValueAxis axis = new NumberAxis();
    axis.setLowerBound(scale.getLowerBound());
    axis.setUpperBound(scale.getUpperBound());
    PaintScaleLegend legend = new PaintScaleLegend(scale, axis);
    legend.setMargin(new RectangleInsets(10, 10, 10, 10));
    legend.setPosition(RectangleEdge.RIGHT);

    XYPlot plot = new XYPlot(dataset, xAxis, yAxis, renderer);
    JFreeChart chart = new JFreeChart(title, plot);
    chart.removeLegend();
    chart.addSubtitle(legend);
    chart.setBackgroundPaint(Color.white);
    return chart;
  }
  
  private JFreeChart createHistogramChart(String title) {
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
}
