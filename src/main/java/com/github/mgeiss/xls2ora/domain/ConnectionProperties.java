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

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Markus Geiss
 * @version 1.0
 */
public class ConnectionProperties implements Serializable {

    private String host;
    private String port;
    private String service;
    private String user;
    private char[] password;
    private transient Connection connection;

    public ConnectionProperties() {
        super();
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public char[] getPassword() {
        return password;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    public Connection getConnection() throws ClassNotFoundException, SQLException {
        if (this.connection == null) {
            Class.forName("oracle.jdbc.OracleDriver");

            StringBuilder connectionString = new StringBuilder();
            connectionString.append("jdbc:oracle:thin:");
            connectionString.append("@");
            connectionString.append(this.host);
            connectionString.append(":");
            connectionString.append(this.port);
            connectionString.append(":");
            connectionString.append(this.service);

            this.connection = DriverManager.getConnection(connectionString.toString(), this.user, new String(this.password));
            this.connection.setAutoCommit(false);
        }

        return this.connection;
    }

    public void cleanUp() {
        if (this.connection != null) {
            try {
                this.connection.close();
                this.connection = null;
            } catch (Exception ex) {
            }
        }
    }
}
