package ch.zhaw.ias.dito.ui.util;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.PaintScale;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.chart.title.PaintScaleLegend;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYZDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;

import ch.zhaw.ias.dito.Matrix;

public class BlockPlotPanel extends JPanel {
  private JFreeChart chart;
  private XYPlot plot; 

  public BlockPlotPanel(Matrix m) {
    this(m, m.extremum(false), m.extremum(true));
  }
  
  public BlockPlotPanel(Matrix m, double lowerBound, double upperBound) {
    super(new BorderLayout());
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
    PaintScale scale = new ColorPaintScale(lowerBound, upperBound);
    renderer.setPaintScale(scale);
    ValueAxis axis = new NumberAxis();
    axis.setLowerBound(scale.getLowerBound());
    axis.setUpperBound(scale.getUpperBound());
    PaintScaleLegend legend = new PaintScaleLegend(scale, axis);
    legend.setMargin(new RectangleInsets(10, 10, 10, 10));
    legend.setPosition(RectangleEdge.RIGHT);

    MatrixXYDataset dataset = new MatrixXYDataset(m);
    plot = new XYPlot(dataset, xAxis, yAxis, renderer);
    chart = new JFreeChart(plot);
    chart.removeLegend();
    chart.addSubtitle(legend);
    chart.setBackgroundPaint(Color.white);
    
    ChartPanel chartPanel = new ChartPanel(chart);
    chartPanel.setDisplayToolTips(true);
    this.add(chartPanel, BorderLayout.CENTER);
  }
}