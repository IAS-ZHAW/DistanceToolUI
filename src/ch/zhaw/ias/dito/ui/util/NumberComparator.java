/* released under bsd licence
 * see LICENCE file or http://www.opensource.org/licenses/bsd-license.php for details
 * Institute of Applied Simulation (ZHAW)
 * Author Thomas Niederberger
 */
package ch.zhaw.ias.dito.ui.util;

import java.util.Comparator;

public class NumberComparator implements Comparator<Number> {
  public int compare(Number n1, Number n2) {
    return (int) Math.signum(n1.doubleValue() - n2.doubleValue());
  }
}