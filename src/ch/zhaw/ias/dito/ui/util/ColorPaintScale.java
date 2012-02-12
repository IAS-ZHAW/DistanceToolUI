/* released under bsd licence
 * see LICENCE file or http://www.opensource.org/licenses/bsd-license.php for details
 * Institute of Applied Simulation (ZHAW)
 * Author Thomas Niederberger
 */
package ch.zhaw.ias.dito.ui.util;

import java.awt.Color;
import java.awt.Paint;

import org.jfree.chart.renderer.PaintScale;

/**
 * A Color scale supporting a color range from red to blue on the HSB color space. 
 * @author Thomas Niederberger (nith) - institute of applied simulation (IAS)
 */
public class ColorPaintScale implements PaintScale {
  private double lowerBound;
  private double upperBound;
  private double step;
  
  public ColorPaintScale(double lowerBound, double upperBound) {
    this.lowerBound = lowerBound;
    this.upperBound = upperBound;
    //0.666 is chosen because this will deliver a blue color for the upper bound.
    this.step = 0.666/(upperBound - lowerBound);
  }
  
  @Override
  public Paint getPaint(double value) {
    return Color.getHSBColor((float) (step*(value-lowerBound)), 1.0f, 1.0f);
  }
  
  @Override
  public double getLowerBound() {
    return lowerBound;
  }
  
  @Override
  public double getUpperBound() {
    return upperBound;
  }
}
