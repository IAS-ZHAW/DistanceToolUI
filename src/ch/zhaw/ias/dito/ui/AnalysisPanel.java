package ch.zhaw.ias.dito.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.GrayPaintScale;
import org.jfree.chart.renderer.LookupPaintScale;
import org.jfree.chart.renderer.PaintScale;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.data.statistics.SimpleHistogramBin;
import org.jfree.data.statistics.SimpleHistogramDataset;
import org.jfree.data.xy.XYZDataset;
import org.netbeans.validation.api.ui.ValidationGroup;

import ch.zhaw.ias.dito.DVector;
import ch.zhaw.ias.dito.Matrix;
import ch.zhaw.ias.dito.ui.resource.Translation;
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
    tabs.addTab(title, new ChartPanel(chart));
    this.setLayout(new BorderLayout());
    this.add(tabs, BorderLayout.CENTER);
  }
  
  @Override
  public void saveToModel() {
    // TODO Auto-generated method stub
    
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
    //PaintScale scale = new ColorPaintScale(distanceMatrix.extremum(false), distanceMatrix.extremum(true));
    
    PaintScale scale = new GrayPaintScale(distanceMatrix.extremum(false), distanceMatrix.extremum(true));
    renderer.setPaintScale(scale);
    XYPlot plot = new XYPlot(dataset, xAxis, yAxis, renderer);
    //plot.setBackgroundPaint(Color.lightGray);
    //plot.setDomainGridlinesVisible(false);
    //plot.setRangeGridlinePaint(Color.white);
    JFreeChart chart = new JFreeChart(title, plot);
    chart.removeLegend();
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
        
    int summe = 0;
    for (int i = 0; i < distanceMatrix.getColCount(); i++) {
      DVector v = distanceMatrix.col(i);
      dataset.addObservations(v.getValues());
      summe += v.getValues().length;
    }
    return ChartFactory.createHistogram(title, Translation.INSTANCE.get("misc.graphic.distance"), Translation.INSTANCE.get("misc.graphic.frequency"), dataset, PlotOrientation.VERTICAL, false, true, false);
  }
}
