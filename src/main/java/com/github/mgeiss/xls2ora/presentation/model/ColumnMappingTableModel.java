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
package com.github.mgeiss.xls2ora.presentation.model;

import com.github.mgeiss.xls2ora.domain.ColumnMapping;
import com.github.mgeiss.xls2ora.util.Messages;
import java.util.LinkedList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Markus Geiss
 * @version 1.0
 */
public class ColumnMappingTableModel extends AbstractTableModel {

    private List<ColumnMapping> data;

    public ColumnMappingTableModel() {
        super();
    }

    public void addColumnMapping(ColumnMapping columnMapping) {
        if (this.data == null) {
            this.data = new LinkedList<>();
        }
        this.data.add(columnMapping);
    }

    public void removeColumnMapping(int index) {
        if (this.data != null) {
            this.data.remove(index);
        }
    }

    public ColumnMapping getColumnMapping(int index) {
        ColumnMapping columnMapping = null;
        if (this.data != null) {
            this.data.get(index);
        }
        return columnMapping;
    }

    public List<ColumnMapping> getData() {
        return this.data;
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public int getRowCount() {
        if (this.data != null) {
            return data.size();
        }
        return 0;
    }

    @Override
    public Object getValueAt(int row, int column) {
        Object result = null;

        ColumnMapping columnMapping = this.data.get(row);

        switch (column) {
            case 0:
                result = columnMapping.getTableColumn();
                break;
            case 1:
                result = columnMapping.getSheetColumn();
                break;
            case 2:
                result = columnMapping.getDefaultValue();
                break;
        }

        return result;
    }

    @Override
    public Class<?> getColumnClass(int column) {
        Class<?> type = null;
        switch (column) {
            case 0:
                type = String.class;
                break;
            case 1:
                type = String.class;
                break;
            case 2:
                type = Object.class;
                break;
        }
        return type;
    }

    @Override
    public String getColumnName(int column) {
        String name = null;
        switch (column) {
            case 0:
                name = Messages.getText("xls2ora.columnmapping.tablemodel.dbcolumn");
                break;
            case 1:
                name = Messages.getText("xls2ora.columnmapping.tablemodel.excelcolumn");
                break;
            case 2:
                name = Messages.getText("xls2ora.columnmapping.tablemodel.default");
                break;
        }
        return name;
    }
}
