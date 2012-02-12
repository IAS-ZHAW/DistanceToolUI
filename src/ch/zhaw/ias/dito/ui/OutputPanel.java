/* released under bsd licence
 * see LICENCE file or http://www.opensource.org/licenses/bsd-license.php for details
 * Institute of Applied Simulation (ZHAW)
 * Author Thomas Niederberger
 */
package ch.zhaw.ias.dito.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXTextField;
import org.netbeans.validation.api.builtin.Validators;
import org.netbeans.validation.api.ui.ValidationGroup;

import ch.zhaw.ias.dito.Matrix;
import ch.zhaw.ias.dito.config.DitoConfiguration;
import ch.zhaw.ias.dito.config.Output;
import ch.zhaw.ias.dito.ui.resource.Translation;
import ch.zhaw.ias.dito.ui.util.ExtensionFileFilter;
import ch.zhaw.ias.dito.ui.util.HelpArea;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class OutputPanel extends DitoPanel implements ActionListener {
  private JXTextField filePath = new JXTextField();
  private JXButton browseButton;
  private JXTextField separator = new JXTextField();
  private JXTextField precision = new JXTextField();
  private JXButton saveButton = new JXButton(Translation.INSTANCE.get("s5.lb.save"));
  
  public OutputPanel(HelpArea helpArea) {
    super(ScreenEnum.OUTPUT, ScreenEnum.ANALYSIS, null);
    
    FormLayout layout = new FormLayout("max(50dlu; pref), 5dlu, max(100dlu; pref), 5dlu, max(50dlu; pref), 5dlu, max(100dlu; pref), 5dlu, max(50dlu; pref)", 
      "pref, 2dlu, pref, 2dlu, pref, 2dlu, pref, 2dlu, pref");
    //layout.setRowGroups(new int[][]{{2, 4, 6}}); 
    CellConstraints cc = new CellConstraints();
    DefaultFormBuilder fb = new DefaultFormBuilder(layout, Translation.INSTANCE.getBundle());
    
    browseButton = new JXButton(Translation.INSTANCE.get("s1.bu.browse"));
    filePath.setName(Translation.INSTANCE.get("s1.lb.file"));

    fb.addI15dSeparator("s5.title.file", cc.xyw(1, 1, 9));
    fb.addI15dLabel("s1.lb.file", cc.xy(1, 3));
    fb.add(filePath, cc.xyw(3, 3, 5));
    fb.add(browseButton, cc.xy(9, 3));
    fb.addI15dLabel("s1.lb.separator", cc.xy(1, 5));
    fb.add(separator, cc.xy(7, 5));
    fb.addI15dLabel("s5.lb.precision", cc.xy(1, 7));
    fb.add(precision, cc.xy(7, 7));
    fb.add(saveButton, cc.xy(7, 9));
    
    //validationGroup.add(filePath, Validators.fFILE_MUST_BE_FILE);
    //validationGroup.add(separator, Validators.REQUIRE_NON_EMPTY_STRING);
    Output o = Config.INSTANCE.getDitoConfig().getOutput();
    filePath.setText(o.getFilename());
    separator.setText(Character.toString(o.getSeparator()));
    precision.setText(Integer.toString(o.getPrecision()));
    
    browseButton.addActionListener(this);
    saveButton.addActionListener(this);
    this.setLayout(new BorderLayout());
    this.add(fb.getPanel(), BorderLayout.CENTER);
  }
  
  @Override
  public void saveToModel() {
    Output o = Config.INSTANCE.getDitoConfig().getOutput();
    o.setFilename(filePath.getText());
    o.setSeparator(separator.getText().charAt(0));
    o.setPrecision(Integer.parseInt(precision.getText()));
  }
  
  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == browseButton) {
      JFileChooser fileChooser = new JFileChooser(filePath.getText());
      fileChooser.setFileFilter(ExtensionFileFilter.CSV);
      int returnVal = fileChooser.showOpenDialog(this);
      
      if (returnVal == JFileChooser.APPROVE_OPTION) {
          File file = fileChooser.getSelectedFile();
          filePath.setText(file.getAbsolutePath());
      }
    } else if (e.getSource() == saveButton) {
      saveToModel();
      DitoConfiguration config = Config.INSTANCE.getDitoConfig();
      String outputFilename = config.getOutput().getFilename().replace("$$METHOD$$", config.getMethod().getName());
      Matrix m = Config.INSTANCE.getDistanceMatrix();
      try {
        Matrix.writeToFile(m, outputFilename, config.getOutput().getSeparator(), config.getOutput().getPrecision());
      } catch (IOException e1) {
        // TODO Error-Handling
        e1.printStackTrace();
      }
    }
  }
}
