/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    //TODO who did this
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db.DB;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db.RequirementsCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.utils.ScrollablePanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.utils.ScrollableTab;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementStatus;

/**
 *
 * The view for viewing reports.
 * @author TODO
 *
 */
@SuppressWarnings("serial")
public class ReportsPanel extends JPanel implements ScrollablePanel {
	
	/** the tab that created this panel */
	ScrollableTab parent;
	/** the layout for this panel */
	private GridBagLayout panelLayout;
	
	private JComboBox reports;

	private List<RequirementModel> model;
	private Report report = Report.STATUS;
	private DefaultPieDataset dataset;
	private JFreeChart chart;
	private ChartPanel chartPanel;

	/**
	 * Constructor
	 * @param iterationTab the tab that created this panel
	 */
	public ReportsPanel() {
		model = new LinkedList<RequirementModel>();
		dataset = new DefaultPieDataset();
		dataset.setValue("Loading", 1.0);
		chart = ChartFactory.createPieChart(
            "Loading",  // chart title
            dataset,             // data
            false,               // include legend
            true,
            false
        );
		
		addComponents();

		refresh();
	}

	@Override
	public void setTab(ScrollableTab tab) {
		parent = tab;
	}

	
	/**
	 * add all the components (labels, text fields, buttons) to this panel's view
	 *
	 */
	private void addComponents() {
		panelLayout = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		setLayout(panelLayout);
		
		chartPanel = new ChartPanel(chart);
		reports = new JComboBox(Report.values());
		reports.setBackground(Color.white);
		reports.addItemListener(new ItemListener() {
			@Override public void itemStateChanged(ItemEvent e) {
				report = (Report) reports.getSelectedItem();
				refreshChart();
			}
		});
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		add(chartPanel, c);
		c.gridy = 1;
		add(reports, c);
	}
	
	/**
	 * Update the model the chart to reflect the most up to date information.
	 */
	public void refresh() {
		System.out.println("Refreshing");
		DB.getAllRequirements(new RequirementsCallback() {
			@Override public void callback(List<RequirementModel> reqs) {
				model = reqs;
				refreshChart();
			}
		});
	}
	
	/**
	 * Update the charts to reflect the most up to date information.
	 */
	public void refreshChart() {
		System.out.println("Refreshing chart");
		dataset.clear();
		report.updateDataset(model, dataset);
		chart.setTitle(report.toString());
		chartPanel.repaint();
		System.out.println("Repainting");
	}
		
	/**
	 * close this tab/panel
	 *
	 */
	public void close() {
		parent.getTabController().closeCurrentTab();
	}
	
	/**
	 * Sets whether input is enabled for this panel and its children. This should be used instead of 
	 * JComponent#setEnabled because setEnabled does not affect its children.
	 * 
	 * @param enabled	Whether or not input is enabled.
	 */
	protected void setInputEnabled(boolean enabled) {
		// TODO: implement if made editable
	}
	
	public enum Report {
		STATUS {
			@Override public void updateDataset(List<RequirementModel> model, 
					DefaultPieDataset dataset) {
				Map<RequirementStatus, Integer> map = new HashMap<RequirementStatus, Integer>();
			
				for (RequirementModel req : model) {
					RequirementStatus status = req.getStatus();
					if (map.containsKey(status)) {
						map.put(status, 1 + map.get(status));
					} else {
						map.put(status, 1);
					}
				}
				
				for (RequirementStatus key : map.keySet()) {
					System.out.println("\t"+key+": "+map.get(key));
					dataset.setValue(key.toString(), new Double(map.get(key)));
				}
			}
		},
		ITERATION {
			@Override public void updateDataset(List<RequirementModel> model, 
					DefaultPieDataset dataset) {
				Map<Iteration, Integer> map = new HashMap<Iteration, Integer>();
			
				for (RequirementModel req : model) {
					Iteration status = req.getIteration();
					if (map.containsKey(status)) {
						map.put(status, 1 + map.get(status));
					} else {
						map.put(status, 1);
					}
				}
				
				for (Iteration key : map.keySet()) {
					System.out.println("\t"+key+": "+map.get(key));
					if (key != null) 
						dataset.setValue(key.toString(), new Double(map.get(key)));
					else
						dataset.setValue("Backlog", new Double(map.get(key)));
				}
			}
		},
		ASSIGNED_TO {
			@Override public void updateDataset(List<RequirementModel> model, 
					DefaultPieDataset dataset) {
				Map<User, Integer> map = new HashMap<User, Integer>();
			
				for (RequirementModel req : model) {
					User status = req.getCreator(); // TODO: replace with assigned to
					if (map.containsKey(status)) {
						map.put(status, 1 + map.get(status));
					} else {
						map.put(status, 1);
					}
				}
				
				for (User key : map.keySet()) {
					System.out.println("\t"+key+": "+map.get(key));
					dataset.setValue(key.getName(), new Double(map.get(key)));
				}
			}
		};
		
		public abstract void updateDataset(List<RequirementModel> model, DefaultPieDataset dataset);
	}
}
