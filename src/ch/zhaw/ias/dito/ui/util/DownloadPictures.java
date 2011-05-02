package ch.zhaw.ias.dito.ui.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import ch.zhaw.ias.dito.Coding;
import ch.zhaw.ias.dito.dist.DistanceMethodEnum;

public class DownloadPictures {
  /** The base URL of the mathematical equation transformation service. */
  private static final String MATHTRAN_BASE_URL = "http://www.mathtran.org/cgi-bin/mathtran?";
  
  public static void savePic(DistanceMethodEnum method) throws InterruptedException {
    BufferedInputStream bis = null;
    BufferedOutputStream bos = null;

    try {
      String formula = method.getFormula();
      URL url = new URL(MATHTRAN_BASE_URL + "D=6&tex=" + URLEncoder.encode(formula));
      System.out.println(method.getName() + ":" + formula);
      
      bis = new BufferedInputStream(url.openStream());
      bos = new BufferedOutputStream( new FileOutputStream("src/ch/zhaw/ias/dito/ui/resource/formula/" + method.getName() + ".png"));

      int i;
      while ((i = bis.read()) != -1)
      {
         bos.write( i );
         //System.out.print((char) i);
      }
    } catch (MalformedURLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally {
      try {
        bos.flush();

        bos.close();          
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }
    
  public static void main(String... args) throws InterruptedException {
    for (DistanceMethodEnum method : DistanceMethodEnum.get(Coding.BINARY)) {
      savePic(method);
    }
    for (DistanceMethodEnum method : DistanceMethodEnum.get(Coding.REAL)) {
      savePic(method);
    }    
  }
}
