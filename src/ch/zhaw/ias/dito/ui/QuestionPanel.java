package ch.zhaw.ias.dito.ui;

import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.table.AbstractTableModel;

import org.jdesktop.swingx.JXTable;

import ch.zhaw.ias.dito.config.Question;
import ch.zhaw.ias.dito.ui.resource.Translation;

public class QuestionPanel extends DitoPanel {
  private JXTable table;
  private JScrollPane sp;
  
  public QuestionPanel() {
    super(ScreenEnum.QUESTION, ScreenEnum.INPUT, ScreenEnum.OUTPUT);
    List<Question> questions = Config.INSTANCE.getDitoConfig().getQuestions();
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
    
    @Override
    public String getColumnName(int column) {
      return Translation.INSTANCE.get("s2.title." + column);
    }
  }

}
