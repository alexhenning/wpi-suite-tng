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

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.reports;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
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
 * @author alex henning
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
	private List<Report> reportsList;
	private Report report;
	private JPanel cardPanel;
	
	private DefaultPieDataset pieDataset;
	private JFreeChart pieChart;
	private ChartPanel pieChartPanel;
	
	private DefaultCategoryDataset barDataset;
	private JFreeChart barChart;
	private ChartPanel barChartPanel;


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
		
		barDataset = new DefaultCategoryDataset();
		barDataset.setValue(1.0, "Loading", "");
		barChart = ChartFactory.createBarChart(
				"Loading",
				null,
				"Requirements", 
				barDataset,             // data
				PlotOrientation.VERTICAL,
				false,               // include legend
				true,
				false
        );
		NumberAxis rangeAxis = (NumberAxis) ((CategoryPlot) barChart.getPlot()).getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits()); 
		
		ReportDataProvider[] dataProviders = {new StatusDataProvider(),
				new IterationDataProvider(), new AssigneeDataProvider()};
		reportsList = new ArrayList<Report>();
		for (ReportDataProvider dataProvider: dataProviders) {
			for (ReportType type: ReportType.values()) {
				reportsList.add(new Report(dataProvider, type));
			}
		}
		report = reportsList.get(0);
		
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
		cardPanel.add(pieChartPanel, ReportType.PIE_CHART.toString());
		
		barChartPanel = new ChartPanel(barChart);
		cardPanel.add(barChartPanel, ReportType.BAR_CHART.toString());
		
		
		reports = new JComboBox(new DefaultComboBoxModel(reportsList.toArray()));
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
		System.out.println("Refreshing chart: "+report);
		((CardLayout) cardPanel.getLayout()).show(cardPanel, report.getType().toString());
		if (report.getType().equals(ReportType.PIE_CHART)) {
			pieDataset.clear();
			Map<Object, Integer> data = report.extractData(model);
			
			for (Object key : data.keySet()) {
				pieDataset.setValue(key.toString(), new Double(data.get(key)));
			}
			
			pieChart.setTitle(report.toString());
			pieChartPanel.repaint();
		} else if (report.getType().equals(ReportType.BAR_CHART)) {
			barDataset.clear();
			Map<Object, Integer> data = report.extractData(model);
			
			for (Object key : data.keySet()) {
				barDataset.setValue(new Double(data.get(key)), "", key.toString());
			}
			
			barChart.setTitle(report.toString());
			barChartPanel.repaint();
		}
	}
		
	/**
	 * close this tab/panel
	 *
	 */
	public void close() {
		parent.getTabController().closeCurrentTab();
	}
}
