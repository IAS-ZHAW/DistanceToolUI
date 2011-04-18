package ch.zhaw.ias.dito.ui.util;
import javax.swing.*;

import org.math.plot.*;

import ch.zhaw.ias.dito.DVector;
import ch.zhaw.ias.dito.dist.DistanceMethodEnum;
import ch.zhaw.ias.dito.dist.DistanceSpec;

import static org.math.array.DoubleArray.*;

public class DistancePlot {
  private DistanceSpec spec;
  
  public DistancePlot(DistanceMethodEnum method) {
    this.spec = method.getSpec();
    // define your data
    double[] x = increment(0.1, 0.1, 5.0);
    double[] y = increment(0.1, 0.1, 5.0);
    double[][] z1 = f1(x, y);

    // create your PlotPanel (you can use it as a JPanel) with a legend at SOUTH
    Plot3DPanel plot = new Plot3DPanel("SOUTH");
    plot.setEditable(false);

    // add grid plot to the PlotPanel
    plot.addGridPlot("Distanz", x, y, z1);

    // put the PlotPanel in a JFrame like a JPanel
    JFrame frame = new JFrame(method.getName());
    frame.setSize(600, 600);
    frame.setContentPane(plot);
    frame.setVisible(true);
  }

  public double f1(double x, double y) {
    return spec.distance(new DVector(x, x, x), new DVector(y, y, y));
  }

  public double[][] f1(double[] x, double[] y) {
    double[][] z = new double[y.length][x.length];
    for (int i = 0; i < x.length; i++) {
      for (int j = 0; j < y.length; j++) {
        z[j][i] = f1(x[i], y[j]);
      }
    }
    return z;
  }
}
