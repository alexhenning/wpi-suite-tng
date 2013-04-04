/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    //Josh
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class ViewReqTable extends AbstractTableModel {
	
	public enum Mode {
		VIEW,
		EDIT
	};

    private String[] columnNames = { "ID", "Name", "Iteration", "Status", "Priority", "Estimate"};
    //first list is row, second list is column data
    
    private Object[][] data = {};
    
    private Mode editMode;
    
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

    public Class<?> getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    public boolean isCellEditable(int row, int col) {
    	if(editMode == Mode.VIEW || col == 0 || (getValueAt(row, 3) == "COMPLETE" && col != 3)
    			|| (getValueAt(row,3) == "DELETED" && col != 3)) { // Id cell should not be editable, even in edit mode
    		return false;                                          
    	} else {
    		return true;
    	}
    }

    public void setValueAt(Object value, int row, int col) {
        data[row][col] = value;
        fireTableCellUpdated(row, col);
    }
    
    public void setData(Object[][] data){
    	this.data = data;
    }
    
    public void setMode(Mode editMode) {
    	this.editMode = editMode;
    }
    
    public Mode getMode() {
    	return editMode;
    }
    
     
}

