package ch.zhaw.ias.dito.ui.util;

import org.jfree.data.DomainOrder;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.general.DatasetGroup;
import org.jfree.data.xy.XYDataset;

public class MdsXYDataset implements XYDataset {
  private double[][] values;

  public MdsXYDataset(double[][] values) {
    this.values = values;
  }
  
  @Override
  public int getItemCount(int series) {
    return values.length;
  }

  @Override
  public Number getX(int series, int item) {
    return new Double(getXValue(series, item));
  }

  @Override
  public double getXValue(int series, int item) {
    return values[item][0];
  }

  @Override
  public Number getY(int series, int item) {
    return new Double(getYValue(series, item));
  }

  @Override
  public double getYValue(int series, int item) {
    return values[item][1];
  }

  @Override
  public int getSeriesCount() {
    return 1;
  }

  @Override
  public Comparable getSeriesKey(int series) {
    return "item #" + series;
  }

  @Override
  public int indexOf(Comparable seriesKey) {
    return 0;
  }

  @Override
  public void addChangeListener(DatasetChangeListener listener) {
    
  }

  @Override
  public DatasetGroup getGroup() {
    return null;
  }

  @Override
  public void removeChangeListener(DatasetChangeListener listener) {

  }

  @Override
  public void setGroup(DatasetGroup group) {
    
  }
  
  @Override
  public DomainOrder getDomainOrder() {
    return DomainOrder.ASCENDING;
  }
}
