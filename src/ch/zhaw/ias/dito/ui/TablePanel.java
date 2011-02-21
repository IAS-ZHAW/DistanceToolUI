package ch.zhaw.ias.dito.ui;

import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.table.TableColumnModelExt;

import ch.zhaw.ias.dito.config.Question;

public class TablePanel extends JXPanel {
  private JXTable table;
  private JScrollPane sp;
  
  public TablePanel(List<Question> questions) {
    QuestionTableModel model = new QuestionTableModel(questions);
    table = new JXTable(model);
    table.setColumnControlVisible(true);
    sp = new JScrollPane(table);
    //table.setColumnModel(new QuestionColumnModel());
    this.add(sp);
  }
  
  static class QuestionTableModel extends AbstractTableModel {
    private List<Question> questions;
    public QuestionTableModel(List<Question> questions) {
      this.questions = questions;
    }
    
    @Override
    public int getColumnCount() {
      return 5;
    }
    
    @Override
    public int getRowCount() {
      return questions.size();
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
      Question q = questions.get(rowIndex);
      if (columnIndex == 0) {
        return q.getColumn();
      } else if (columnIndex == 1) {
        return q.getName();
      } else if (columnIndex == 2) {
        return q.getDistanceWeight();
      } else if (columnIndex == 3) {
        return q.getQuestionWeight();
      } else if (columnIndex == 4) {
        return q.getScaling();
      }
      throw new IllegalArgumentException("this is the end of the world");
    }
  }
  
  /*static class QuestionColumnModel implements TableColumnModelExt {
    
  }*/
}
