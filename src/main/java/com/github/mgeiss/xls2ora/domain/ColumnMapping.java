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
package com.github.mgeiss.xls2ora.domain;

/**
 *
 * @author Markus Geiss
 * @version 1.0
 */
public class ColumnMapping {

    private String sheetColumn;
    private String tableColumn;
    private Object defaultValue;

    public ColumnMapping() {
        super();
    }

    public String getSheetColumn() {
        return sheetColumn;
    }

    public void setSheetColumn(String sheetColumn) {
        this.sheetColumn = sheetColumn;
    }

    public String getTableColumn() {
        return tableColumn;
    }

    public void setTableColumn(String tableColumn) {
        this.tableColumn = tableColumn;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }
}
