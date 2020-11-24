package org.fl.FruityLoader;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ConversionHandler {

    /**
     * Path of project folder with front slashes.
     */
    private String workingDirectory = "";

    /**
     * Name of the current project file.
     * */
    private String currentProject = "";

    /**
     * The position, where the significant byte is changed.
     */
    private final int writePosition = 20;

    /**
     * The position, where an unsuccessful write is stopped.
     */
    private final int breakPosition = 500;

    /**
     * The new char to overwrite the read protection.
     * */
    private final char replacementChar = (char) 0x03;

    /**
     * Default Constructor.
     * */
    public ConversionHandler() {
    }

    /**
     * Reads bytes from an InputStream.
     *
     * @param bytes
     * @param is
     * @return int
     */
    public int read(final InputStream is, final byte[] bytes)
            throws IOException {
        return is.read(bytes);
    }

    /**
     * @param name
     * @return int
     */
    public int onecIndex(final String name) {
        int charIndex = 0;
        InputStream is = null;
        try {
            is = new FileInputStream(getFlProject(name));
        } catch (FileNotFoundException e) {
            System.out.println();
            e.printStackTrace();
        }
        byte[] bytes = new byte[writePosition];
        int bytesRead = -1;
        try {
            while ((bytesRead = read(is, bytes)) != -1) {
                for (int i = 0; i < bytes.length; i++) {
                    if (Integer.toHexString((char) bytes[i]).equals("1c")) {
                        return charIndex + 1;
                    } else {
                        charIndex++;
                    }
                    if (charIndex > breakPosition) {
                        return -1;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * @param name
     */
    public void prepare(final String name) {
        changeChar(getFlProject(name), onecIndex(name), replacementChar);
    }

    /**
     * @param file
     */
    public void open(final File file) {
        Desktop desktop = Desktop.getDesktop();
        if (file.exists()) {
            try {
                desktop.open(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Copies or replaces a file.
     *
     * @param source
     * @param target
     * */
    public void copy(final File source, final File target) {
        try {
            Files.copy(source.toPath(), target.toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param name
     * @param nr
     * */
    public void loadBackup(final String name, final int nr) {
        int version = nr;
        File projectPath = getFlProject(name);
        File backupPath = getFlBackup(name, version);
        copy(backupPath, projectPath);
    }

    /**
     * @param name
     * */
    public void backup(final String name) {
        int version = backups(name) + 1;
        File projectPath = getFlProject(name);
        File backupPath = getFlBackup(name, version);
        if (!backupPath.getParentFile().exists()) {
            backupPath.getParentFile().mkdirs();
        }
        copy(projectPath, backupPath);
    }

    /**
     * @param name
     *
     * @return int fileVersion
     * */
    public int backups(final String name) {
        File backupFolder = new File(getWorkingDirectory() + "/" + name);
        if (backupFolder.exists()) {
            String[] backups = backupFolder.list();
            int version = 0;
            for (String backup : backups) {
                if (backup.contains(name)) {
                    int v = Integer.parseInt(backup.substring(
                            name.length() + 1,
                            backup.length() - ".flp".length()));
                    if (v > version) {
                        version = v;
                    }
                }
            }
            return version;
        } else {
            return 0;
        }
    }

    /**
     * @param name
     * @return File projectFile
     * */
    public File getFlProject(final String name) {
        return new File(getWorkingDirectory() + "/" + name + ".flp");
    }

    /**
     * @param name
     * @param version
     * @return File backupFile
     * */
    public File getFlBackup(final String name, final int version) {
        return new File(getWorkingDirectory() + "/" + name
                + "/" + name + "_" + version + ".flp");
    }

    /**
     * @param file
     * @param index
     * @param replace
     * */
    public void changeChar(final File file, final int index,
                           final char replace) {
        if (index < 0) {
            return;
        }
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(file, "rw");
            raf.seek(index);
            raf.write(replace);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                raf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
    * Getters and Setters
    * */

    /**
     * @return String workingDirectory
     * */
    public String getWorkingDirectory() {
        return workingDirectory;
    }

    /**
     * @param newWorkingDirectory
     * */
    public void setWorkingDirectory(final String newWorkingDirectory) {
        this.workingDirectory = newWorkingDirectory;
    }

    /**
     * @return String currentProject
     * */
    public String getCurrentProject() {
        return currentProject;
    }

    /**
     * @param newCurrentProject
     * */
    public void setCurrentProject(final String newCurrentProject) {
        this.currentProject = newCurrentProject;
    }
}
