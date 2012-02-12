/* released under bsd licence
 * see LICENCE file or http://www.opensource.org/licenses/bsd-license.php for details
 * Institute of Applied Simulation (ZHAW)
 * Author Thomas Niederberger
 */
package ch.zhaw.ias.dito.ui.util;

import java.awt.BorderLayout;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JFrame;

import org.jdesktop.swingx.JXFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;

import ch.zhaw.ias.dito.config.Question;
import ch.zhaw.ias.dito.ui.resource.Translation;

public class HistogramFrame extends JXFrame {
  
  public HistogramFrame(Question q) {
    String title = Translation.INSTANCE.get("misc.graphic.histogram") + " " + q.getName();
    setTitle(title);
    HistogramDataset hist = new HistogramDataset();
    hist.setType(HistogramType.FREQUENCY);
    
    Set<Double> values = new TreeSet<Double>();
    q.getData().addValuesToCollection(values);
    int numOfBins = Math.min(values.size(), (q.getData().filteredLength()/20) + 10);
    hist.addSeries(q.getName(), q.getData().getValues(), numOfBins);
    
    JFreeChart chart = ChartFactory.createHistogram(title, Translation.INSTANCE.get("misc.graphic.value"), Translation.INSTANCE.get("misc.graphic.frequency"), hist, PlotOrientation.VERTICAL, false, true, false);
    this.add(new ChartPanel(chart),  BorderLayout.CENTER);
    
    this.setSize(300, 300);
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    this.setVisible(true);
  }
}
