
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Paint;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.jdesktop.swingx.JXFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.GrayPaintScale;
import org.jfree.chart.renderer.LookupPaintScale;
import org.jfree.chart.renderer.PaintScale;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;
import org.jfree.data.xy.XYZDataset;

import ch.zhaw.ias.dito.DVector;
import ch.zhaw.ias.dito.Matrix;
import ch.zhaw.ias.dito.config.Question;
import ch.zhaw.ias.dito.ui.util.MatrixXYZDataset;


public class JFreeChartFrame extends JXFrame {
  
  public JFreeChartFrame() {
    //XYd
    /*HistogramDataset hist = new HistogramDataset();
    hist.setType(HistogramType.FREQUENCY);
    hist.addSeries(q.getName(), q.getData().getValues(), 10);*/
    Matrix m;
    try {
      m = Matrix.readFromFile(new File("c:/daten/java-workspace/DistanceToolCore/testdata/m200x130rand.csv"), ',');
      MatrixXYZDataset matrixDataset = new MatrixXYZDataset(m);
      JFreeChart chart = createChart(matrixDataset);//ChartFactory.createHistogram(q.getName(), "Wert", "Häufigkeit", hist, PlotOrientation.VERTICAL, false, true, false);
          
      BufferedImage image = chart.createBufferedImage(500,300);

      JLabel lblChart = new JLabel();
      lblChart.setIcon(new ImageIcon(image));
      this.add(lblChart, BorderLayout.CENTER);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    this.setSize(1000, 1000);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setVisible(true);
  }
  
  private static JFreeChart createChart(XYZDataset dataset) {
    NumberAxis xAxis = new NumberAxis("X");
    xAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
    xAxis.setLowerMargin(0.0);
    xAxis.setUpperMargin(0.0);
    NumberAxis yAxis = new NumberAxis("Y");
    yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
    yAxis.setLowerMargin(0.0);
    yAxis.setUpperMargin(0.0);
    XYBlockRenderer renderer = new XYBlockRenderer();
    //PaintScale scale = new LookupPaintScale(0, 16, Paint);
    PaintScale scale = new GrayPaintScale(0, 16.0);
    renderer.setPaintScale(scale);
    XYPlot plot = new XYPlot(dataset, xAxis, yAxis, renderer);
    plot.setBackgroundPaint(Color.lightGray);
    plot.setDomainGridlinesVisible(false);
    plot.setRangeGridlinePaint(Color.white);
    JFreeChart chart = new JFreeChart("XYBlockChartDemo1", plot);
    chart.removeLegend();
    chart.setBackgroundPaint(Color.white);
    return chart;
  }


  
  public static void main(String... args) {
    new JFreeChartFrame();
  }
}
