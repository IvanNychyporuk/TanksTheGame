package tanks;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ActionsWriter {

    private File actionsFile;
    private long startTime = System.currentTimeMillis();

    private boolean isGameOver;

    public ActionsWriter() {
        actionsFile = new File("actions/actions.txt");
        checkActionsFile();
        isGameOver = false;
    }

    private void checkActionsFile() {

        File actionsDir = new File("actions/");

        if (!actionsDir.exists()) {
            try {
                Files.createDirectory(Paths.get("actions/"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected void writeAction(String consoleMessage) {

        long actionTime = System.currentTimeMillis() - startTime;

        System.out.println(consoleMessage);

        if (consoleMessage.equals("GAME OVER")) {
            isGameOver = true;
        }

        consoleMessage += "(" + String.valueOf(actionTime) + ")";

        try (FileOutputStream fos = new FileOutputStream(actionsFile, true);
             PrintStream writer = new PrintStream(fos);
        ) {
            if (!isGameOver) {
                writer.println(consoleMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void clearActionsFile() {
        try {
            FileOutputStream fos = new FileOutputStream(actionsFile);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
