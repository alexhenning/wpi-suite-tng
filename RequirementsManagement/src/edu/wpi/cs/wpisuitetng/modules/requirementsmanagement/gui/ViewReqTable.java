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
 *    //Tim C
 *    //James
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui;

import javax.swing.table.AbstractTableModel;

/**
 *
 * model for the requirements table
 * @author Josh
 * @author Tim C
 * @author James
 *
 */
@SuppressWarnings("serial")
public class ViewReqTable extends AbstractTableModel {
	
	/**
	 *
	 * type to see if the table is in view or edit mode
	 *
	 */
	public enum Mode {
		VIEW,
		EDIT
	};

    /** name of the columns */
    private String[] columnNames = { "ID", "Name", "Iteration", "Status", "Priority", "Estimate"};
    //first list is row, second list is column data
    
    /** data in each cell*/
    private Object[][] data = {};
    
    /** the mode the table is in*/
    private Mode editMode;
    
    /**
     * get the number of columns
     *
     * @return the number of columns
     */
    public int getColumnCount() {
        return columnNames.length;
    }

    /**
     * get the number of rows
     *
     * @return the number of rows
     */
    public int getRowCount() {
        return data.length;
    }

    /**
     * get the name of a specified column
     *
     * @param col the column number
     * @return the name of the column
     */
    public String getColumnName(int col) {
        return columnNames[col];
    }

    /**
     * get the object in a cell
     *
     * @param row the row of the cell
     * @param col the column of the cell
     * @return the object at the cell
     */
    public Object getValueAt(int row, int col) {
        return data[row][col];
    }

    /**
     * get the type of class in a column
     *
     * @param c the column number
     * @return the kind of class in the column
     */
    public Class<?> getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    /**
     * see if a cell is editable, true if it is, false if it is not
     *
     * @param row the row of the cell
     * @param col the column of the cell
     * @return true if the cell is editable, false otherwise
     */
    public boolean isCellEditable(int row, int col) {
    	if(editMode == Mode.VIEW || col == 0 || col == 3 || (getValueAt(row, 3) == "COMPLETE" && col != 3)
    			|| (getValueAt(row,3) == "DELETED" && col != 3)) { // Id cell should not be editable, even in edit mode
    		return false;                                          
    	} else {
    		return true;
    	}
    }

    /**
     * set the value at a cell
     *
     * @param value the value to set the cell
     * @param row the row of the cell
     * @param col the column of the cell
     */
    public void setValueAt(Object value, int row, int col) {
        data[row][col] = value;
        fireTableCellUpdated(row, col);
    }
    
    /**
     * set data
     *
     * @param data the data to set
     */
    public void setData(Object[][] data){
    	this.data = data;
    }
    
    /**
     * set the mode of the table
     *
     * @param editMode the mode to set the table to, either view or edit
     */
    public void setMode(Mode editMode) {
    	this.editMode = editMode;
    }
    
    /**
     * get the mode of the table
     *
     * @return the mode of the table
     */
    public Mode getMode() {
    	return editMode;
    }
    
     
}

