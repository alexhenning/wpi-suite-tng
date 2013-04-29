/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    william
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui;

import javax.swing.table.AbstractTableModel;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.PermissionLevel;

/**
 *
 * The model for the permissions table
 * @author William
 *
 */
@SuppressWarnings("serial")
public class ViewUserTable extends AbstractTableModel {

	/** the names of the columns */
	private String[] columnNames = {"ID", "Name", "Username", "Permissions"};
	//first list is row, second list is column data

	/** the data in each cell */
	private Object[][] data = {};

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
	 * get the specified column's name
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
	 * @param row the row that the cell is in
	 * @param col the column that the cell is in
	 * @return the object at the specified cell
	 */
	public Object getValueAt(int row, int col) {
		return data[row][col];
	}

	/**
	 * get the kind of class specified in the colum
	 *
	 * @param c the column number
	 * @return the kind of class in that colum
	 */
	public Class<?> getColumnClass(int c) {
		if (c == 3) return PermissionLevel.class;
		else return getValueAt(0, c).getClass();
	}

	/**
	 * See if the cell is editable, since this table will not have editable cells, it will return false
	 *
	 * @param row the row of the cell
	 * @param col the column of the cell
	 * @return will return false since this table is not editable
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
