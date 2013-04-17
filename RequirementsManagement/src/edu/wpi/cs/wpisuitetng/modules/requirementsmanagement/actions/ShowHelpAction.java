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

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.actions;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.AbstractAction;

import org.jfree.io.IOUtils;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.utils.MainTabController;


/**
 * Action for opening a pdf for help.
 *
 */
@SuppressWarnings("serial")
public class ShowHelpAction extends AbstractAction{

	File pdfFile = null;
	
	public ShowHelpAction() {
		super("Help");
		putValue(MNEMONIC_KEY, KeyEvent.VK_F1);
	}
	
	/**
	 * The action performed, adds a createIterationTab to the main tab controller
	 *
	 * @param e The action event
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (Desktop.isDesktopSupported()) {
		    try {
		    	if (pdfFile == null || !pdfFile.exists()) {
		    		pdfFile = File.createTempFile("help-reqmanagment-", ".pdf");
		    		InputStream in = ShowHelpAction.class.getResourceAsStream("/help.pdf");
		    		OutputStream out = new FileOutputStream(pdfFile);
		    		IOUtils.getInstance().copyStreams(in, out);
		    	}

		        Desktop.getDesktop().open(pdfFile);
		    } catch (IOException ex) {
		        // no application registered for PDFs
		    }
		}
	}
}
