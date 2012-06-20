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
package com.github.mgeiss.xls2ora.business;

import com.github.mgeiss.xls2ora.domain.ColumnMapping;
import java.awt.HeadlessException;
import java.sql.*;
import java.util.List;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

/**
 *
 * @author Markus Geiss
 * @version 1.0
 */
public class ImportThread extends Thread {

    private WorkflowController workflowController;
    private JProgressBar progressBar;

    public ImportThread(WorkflowController workflowController, JProgressBar progressBar) {
        super();
        this.workflowController = workflowController;
        this.progressBar = progressBar;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void run() {
        try {
            Connection databaseConnection;
            PreparedStatement dataBaseStatement;
            try (Connection excelConnection = (Connection) this.workflowController.getAttribute("excelConnection"); Statement excelStmt = excelConnection.createStatement()) {
                try (ResultSet countRset = excelStmt.executeQuery("select count(*) from [" + this.workflowController.getAttribute("sheet") + "$]")) {
                    if (countRset.next()) {
                        this.progressBar.setMaximum(countRset.getInt(1));
                    }
                }
                this.progressBar.setStringPainted(true);
                try (ResultSet rset = excelStmt.executeQuery("select * from [" + this.workflowController.getAttribute("sheet") + "$]")) {
                    databaseConnection = (Connection) this.workflowController.getAttribute("databaseConnection");
                    StringBuilder insertSql = new StringBuilder();
                    insertSql.append("insert into ");
                    insertSql.append(this.workflowController.getAttribute("targetTable"));
                    insertSql.append("(");
                    StringBuilder values = new StringBuilder();
                    List<ColumnMapping> colList = (List<ColumnMapping>) this.workflowController.getAttribute("mapping");
                    int count = 1;
                    for (ColumnMapping columnMapping : colList) {
                        insertSql.append(columnMapping.getTableColumn());
                        values.append("?");
                        if (count < colList.size()) {
                            insertSql.append(", ");
                            values.append(", ");
                        }
                        count++;
                    }
                    insertSql.append(") values ( ");
                    insertSql.append(values);
                    insertSql.append(")");
                    dataBaseStatement = databaseConnection.prepareStatement(insertSql.toString());
                    int value = 1;
                    while (rset.next()) {
                        count = 1;
                        for (ColumnMapping columnMapping : colList) {
                            if (columnMapping.getSheetColumn() != null) {
                                dataBaseStatement.setObject(count, rset.getObject(columnMapping.getSheetColumn()));
                            } else {
                                dataBaseStatement.setObject(count, columnMapping.getDefaultValue());
                            }
                            count++;
                        }
                        dataBaseStatement.executeUpdate();
                        databaseConnection.commit();
                        this.progressBar.setValue(value++);
                    }
                }
            }
            dataBaseStatement.close();
            databaseConnection.close();

            JOptionPane.showMessageDialog(this.progressBar.getParent(), "Import der Daten erfolgreich beendet!");
            System.exit(0);
        } catch (SQLException | HeadlessException ex) {
            JXErrorPane.showDialog(null, new ErrorInfo("Fehler beim Importieren!", "Es ist ein unerwartet Fehler aufgetreten!", null, null, ex, Level.SEVERE, System.getenv()));
        }
    }
}
