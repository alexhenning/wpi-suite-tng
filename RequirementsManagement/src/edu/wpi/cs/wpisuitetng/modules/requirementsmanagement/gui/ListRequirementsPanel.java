package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.DB;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.RequirementsCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.SingleRequirementCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.listeners.FilterListener;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;

@SuppressWarnings("serial")
public class ListRequirementsPanel extends JPanel {
	
	ListRequirementsTab parent;
	boolean inputEnabled;
	JTable table;
	JTextField filterBox;
	ViewReqTable tableModel;
	TableRowSorter<ViewReqTable> sorter;
	JComboBox columnSelector;
	String[] columnNames = { "ID", "Name", "Status", "Priority", "Estimate"};

	public ListRequirementsPanel(final ListRequirementsTab parent) {
		this.parent = parent;
		
		// Indicate that input is enabled
		inputEnabled = true;

		// Add all components to this panel
		addComponents();
		updateAllRequirementList();
		
		table.addMouseListener(new MouseListener() {
			@Override
			public void mousePressed(MouseEvent e) {
                if (e.getClickCount() == 2) {
                	DB.getSingleRequirement((String) table.getModel().getValueAt(table.getSelectedRow(), 0),
                			new SingleRequirementCallback() {
						@Override
						public void callback(RequirementModel req) {
							parent.tabController.addEditRequirementTab(req);
						}
                	});
                }
			}
			@Override public void mouseReleased(MouseEvent arg0) {}
			@Override public void mouseExited(MouseEvent arg0) {}
			@Override public void mouseEntered(MouseEvent arg0) {}
			@Override public void mouseClicked(MouseEvent arg0) {}
		});
	}

	/**
	 * adds the components to the panel and places constraints on them
	 * for the SpringLayout manager.
	 * @param layout the layout manager
	 */
	protected void addComponents() {
		//borderlayout so the table can expand while the filter area remains constant
		setLayout(new BorderLayout());
		
		//create the table part of the GUI
		tableModel = new ViewReqTable();
		sorter = new TableRowSorter<ViewReqTable>(tableModel);
		table = new JTable(tableModel);
		table.setRowSorter(sorter);
		table.setPreferredScrollableViewportSize(new Dimension(500, 100));
		table.setFillsViewportHeight(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		//Add the table to a scrollpane and add it
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(200, 100));
		add(scrollPane, BorderLayout.CENTER);
		
		//create the filter part of the GUI
		JPanel filterArea = new JPanel(new FlowLayout());
		
		//add the column enum selector
		columnSelector = new JComboBox(columnNames);
		columnSelector.setSelectedIndex(0);
		columnSelector.addActionListener(new FilterListener());
		filterArea.add(columnSelector);
		
		//add filter text label
		JLabel filterLabel = new JLabel("Filter Text:", SwingConstants.TRAILING);
		filterArea.add(filterLabel);
		
		//create input text box
		filterBox = new JTextField(30);
		
		//add a listener to the filterbox
		filterBox.getDocument().addDocumentListener(
				new DocumentListener() {
					public void changedUpdate(DocumentEvent e) {
						newFilter();
					}
					public void insertUpdate(DocumentEvent e) {
                        newFilter();
                    }
                    public void removeUpdate(DocumentEvent e) {
                        newFilter();
                    }
				});
		
		//add components to GUI
		filterLabel.setLabelFor(filterBox);
		filterArea.add(filterBox);
		filterArea.setPreferredSize(new Dimension(100, 50));
		add(filterArea, BorderLayout.PAGE_END);	
	}
	
	private void newFilter() {
		RowFilter<ViewReqTable, Object> rf = null;
		//If current expression doesn't parse, don't update.
		try {
			rf = RowFilter.regexFilter(filterBox.getText(), columnSelector.getSelectedIndex());
		} 
		catch (java.util.regex.PatternSyntaxException e) {
			return;
		}
		sorter.setRowFilter(rf);
	}
	
	/**
	 * Sets whether input is enabled for this panel and its children. This should be used instead of 
	 * JComponent#setEnabled because setEnabled does not affect its children.
	 * 
	 * @param enabled	Whether or not input is enabled.
	 */
	protected void setInputEnabled(boolean enabled) {
		inputEnabled = enabled;

		// TODO: implement
	}

	public void updateAllRequirementList() {
		DB.getAllRequirements(new UpdateTableCallback());
	}
	
	public ViewReqTable getTable(){
		return this.tableModel;
	}
	
	class UpdateTableCallback implements RequirementsCallback {
		@Override
		public void callback(List<RequirementModel> reqs) {
			if (reqs.size() > 0) {
				// put the data in the table
				Object[][] entries = new Object[reqs.size()][5];
				int i = 0;
				for(RequirementModel req : reqs) {
					entries[i][0] = String.valueOf(req.getId());
					entries[i][1] = req.getName();
					if (req.getStatus() != null) {
						entries[i][2] = req.getStatus().toString();
					}
					else {
						entries[i][2] = "Error: Status set to null";
					}
					if (req.getPriority() != null) {
						entries[i][3] = req.getPriority().toString();
					}
					else {
						entries[i][3] = "";
					}
					entries[i][4] = req.getEstimate()+"";
					i++;
					}
				getTable().setData(entries);
				getTable().fireTableStructureChanged();
			}
			else {
				// do nothing, there are no requirements
			}
		}
		
	}
}
