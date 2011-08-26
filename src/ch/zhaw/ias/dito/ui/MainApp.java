package ch.zhaw.ias.dito.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.bind.JAXBException;

import ch.zhaw.ias.dito.DVector;
import ch.zhaw.ias.dito.DistanceAlgorithm;
import ch.zhaw.ias.dito.Matrix;
import ch.zhaw.ias.dito.config.ConfigProperty;
import ch.zhaw.ias.dito.config.DitoConfiguration;

import ch.zhaw.ias.dito.io.CommandlineParser;

/**
 * MainApp for distance calculations using a command line interface
 * @author Thomas Niederberger (nith) - institute of applied simulation (IAS)
 *
 */
public class MainApp {
  /**
   * Allows two approaches: analyze a single file or analyze a directory
   * to analyze a single file only the path to a dito-configfile must be passed using the -config parameter
   * in the output file path the string $$METHOD$$ will be replaced with the currently selected method (Euklid...)
   * 
   * to analyze a complete directory it is necessary to pass the path to a dito-config file by using the -config parameter,
   * an input directory containing .csv files by using the -inputDir parameter and an output directory to save the distance matrixed by 
   * using the -outputDir parameter. The matrixes will be saved to the output directory under the filename "/distance" + original filename.
   * @param args
   */
	public static void main(String... args) {
		HashMap<String, String> params = CommandlineParser.parse(args);
		try {
		    //DitoConfiguration config = DitoConfiguration.loadFromFile(params.get("-config"));
		    String filename = params.get("-config");
		    String outputDir = params.get("-outputDir");
		    String inputDir = params.get("-inputDir");
		    if (inputDir != null || outputDir != null) {
		      if (inputDir == null || outputDir == null) {
		        System.out.println("Parameter inputDir or outputDir missing");
		        return;
		      }
		      directoryProcessing(filename, inputDir, outputDir);
		    } else {
		      singleFileProcessing(filename);
		    }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
      e.printStackTrace();
    }
	}
	
	private static void directoryProcessing(String filename,
      String inputDir, String outputDir) throws FileNotFoundException, JAXBException {
    File directory = new File(inputDir);
    String[] files = directory.list();
	  for (int i = 0; i < files.length; i++) {
	    File currentFile = new File(files[i]);
	    if (currentFile.isDirectory() || !currentFile.getName().endsWith(".csv")) {
	      continue;
	    }
	    DitoConfiguration config = DitoConfiguration.loadFromFile(filename);
	    config.getInput().setFilename(directory.getAbsolutePath() + "/" + files[i]);
	    //invalidate property --> reload matrix file
	    config.propertyChanged(ConfigProperty.INPUT_FILENAME);
	    	    
	    //System.out.println("processing file: " + files[i] + " with " + config.getData().getColCount() + " cols and " + config.getData().getRowCount() + " rows");
	    System.out.print(files[i] + ";");
	    DistanceAlgorithm algo = new DistanceAlgorithm(config, false);
	    Matrix varianceMatrix = algo.getRescaledOfFiltered();
	    for (int j = 0; j < varianceMatrix.getColCount(); j++) {
	      DVector v = varianceMatrix.col(j);
	      System.out.print(v.unscaledVariance()/(v.length() + 1) + ";");
	    }
	    System.out.println();
	    
	    Matrix dist = algo.doIt(false);
	    
	    //String outputFilename = config.getOutput().getFilename().replace("$$METHOD$$", config.getMethod().getName());
	    String outputFilename = outputDir + "/distance-" + currentFile.getName();
	    try {
	      Matrix.writeToFile(dist, outputFilename, config.getOutput().getSeparator(), config.getOutput().getPrecision());
	    } catch (IOException e1) {
	      // TODO Error-Handling
	      e1.printStackTrace();
	    }
	  }
  }

  public static void singleFileProcessing(String filename) throws IOException, JAXBException {
    DitoConfiguration config = DitoConfiguration.loadFromFile(filename);
    DistanceAlgorithm algo = new DistanceAlgorithm(config, false);
    Matrix dist = algo.doIt(false);
    
    String outputFilename = config.getOutput().getFilename().replace("$$METHOD$$", config.getMethod().getName());
    try {
      Matrix.writeToFile(dist, outputFilename, config.getOutput().getSeparator(), config.getOutput().getPrecision());
    } catch (IOException e1) {
      // TODO Error-Handling
      e1.printStackTrace();
    }
	}
}
