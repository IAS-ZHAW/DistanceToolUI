package ch.zhaw.ias.dito.ui.util;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class ExtensionFileFilter extends FileFilter {
  public final static ExtensionFileFilter CSV = new ExtensionFileFilter(".csv");
  public final static ExtensionFileFilter DITO = new ExtensionFileFilter(".dito");
  
  private String extension;
  
  public ExtensionFileFilter(String extension) {
    this.extension = extension;
  }
  
  @Override
  public boolean accept(File f) {
    return f.isDirectory() || f.getAbsolutePath().endsWith(extension);
  }

  @Override
  public String getDescription() {
    return extension + " Files";
  }
}
