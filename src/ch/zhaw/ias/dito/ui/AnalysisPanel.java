package ch.zhaw.ias.dito.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.xml.bind.JAXBException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.VectorRenderer;
import org.jfree.data.statistics.SimpleHistogramBin;
import org.jfree.data.statistics.SimpleHistogramDataset;
import org.jfree.data.xy.VectorSeries;
import org.jfree.data.xy.VectorSeriesCollection;
import org.jfree.data.xy.VectorXYDataset;
import org.jfree.data.xy.XYBarDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.netbeans.validation.api.ui.ValidationGroup;

import ch.zhaw.ias.dito.DVector;
import ch.zhaw.ias.dito.DecompositionException;
import ch.zhaw.ias.dito.DistanceAlgorithm;
import ch.zhaw.ias.dito.EigenvalueDecomposition;
import ch.zhaw.ias.dito.Matrix;
import ch.zhaw.ias.dito.MdsDecomposition;
import ch.zhaw.ias.dito.PcaDecomposition;
import ch.zhaw.ias.dito.dist.DistanceMethodEnum;
import ch.zhaw.ias.dito.ui.resource.Translation;
import ch.zhaw.ias.dito.ui.util.BlockPlotPanel;
import ch.zhaw.ias.dito.ui.util.HelpArea;
import ch.zhaw.ias.dito.ui.util.MdsXYDataset;
import ch.zhaw.ias.dito.ui.util.SingleHistogramPanel;

public class AnalysisPanel extends DitoPanel implements ActionListener {
  private JTabbedPane tabs = new JTabbedPane();
  private JComboBox dimensionsCombo;
  private double[][] mdsValues;
  private EigenvalueDecomposition decomp;
  
