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
package org.mazarineblue.runners.swingrunner;

import java.awt.Component;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.executors.util.AbstractExecutorFactoryTestHelper;
import static org.mazarineblue.pictures.ImageUtil.createPicture;
import org.mazarineblue.pictures.Picture;
import org.mazarineblue.runners.swingrunner.config.Config;
import org.mazarineblue.runners.swingrunner.screens.about.AboutDialog;
import org.mazarineblue.runners.swingrunner.screens.about.AboutDialogBuilder;
import org.mazarineblue.runners.swingrunner.screens.about.GraphicalTextImageFetcher;
import org.mazarineblue.runners.swingrunner.screens.about.ImageFetcher;
import org.mazarineblue.runners.swingrunner.screens.about.ImagePanel;
import org.mazarineblue.runners.swingrunner.screens.about.URLImageFetcher;
import org.mazarineblue.runners.swingrunner.screens.main.MainFrame;
import org.mazarineblue.runners.swingrunner.screens.main.MainFrameBuilder;
import org.mazarineblue.runners.swingrunner.util.DummyExceptionReporter;
import org.mazarineblue.runners.swingrunner.util.ExceptionReporter;
import org.mazarineblue.runners.swingrunner.util.TestFileSelector;
import static org.mazarineblue.utilities.swing.SwingUtil.clickButton;
import static org.mazarineblue.utilities.swing.SwingUtil.fetchChildNamed;
import static org.mazarineblue.utilities.swing.SwingUtil.fetchWindowTitled;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class AboutDialogTest
        extends AbstractExecutorFactoryTestHelper {

    private AboutDialog dialog;
    private Picture mazarineBlue;

    @Before
    public void setupClass()
            throws IOException {
        URL url = getClass().getClassLoader().getResource(new File("logo.png").getPath());
        mazarineBlue = createPicture(ImageIO.read(url));
    }

    @After
    public void teardownClass() {
        dialog.dispose();
        dialog = null;
        mazarineBlue = null;
    }

    @Test
    public void testIT()
            throws IOException {
        Config config = new Config(this);
        MainFrameBuilder builder = new MainFrameBuilder();
        builder.setFileSystem(this);
        builder.setConfig(config);
        builder.setFileSelector(new TestFileSelector(new File("")));
        builder.setExceptionHandler(new TestExceptionHandler());
        builder.setFeedExecutorFactory(this);
        MainFrame frame = new MainFrame(builder);

//        model.addListener(new TestListener(fs));
        clickButton(frame, "aboutMenuItem");
        dialog = fetchWindowTitled(frame, "About MazarineBlue", AboutDialog.class);
        assertEquals("MazarineBlue", getTextFromLabel(dialog, "titleLabel"));
        assertEquals("Version", getTextFromLabel(dialog, "versionLabel"));
        assertEquals("Build date", getTextFromLabel(dialog, "buildDateLabel"));
        assertEquals("Lead developer", getTextFromLabel(dialog, "leadDeveloperLabel"));
        assertEquals("Graphical design", getTextFromLabel(dialog, "graphicalDesignLabel"));
        assertEquals("Alex de Kruijff", getTextFromLabel(dialog, "leadDeveloperName"));
        assertEquals("Daan Verbiest", getTextFromLabel(dialog, "graphicalDesignName"));

        Image image = fetchChildNamed(dialog, "logoPanel", ImagePanel.class).getImage();
        Picture actual = createPicture(image);
        assertEquals(mazarineBlue, actual);
    }

    @Test
    public void defaultLogo()
            throws IOException {
        ImageFetcher fetcher = new GraphicalTextImageFetcher(150, 150);
        dialog = new AboutDialog(new AboutDialogBuilder().setImageFetcher(fetcher).setResourceBundle("MazarineBlue"));
        assertEquals("MazarineBlue", getTextFromLabel(dialog, "titleLabel"));
        assertEquals("Version", getTextFromLabel(dialog, "versionLabel"));
        assertEquals("Build date", getTextFromLabel(dialog, "buildDateLabel"));
        assertEquals("Lead developer", getTextFromLabel(dialog, "leadDeveloperLabel"));
        assertEquals("Graphical design", getTextFromLabel(dialog, "graphicalDesignLabel"));
        assertEquals("Alex de Kruijff", getTextFromLabel(dialog, "leadDeveloperName"));
        assertEquals("Daan Verbiest", getTextFromLabel(dialog, "graphicalDesignName"));

        Image image = fetchChildNamed(dialog, "logoPanel", ImagePanel.class).getImage();
        Picture actual = createPicture(image);
        assertNotEquals(mazarineBlue, actual);
    }

    @Test
    public void logoURL_NonExistingFile()
            throws IOException {
        URL url = new URL("file://foo");
        ImageFetcher backup = new GraphicalTextImageFetcher(150, 150);
        ExceptionReporter reporter = new DummyExceptionReporter();
        ImageFetcher fetcher = new URLImageFetcher(url, backup, reporter);
        dialog = new AboutDialog(new AboutDialogBuilder().setImageFetcher(fetcher).setResourceBundle("MazarineBlue"));
        assertEquals("MazarineBlue", getTextFromLabel(dialog, "titleLabel"));
        assertEquals("Version", getTextFromLabel(dialog, "versionLabel"));
        assertEquals("Build date", getTextFromLabel(dialog, "buildDateLabel"));
        assertEquals("Lead developer", getTextFromLabel(dialog, "leadDeveloperLabel"));
        assertEquals("Graphical design", getTextFromLabel(dialog, "graphicalDesignLabel"));
        assertEquals("Alex de Kruijff", getTextFromLabel(dialog, "leadDeveloperName"));
        assertEquals("Daan Verbiest", getTextFromLabel(dialog, "graphicalDesignName"));

        Image image = fetchChildNamed(dialog, "logoPanel", ImagePanel.class).getImage();
        Picture picture = createPicture(image);
    }

    private static String getTextFromLabel(Component parent, String name) {
        return fetchChildNamed(parent, name, JLabel.class).getText();
    }
}
