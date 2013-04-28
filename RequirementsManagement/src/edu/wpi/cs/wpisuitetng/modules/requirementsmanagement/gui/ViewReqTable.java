/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Josh
 *    Tim C
 *    James
 *    Jacob Palnick
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui;

import javax.swing.table.AbstractTableModel;

/**
 *
 * model for the requirements table
 * @author Josh
 * @author Tim C
 * @author James
 * @author Jacob Palnick
 *
 * @version $Revision: 1.0 $
 */


@SuppressWarnings("serial")
public class ViewReqTable extends AbstractTableModel {
	
	public static final int ID = 0;
	public static final int NAME = 1;
	public static final int DESCRIPTION = 2;
	public static final int ITERATION = 3;
	public static final int STATUS = 4;
	public static final int PRIORITY = 5;
	public static final int ESTIMATE = 6;
	public static final int RELEASE = 7;
	
	/**
	 *
	 * type to see if the table is in view or edit mode
	 *
	 * @author Owner
	 * @version $Revision: 1.0 $
	 */
	public enum Mode {
		VIEW,
		EDIT
	};

    /** name of the columns */
    private String[] columnNames = { "ID", "Name", "Description", "Iteration", "Status", "Priority", "Estimate"};
    //first list is row, second list is column data
    
    /** data in each cell*/
    private Object[][] data = {};
    
    /** the mode the table is in*/
    private Mode editMode;
    
    /**
     * get the number of columns
     *
    
     * @return the number of columns * @see javax.swing.table.TableModel#getColumnCount()
     */
    public int getColumnCount() {
        return columnNames.length;
    }

    /**
     * get the number of rows
     *
    
     * @return the number of rows * @see javax.swing.table.TableModel#getRowCount()
     */
    public int getRowCount() {
        return data.length;
    }

    /**
     * get the name of a specified column
     *
     * @param col the column number
    
     * @return the name of the column * @see javax.swing.table.TableModel#getColumnName(int)
     */
    public String getColumnName(int col) {
        return columnNames[col];
    }

    /**
     * get the object in a cell
     *
     * @param row the row of the cell
     * @param col the column of the cell
    
     * @return the object at the cell * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int row, int col) {
        return data[row][col];
    }

    /**
     * get the type of class in a column
     *
     * @param c the column number
    
     * @return the kind of class in the column * @see javax.swing.table.TableModel#getColumnClass(int)
     */
    public Class<?> getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    /**
     * see if a cell is editable, true if it is, false if it is not
     *
     * @param row the row of the cell
     * @param col the column of the cell
    
     * @return true if the cell is editable, false otherwise * @see javax.swing.table.TableModel#isCellEditable(int, int)
     */
    public boolean isCellEditable(int row, int col) {
    	return !(editMode == Mode.VIEW || col == ID || col == STATUS || (getValueAt(row, STATUS) == "COMPLETE" && col != STATUS)
    			|| (getValueAt(row, STATUS) == "DELETED" && col != STATUS));// Id cell should not be editable, even in edit mode
    }

    /**
     * set the value at a cell
     *
     * @param value the value to set the cell
     * @param row the row of the cell
     * @param col the column of the cell
     * @see javax.swing.table.TableModel#setValueAt(Object, int, int)
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
     * Returns the data representation
     *
    
     * @return data */
    public Object[][] getData() {
    	return data;
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
    
     * @return the mode of the table */
    public Mode getMode() {
    	return editMode;
    }
    
     
}

