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
package org.mazarineblue.mbt.gui.control;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.WeakReference;
import java.util.function.Consumer;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JDialog;

/**
 * A {@code DisposeDialogAction} is a {@link Action} that makes a call to the
 * {@link JDialog#dispose} method.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class ConsumerAction
        extends AbstractAction {

    private static final long serialVersionUID = 1L;
    private transient WeakReference<JDialog> owner;
    private final Consumer<ActionEvent> consumer;

    /**
     * Constructs a {@code DisposeDialogAction} that disposes the specified
     * dialog, when called.
     *
     * @param owner the dialog to dispose.
     * @param test  the name ({@code Action.NAME}) for the action.
     */
    public ConsumerAction(JDialog owner, String test, Consumer<ActionEvent> consumer) {
        super(test);
        this.owner = new WeakReference<>(owner);
        this.consumer = consumer;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        consumer.accept(e);
        owner.get().dispose();
    }

    private void writeObject(ObjectOutputStream output)
            throws IOException {
        output.writeObject(owner.get());
    }

    private void readObject(ObjectInputStream input)
            throws IOException, ClassNotFoundException {
        owner = new WeakReference<>((JDialog) input.readObject());
    }
}
