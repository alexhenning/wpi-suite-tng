package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

public class ViewReqTable extends AbstractTableModel {

    private String[] columnNames = { "ID", "Name", "Status", "Priority", "Estimate"};
    //first list is row, second list is column data
    private Object[][] data = {{"BOGUSID", "BOGUSNAME", "BOGUSSTATUS", "BOGUSPRIORITY", "BOGUSEST"}};
    
    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return data.length;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        return data[row][col];
    }

    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    /*
     * Don't need to implement this method unless your table's
     * editable.
     */
    public boolean isCellEditable(int row, int col) {
       return false; //Cells should not be editable in table, should be able to double click and open edit tab
    }

    /*
     * Don't need to implement this method unless your table's
     * data can change.
     */
    public void setValueAt(Object value, int row, int col) {
        data[row][col] = value;
        fireTableCellUpdated(row, col);
    }
    
    
     
}

