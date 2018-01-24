package net.alkaonline.alkarandombox.boxmanager;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class BoxManager {

    private final Path randomBoxFolder;
    private List<Box> boxes;

    public BoxManager(Path randomBoxFolder) {
        this.randomBoxFolder = randomBoxFolder;
    }

    public void loadBoxList() throws IOException {
        this.boxes = loadBoxList(randomBoxFolder);
    }

    public List<Box> getBoxes() {
        return boxes;
    }

    public Box getBoxByName(String name) {
        for (Box box : boxes) {
            if (box.getName().equals(name)) {
                return box;
            }
        }
        return null;
    }

    public Box getBoxByDisplayName(String displayName) {
        if (boxes == null || displayName == null) return null;

        for (Box box : boxes) {
            if (box.getDisplayName().equals(displayName)) {
                return box;
            }
        }
        return null;
    }

    private static List<Box> loadBoxList(Path randomBoxFolder) throws IOException {
        Files.createDirectories(randomBoxFolder);

        List<Box> result = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(randomBoxFolder, "*.yml")) {
            for (Path file : stream) {
                result.add(Box.fromConfig(file));
            }
        }

        return result;
    }

}
