package viewer;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class MyTableModel extends AbstractTableModel {
    private final String[] columns;
    private final List<Object[]> data;

    public MyTableModel(String[]  columns, List<Object[]> data) {
        this.columns = columns;
        this.data = data;
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data.get(rowIndex)[columnIndex];
    }

    @Override
    public String getColumnName(int index) {
        return columns[index];
    }
}
