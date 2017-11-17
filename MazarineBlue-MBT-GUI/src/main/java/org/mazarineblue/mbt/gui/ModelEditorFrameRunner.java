/*
 * Copyright (c) 2016 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.mazarineblue.mbt.gui;

import static java.awt.EventQueue.invokeLater;
import static java.util.logging.Logger.getLogger;
import org.mazarineblue.mbt.gui.model.GraphModel;
import org.mazarineblue.mbt.gui.model.State;
import org.mazarineblue.mbt.gui.model.Transition;

public class ModelEditorFrameRunner {

    public static void main(String[] args) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
        } catch (ClassNotFoundException ex) {
            getLogger(ModelEditorFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            getLogger(ModelEditorFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            getLogger(ModelEditorFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            getLogger(ModelEditorFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        GraphModel model = GraphModel.createDefault();
        State start = State.createDefault("Start").addViews("Login");
        State l0 = State.createDefault("Level 0").addViews("Login", "Stairs");
        State l1 = State.createDefault("Level 1").addViews("Stairs");
        State l2 = State.createDefault("Level 2").addViews("Stairs");
        State l3 = State.createDefault("Level 3").addViews("Stairs");
        model.addState(start, l0, l1, l2, l3);

        model.addTransition(Transition.createDefault("Wrong username").setSources(start).setDestination(start));
        model.addTransition(Transition.createDefault("Wrong password").setSources(start).setDestination(start));
        model.addTransition(Transition.createDefault("Correct username & password").setSources(start).setDestination(l0));

        model.addTransition(Transition.createDefault("To Level 0").setSources(l1).setDestination(l0));
        model.addTransition(Transition.createDefault("To Level 1").setSources(l0, l2).setDestination(l1));
        model.addTransition(Transition.createDefault("To Level 2").setSources(l1).setDestination(l2));

        ModelEditorFrame frame = new ModelEditorFrame(model);
        invokeLater(() -> frame.setVisible(true));
        Thread.setDefaultUncaughtExceptionHandler((t, ex) -> {
            ExceptionDialog dialog = new ExceptionDialog(frame, ex);
            invokeLater(() -> dialog.setVisible(true));
        });
    }

    private ModelEditorFrameRunner() {
    }
}
