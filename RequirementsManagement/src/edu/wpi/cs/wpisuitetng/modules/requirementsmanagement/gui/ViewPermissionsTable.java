/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui;

import javax.swing.table.AbstractTableModel;

/**
 *
 * The model for the permissions table
 * @author TODO
 *
 * @version $Revision: 1.0 $
 */
@SuppressWarnings("serial")
public class ViewPermissionsTable extends AbstractTableModel {

	/** the names of the columns */
	private String[] columnNames = {"Username", "Permissions"};
	//first list is row, second list is column data

	/** the data in each cell */
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
	 * get the specified column's name
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
	 * @param row the row that the cell is in
	 * @param col the column that the cell is in
	
	 * @return the object at the specified cell * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int row, int col) {
		return data[row][col];
	}

	/**
	 * get the kind of class specified in the colum
	 *
	 * @param c the column number
	
	 * @return the kind of class in that colum * @see javax.swing.table.TableModel#getColumnClass(int)
	 */
	public Class<?> getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	/**
	 * See if the cell is editable, since this table will not have editable cells, it will return false
	 *
	 * @param row the row of the cell
	 * @param col the column of the cell
	
	 * @return will return false since this table is not editable * @see javax.swing.table.TableModel#isCellEditable(int, int)
	 */
	public boolean isCellEditable(int row, int col) {
		return false; //Cells should not be editable in table, should be able to double click and open edit tab
	}

	/**
	 * set the value of a cell
	 *
	 * @param value the value to set the cell to
	 * @param row the row of the cell
	 * @param col the column of the cell
	 * @see javax.swing.table.TableModel#setValueAt(Object, int, int)
	 */
	public void setValueAt(Object value, int row, int col) {
		data[row][col] = value;
		fireTableCellUpdated(row, col);
	}

	/**
	 * set an object
	 *
	 * @param data the data to set the object to
	 */
	public void setData(Object[][] data){
		this.data = data;
	}
}
