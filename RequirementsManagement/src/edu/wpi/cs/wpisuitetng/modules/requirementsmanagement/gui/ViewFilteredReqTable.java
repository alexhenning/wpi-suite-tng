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
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui;

import javax.swing.table.AbstractTableModel;

/**
 *
 * model for the iteration filtered requirements table
 * @author Josh
 * @author Tim C
 * @author James
 *
 * @version $Revision: 1.0 $
 */


@SuppressWarnings("serial")
public class ViewFilteredReqTable extends AbstractTableModel {
	
	public static final int ID = 0;
	public static final int NAME = 1;
	public static final int DESCRIPTION = 2;
	public static final int STATUS = 3;
	public static final int PRIORITY = 4;
	public static final int ESTIMATE = 5;
	public static final int RELEASE = 6;

    /** name of the columns */
    private String[] columnNames = { "ID", "Name", "Description", "Status", "Priority", "Estimate"};
    //first list is row, second list is column data
    
    /** data in each cell*/
    private Object[][] data = {};
    
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
    		return false;                                          
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
     
}

