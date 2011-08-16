package ch.zhaw.ias.dito.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.bind.JAXBException;

import ch.zhaw.ias.dito.DistanceAlgorithm;
import ch.zhaw.ias.dito.Matrix;
import ch.zhaw.ias.dito.config.ConfigProperty;
import ch.zhaw.ias.dito.config.DitoConfiguration;

import ch.zhaw.ias.dito.io.CommandlineParser;

public class MainApp {
	public static void main(String... args) {
		HashMap<String, String> params = CommandlineParser.parse(args);
		try {
		    DitoConfiguration config = DitoConfiguration.loadFromFile(params.get("-config"));
		    String outputDir = params.get("-outputDir");
		    String inputDir = params.get("-inputDir");
		    if (inputDir != null || outputDir != null) {
		      if (inputDir == null || outputDir == null) {
		        System.out.println("Parameter inputDir or outputDir missing");
		        return;
		      }
		      directoryProcessing(config, inputDir, outputDir);
		    } else {
		      singleFileProcessing(config);
		    }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
      e.printStackTrace();
    }
	}
	
	private static void directoryProcessing(DitoConfiguration config,
      String inputDir, String outputDir) {
    File directory = new File(inputDir);
    String[] files = directory.list();
	  for (int i = 0; i < files.length; i++) {
	    File currentFile = new File(files[i]);
	    if (currentFile.isDirectory() || !currentFile.getName().endsWith(".csv")) {
	      continue;
	    }
	    
	    config.getInput().setFilename(directory.getAbsolutePath() + "/" + files[i]);
	    //invalidate property --> reload matrix file
	    config.propertyChanged(ConfigProperty.INPUT_FILENAME);
	    
	    System.out.println("processing file: " + files[i] + " with " + config.getData().getColCount() + " cols and " + config.getData().getRowCount() + " rows");
	    DistanceAlgorithm algo = new DistanceAlgorithm(config, false);
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

  public static void singleFileProcessing(DitoConfiguration config) throws IOException {
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
