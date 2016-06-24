/*
 * Copyright (c) 2012-2014 Alex de Kruijff
 * Copyright (c) 2014-2015 Specialisterren
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
package org.mazarineblue.runner.swing;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileFilter;
import org.mazarineblue.keyworddriven.sheetfactories.SheetFactory;
import org.mazarineblue.keyworddriven.feeds.Feed;
import org.mazarineblue.runner.RecentFiles;
import org.mazarineblue.runner.exceptions.FactoryHasNoFileException;
import org.mazarineblue.runner.exceptions.InvalidFileFormatException;
import org.mazarineblue.runner.tasks.Task;
import org.mazarineblue.runner.tasks.TestTask;
import org.mazarineblue.runner.sheetFactorySelector.SheetFactorySelector;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class MainFrame
        extends JFrame {

    private final SheetFactorySelector sheetFactorySelector;
    private SheetFactory currentSheetFactory;
    private final List<TaskPanel> taskPanels = new ArrayList<>();
    private final DefaultComboBoxModel<File> files = new DefaultComboBoxModel<>();
    private final DefaultComboBoxModel<String> sheetNames = new DefaultComboBoxModel<>();
    private final RecentFiles recentFiles;
    private File previousRecentFile = null;
    private int busy = 0;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    javax.swing.JMenuItem aboutMenuItem;
    javax.swing.JLabel actualDelayLabel;
    javax.swing.JPanel afterwardsContentPanel;
    org.jdesktop.swingx.JXBusyLabel busyLabel;
    javax.swing.JButton cancelButton;
    javax.swing.JPanel commandPanel;
    javax.swing.JLabel dateLabel;
    org.jdesktop.swingx.JXDatePicker datePicker;
    javax.swing.JLabel delayLabel;
    javax.swing.JSlider delaySlider;
    javax.swing.JMenu editMenu;
    javax.swing.JCheckBox emailLogCheckBox;
    javax.swing.JCheckBox emailReportCheckBox;
    javax.swing.JButton executeSheetButton;
    javax.swing.JLabel fileLabel;
    javax.swing.JMenu fileMenu;
    javax.swing.JMenu helpMenu;
    javax.swing.JButton hideButton;
    javax.swing.JComboBox hourComboBox;
    javax.swing.JMenuBar jMenuBar1;
    javax.swing.JScrollPane jScrollPane1;
    org.jdesktop.swingx.JXTaskPaneContainer jXTaskPaneContainer2;
    javax.swing.JComboBox minutesComboBox;
    javax.swing.JCheckBox openLogCheckBox;
    javax.swing.JCheckBox openReportCheckBox;
    javax.swing.JButton pauseButton;
    javax.swing.JComboBox recentFilesComboBox;
    javax.swing.JButton scheduleButton;
    javax.swing.JPanel scheduleContentPanel;
    javax.swing.JButton selectFileButton;
    javax.swing.JComboBox selectInitialSheetComboBox;
    javax.swing.JLabel sheetLabel;
    org.jdesktop.swingx.JXTaskPane taskPane;
    javax.swing.JLabel timeLabel;
    javax.swing.JLabel timeSeperationLabel;
    // End of variables declaration//GEN-END:variables

    public MainFrame(SheetFactorySelector sheetFactorySelector) {
        this.sheetFactorySelector = sheetFactorySelector;
        this.recentFiles = new RecentFiles();
        initComponents();
    }

    @Override
    public void setVisible(boolean flag) {
        super.setVisible(flag);
        if (flag == false)
            return;
        loadRecentFiles();
        loadSheets((File) recentFilesComboBox.getSelectedItem());
    }

    private void loadRecentFiles() {
        try {
            Collection<File> list = recentFiles.readRecentFiles();
            files.removeAllElements();
            for (File file : list)
                files.addElement(file);
        } catch (IOException ex) {
            // We do nothing if the the recent file doesn't exists.
        }
    }

    private void loadSheets(File file) {
        if (file == null)
            return;
        increaseBusy();
        currentSheetFactory = sheetFactorySelector.getSheetFactory(file);
        reloadForm();
    }

    void increaseBusy() {
        if (++busy == 1)
            busyLabel.setBusy(true);
    }

    private void reloadForm() {
        new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground()
                    throws Exception {
                return null;
            }

            @Override
            protected void done() {
                decreaseBusy();
                try {
                    get();
                    Object tmp = sheetNames.getSelectedItem();
                    sheetNames.removeAllElements();
                    for (String sheetName : currentSheetFactory.getSheetNames())
                        sheetNames.addElement(sheetName);
                    if (0 <= sheetNames.getIndexOf(tmp))
                        sheetNames.setSelectedItem(tmp);
                } catch (InterruptedException ex) {
                } catch (FactoryHasNoFileException | InvalidFileFormatException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE,
                                                                    null, ex);
                } catch (ExecutionException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE,
                                                                    null, ex);
                }
            }
        }.execute();
    }

    void decreaseBusy() {
        if (--busy == 0)
            busyLabel.setBusy(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        commandPanel = new javax.swing.JPanel();
        fileLabel = new javax.swing.JLabel();
        recentFilesComboBox = new javax.swing.JComboBox();
        selectFileButton = new javax.swing.JButton();
        busyLabel = new org.jdesktop.swingx.JXBusyLabel();
        sheetLabel = new javax.swing.JLabel();
        selectInitialSheetComboBox = new javax.swing.JComboBox();
        javax.swing.JPanel schedulePanel = new javax.swing.JPanel();
        javax.swing.JLabel schedulejLabel = new javax.swing.JLabel();
        scheduleContentPanel = new javax.swing.JPanel();
        dateLabel = new javax.swing.JLabel();
        datePicker = new org.jdesktop.swingx.JXDatePicker();
        timeLabel = new javax.swing.JLabel();
        hourComboBox = new javax.swing.JComboBox();
        timeSeperationLabel = new javax.swing.JLabel();
        minutesComboBox = new javax.swing.JComboBox();
        javax.swing.JPanel afterwardsPanel = new javax.swing.JPanel();
        javax.swing.JLabel afterwardsLabel = new javax.swing.JLabel();
        afterwardsContentPanel = new javax.swing.JPanel();
        openLogCheckBox = new javax.swing.JCheckBox();
        openReportCheckBox = new javax.swing.JCheckBox();
        emailLogCheckBox = new javax.swing.JCheckBox();
        emailReportCheckBox = new javax.swing.JCheckBox();
        executeSheetButton = new javax.swing.JButton();
        scheduleButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jXTaskPaneContainer2 = new org.jdesktop.swingx.JXTaskPaneContainer();
        taskPane = new org.jdesktop.swingx.JXTaskPane();
        pauseButton = new javax.swing.JButton();
        hideButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        javax.swing.JPanel optionsPanel = new javax.swing.JPanel();
        delaySlider = new javax.swing.JSlider();
        delayLabel = new javax.swing.JLabel();
        javax.swing.JLabel optionsLabel = new javax.swing.JLabel();
        actualDelayLabel = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        editMenu = new javax.swing.JMenu();
        helpMenu = new javax.swing.JMenu();
        aboutMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("MazarineBlue");

        commandPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                commandPanelComponentShown(evt);
            }
        });

        fileLabel.setDisplayedMnemonic('f');
        fileLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        fileLabel.setLabelFor(recentFilesComboBox);
        fileLabel.setText("File");
        fileLabel.setFocusable(false);

        recentFilesComboBox.setEditable(true);
        recentFilesComboBox.setModel(files);
        recentFilesComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                recentFilesComboBoxItemStateChanged(evt);
            }
        });

        selectFileButton.setMnemonic('c');
        selectFileButton.setText("Select file");
        selectFileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectFileButtonActionPerformed(evt);
            }
        });

        sheetLabel.setDisplayedMnemonic('s');
        sheetLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        sheetLabel.setLabelFor(selectInitialSheetComboBox);
        sheetLabel.setText("Sheet");
        sheetLabel.setFocusable(false);

        selectInitialSheetComboBox.setModel(sheetNames);

        schedulePanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        schedulejLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        schedulejLabel.setText("Schedule");

        dateLabel.setDisplayedMnemonic('d');
        dateLabel.setText("Date");

        timeLabel.setDisplayedMnemonic('t');
        timeLabel.setText("Time");

        hourComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "--", "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));

        timeSeperationLabel.setText(":");

        minutesComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "--", "00", "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55" }));

        javax.swing.GroupLayout scheduleContentPanelLayout = new javax.swing.GroupLayout(scheduleContentPanel);
        scheduleContentPanel.setLayout(scheduleContentPanelLayout);
        scheduleContentPanelLayout.setHorizontalGroup(
            scheduleContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, scheduleContentPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(scheduleContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dateLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(timeLabel, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addGroup(scheduleContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(scheduleContentPanelLayout.createSequentialGroup()
                        .addComponent(hourComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(timeSeperationLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(minutesComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(datePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        scheduleContentPanelLayout.setVerticalGroup(
            scheduleContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(scheduleContentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(scheduleContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(datePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dateLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(scheduleContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(hourComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(timeLabel)
                    .addComponent(minutesComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(timeSeperationLabel))
                .addContainerGap())
        );

        javax.swing.GroupLayout schedulePanelLayout = new javax.swing.GroupLayout(schedulePanel);
        schedulePanel.setLayout(schedulePanelLayout);
        schedulePanelLayout.setHorizontalGroup(
            schedulePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(schedulePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(schedulePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(schedulejLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, schedulePanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(scheduleContentPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        schedulePanelLayout.setVerticalGroup(
            schedulePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(schedulePanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(schedulejLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scheduleContentPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        afterwardsPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        afterwardsLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        afterwardsLabel.setText("Afterwards");
        afterwardsLabel.setFocusable(false);

        openLogCheckBox.setMnemonic('l');
        openLogCheckBox.setText("Open log");

        openReportCheckBox.setMnemonic('r');
        openReportCheckBox.setSelected(true);
        openReportCheckBox.setText("Open report");

        emailLogCheckBox.setMnemonic('o');
        emailLogCheckBox.setText("E-mail log");

        emailReportCheckBox.setMnemonic('p');
        emailReportCheckBox.setText("E-mail report");

        javax.swing.GroupLayout afterwardsContentPanelLayout = new javax.swing.GroupLayout(afterwardsContentPanel);
        afterwardsContentPanel.setLayout(afterwardsContentPanelLayout);
        afterwardsContentPanelLayout.setHorizontalGroup(
            afterwardsContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(afterwardsContentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(afterwardsContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(emailLogCheckBox)
                    .addComponent(openLogCheckBox))
                .addGap(18, 18, 18)
                .addGroup(afterwardsContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(openReportCheckBox)
                    .addComponent(emailReportCheckBox))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        afterwardsContentPanelLayout.setVerticalGroup(
            afterwardsContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(afterwardsContentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(afterwardsContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(openReportCheckBox)
                    .addComponent(openLogCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(afterwardsContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(emailLogCheckBox)
                    .addComponent(emailReportCheckBox))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout afterwardsPanelLayout = new javax.swing.GroupLayout(afterwardsPanel);
        afterwardsPanel.setLayout(afterwardsPanelLayout);
        afterwardsPanelLayout.setHorizontalGroup(
            afterwardsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(afterwardsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(afterwardsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(afterwardsLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, afterwardsPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(afterwardsContentPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        afterwardsPanelLayout.setVerticalGroup(
            afterwardsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(afterwardsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(afterwardsLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(afterwardsContentPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        executeSheetButton.setMnemonic('x');
        executeSheetButton.setText("Execute");
        executeSheetButton.setSelected(true);
        executeSheetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                executeSheetButtonActionPerformed(evt);
            }
        });

        scheduleButton.setMnemonic('c');
        scheduleButton.setText("Schedule");
        scheduleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scheduleButtonActionPerformed(evt);
            }
        });

        org.jdesktop.swingx.VerticalLayout verticalLayout5 = new org.jdesktop.swingx.VerticalLayout();
        verticalLayout5.setGap(14);
        jXTaskPaneContainer2.setLayout(verticalLayout5);

        taskPane.setScrollOnExpand(true);
        jXTaskPaneContainer2.add(taskPane);

        jScrollPane1.setViewportView(jXTaskPaneContainer2);

        pauseButton.setText("Pause / Resume");
        pauseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pauseButtonActionPerformed(evt);
            }
        });

        hideButton.setText("Hide");
        hideButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hideButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        optionsPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        optionsPanel.setToolTipText("");

        delaySlider.setMajorTickSpacing(1000);
        delaySlider.setMaximum(2000);
        delaySlider.setMinorTickSpacing(200);
        delaySlider.setPaintLabels(true);
        delaySlider.setPaintTicks(true);
        delaySlider.setSnapToTicks(true);
        delaySlider.setToolTipText("Delay time in ms");
        delaySlider.setValue(0);
        delaySlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                delaySliderStateChanged(evt);
            }
        });
        delaySlider.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                delaySliderPropertyChange(evt);
            }
        });

        delayLabel.setText("Delay           ");

        optionsLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        optionsLabel.setText("Options");

        actualDelayLabel.setText("(10000 ms)");

        javax.swing.GroupLayout optionsPanelLayout = new javax.swing.GroupLayout(optionsPanel);
        optionsPanel.setLayout(optionsPanelLayout);
        optionsPanelLayout.setHorizontalGroup(
            optionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(optionsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(optionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(optionsLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(optionsPanelLayout.createSequentialGroup()
                        .addGroup(optionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(delayLabel)
                            .addComponent(actualDelayLabel))
                        .addGap(18, 18, 18)
                        .addComponent(delaySlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        optionsPanelLayout.setVerticalGroup(
            optionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(optionsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(optionsLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(optionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(delaySlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(optionsPanelLayout.createSequentialGroup()
                        .addComponent(delayLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(actualDelayLabel)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout commandPanelLayout = new javax.swing.GroupLayout(commandPanel);
        commandPanel.setLayout(commandPanelLayout);
        commandPanelLayout.setHorizontalGroup(
            commandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(commandPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(commandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(sheetLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(fileLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(commandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(commandPanelLayout.createSequentialGroup()
                        .addComponent(recentFilesComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(selectFileButton)
                        .addGap(18, 18, 18)
                        .addComponent(busyLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, commandPanelLayout.createSequentialGroup()
                        .addGroup(commandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(afterwardsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(selectInitialSheetComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(schedulePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(optionsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, commandPanelLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(scheduleButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(executeSheetButton)))
                        .addGap(18, 18, 18)
                        .addGroup(commandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(commandPanelLayout.createSequentialGroup()
                                .addComponent(cancelButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(hideButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(pauseButton))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE))))
                .addContainerGap())
        );
        commandPanelLayout.setVerticalGroup(
            commandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(commandPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(commandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(commandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(selectFileButton)
                        .addComponent(recentFilesComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(fileLabel))
                    .addComponent(busyLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(commandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(commandPanelLayout.createSequentialGroup()
                        .addGroup(commandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(selectInitialSheetComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(sheetLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(optionsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(schedulePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(afterwardsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(commandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(executeSheetButton)
                            .addComponent(scheduleButton)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(commandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pauseButton)
                    .addComponent(hideButton)
                    .addComponent(cancelButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        fileLabel.getAccessibleContext().setAccessibleDescription("Select a recent file");
        recentFilesComboBox.getAccessibleContext().setAccessibleName("");
        recentFilesComboBox.getAccessibleContext().setAccessibleDescription("Select a recent file");
        selectFileButton.getAccessibleContext().setAccessibleDescription("Select a file");
        sheetLabel.getAccessibleContext().setAccessibleDescription("Select the intial sheet to execute");
        selectInitialSheetComboBox.getAccessibleContext().setAccessibleName("Select a sheet");
        selectInitialSheetComboBox.getAccessibleContext().setAccessibleDescription("Select the intial sheet to execute");
        executeSheetButton.getAccessibleContext().setAccessibleDescription("Execute the instruction sheet");

        fileMenu.setText("File");
        jMenuBar1.add(fileMenu);

        editMenu.setText("Edit");
        jMenuBar1.add(editMenu);

        helpMenu.setMnemonic('H');
        helpMenu.setText("Help");
        helpMenu.setToolTipText("");

        aboutMenuItem.setMnemonic('A');
        aboutMenuItem.setText("About MazarineBlue");
        aboutMenuItem.setToolTipText("");
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(aboutMenuItem);

        jMenuBar1.add(helpMenu);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(commandPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(commandPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void commandPanelComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_commandPanelComponentShown
        loadRecentFiles();
    }//GEN-LAST:event_commandPanelComponentShown

    private void selectFileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectFileButtonActionPerformed
        selectFile();
        loadSheets((File) recentFilesComboBox.getSelectedItem());
        saveRecentFiles();
    }//GEN-LAST:event_selectFileButtonActionPerformed

    // <editor-fold defaultstate="collapsed" desc="First used in selectFileButtonActionPerformed()">
    private void selectFile() {
        File directory = getLastWorkingDirectory();
        FileFilter filter = new SheetFileFilter(sheetFactorySelector);
        FileSelector dialog = new FileSelector(directory, filter);
        switch (dialog.getActionValue()) {
            case JFileChooser.APPROVE_OPTION:
                File file = dialog.getSelectedFile();
                insertSelectedRecentFile(file);
                break;
        }
    }

    private File getLastWorkingDirectory() {
        if (recentFilesComboBox.getItemCount() <= 0)
            return null;
        File file = (File) recentFilesComboBox.getItemAt(0);
        return file.isDirectory() ? file : file.getParentFile();
    }

    private boolean insertSelectedRecentFile(File file) {
        if (file.equals(files.getElementAt(0)))
            return false;
        files.insertElementAt(file, 0);
        removeRecentFilesExcess(file);
        recentFilesComboBox.setSelectedIndex(0);
        return true;
    }

    private void removeRecentFilesExcess(File file) {
        for (int i = files.getSize() - 1; i > 0; --i) {
            File other = files.getElementAt(i);
            if (file.equals(files.getElementAt(i)))
                files.removeElementAt(i);
        }
        while (files.getSize() > recentFiles.getMaxRecentFiles())
            files.removeElementAt(recentFiles.getMaxRecentFiles());
    }

    private void saveRecentFiles() {
        try {
            List<File> list = new ArrayList();
            for (int i = 0; i < files.getSize(); ++i)
                list.add(files.getElementAt(i));
            recentFiles.storeRecentFiles(list);
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        }
    }

    private volatile int recentFilesComboBoxItemStateChangedActive = 0;
    // </editor-fold>

    private void recentFilesComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_recentFilesComboBoxItemStateChanged
        if (recentFilesComboBoxItemStateChangedHasLock())
            return;
        recentFilesComboBoxItemStateChangedGainLock();

        File file = (File) files.getSelectedItem();
        if (recentFilesComboBoxItemStateChangedNothingToDo(file))
            return;

        if (insertSelectedRecentFile(file)) {
            saveRecentFiles();
            loadSheets(file);
        }

        recentFilesComboBoxItemStateChangedReleaseLock();
    }//GEN-LAST:event_recentFilesComboBoxItemStateChanged

    // <editor-fold defaultstate="collapsed" desc="First used in recentFilesComboBoxItemStateChanged()">
    private boolean recentFilesComboBoxItemStateChangedHasLock() {
        if (recentFilesComboBoxItemStateChangedActive > 0)
            return true;
        synchronized (this) {
            return recentFilesComboBoxItemStateChangedActive > 0;
        }
    }

    private void recentFilesComboBoxItemStateChangedGainLock() {
        synchronized (this) {
            ++recentFilesComboBoxItemStateChangedActive;
        }
    }

    private boolean recentFilesComboBoxItemStateChangedNothingToDo(File file) {
        if (recentFilesComboBoxItemStateChangedFalsePositive(file))
            return true;
        previousRecentFile = file;
        return recentFilesComboBoxItemStateChangedEmptyPath(file);
    }

    private boolean recentFilesComboBoxItemStateChangedFalsePositive(File file) {
        if (file.equals(previousRecentFile)) {
            synchronized (this) {
                --recentFilesComboBoxItemStateChangedActive;
            }
            return true;
        }
        return false;
    }

    private boolean recentFilesComboBoxItemStateChangedEmptyPath(File file) {
        if (file == null)
            synchronized (this) {
                --recentFilesComboBoxItemStateChangedActive;
                return true;
            }
        return false;
    }

    private void recentFilesComboBoxItemStateChangedReleaseLock() {
        synchronized (this) {
            --recentFilesComboBoxItemStateChangedActive;
        }
    }
    // </editor-fold>

    private void executeSheetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_executeSheetButtonActionPerformed
        resetErrors();
        boolean error = checkRunningErrors();
        if (error)
            return;
        runTask(new Date());
    }//GEN-LAST:event_executeSheetButtonActionPerformed

    // <editor-fold defaultstate="collapsed" desc="First used in executeSheetButtonActionPerformed()">
    private void resetErrors() {
        fileLabel.setForeground(Color.black);
        sheetLabel.setForeground(Color.black);
        dateLabel.setForeground(Color.black);
        timeLabel.setForeground(Color.black);
    }

    private boolean checkRunningErrors() {
        boolean error = false;
        if (files.getSize() == 0) {
            error = true;
            fileLabel.setForeground(Color.red);
        }
        if (sheetNames.getSize() == 0) {
            error = true;
            sheetLabel.setForeground(Color.red);
        }
        return error;
    }

    private void runTask(Date sceduledDate) {
        Task task = createTask(sceduledDate);
        TaskPanel taskPanel = createTaskPanel(task);
        addTaskToList(taskPanel);
        new TaskWorker(this, taskPanel, task).execute();
    }

    private Task createTask(Date scheduled) {
        File file = (File) files.getSelectedItem();
        String sheetName = (String) sheetNames.getSelectedItem();

        currentSheetFactory = sheetFactorySelector.getSheetFactory(file);
        reloadForm();
        DocumentManager currentDocumentManager = new FileDocumentManager(file);
        Feed feed = currentSheetFactory.getSheetFeed(sheetName);

        TestTask task = new TestTask(currentDocumentManager, currentSheetFactory,
                                     feed);
        task.setDelay(delaySlider.getValue());
        task.setScheduledDate(scheduled);
        if (openLogCheckBox.isSelected())
            task.setOpenLog();
        if (openReportCheckBox.isSelected())
            task.setOpenReport();
        if (emailLogCheckBox.isSelected())
            task.setEmailLog();
        if (emailReportCheckBox.isSelected())
            task.setEmailReport();
        return task;
    }

    private TaskPanel createTaskPanel(Task task) {
        return new TaskPanel(task);
    }

    private void addTaskToList(TaskPanel taskPanel) {
        int index = getIndexBefore(taskPanel);
        taskPanels.add(index, taskPanel);
        taskPane.add(taskPanel, index);
    }

    private int getIndexBefore(TaskPanel taskPanel) {
        int index = 0, n = taskPanels.size();
        for (; index < n; ++index) {
            TaskPanel other = taskPanels.get(index);
            if (taskPanel.before(other))
                return index;
        }
        return n;
    }
    // </editor-fold>

    private void scheduleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scheduleButtonActionPerformed
        resetErrors();
        boolean error1 = checkRunningErrors();
        boolean error2 = checkScheduleErrors();
        if (error1 || error2)
            return;
        runTask(getScheduledDate());
    }//GEN-LAST:event_scheduleButtonActionPerformed

    // <editor-fold defaultstate="collapsed" desc="First used in scheduleButtonActionPerformed()">
    private boolean checkScheduleErrors() {
        boolean error = false;
        Date date = datePicker.getDate();
        Object hour = hourComboBox.getSelectedItem();
        Object minutes = minutesComboBox.getSelectedItem();
        if (date == null) {
            error = true;
            dateLabel.setForeground(Color.red);
        }
        if (hour.equals("--") || minutes.equals("--")) {
            error = true;
            timeLabel.setForeground(Color.red);
        }
        if (error == false && getScheduledDate().before(new Date())) {
            error = true;
            dateLabel.setForeground(Color.red);
            timeLabel.setForeground(Color.red);
        }
        return error;
    }

    private Date getScheduledDate() {
        String hour = (String) hourComboBox.getSelectedItem();
        String minutes = (String) minutesComboBox.getSelectedItem();
        Calendar cal = Calendar.getInstance();
        Date date = datePicker.getDate();
        if (date == null)
            return null;
        cal.setTime(date);
        cal.add(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
        cal.add(Calendar.MINUTE, Integer.parseInt(minutes));
        return cal.getTime();
    }
    // </editor-fold>

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        for (int i = taskPanels.size() - 1; i >= 0; --i) {
            TaskPanel taskPanel = taskPanels.get(i);
            if (taskPanel.isSelected()) {
                taskPanel.cancle();
                removeTaskPanelFromList(taskPanel);
            }
        }
    }//GEN-LAST:event_cancelButtonActionPerformed

    // <editor-fold defaultstate="collapsed" desc="First used in cancleButtonActionPerformed()">
    private void removeTaskPanelFromList(TaskPanel taskPanel) {
        int index = getIndex(taskPanel);
        if (index < 0)
            return;
        taskPanels.remove(index);
        taskPane.remove(index);
        taskPane.updateUI();
    }

    private int getIndex(TaskPanel taskPanel) {
        int index = 0, n = taskPanels.size();
        for (; index < n; ++index) {
            TaskPanel other = taskPanels.get(index);
            if (taskPanel.equals(other))
                return index;
        }
        return -1;
    }
    // </editor-fold>

    private void hideButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hideButtonActionPerformed
        for (int i = taskPanels.size() - 1; i >= 0; --i) {
            TaskPanel taskPanel = taskPanels.get(i);
            if (taskPanel.isSelected())
                removeTaskPanelFromList(taskPanel);
        }
    }//GEN-LAST:event_hideButtonActionPerformed

    private void pauseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pauseButtonActionPerformed
        for (TaskPanel taskPanel : taskPanels)
            if (taskPanel.isSelected())
                taskPanel.pauseOrResume();
    }//GEN-LAST:event_pauseButtonActionPerformed

    private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuItemActionPerformed
        JFrame frame = new AboutFrame();
        frame.setAlwaysOnTop(true);
        frame.setLocationRelativeTo(this);
        frame.setVisible(true);
    }//GEN-LAST:event_aboutMenuItemActionPerformed

    private void delaySliderPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_delaySliderPropertyChange
        updateDelaySlider();
    }//GEN-LAST:event_delaySliderPropertyChange

    private void delaySliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_delaySliderStateChanged
        updateDelaySlider();
    }//GEN-LAST:event_delaySliderStateChanged

    private void updateDelaySlider() {
        int value = delaySlider.getValue();
        actualDelayLabel.setText("(" + value + " ms)");
    }
}
