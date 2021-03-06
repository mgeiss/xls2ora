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
package com.github.mgeiss.xls2ora;

import com.github.mgeiss.xls2ora.presentation.control.WorkflowController;
import com.github.mgeiss.xls2ora.presentation.view.SelectSourcePanel;
import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import com.jgoodies.looks.plastic.theme.ExperienceBlue;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * <code>Xls2Ora</code> is a wizard based utility to import a sheet from a .xls
 * File into an Oracle Database. It is possible to rename the columns and set a
 * default value before importing.
 *
 * @author Markus Geiss
 * @version 1.0
 */
public class Xls2Ora {

    public Xls2Ora() {
        super();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeAndWait(() -> {
            try {
                PlasticLookAndFeel.setPlasticTheme(new ExperienceBlue());
                UIManager.setLookAndFeel(new PlasticLookAndFeel());
            } catch (Exception ex) {
            }
        });

        WorkflowController controller = new WorkflowController();
        controller.setStartPanel(new SelectSourcePanel(controller));
    }
}