  public AnalysisPanel(HelpArea helpArea) {
    super(ScreenEnum.ANALYSIS, ScreenEnum.METHOD, ScreenEnum.OUTPUT);
    Matrix distanceMatrix = Config.INSTANCE.getDistanceMatrix();
    
    String title = Translation.INSTANCE.get("misc.graphic.histogram");
    JFreeChart chart = createHistogramChart(title, distanceMatrix);
    tabs.addTab(title, new ChartPanel(chart));
    
    title = Translation.INSTANCE.get("misc.graphic.singleHistogram");
    tabs.addTab(title, new SingleHistogramPanel(distanceMatrix));
    
    //euclidian distance -> execute PCA
    if (Config.INSTANCE.getDitoConfig().getMethod().getMethod() == DistanceMethodEnum.get("Euklid")) {
      title = Translation.INSTANCE.get("misc.graphic.block");
      tabs.addTab(title, new BlockPlotPanel(distanceMatrix));
      
      title = Translation.INSTANCE.get("misc.graphic.pca");
      try {
        DistanceAlgorithm algo = new DistanceAlgorithm(Config.INSTANCE.getDitoConfig(), true);
        decomp = new PcaDecomposition(algo.getRescaledOfFiltered());
        mdsValues = decomp.getReducedDimensions();
        tabs.addTab(title, createDecompositionPanel(title, mdsValues));
        mdsValues = ((PcaDecomposition) decomp).getQuestions(2);
        title = Translation.INSTANCE.get("misc.graphic.question");
        tabs.addTab(title, new ChartPanel(createQuestionChart(title, mdsValues)));
        title = Translation.INSTANCE.get("misc.graphic.eigenvalues");
        tabs.addTab(title, new ChartPanel(createEigenvaluesChart(title, decomp)));
      } catch (DecompositionException e) {
        String error = Translation.INSTANCE.get(e.getMessage());
        title += " [" + error + "]";
        tabs.addTab(title, new JPanel());
        title = Translation.INSTANCE.get("misc.graphic.question");
        title += " [" + error + "]";
        tabs.addTab(title, new JPanel());
        title = Translation.INSTANCE.get("misc.graphic.eigenvalues");
        title += " [" + error + "]";
        tabs.addTab(title, new JPanel());
      }
    } else {
      //those analysis tools are only supported for datasets smaller than 500
      if (distanceMatrix.getColCount() <= 500) {
        title = Translation.INSTANCE.get("misc.graphic.block");
        tabs.addTab(title, new BlockPlotPanel(distanceMatrix));
        
        try {
          title = Translation.INSTANCE.get("misc.graphic.mds");
          decomp = new MdsDecomposition(distanceMatrix);
          double[][] mdsValues = decomp.getReducedDimensions();
          tabs.addTab(title, createDecompositionPanel(title, mdsValues));
          
          title = Translation.INSTANCE.get("misc.graphic.eigenvalues");
          tabs.addTab(title, new ChartPanel(createEigenvaluesChart(title, decomp)));
        } catch (DecompositionException e) {
          String error = Translation.INSTANCE.get(e.getMessage());
          title = Translation.INSTANCE.get("misc.graphic.question");
          title += " [" + error + "]";
          tabs.addTab(title, new JPanel());
          title = Translation.INSTANCE.get("misc.graphic.eigenvalues");
          title += " [" + error + "]";
          tabs.addTab(title, new JPanel());
        }
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
  
  private JPanel createDecompositionPanel(String title, double[][] mdsValues) {
    JFreeChart chart = ChartFactory.createScatterPlot(title, null, null, new MdsXYDataset(mdsValues), PlotOrientation.VERTICAL, false, true, false);
    XYPlot plot = (XYPlot) chart.getPlot();
    plot.getRenderer().setBaseToolTipGenerator(new XYToolTipGenerator() {
      @Override
      public String generateToolTip(XYDataset dataset, int series, int item) {
        return "item #" + (item + 1);
      }
    });
    
    JPanel mdsPanel = new JPanel(new BorderLayout(0, 10));
    mdsPanel.add(new ChartPanel(chart), BorderLayout.CENTER);
    dimensionsCombo = new JComboBox(new Integer[] {2, 3});
    JButton exportButton = new JButton(Translation.INSTANCE.get("s4.bu.exportDecomposition"));
    exportButton.addActionListener(this);
    JPanel mdsInputPanel = new JPanel();
    mdsInputPanel.setLayout(new BoxLayout(mdsInputPanel, BoxLayout.LINE_AXIS));
    mdsInputPanel.add(Box.createHorizontalGlue());
    mdsInputPanel.add(Box.createHorizontalGlue());
    mdsInputPanel.add(new JLabel(Translation.INSTANCE.get("s4.lb.dimension")));
    mdsInputPanel.add(Box.createHorizontalStrut(10));
    mdsInputPanel.add(dimensionsCombo);
    mdsInputPanel.add(Box.createHorizontalStrut(10));
    mdsInputPanel.add(exportButton);
    mdsPanel.add(mdsInputPanel, BorderLayout.SOUTH);   
    
    return mdsPanel;
  }
  
  private JFreeChart createQuestionChart(String title, double[][] values) {
    JFreeChart chart = ChartFactory.createScatterPlot(title, null, null, new MdsXYDataset(values), PlotOrientation.VERTICAL, false, true, false);
    XYPlot plot = (XYPlot) chart.getPlot();
    plot.setRenderer(1, new VectorRenderer());
    plot.setDataset(1, getVectorDataset(values));
    plot.getRenderer().setBaseItemLabelsVisible(true);
    
    plot.getRenderer().setBaseItemLabelPaint(Color.BLUE);
    plot.getRenderer().setBaseItemLabelGenerator(new XYItemLabelGenerator() {
      @Override
      public String generateLabel(XYDataset dataset, int series, int item) {
        return "#" + (item + 1);
      }
    });
    plot.getRenderer().setBaseToolTipGenerator(new XYToolTipGenerator() {
      @Override
      public String generateToolTip(XYDataset dataset, int series, int item) {
        return "#" + (item + 1) + ":" + Config.INSTANCE.getDitoConfig().getQuestion(item + 1).getName();
      }
    });
    return chart;
  }
   
  private static VectorXYDataset getVectorDataset(double[][] mdsValues) {
    VectorSeries s1 = new VectorSeries("Series 1");
    for (int i = 0; i < mdsValues.length; i++) {
      s1.add(0.0, 0.0, mdsValues[i][0], mdsValues[i][1]);
    }
    VectorSeriesCollection dataset = new VectorSeriesCollection();
    dataset.addSeries(s1);
    return dataset;
  }
  
  private JFreeChart createEigenvaluesChart(String title, EigenvalueDecomposition decomp) {
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
    double[][] mdsValues = decomp.getReducedDimensions(dimensions);
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
