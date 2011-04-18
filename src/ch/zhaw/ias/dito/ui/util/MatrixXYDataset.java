package ch.zhaw.ias.dito.ui.util;

import org.jfree.data.DomainOrder;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.general.DatasetGroup;
import org.jfree.data.xy.XYZDataset;

import ch.zhaw.ias.dito.DVector;
import ch.zhaw.ias.dito.Matrix;

public class MatrixXYDataset implements XYZDataset {
  private Matrix m;

  public MatrixXYDataset(Matrix m) {
    this.m = m;
  }
  
  @Override
  public int getItemCount(int series) {
    return m.getRowCount() * m.getColCount();
  }

  @Override
  public Number getX(int series, int item) {
    return new Double(getXValue(series, item));
  }

  @Override
  public double getXValue(int series, int item) {
    return item / m.getColCount();
  }

  @Override
  public Number getY(int series, int item) {
    return new Double(getYValue(series, item));
  }

  @Override
  public double getYValue(int series, int item) {
    return item % m.getColCount();
  }
  
  @Override
  public Number getZ(int series, int item) {
    return new Double(getZValue(series, item));
  }

  @Override
  public double getZValue(int series, int item) {
    int row = (int) getXValue(series, item);
    int col = (int) getYValue(series, item);
    DVector v = m.col(col);
    return v.component(row);
  }

  @Override
  public int getSeriesCount() {
    return 1;
  }

  @Override
  public Comparable getSeriesKey(int series) {
    return "Test";
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
