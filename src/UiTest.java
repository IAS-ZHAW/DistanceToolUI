
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXMultiThumbSlider;
import org.jdesktop.swingx.color.GradientThumbRenderer;
import org.jdesktop.swingx.color.GradientTrackRenderer;
import org.jdesktop.swingx.multislider.DefaultMultiThumbModel;
import org.jdesktop.swingx.multislider.MultiThumbModel;
import org.jdesktop.swingx.multislider.Thumb;
import org.jdesktop.swingx.multislider.ThumbDataListener;


public class UiTest extends JXFrame {
  
  public UiTest() {

    this.setLayout(new BorderLayout());
    JXMultiThumbSlider<Integer> slider = new JXMultiThumbSlider<Integer>();
    slider.setMinimumValue(0.0f);
    slider.setMaximumValue(100.0f);
    DefaultMultiThumbModel model = new DefaultMultiThumbModel();
    model.addThumb(0.0f, Color.BLUE);
    model.addThumb(1.0f, Color.RED);
    slider.setModel(model);
    slider.setThumbRenderer(new GradientThumbRenderer());
    slider.setTrackRenderer(new GradientTrackRenderer());
    this.add(slider, BorderLayout.CENTER);

    this.setSize(1000, 1000);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setVisible(true);
  }
  
  public static void main(String... args) {
    new UiTest();
  }
}
