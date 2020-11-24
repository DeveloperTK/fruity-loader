package org.fl.FruityLoader;

import java.io.File;
import java.net.URISyntaxException;

public final class FruityLoader {

    private FruityLoader() {
        System.gc();
    }

    /**
     * The current semantic version number.
     * */
    private static String versionNumber = "1.0.2";

    /**
     * The static instance of the FileConversion class.
     * */
    private static ConversionHandler conversionHandler;

    /**
     * @param args
     * */
    public static void main(final String[] args) {
        conversionHandler = new ConversionHandler();

        try {
            conversionHandler.setWorkingDirectory(new File(
                    FruityLoader.class.getProtectionDomain()
                            .getCodeSource().getLocation().toURI())
                    .getPath());
        } catch (URISyntaxException ex) {
            System.out.println("The working directory cannot be set. "
                    + "Defaulting to "
                    + conversionHandler.getWorkingDirectory());
        }

        if (!(args.length > 0 && args[0].equalsIgnoreCase("nogui"))) {
            GraphicalInterface graphicalInterface = new GraphicalInterface();
            graphicalInterface.guiHandler(args);
        }

        ConsoleInterface cli = new ConsoleInterface();
        cli.consoleHandler(args);
    }

    /**
     * @return String versionNumber
     * */
    public static String getVersionNumber() {
        return versionNumber;
    }

    /**
     * @return ConversionHandler conversionHandler
     * */
    public static ConversionHandler getConversionHandler() {
        return conversionHandler;
    }
}
