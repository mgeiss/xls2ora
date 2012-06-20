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

import com.github.mgeiss.xls2ora.presentation.control.WorkflowController;
import java.awt.*;
import javax.swing.*;

/**
 *
 * @author Markus Geiss
 * @version 1.0
 */
public abstract class WizardPanel extends JPanel {

    protected WorkflowController workflowController;
    private String header;
    private String additionalInfo;
    private ImageIcon icon;

    public WizardPanel(WorkflowController workflowController, String header, String additionalInfo, ImageIcon icon) {
        super();
        this.workflowController = workflowController;
        this.header = header;
        this.additionalInfo = additionalInfo;
        this.icon = icon;
        this.init();
    }

    public abstract WizardPanel getPredecessorPanel();

    public abstract WizardPanel getSuccesorPanel();

    public abstract void prepare();

    public abstract void proceed();
    private JPanel contentWrapper = new JPanel(new BorderLayout());

    protected void addContent(Component content) {
        this.contentWrapper.add(content, BorderLayout.CENTER);
        this.contentWrapper.revalidate();
        this.contentWrapper.repaint();
    }

    private void init() {
        super.setLayout(new BorderLayout());
        super.add(this.getHeaderPanel(), BorderLayout.NORTH);
        this.contentWrapper = new JPanel(new BorderLayout());
        this.contentWrapper.add(new JSeparator(), BorderLayout.NORTH);
        this.contentWrapper.add(new JSeparator(), BorderLayout.SOUTH);
        super.add(contentWrapper, BorderLayout.CENTER);
    }

    private JPanel getHeaderPanel() {
        JPanel headerPanel = new JPanel(new GridBagLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        headerPanel.setBackground(Color.WHITE);

        JLabel headerLabel = new JLabel(this.header);
        headerLabel.setOpaque(false);
        headerLabel.setFont(headerLabel.getFont().deriveFont(Font.BOLD));
        headerPanel.add(headerLabel,
                new GridBagConstraints(0, 0, 1, 1, 0.00D, 0.00D, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));

        JLabel additionalInfoLbl = new JLabel(this.additionalInfo);
        additionalInfoLbl.setOpaque(false);
        additionalInfoLbl.setFont(additionalInfoLbl.getFont().deriveFont(Font.PLAIN));
        headerPanel.add(additionalInfoLbl,
                new GridBagConstraints(0, 1, 1, 1, 0.00D, 0.00D, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 20, 10, 0), 0, 0));

        JLabel spacerLabel = new JLabel();
        spacerLabel.setOpaque(false);
        headerPanel.add(spacerLabel,
                new GridBagConstraints(1, 0, 1, 2, 1.00D, 0.00D, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

        JLabel iconLabel = new JLabel(this.icon);
        iconLabel.setOpaque(false);
        headerPanel.add(iconLabel,
                new GridBagConstraints(2, 0, 1, 2, 0.00D, 0.00D, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

        return headerPanel;
    }
}
