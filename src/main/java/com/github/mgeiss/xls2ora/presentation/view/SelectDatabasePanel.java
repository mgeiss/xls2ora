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

import com.github.mgeiss.xls2ora.domain.ConnectionProperties;
import com.github.mgeiss.xls2ora.presentation.control.WorkflowController;
import com.github.mgeiss.xls2ora.util.Messages;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.*;

/**
 *
 * @author Markus Geiss
 * @version 1.0
 */
public class SelectDatabasePanel extends WizardPanel implements ActionListener {

    private static final File DB_PROPERTIES_FILE = new File(System.getProperty("user.home") + "\\e11t\\connprop.dat");
    private SelectSourcePanel sourcePanel;
    private MappingPanel mappingPanel;
    private Color defaultLabelColor;
    private ConnectionProperties connectionProperties;
    private JTextField hostField;
    private JTextField portField;
    private JTextField serviceField;
    private JTextField tableField;
    private JTextField userField;
    private JPasswordField passwordField;
    private JLabel statusTestLabel;

    public SelectDatabasePanel(WorkflowController workflowController, SelectSourcePanel sourcePanel) {
        super(workflowController, Messages.getText("xls2ora.selectdb.panel.title"), Messages.getText("xls2ora.selectdb.panel.hint"), new ImageIcon(ClassLoader.getSystemResource("icons/database.png")));
        this.sourcePanel = sourcePanel;
        this.init();
    }

    private void init() {
        JPanel content = new JPanel(new GridBagLayout());

        content.add(new JLabel(Messages.getText("xls2ora.selectdb.panel.host")),
                new GridBagConstraints(0, 0, 1, 1, 0.00D, 0.00D, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 5), 1, 1));

