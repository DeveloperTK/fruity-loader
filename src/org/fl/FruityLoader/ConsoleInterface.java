package org.fl.FruityLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author Christian Schliz
 * @since 1.0.2
 * */
public class ConsoleInterface {

    /**
     * Interface for the <code>nogui</code> mode.
     * */
    public ConsoleInterface() {
    }

    /**
     * The programs entry point.
     *
     * @param args
     */
    public synchronized void consoleHandler(final String[] args) {
        String prefix = "\nfruityloader@"
                + FruityLoader.getVersionNumber() + "# ";
        ConversionHandler conv = FruityLoader.getConversionHandler();
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                System.in));

        conv.setCurrentProject("");

        System.out.println("Welcome to FruityLoader. "
                + "Type 'help' for a list of commands.");
        System.out.println("It is currently set to "
                + conv.getWorkingDirectory());
        System.out.print(prefix);

        while (true) {
            String line = "";

            try {
                line = reader.readLine();
            } catch (IOException e) {
                System.err.println("Error while reading command input.");
                e.printStackTrace();
                continue;
            }

            String[] split = line.split(" ");
            // s.toLowerCase() ensures case insensitivity
            String command = split[0].toLowerCase();

            switch (command) {
                case "cd":
                    if (split.length < 2) {
                        System.out.println("Error! Usage: cd <dir>");
                        break;
                    }

                    break;
                case "pwd":
                case "dir":
                    System.out.println(conv.getWorkingDirectory());
                    break;
                case "open":
                    if (split.length < 2) {
                        System.out.println("Error! Usage: open <name>");
                        break;
                    }

                    // "open <file>" command - opens a project
                    conv.setCurrentProject(split[1]);

                    System.out.println("Opening " + conv.getCurrentProject()
                            + "...");
                    conv.prepare(conv.getCurrentProject());
                    conv.open(conv.getFlProject(conv.getCurrentProject()));

                    break;
                case "backup":
                    // "backup <file>" command - copies a backup
                    conv.backup(conv.getCurrentProject());

                    break;
                case "openb":
                    // projectName has to be the first argument
                    conv.setCurrentProject(split[1]);

                    // Load a backup copy of the original file
                    conv.loadBackup(conv.getCurrentProject(),
                            Integer.parseInt(split[2]));

                    System.out.println("Opening "
                            + conv.getCurrentProject() + "...");
                    conv.prepare(conv.getCurrentProject());
                    conv.open(conv.getFlProject(conv.getCurrentProject()));

                    break;
                case "help":
                case "?":
                    // "help" command - prints a help dialog
                    printHelpDialog();

                    break;
                case "exit":
                case "quit":
                    // "exit" command - exits out of the cli
                    System.out.println("Thank you and goodbye.");
                    return;
                default:
                    // The command was not recognized
                    System.out.println("Unrecognized command. "
                            + "Type help for a list of commands.");
            }

            System.out.print(prefix);
        }
    }

    private void printHelpDialog() {
        InputStream helpText = this.getClass()
                .getResourceAsStream("helptext.txt");
        Scanner sc = new Scanner(helpText);
        List<String> lines = new ArrayList<>();

        while (sc.hasNextLine()) {
            lines.add(sc.nextLine());
        }

        lines.forEach(System.out::println);
    }
}
