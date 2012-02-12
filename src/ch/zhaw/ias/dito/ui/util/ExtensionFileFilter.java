/* released under bsd licence
 * see LICENCE file or http://www.opensource.org/licenses/bsd-license.php for details
 * Institute of Applied Simulation (ZHAW)
 * Author Thomas Niederberger
 */
package ch.zhaw.ias.dito.ui.util;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class ExtensionFileFilter extends FileFilter {
  public final static ExtensionFileFilter CSV = new ExtensionFileFilter(".csv");
  public final static ExtensionFileFilter CSV_OR_TXT = new ExtensionFileFilter(".csv", ".txt");
  public final static ExtensionFileFilter DITO = new ExtensionFileFilter(".dito");
  
  private String[] extensions;
  
  public ExtensionFileFilter(String... extensions) {
    this.extensions = extensions;
  }
  
  @Override
  public boolean accept(File f) {
    if (f.isDirectory()) {
      return true;
    } 
    String path = f.getAbsolutePath();
    for (String s : extensions) {
      if (path.endsWith(s)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public String getDescription() {
    String desc = "";
    for (String s : extensions) {
      desc += s + ", ";
    }
    return desc.substring(0, desc.length()-3) + " Files";
  }
}
