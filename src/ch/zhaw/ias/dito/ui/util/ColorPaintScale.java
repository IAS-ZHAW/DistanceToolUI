package ch.zhaw.ias.dito.ui.util;

import java.awt.Color;
import java.awt.Paint;

import org.jfree.chart.renderer.PaintScale;

public class ColorPaintScale implements PaintScale {
  private double lowerBound;
  private double upperBound;
  private double step;
  
  public ColorPaintScale(double lowerBound, double upperBound) {
    this.lowerBound = lowerBound;
    this.upperBound = upperBound;
    this.step = (upperBound - lowerBound)/255;
  }
  
  @Override
  public Paint getPaint(double value) {
    return Color.getHSBColor((float) (step*(value-lowerBound)), 255.0f, 127.0f);
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
