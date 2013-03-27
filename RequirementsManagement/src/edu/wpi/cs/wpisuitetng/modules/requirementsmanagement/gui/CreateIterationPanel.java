package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.TextArea;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.actions.IterationTab;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.CreateIterationController;

@SuppressWarnings("serial")
public class CreateIterationPanel extends JPanel{
	
	IterationTab parent;
	private GridBagLayout panelLayout;
	JLabel lbl1, lbl2, lbl3;
	TextArea startDate, endDate, iterationNumber;
	JButton submit;
	boolean inputEnable;
	
	public CreateIterationPanel(IterationTab iterationTab){
		this.parent = iterationTab;
		
		inputEnable =true;
		
		addComponents();	
	}

	
	private void addComponents(){
		panelLayout =new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		setLayout(panelLayout);
		
		lbl1 = new JLabel("Start Date (mm/dd/yyyy)");
		lbl2 = new JLabel("End Date (mm/dd/yyyy)");
		lbl3 = new JLabel ("Iteration Number");
		
		startDate = new TextArea();
		endDate = new TextArea();
		iterationNumber = new TextArea();
		
		submit = new JButton("Submit");
		submit.addActionListener(new CreateIterationController(this));
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		add(lbl1, c);
		c.gridx = 1;
		add(startDate, c);
		c.gridy = 1;
		c.gridx = 0;
		add(lbl2, c);
		c.gridx = 1;
		add(endDate, c);
		c.gridy = 2;
		c.gridx = 0;
		add(lbl3, c);
		c.gridx = 1;
		add(iterationNumber, c);
		c.gridy = 3;
		add(submit, c);
		
		
	}
}
