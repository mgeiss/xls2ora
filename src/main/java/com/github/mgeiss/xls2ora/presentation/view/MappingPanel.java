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

import com.github.mgeiss.xls2ora.domain.ColumnData;
import com.github.mgeiss.xls2ora.domain.ColumnMapping;
import com.github.mgeiss.xls2ora.presentation.control.WorkflowController;
import com.github.mgeiss.xls2ora.presentation.model.ColumnMappingTableModel;
import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.error.ErrorInfo;

/**
 *
 * @author Markus Geiss
 * @version 1.0
 */
public class MappingPanel extends WizardPanel implements ActionListener, ListSelectionListener {

    private SelectDatabasePanel databasePanel;
    private StatusPanel statusPanel;
    protected List<ColumnData> sheetColumns;
    protected List<ColumnData> tableColumns;
    private JPanel content;
    private JButton addButton;
    private JButton addDefaultButton;
    private JButton removeButton;
    private ColumnMappingTableModel tableModel;
    private JXTable table;

    public MappingPanel(WorkflowController workflowController, SelectDatabasePanel databasePanel) {
        super(workflowController, "Zuordnung", "Ordnen sie die Spalten der Excel-Tabelle den Spalten der DB-Tabelle zu.", new ImageIcon(MappingPanel.class.getResource("/org/geiss/tools/e11t/resource/image/mapping.png")));
        this.databasePanel = databasePanel;
        this.init();
    }

    private void init() {
        this.content = new JPanel(new BorderLayout());

        JToolBar tableControl = new JToolBar();
        tableControl.setRollover(true);
        tableControl.setFloatable(false);

        this.addButton = new JButton(new ImageIcon(MappingPanel.class.getResource("/org/geiss/tools/e11t/resource/image/table_add.png")));
        this.addButton.setToolTipText("Zuordnung hinzufügen");
        this.addButton.addActionListener(this);
        tableControl.add(addButton);

        this.addDefaultButton = new JButton(new ImageIcon(MappingPanel.class.getResource("/org/geiss/tools/e11t/resource/image/table_edit.png")));
        this.addDefaultButton.setToolTipText("Vorgabewert hinzufügen");
        this.addDefaultButton.addActionListener(this);
        tableControl.add(addDefaultButton);

        this.removeButton = new JButton(new ImageIcon(MappingPanel.class.getResource("/org/geiss/tools/e11t/resource/image/table_delete.png")));
        this.removeButton.setToolTipText("Eintrag entfernen");
        this.removeButton.addActionListener(this);
        this.removeButton.setEnabled(false);
        tableControl.add(removeButton);

        this.content.add(tableControl, BorderLayout.NORTH);

        this.tableModel = new ColumnMappingTableModel();
        this.table = new JXTable(this.tableModel);
        this.table.setColumnControlVisible(true);
        this.table.setHighlighters(HighlighterFactory.createAlternateStriping());
        this.table.getSelectionModel().addListSelectionListener(this);
        this.content.add(new JScrollPane(this.table), BorderLayout.CENTER);

        super.addContent(content);
    }

    private void addMapping() {
        CreateColumnMappingPanel createPanel = new CreateColumnMappingPanel(this);
        if (JOptionPane.showConfirmDialog(this, createPanel, "Zuordnung", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE) == JOptionPane.OK_OPTION) {
            ColumnMapping columnMapping = new ColumnMapping();
            columnMapping.setTableColumn(createPanel.datbaseColumnComboBox.getSelectedItem().toString());
            columnMapping.setSheetColumn(createPanel.sheetColumnComboBox.getSelectedItem().toString());
            this.tableModel.addColumnMapping(columnMapping);
            this.tableModel.fireTableDataChanged();
        }
    }

