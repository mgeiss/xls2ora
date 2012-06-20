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
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Markus Geiss
 */
public class ContentContainer extends JPanel {

    private WorkflowController workflowController;
    private WizardPanel actualWizardPanel;
    private JButton backButton;
    private JButton nextButton;

    public ContentContainer(WorkflowController workflowController) {
        super();
        this.workflowController = workflowController;
        this.init();
    }

    public void setWizardPanel(WizardPanel wizardPanel) {
        if (this.actualWizardPanel != null) {
            super.remove(this.actualWizardPanel);
        }

        super.add(wizardPanel, BorderLayout.CENTER);
        this.actualWizardPanel = wizardPanel;

        super.revalidate();
        super.repaint();
    }

    public WizardPanel getWizardPanel() {
        return this.actualWizardPanel;
    }

    public void setBackButtonEnabled(boolean enabled) {
        this.backButton.setEnabled(enabled);
    }

    public void switchNext(boolean done) {
        if (done) {
            this.nextButton.setActionCommand(WorkflowController.ACTION_DONE);
            this.nextButton.setText("Fertig");
        } else {
            this.nextButton.setActionCommand(WorkflowController.ACTION_NEXT);
            this.nextButton.setText("Weiter>");
        }
    }

    private void init() {
        super.setLayout(new BorderLayout());

        super.add(this.getButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel getButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        this.backButton = new JButton("<Zurück");
        this.backButton.addActionListener(this.workflowController);
        this.backButton.setActionCommand(WorkflowController.ACTION_BACK);
        buttonPanel.add(this.backButton);

        this.nextButton = new JButton("Weiter>");
        this.nextButton.addActionListener(this.workflowController);
        this.nextButton.setActionCommand(WorkflowController.ACTION_NEXT);
        buttonPanel.add(this.nextButton);

        Dimension spacerDimension = new Dimension(10, 1);
        JLabel spacerLabel = new JLabel();
        spacerLabel.setMinimumSize(spacerDimension);
        spacerLabel.setMaximumSize(spacerDimension);
        spacerLabel.setPreferredSize(spacerDimension);
        buttonPanel.add(spacerLabel);

        JButton cancelButton = new JButton("Abbrechen");
        cancelButton.addActionListener(this.workflowController);
        cancelButton.setActionCommand(WorkflowController.ACTION_CANCEL);
        buttonPanel.add(cancelButton);

        return buttonPanel;
    }
}
