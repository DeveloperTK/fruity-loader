package org.fl.FruityLoader;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

public final class GraphicalInterface extends JPanel
        implements ActionListener {

    private JFrame frame;
    private JButton button;
    private final JFileChooser fc = new JFileChooser();

    public GraphicalInterface() {
    }

    public void guiHandler(final String[] args) {
        SwingUtilities.invokeLater(this::createWindow);
    }

    private void createWindow() {
        //Create and set up the window.
        frame = new JFrame("FruityLoader "
                + FruityLoader.getVersionNumber());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(240, 100));

        button = new JButton("Open File");
        button.setVerticalTextPosition(AbstractButton.CENTER);
        button.setHorizontalTextPosition(AbstractButton.CENTER);
        button.setMnemonic(KeyEvent.VK_O);
        button.addActionListener(this);
        button.setFont(new Font("", Font.BOLD, 24));
        frame.add(button);

        //Display the window.
        frame.setVisible(true);
    }

    public void actionPerformed(final ActionEvent e) {
        if (e.getSource() == button) {
            int returnVal = fc.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                if (file.isFile() && file.exists() && file.canWrite()) {
                    ConversionHandler conv = FruityLoader
                            .getConversionHandler();
                    conv.setWorkingDirectory(file.getParent());
                    conv.setCurrentProject(file.getName());
                    conv.prepare(conv.getCurrentProject());
                    conv.open(conv.getFlProject(conv
                            .getCurrentProject()));
                } else {
                    JOptionPane.showMessageDialog(frame,
                            "Error Reading the File. "
                                    + "check that it exists "
                                    + "and you can write to it",
                            "Inane error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

}
