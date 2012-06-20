/*
 * Copyright 2012 Markus Geiss.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.mgeiss.xls2ora.presentation.view;

import com.github.mgeiss.xls2ora.business.ImportThread;
import com.github.mgeiss.xls2ora.presentation.control.WorkflowController;
import java.awt.*;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 *
 * @author Markus Geiss
 * @version 1.0
 */
public class StatusPanel extends WizardPanel {

    private JProgressBar transferProgress;
	private JPanel transferPanel;
    
	public StatusPanel(WorkflowController workflowController) {
		super(workflowController, "Import", "Die Daten können nun übertragen werden.", new ImageIcon(ClassLoader.getSystemResource("icons/run.png")));
		this.init();
	}
	
	private void init() {
		this.transferPanel = new JPanel(new GridBagLayout());
		
		this.transferPanel.add(new JLabel(""),
				new GridBagConstraints(0, 0, 1, 1, 0.00D, 0.30D, GridBagConstraints.WEST, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 1, 1));
		
		JLabel titleLabel = new JLabel("Transferstatus:");
		titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD));
		this.transferPanel.add(titleLabel,
				new GridBagConstraints(0, 1, 1, 1, 0.00D, 0.00D, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 1, 1));

		this.transferProgress = new JProgressBar();
		this.transferPanel.add(this.transferProgress,
				new GridBagConstraints(0, 2, 1, 1, 0.00D, 0.00D, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 1, 1));
		this.transferProgress.setPreferredSize(new Dimension(400, 23));
		
		JPanel statusPanel = new JPanel(new GridBagLayout());
		this.transferPanel.add(statusPanel,
				new GridBagConstraints(0, 3, 1, 1, 0.00D, 0.00D, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 1, 1));
		
		this.transferPanel.add(new JLabel(),
				new GridBagConstraints(0, 4, 1, 1, 0.00D, 0.50D, GridBagConstraints.WEST, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 1, 1));
		
		super.addContent(this.transferPanel);
	}

	@Override
	public WizardPanel getPredecessorPanel() {
		return null;
	}

	@Override
	public WizardPanel getSuccesorPanel() {
		return null;
	}

	@Override
	public void prepare() {
	}

	@Override
	public void proceed() {
		new ImportThread(super.workflowController, this.transferProgress).start();
	}
}
