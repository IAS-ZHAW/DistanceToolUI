package ch.zhaw.ias.dito.ui;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.table.AbstractTableModel;

import org.jdesktop.swingx.JXTable;
import org.netbeans.validation.api.ui.ValidationGroup;

import ch.zhaw.ias.dito.config.Question;
import ch.zhaw.ias.dito.config.TableColumn;
import ch.zhaw.ias.dito.ui.resource.Translation;

public class QuestionPanel extends DitoPanel {
  private JXTable table;
  private JScrollPane sp;
  
  public QuestionPanel(ValidationGroup validationGroup) {
    super(ScreenEnum.QUESTION, ScreenEnum.INPUT, ScreenEnum.OUTPUT, validationGroup);
    List<Question> questions = Config.INSTANCE.getDitoConfig().getQuestions();
    QuestionTableModel model = new QuestionTableModel(questions);
    table = new JXTable(model);
    table.setColumnControlVisible(true);
    sp = new JScrollPane(table);
    //table.setColumnModel(new QuestionColumnModel());
    this.setLayout(new BorderLayout());
    this.add(sp, BorderLayout.CENTER);
  }
  
  static class QuestionTableModel extends AbstractTableModel {
    private List<Question> questions;
    public QuestionTableModel(List<Question> questions) {
      this.questions = questions;
    }
    
    @Override
    public int getColumnCount() {
      return TableColumn.values().length;
    }
    
    @Override
    public int getRowCount() {
      return questions.size();
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
      Question q = questions.get(rowIndex);
      TableColumn column = TableColumn.getById(columnIndex);
      Object value = q.getValue(column);
      if (columnIndex == TableColumn.TYPE.getId()) {
        return Translation.INSTANCE.get("misc.type." + value.toString());
      } else {
        return value;
      }
    }
    
    @Override
    public String getColumnName(int column) {
      return Translation.INSTANCE.get("s2.title." + column);
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
      TableColumn column = TableColumn.getById(columnIndex);
      return column.isEditable();
    }
    
    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
      Question q = questions.get(rowIndex);
      TableColumn column = TableColumn.getById(columnIndex);
      q.setValue(column, value);
    }
  }

}
