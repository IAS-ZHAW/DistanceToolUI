package ch.zhaw.ias.dito.ui.util;

import java.util.Comparator;

public class NumberComparator implements Comparator<Number> {
  public int compare(Number n1, Number n2) {
    return (int) Math.signum(n1.doubleValue() - n2.doubleValue());
  }
}