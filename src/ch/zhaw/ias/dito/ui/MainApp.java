package ch.zhaw.ias.dito.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.bind.JAXBException;

import ch.zhaw.ias.dito.DistanceAlgorithm;
import ch.zhaw.ias.dito.Matrix;
import ch.zhaw.ias.dito.config.DitoConfiguration;

import ch.zhaw.ias.dito.io.CommandlineParser;

public class MainApp {
	public static void main(String... args) {
		HashMap<String, String> params = CommandlineParser.parse(args);
		try {		       
		    DitoConfiguration config = DitoConfiguration.loadFromFile(params.get("-p"));
		    //System.out.println("reading input-file: " + config.getInput().getFilename());
		    Matrix m = Matrix.readFromFile(new File(config.getInput().getFilename()), config.getInput().getSeparator());
		    DistanceAlgorithm algo = new DistanceAlgorithm(config, false);
		    Matrix dist = algo.doIt(false);
		    
	      String outputFilename = config.getOutput().getFilename().replace("$$METHOD$$", config.getMethod().getName());
	      try {
	        Matrix.writeToFile(m, outputFilename, config.getOutput().getSeparator(), config.getOutput().getPrecision());
	      } catch (IOException e1) {
	        // TODO Error-Handling
	        e1.printStackTrace();
	      }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
      e.printStackTrace();
    }
	}
}
