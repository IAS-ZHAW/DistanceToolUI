/* released under bsd licence
 * see LICENCE file or http://www.opensource.org/licenses/bsd-license.php for details
 * Institute of Applied Simulation (ZHAW)
 * Author Thomas Niederberger
 */
package ch.zhaw.ias.dito.ui.util;

import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ch.zhaw.ias.dito.ui.resource.Translation;

public class HelpLabel extends JPanel implements ActionListener {
  private HelpArea helpArea;
  private String reference;
  private ImageIcon HELP_ICON = new ImageIcon(Toolkit.getDefaultToolkit().getImage(Translation.class.getResource("ic-help.png")));
  
  public HelpLabel(HelpArea helpArea, String reference) {
    this(helpArea, reference, reference);
  }
  
  public HelpLabel(HelpArea helpArea, String translationKey, String reference) {
    this.helpArea = helpArea;
    this.reference = reference;
    this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
    JLabel text = new JLabel(Translation.INSTANCE.get(translationKey));
    JButton icon = new JButton(HELP_ICON);
    
    icon.setBorderPainted(false);  
    icon.setContentAreaFilled(false);  
    icon.setFocusPainted(false);  
    icon.setOpaque(false);  
    
    icon.addActionListener(this);
    
    this.add(text);
    this.add(Box.createHorizontalGlue());
    this.add(icon);
  }
  
  @Override
  public void actionPerformed(ActionEvent e) {
    helpArea.showHelpTopic(reference);
  }
}
