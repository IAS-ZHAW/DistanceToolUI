package ch.zhaw.ias.dito.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.xml.bind.JAXBException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.statistics.SimpleHistogramBin;
import org.jfree.data.statistics.SimpleHistogramDataset;
import org.jfree.data.xy.XYBarDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.netbeans.validation.api.ui.ValidationGroup;

import ch.zhaw.ias.dito.DVector;
import ch.zhaw.ias.dito.Matrix;
import ch.zhaw.ias.dito.MdsDecomposition;
import ch.zhaw.ias.dito.ui.resource.Translation;
import ch.zhaw.ias.dito.ui.util.BlockPlotPanel;
import ch.zhaw.ias.dito.ui.util.HelpArea;
import ch.zhaw.ias.dito.ui.util.MdsXYDataset;
import ch.zhaw.ias.dito.ui.util.SingleHistogramPanel;

public class AnalysisPanel extends DitoPanel implements ActionListener {
  private JTabbedPane tabs = new JTabbedPane();
  private JComboBox dimensionsCombo;
  //private double[][] mdsValues;
  private MdsDecomposition decomp;
  
  public AnalysisPanel(HelpArea helpArea) {
    super(ScreenEnum.ANALYSIS, ScreenEnum.METHOD, ScreenEnum.OUTPUT);
    Matrix distanceMatrix = Config.INSTANCE.getDistanceMatrix();
    
    String title = Translation.INSTANCE.get("misc.graphic.histogram");
    JFreeChart chart = createHistogramChart(title, distanceMatrix);
    tabs.addTab(title, new ChartPanel(chart));
    
    title = Translation.INSTANCE.get("misc.graphic.singleHistogram");
    tabs.addTab(title, new SingleHistogramPanel(distanceMatrix));
    
    //those analysis tools are only supported for datasets smaller than 500
    if (distanceMatrix.getColCount() <= 500) {
      title = Translation.INSTANCE.get("misc.graphic.block");
      tabs.addTab(title, new BlockPlotPanel(distanceMatrix));
      
      title = Translation.INSTANCE.get("misc.graphic.mds");
      decomp = new MdsDecomposition(distanceMatrix);
      double[][] mdsValues = decomp.getMds();
      
      JPanel mdsPanel = new JPanel();
      mdsPanel.setLayout(new BoxLayout(mdsPanel, BoxLayout.PAGE_AXIS));
      mdsPanel.add(new ChartPanel(createMdsChart(title, mdsValues)));
      dimensionsCombo = new JComboBox(new Integer[] {2, 3});
      JButton exportButton = new JButton("Export MDS TODO");
      exportButton.addActionListener(this);
      mdsPanel.add(dimensionsCombo);
      mdsPanel.add(exportButton);
      
      tabs.addTab(title, mdsPanel);
      title = Translation.INSTANCE.get("misc.graphic.eigenvalues");
      tabs.addTab(title, new ChartPanel(createEigenvaluesChart(title, decomp)));
    } else {
      String tooBig = Translation.INSTANCE.get("misc.graphic.bigDataset");
      title = Translation.INSTANCE.get("misc.graphic.block");
      title += " [" + tooBig + "]";
      tabs.addTab(title, new JPanel());
      tabs.setEnabledAt(2, false);
      
      title = Translation.INSTANCE.get("misc.graphic.mds");
      title += " [" + tooBig + "]";
      tabs.addTab(title, new JPanel());
      tabs.setEnabledAt(3, false);
    }
    
    this.setLayout(new BorderLayout());
    this.add(tabs, BorderLayout.CENTER);
  }
  
  @Override
  public void saveToModel() {
    // nothing to be saved
  }
    
  private JFreeChart createHistogramChart(String title, Matrix distanceMatrix) {
    int NUM_OF_BINS = 50;
    SimpleHistogramDataset dataset = new SimpleHistogramDataset("");
    dataset.setAdjustForBinSize(false);
    double min = distanceMatrix.extremum(false);
    double max = distanceMatrix.extremum(true);
    double spacing = (max-min)/NUM_OF_BINS;
    double currentBound = min;

    for (int i = 0; i < NUM_OF_BINS-1; i++) {
      dataset.addBin(new SimpleHistogramBin(currentBound, (currentBound + spacing), true, false));
      currentBound += spacing;
    }
    //ensure that the maximum is included and not lost because of numerical problems
    dataset.addBin(new SimpleHistogramBin(currentBound, max, true, true));
    for (int i = 0; i < distanceMatrix.getColCount(); i++) {
      DVector v = distanceMatrix.col(i);
      dataset.addObservations(v.getValues());
    }
    return ChartFactory.createHistogram(title, Translation.INSTANCE.get("misc.graphic.distance"), Translation.INSTANCE.get("misc.graphic.frequency"), dataset, PlotOrientation.VERTICAL, false, true, false);
  }
  
  private JFreeChart createMdsChart(String title, double[][] mdsValues) {
    JFreeChart chart = ChartFactory.createScatterPlot(title, null, null, new MdsXYDataset(mdsValues), PlotOrientation.VERTICAL, false, true, false);
    XYPlot plot = (XYPlot) chart.getPlot();
    plot.getRenderer().setBaseToolTipGenerator(new XYToolTipGenerator() {
      @Override
      public String generateToolTip(XYDataset dataset, int series, int item) {
        return "item #" + (item + 1);
      }
    });
    return chart;
  }
  
  private JFreeChart createEigenvaluesChart(String title, MdsDecomposition decomp) {
    double[] values = decomp.getSortedEigenvalues();

    XYSeries series = new XYSeries("Series 1");
    for (int i = 0; i < values.length; i++) {
      series.add((i+1), values[i]);
    }
    XYSeriesCollection collection = new XYSeriesCollection();
    collection.addSeries(series);
    XYBarDataset dataset = new XYBarDataset(collection, 0.9);

    return ChartFactory.createXYBarChart(title, null, false, null, dataset, PlotOrientation.VERTICAL, false, true, false);
  }
  
  @Override
  public void actionPerformed(ActionEvent e) {
    JFileChooser fileChooser = new JFileChooser();
    Integer dimensions = (Integer) dimensionsCombo.getSelectedItem();
    double[][] mdsValues = decomp.getMds(dimensions);
    int returnVal = fileChooser.showSaveDialog(this);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      File file = fileChooser.getSelectedFile();        
      Matrix m = Matrix.createDoubleMatrix(mdsValues);
      try {
        Matrix.writeToFile(m, file.getAbsolutePath(), ';', 5);
      } catch (IOException ex) {
        // TODO Auto-generated catch block
        ex.printStackTrace();
      }
    }
  }
}
