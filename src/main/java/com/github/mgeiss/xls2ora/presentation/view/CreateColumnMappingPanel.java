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

import com.github.mgeiss.xls2ora.util.Messages;
import java.awt.*;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Markus Geiss
 * @version 1.0
 */
public class CreateColumnMappingPanel extends JPanel {
    
	private MappingPanel mappingPanel;
	public CreateColumnMappingPanel(MappingPanel mappingPanel) {
		super();
		this.mappingPanel = mappingPanel;
		this.init();
	}
	
	protected JComboBox datbaseColumnComboBox;
	protected JComboBox sheetColumnComboBox;
	private void init() {
		JPanel contentPanel = new JPanel(new BorderLayout());
		
		JPanel selectionPanel = new JPanel(new GridBagLayout());
		contentPanel.add(selectionPanel, BorderLayout.CENTER);
		
		selectionPanel.add(new JLabel(Messages.getText("xls2ora.createcolumnmapping.panel.dbcolumn")), 
				new GridBagConstraints(0, 0, 1, 1, 0.00D, 0.00D, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 1, 1));

		this.datbaseColumnComboBox = new JComboBox(this.mappingPanel.tableColumns.toArray());
		this.datbaseColumnComboBox.setMinimumSize(new Dimension(120, 23));
		this.datbaseColumnComboBox.setPreferredSize(new Dimension(120, 23));
		selectionPanel.add(this.datbaseColumnComboBox, 
				new GridBagConstraints(1, 0, 1, 1, 0.00D, 0.00D, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 1, 1));

		selectionPanel.add(new JLabel(Messages.getText("xls2ora.createcolumnmapping.panel.excelcolumn")), 
				new GridBagConstraints(2, 0, 1, 1, 0.00D, 0.00D, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 1, 1));

		this.sheetColumnComboBox = new JComboBox(this.mappingPanel.sheetColumns.toArray());
		this.sheetColumnComboBox.setMinimumSize(new Dimension(120, 23));
		this.sheetColumnComboBox.setPreferredSize(new Dimension(120, 23));
		selectionPanel.add(this.sheetColumnComboBox, 
				new GridBagConstraints(3, 0, 1, 1, 0.00D, 0.00D, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 1, 1));

		super.add(contentPanel);
	}
}
