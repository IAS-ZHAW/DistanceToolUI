package ch.zhaw.ias.dito.ui.util;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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

import ch.zhaw.ias.dito.DVector;
import ch.zhaw.ias.dito.Matrix;
import ch.zhaw.ias.dito.ui.resource.Translation;

public class BlockPlotPanel extends JPanel implements ChangeListener {
  private Matrix m;
  private JFreeChart chart;
  private JSlider slider;
  private XYPlot plot; 

  public BlockPlotPanel(Matrix m) {
    super(new BorderLayout());
    this.m = m;
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
    PaintScale scale = new ColorPaintScale(m.extremum(false), m.extremum(true));
    renderer.setPaintScale(scale);
    ValueAxis axis = new NumberAxis();
    axis.setLowerBound(scale.getLowerBound());
    axis.setUpperBound(scale.getUpperBound());
    PaintScaleLegend legend = new PaintScaleLegend(scale, axis);
    legend.setMargin(new RectangleInsets(10, 10, 10, 10));
    legend.setPosition(RectangleEdge.RIGHT);

    MatrixXYZDataset dataset = new MatrixXYZDataset(m);
    plot = new XYPlot(dataset, xAxis, yAxis, renderer);
    chart = new JFreeChart(plot);
    chart.removeLegend();
    chart.addSubtitle(legend);
    chart.setBackgroundPaint(Color.white);
    
    ChartPanel chartPanel = new ChartPanel(chart);
    chartPanel.setDisplayToolTips(true);
    this.add(chartPanel, BorderLayout.CENTER);
    
    this.slider = new JSlider(0, m.getColCount()-1, 0);
    slider.setPaintLabels(true);
    slider.setMajorTickSpacing(50);
    slider.setPaintTicks(true);
    this.slider.addChangeListener(this);    
    this.add(slider, BorderLayout.SOUTH);
  }

  public void stateChanged(ChangeEvent event) {
    int value = this.slider.getValue();
    switchColumn(value);
  }
  
  private void switchColumn(int column) {
    chart.setTitle(Translation.INSTANCE.get("misc.graphic.singleHistogramColumn") + " " + column);
    this.m = m.sortBy(column);
    plot.setDataset(new MatrixXYZDataset(m));
    plot.datasetChanged(null);
  }
}