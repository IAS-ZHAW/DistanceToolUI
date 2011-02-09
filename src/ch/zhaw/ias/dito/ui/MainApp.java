package ch.zhaw.ias.dito.ui;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ch.zhaw.ias.dito.DVector;
import ch.zhaw.ias.dito.Matrix;
import ch.zhaw.ias.dito.dist.CanberraDist;
import ch.zhaw.ias.dito.dist.DistanceMethodEnum;
import ch.zhaw.ias.dito.dist.DistanceSpec;
import ch.zhaw.ias.dito.dist.EuklidianDist;
import ch.zhaw.ias.dito.dist.ManhattanDist;
import ch.zhaw.ias.dito.io.CommandlineParser;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

public class MainApp {
	public static void main(String... args) {
		HashMap<String, String> params = CommandlineParser.parse(args);
	    CSVReader reader;
		try {
			reader = new CSVReader(new FileReader(params.get("-i")), ';');
			String [] nextLine;
			List<DVector> vectors = new ArrayList<DVector>();
		    while ((nextLine = reader.readNext()) != null) {
		    	double[] values = new double[nextLine.length];
		        for (int i = 0; i < values.length; i++) {
		        	if (nextLine[i].length() == 0 || nextLine[i].isEmpty() == true || " ".equals(nextLine[i])) {
		        		values[i] = 0.0;
		        	} else {
		        		values[i] = Double.parseDouble(nextLine[i]);
		        	}
		        }
		        vectors.add(new DVector(values));
		    }
		    
		    Matrix m = new Matrix(vectors.toArray(new DVector[0]));
		    m = m.transpose();		
		    long start2 = System.currentTimeMillis();	        
		    DistanceSpec spec = DistanceMethodEnum.get(params.get("-m"));
		    Matrix dist = m.calculateDistance(spec);
		    System.out.println(System.currentTimeMillis() - start2);
		    System.out.println(dist.isSquare());
		    
		    CSVWriter writer = new CSVWriter(new FileWriter(params.get("-o")), ';', CSVWriter.NO_ESCAPE_CHARACTER, CSVWriter.NO_QUOTE_CHARACTER);
		    
		    for (int i = 0; i < dist.getColCount(); i++) {
		    	DVector col = dist.col(i);
		    	String[] line = new String[col.length()];
		    	for (int j = 0; j < col.length(); j++) {
			    	line[j] = Double.toString(col.component(j));
			    }
		    	writer.writeNext(line);
		    }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