    private void addDefaultValue() {
        try {
            CreateColumnDefaultValuePanel createPanel = new CreateColumnDefaultValuePanel(this);
            if (JOptionPane.showConfirmDialog(this, createPanel, "Zuordnung", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE) == JOptionPane.OK_OPTION) {
                ColumnMapping columnMapping = new ColumnMapping();

                ColumnData selectedColumnData = (ColumnData) createPanel.datbaseColumnComboBox.getSelectedItem();
                columnMapping.setTableColumn(selectedColumnData.toString());
                if (selectedColumnData.getType().equals(Timestamp.class.getName())) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                    columnMapping.setDefaultValue(new Timestamp(sdf.parse(createPanel.columnField.getText()).getTime()));
                } else if (selectedColumnData.getType().equals(BigDecimal.class.getName())) {
                    NumberFormat.getInstance();
                    columnMapping.setDefaultValue(new BigDecimal(NumberFormat.getInstance().parse(createPanel.columnField.getText()).doubleValue()));
                } else {
                    columnMapping.setDefaultValue(createPanel.columnField.getText());
                }
                this.tableModel.addColumnMapping(columnMapping);
                this.tableModel.fireTableDataChanged();
            }
        } catch (HeadlessException | ParseException ex) {
        }
    }

    private void removeMapping() {
        int selectedRow = this.table.getSelectedRow();
        if (selectedRow > -1) {
            this.tableModel.removeColumnMapping(this.table.convertRowIndexToModel(selectedRow));
            this.tableModel.fireTableDataChanged();
        }
    }

    @Override
    public WizardPanel getPredecessorPanel() {
        return this.databasePanel;
    }

    @Override
    public WizardPanel getSuccesorPanel() {
        if (this.statusPanel == null) {
            this.statusPanel = new StatusPanel(super.workflowController);
        }
        return this.statusPanel;
    }

    @Override
    public void prepare() {
        this.sheetColumns = new LinkedList<>();
        this.tableColumns = new LinkedList<>();
        try {
            Connection connection = (Connection) super.workflowController.getAttribute("excelConnection");
            try (Statement stmt = connection.createStatement(); ResultSet rset = stmt.executeQuery("select * from [" + super.workflowController.getAttribute("sheet") + "$]")) {
                ResultSetMetaData rsetMetaData = rset.getMetaData();
                for (int i = 1; i <= rsetMetaData.getColumnCount(); i++) {
                    sheetColumns.add(new ColumnData(rsetMetaData.getColumnLabel(i), rsetMetaData.getColumnClassName(i)));
                }
            }
        } catch (Exception ex) {
            JXErrorPane.showDialog(null, new ErrorInfo("Zuordnungsfehler!", "Bitte überprüfen sie ihre Angaben zur Quelldatei!", null, null, ex, Level.SEVERE, System.getenv()));
            throw new IllegalStateException();
        }

        try {
            Connection connection = (Connection) super.workflowController.getAttribute("databaseConnection");
            try (Statement stmt = connection.createStatement(); ResultSet rset = stmt.executeQuery("select * from " + super.workflowController.getAttribute("targetTable"))) {
                ResultSetMetaData rsetMetaData = rset.getMetaData();
                for (int i = 1; i <= rsetMetaData.getColumnCount(); i++) {
                    tableColumns.add(new ColumnData(rsetMetaData.getColumnLabel(i), rsetMetaData.getColumnClassName(i)));
                }
            }
        } catch (Exception ex) {
            if (ex.getMessage().startsWith("ORA-00942")) {
                if (JOptionPane.showConfirmDialog(this, "Tabelle " + super.workflowController.getAttribute("targetTable") + " nicht gefunden, soll die Tabelle erzeugt werden?", "Zieltabelle nicht gefunden!", JOptionPane.ERROR_MESSAGE) == JOptionPane.OK_OPTION) {
                    this.createTargetTable();
                    this.prepare();
                } else {
                    throw new IllegalStateException();
                }
            } else {
                JXErrorPane.showDialog(null, new ErrorInfo("Zuordnungsfehler!", "Bitte überprüfen sie ihre Angaben zur Zieldatenbank!", null, null, ex, Level.SEVERE, System.getenv()));
                throw new IllegalStateException();
            }
        }
    }

    private void createTargetTable() {
        try {
            StringBuilder createSQL = new StringBuilder();
            createSQL.append("create table ");
            createSQL.append(super.workflowController.getAttribute("targetTable"));
            createSQL.append(" ( ");

            Connection excelConnection = (Connection) super.workflowController.getAttribute("excelConnection");
            try (Statement excelStmt = excelConnection.createStatement(); ResultSet excelRset = excelStmt.executeQuery("select * from [" + super.workflowController.getAttribute("sheet") + "$]")) {
                ResultSetMetaData rsetMetaData = excelRset.getMetaData();
                boolean first = true;
                String sheetColumn = null;
                String columnName = null;
                for (int i = 1; i <= rsetMetaData.getColumnCount(); i++) {
                    if (first) {
                        first = false;
                    } else {
                        createSQL.append(", ");
                    }
                    sheetColumn = rsetMetaData.getColumnLabel(i);
                    columnName = sheetColumn;
                    columnName = columnName.replaceAll(" ", "_");
                    columnName = columnName.replaceAll("\\(", "_");
                    columnName = columnName.replaceAll("\\)", "_");
                    columnName = columnName.replaceAll("#", "_");
                    columnName = columnName.replaceAll("-", "_");
                    columnName = columnName.replaceAll("ö", "oe");
                    columnName = columnName.replaceAll("ü", "ue");
                    columnName = columnName.replaceAll("ä", "ae");
                    columnName = columnName.replaceAll("Ö", "OE");
                    columnName = columnName.replaceAll("Ü", "UE");
                    columnName = columnName.replaceAll("Ä", "AE");
                    columnName = "a_" + columnName;
                    columnName = columnName.toLowerCase();
                    createSQL.append(columnName);

                    if (rsetMetaData.getColumnClassName(i).equals(BigDecimal.class.getName())
                            || rsetMetaData.getColumnClassName(i).equals(Double.class.getName())
                            || rsetMetaData.getColumnClassName(i).equals(Float.class.getName())
                            || rsetMetaData.getColumnClassName(i).equals(Long.class.getName())
                            || rsetMetaData.getColumnClassName(i).equals(Integer.class.getName())) {
                        createSQL.append(" number(");
                        createSQL.append(rsetMetaData.getColumnDisplaySize(i));
                        createSQL.append(", ");
                        createSQL.append(rsetMetaData.getPrecision(i));
                        createSQL.append(")");

                    } else if (rsetMetaData.getColumnClassName(i).equals(Timestamp.class.getName())) {
                        createSQL.append(" timestamp(3)");
                    } else if (rsetMetaData.getColumnClassName(i).equals(String.class.getName())) {
                        createSQL.append(" varchar2(");
                        createSQL.append(rsetMetaData.getColumnDisplaySize(i));
                        createSQL.append(")");
                    }
                    ColumnMapping columnMapping = new ColumnMapping();
                    columnMapping.setTableColumn(columnName);
                    columnMapping.setSheetColumn(sheetColumn);
                    this.tableModel.addColumnMapping(columnMapping);
                }
                createSQL.append(")");
            }

            Connection databaseConnection = (Connection) super.workflowController.getAttribute("databaseConnection");
            Statement databaseStmt = databaseConnection.createStatement();
            databaseStmt.execute(createSQL.toString());
            databaseStmt.close();
        } catch (Exception ex) {
            JXErrorPane.showDialog(null, new ErrorInfo("Datenbankfehler!", "Tabelle " + super.workflowController.getAttribute("targetTable") + " konnte nicht erzeugt werden.!", null, null, ex, Level.SEVERE, System.getenv()));
            throw new IllegalStateException();
        }
    }

    @Override
    public void proceed() {
        if (this.tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Bitte ordnen sie mindestes eine Spalte zu!", "Fehler bei der Zuordnung!", JOptionPane.ERROR_MESSAGE);
            throw new IllegalStateException();
        } else {
            super.workflowController.setAttribute("mapping", this.tableModel.getData());
        }
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource().equals(this.addButton)) {
            this.addMapping();
        } else if (evt.getSource().equals(this.removeButton)) {
            this.removeMapping();
        } else if (evt.getSource().equals(this.addDefaultButton)) {
            this.addDefaultValue();
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent evt) {
        if (evt.getFirstIndex() == -1) {
            this.removeButton.setEnabled(false);
        } else {
            this.removeButton.setEnabled(true);
        }
    }
}