        this.hostField = new JTextField();
        this.hostField.setMinimumSize(new Dimension(180, 23));
        this.hostField.setPreferredSize(new Dimension(180, 23));
        content.add(this.hostField,
                new GridBagConstraints(1, 0, 1, 1, 0.00D, 0.00D, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 5, 0, 0), 1, 1));

        content.add(new JLabel(Messages.getText("xls2ora.selectdb.panel.port")),
                new GridBagConstraints(2, 0, 1, 1, 0.00D, 0.00D, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 5), 1, 1));

        this.portField = new JTextField();
        this.portField.setMinimumSize(new Dimension(180, 23));
        this.portField.setPreferredSize(new Dimension(180, 23));
        content.add(this.portField,
                new GridBagConstraints(3, 0, 1, 1, 0.00D, 0.00D, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 5, 0, 0), 1, 1));

        content.add(new JLabel(Messages.getText("xls2ora.selectdb.panel.service")),
                new GridBagConstraints(0, 1, 1, 1, 0.00D, 0.00D, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 10, 0, 5), 1, 1));

        this.serviceField = new JTextField();
        this.serviceField.setMinimumSize(new Dimension(180, 23));
        this.serviceField.setPreferredSize(new Dimension(180, 23));
        content.add(this.serviceField,
                new GridBagConstraints(1, 1, 1, 1, 0.00D, 0.00D, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 0), 1, 1));

        content.add(new JLabel(Messages.getText("xls2ora.selectdb.panel.tablename")),
                new GridBagConstraints(2, 1, 1, 1, 0.00D, 0.00D, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 10, 0, 5), 1, 1));

        this.tableField = new JTextField();
        this.tableField.setMinimumSize(new Dimension(180, 23));
        this.tableField.setPreferredSize(new Dimension(180, 23));
        content.add(this.tableField,
                new GridBagConstraints(3, 1, 1, 1, 0.00D, 0.00D, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 0), 1, 1));

        content.add(new JLabel(Messages.getText("xls2ora.selectdb.panel.user")),
                new GridBagConstraints(0, 2, 1, 1, 0.00D, 0.00D, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 10, 0, 5), 1, 1));

        this.userField = new JTextField();
        this.userField.setMinimumSize(new Dimension(180, 23));
        this.userField.setPreferredSize(new Dimension(180, 23));
        content.add(this.userField,
                new GridBagConstraints(1, 2, 1, 1, 0.00D, 0.00D, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 0), 1, 1));

        content.add(new JLabel(Messages.getText("xls2ora.selectdb.panel.password")),
                new GridBagConstraints(2, 2, 1, 1, 0.00D, 0.00D, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 10, 0, 5), 1, 1));

        this.passwordField = new JPasswordField();
        this.passwordField.setMinimumSize(new Dimension(180, 23));
        this.passwordField.setPreferredSize(new Dimension(180, 23));
        content.add(this.passwordField,
                new GridBagConstraints(3, 2, 1, 1, 0.00D, 0.00D, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 0), 1, 1));

        this.statusTestLabel = new JLabel();
        this.defaultLabelColor = this.statusTestLabel.getForeground();
        content.add(this.statusTestLabel,
                new GridBagConstraints(0, 3, 3, 1, 0.00D, 0.00D, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 10, 0, 0), 1, 1));

        JButton testButton = new JButton(Messages.getText("xls2ora.selectdb.panel.testconnection"));
        testButton.addActionListener(this);
        content.add(testButton,
                new GridBagConstraints(3, 3, 1, 1, 0.00D, 0.00D, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 10, 0, 0), 1, 1));

        content.add(new JLabel(),
                new GridBagConstraints(4, 4, 1, 1, 1.00D, 1.00D, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));

        super.addContent(content);
    }

    @Override
    public WizardPanel getPredecessorPanel() {
        return this.sourcePanel;
    }

    @Override
    public WizardPanel getSuccesorPanel() {
        if (this.mappingPanel == null) {
            this.mappingPanel = new MappingPanel(super.workflowController, this);
        }
        return this.mappingPanel;
    }

    @Override
    public void prepare() {
        this.tableField.setText(this.workflowController.getAttribute("sheet").toString());
        if (SelectDatabasePanel.DB_PROPERTIES_FILE.exists()) {
            try {
                FileInputStream fin = new FileInputStream(SelectDatabasePanel.DB_PROPERTIES_FILE);
                try (ObjectInputStream oin = new ObjectInputStream(fin)) {
                    this.connectionProperties = (ConnectionProperties) oin.readObject();
                }

                this.hostField.setText(this.connectionProperties.getHost());
                this.portField.setText(this.connectionProperties.getPort());
                this.serviceField.setText(this.connectionProperties.getService());
                this.userField.setText(this.connectionProperties.getUser());
                this.passwordField.setText(new String(this.connectionProperties.getPassword()));

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void proceed() {
        if (super.workflowController.getAttribute("databaseConnection") == null) {
            this.testConnection();
        }
        if (super.workflowController.getAttribute("databaseConnection") == null) {
            throw new IllegalStateException();
        }
    }

    private void testConnection() {
        this.connectionProperties = new ConnectionProperties();
        this.connectionProperties.setHost(this.hostField.getText());
        this.connectionProperties.setPort(this.portField.getText());
        this.connectionProperties.setService(this.serviceField.getText());
        this.connectionProperties.setUser(this.userField.getText());
        this.connectionProperties.setPassword(this.passwordField.getPassword());

        if (this.connectionProperties.getConnection() == null) {
            super.workflowController.setAttribute("databaseConnection", null);
            super.workflowController.setAttribute("targetTable", null);
            this.statusTestLabel.setForeground(Color.RED);
            this.statusTestLabel.setText(Messages.getText("xls2ora.selectdb.panel.connectiontestfailed"));
        } else {
            try {
                if (!SelectDatabasePanel.DB_PROPERTIES_FILE.exists()) {
                    File dir = new File(System.getProperty("user.home") + "/e11t");
                    if (!dir.exists()) {
                        dir.mkdir();
                    }
                    SelectDatabasePanel.DB_PROPERTIES_FILE.createNewFile();
                }
                FileOutputStream fout = new FileOutputStream(SelectDatabasePanel.DB_PROPERTIES_FILE);
                try (ObjectOutputStream oos = new ObjectOutputStream(fout)) {
                    oos.writeObject(this.connectionProperties);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            super.workflowController.setAttribute("databaseConnection", this.connectionProperties.getConnection());
            super.workflowController.setAttribute("targetTable", this.tableField.getText());
            this.statusTestLabel.setForeground(this.defaultLabelColor);
            this.statusTestLabel.setText(Messages.getText("xls2ora.selectdb.panel.connectiontestsucceeded"));
        }
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        this.testConnection();
    }
}
