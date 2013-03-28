package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.DB;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.IterationCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.RequirementsCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.SingleIterationCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.SingleRequirementCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;

@SuppressWarnings("serial")
public class ListIterationsPanel extends JPanel {
	
	ListIterationsTab parent;
	boolean inputEnabled;
	JTable table;
	ViewIterationTable tableModel;
	
	String[] columnNames = {"ID", "Number", "Start Date", "End Date"};


	public ListIterationsPanel(final ListIterationsTab parent) {
		this.parent = parent;
		
		// Indicate that input is enabled
		inputEnabled = true;

		// Add all components to this panel
		addComponents();
		updateAllIterationList();
		
		table.addMouseListener(new MouseListener() {
			@Override
			public void mousePressed(MouseEvent e) {
                if (e.getClickCount() == 2) {
                	DB.getSingleIteration((String) table.getModel().getValueAt(table.getSelectedRow(), 0),
                			new SingleIterationCallback() {
						@Override
						public void callback(Iteration iteration) {
							parent.tabController.addEditIterationTab(iteration);
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
		setLayout(new GridLayout());
		
		tableModel = new ViewIterationTable();
		table = new JTable(tableModel);
		table.setPreferredScrollableViewportSize(new Dimension(1, 1));
		table.setFillsViewportHeight(true);
		
		JScrollPane scrollPane = new JScrollPane(table);
		
		add(scrollPane);
		
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

	public void updateAllIterationList() {
		DB.getAllIterations(new UpdateTableCallback());
	}
	
	public ViewIterationTable getTable(){
		return this.tableModel;
	}
	
	class UpdateTableCallback implements IterationCallback {
		@Override
		public void callback(List<Iteration> iterations) {
			if (iterations.size() > 0) {
				// put the data in the table
				Object[][] entries = new Object[iterations.size()][5];
				int i = 0;
				for(Iteration iteration : iterations) {
					entries[i][0] = String.valueOf(iteration.getId());
					entries[i][1] = String.valueOf(iteration.getIterationNumber());
					entries[i][2] = iteration.getStartDate().toString();
					entries[i][3] = iteration.getEndDate().toString();
					i++;
				}
				getTable().setData(entries);
				getTable().fireTableStructureChanged();
			}
			else {
				// do nothing, there are no iterations
			}
		}
		
	}
}
