/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Alex Henning
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui;

import java.awt.CardLayout;
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
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
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
 * The view for viewing reports.
 */
@SuppressWarnings("serial")
public class ReportsPanel extends JPanel implements ScrollablePanel {
	
	/** the tab that created this panel */
	@SuppressWarnings("rawtypes")
	private ScrollableTab parent;
	/** the layout for this panel */
	private GridBagLayout panelLayout;
	
	private JComboBox reports;

	private List<RequirementModel> model;
	private Report report = Report.STATUS;
	private JPanel cardPanel;
	
	private DefaultPieDataset pieDataset;
	private JFreeChart pieChart;
	private ChartPanel pieChartPanel;

	/**
	 * Constructor
	 * @param iterationTab the tab that created this panel
	 */
	public ReportsPanel() {
		model = new LinkedList<RequirementModel>();
		
		pieDataset = new DefaultPieDataset();
		pieDataset.setValue("Loading", 1.0);
		pieChart = ChartFactory.createPieChart(
            "Loading",  // chart title
            pieDataset,             // data
            false,               // include legend
            true,
            false
        );
		((PiePlot) pieChart.getPlot()).setLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {1} ({2})"));
		
		addComponents();

		refresh();
	}

	@Override
	public void setTab(@SuppressWarnings("rawtypes") ScrollableTab tab) {
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
		
		cardPanel = new JPanel();
		cardPanel.setLayout(new CardLayout());
		
		pieChartPanel = new ChartPanel(pieChart);
		reports = new JComboBox(Report.values());
		reports.setBackground(Color.white);
		reports.addItemListener(new ItemListener() {
			@Override public void itemStateChanged(ItemEvent e) {
				report = (Report) reports.getSelectedItem();
				refreshChart();
			}
		});
		cardPanel.add(pieChartPanel, ReportType.PIE_CHART.toString());
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		add(cardPanel, c);
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
		((CardLayout) cardPanel.getLayout()).show(cardPanel, report.getType().toString());
		if (report.getType().equals(ReportType.PIE_CHART)) {
			pieDataset.clear();
			report.updateDataset(model, pieDataset);
			pieChart.setTitle(report.toString());
			pieChartPanel.repaint();
		}
	}
		
	/**
	 * close this tab/panel
	 *
	 */
	public void close() {
		parent.getTabController().closeCurrentTab();
	}
	
	/**
	 * A type to indicate how the report should display.
	 */
	public enum ReportType {
		PIE_CHART, BAR_CHART;
	}
	
	public enum Report {
		STATUS(ReportType.PIE_CHART) {
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
		ITERATION(ReportType.PIE_CHART) {
			@Override public void updateDataset(List<RequirementModel> model, 
					DefaultPieDataset dataset) {
				Map<Iteration, Integer> map = new HashMap<Iteration, Integer>();
			
				for (RequirementModel req : model) {
					Iteration iteration = req.getIteration();
					if (map.containsKey(iteration)) {
						map.put(iteration, 1 + map.get(iteration));
					} else {
						map.put(iteration, 1);
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
		ASSIGNED_TO(ReportType.PIE_CHART) {
			@Override public void updateDataset(List<RequirementModel> model, 
					DefaultPieDataset dataset) {
				Map<User, Integer> map = new HashMap<User, Integer>();
			
				for (RequirementModel req : model) {
					List<User> users = req.getAssignees();
					for (User user: users) {
						if (map.containsKey(user)) {
							map.put(user, 1 + map.get(user));
						} else {
							map.put(user, 1);
						}
					}
				}
				
				for (User key : map.keySet()) {
					System.out.println("\t"+key+": "+map.get(key));
					dataset.setValue(key.getName(), new Double(map.get(key)));
				}
			}
		};
		
		private ReportType type;
		
		private Report(ReportType type) {
			this.type = type;
		}
		
		public ReportType getType() {
			return type;
		}
		
		public abstract void updateDataset(List<RequirementModel> model, DefaultPieDataset dataset);
	}
}
