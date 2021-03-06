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
package org.mazarineblue.runners.swingrunner.screens.about;

import java.util.ResourceBundle;

/**
 * The {@code AboutDialog} shows a dialog with information about the
 * application.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class AboutDialog
        extends javax.swing.JDialog {

    private static final long serialVersionUID = 1L;

    private final transient ResourceBundle bundle;
    private final transient ImageFetcher fetcher;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    /**
     * Constructs an {@code AboutDialog} and maps it to the specified
     * {@code AboutModel), its owner and an {@code ImageFetcher}.
     *
     * @param builder used to initialize this class
     */
    public AboutDialog(AboutDialogBuilder builder) {
        super(builder.getOwner());
        builder.validate();
        fetcher = builder.getFetcher();
        bundle = builder.getResourceBundle();
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JLabel titleLabel = new javax.swing.JLabel();
        javax.swing.JLabel versionLabel = new javax.swing.JLabel();
        javax.swing.JLabel releaseVersionLabel = new javax.swing.JLabel();
        javax.swing.JLabel leadDeveloperLabel = new javax.swing.JLabel();
        javax.swing.JLabel graphicalDesignLabel = new javax.swing.JLabel();
        javax.swing.JLabel alexDeKruijffLabel = new javax.swing.JLabel();
        javax.swing.JLabel daanVerbiestLabel = new javax.swing.JLabel();
        javax.swing.JPanel logoPanel = new ImagePanel(fetcher.getImage());
        javax.swing.JLabel buildDateLabel = new javax.swing.JLabel();
        javax.swing.JLabel releaseDateLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("About MazarineBlue");
        setResizable(false);

        titleLabel.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        titleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleLabel.setText("MazarineBlue");
        titleLabel.setName("titleLabel"); // NOI18N

        versionLabel.setText("Version");
        versionLabel.setName("versionLabel"); // NOI18N

        releaseVersionLabel.setText(getVersion());

        leadDeveloperLabel.setText("Lead developer");
        leadDeveloperLabel.setName("leadDeveloperLabel"); // NOI18N

        graphicalDesignLabel.setText("Graphical design");
        graphicalDesignLabel.setName("graphicalDesignLabel"); // NOI18N

        alexDeKruijffLabel.setText("Alex de Kruijff");
        alexDeKruijffLabel.setName("leadDeveloperName"); // NOI18N

        daanVerbiestLabel.setText("Daan Verbiest");
        daanVerbiestLabel.setName("graphicalDesignName"); // NOI18N

        logoPanel.setName("logoPanel"); // NOI18N

        javax.swing.GroupLayout logoPanelLayout = new javax.swing.GroupLayout(logoPanel);
        logoPanel.setLayout(logoPanelLayout);
        logoPanelLayout.setHorizontalGroup(
            logoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 150, Short.MAX_VALUE)
        );
        logoPanelLayout.setVerticalGroup(
            logoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 150, Short.MAX_VALUE)
        );

        buildDateLabel.setText("Build date");
        buildDateLabel.setName("buildDateLabel"); // NOI18N

        releaseDateLabel.setText(getBuildDate());
        releaseDateLabel.setName("buildDate"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(logoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(versionLabel)
                            .addComponent(graphicalDesignLabel)
                            .addComponent(leadDeveloperLabel)
                            .addComponent(buildDateLabel))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(alexDeKruijffLabel)
                            .addComponent(daanVerbiestLabel)
                            .addComponent(releaseVersionLabel)
                            .addComponent(releaseDateLabel))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(titleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(logoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(titleLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(versionLabel)
                            .addComponent(releaseVersionLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(buildDateLabel)
                            .addComponent(releaseDateLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(alexDeKruijffLabel)
                            .addComponent(leadDeveloperLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(daanVerbiestLabel)
                            .addComponent(graphicalDesignLabel))))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private String getVersion() {
        try {
            return bundle.getString("MazarineBlue.version");
        } catch (RuntimeException ex) {
            return "Unable to fetch version";
        }
    }

    private String getBuildDate() {
        try {
            return bundle.getString("MazarineBlue.buildDate");
        } catch (RuntimeException ex) {
            return "Unable to fetch release date";
        }
    }
}
