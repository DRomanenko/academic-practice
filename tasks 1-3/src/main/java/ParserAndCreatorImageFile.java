import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ParserAndCreatorImageFile {
    private final Path inputPath;
    private final int blockHeight;
    private final int blockWidth;
    private boolean[][] blockColor;
    private static BufferedImage bufferedImage;

    ParserAndCreatorImageFile(final String inputFile, final int height, final int width, final int blockHeight, final int blockWidth) throws CreateImageException {
        inputPath = checkPath(inputFile, "Incorrect path to input file");
        bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        blockColor = new boolean[height / blockHeight][width / blockWidth];
        this.blockHeight = blockHeight;
        this.blockWidth = blockWidth;
    }

    private Path checkPath(String file, String message) throws CreateImageException {
        try {
            return Paths.get(file);
        } catch (InvalidPathException e) {
            throw new CreateImageException(message + ": " + e.getMessage());
        }
    }

    enum blackColorSynonyms {
        black, dark
    }

    private void parseFile() throws IOException {
        List<String> lines = Files.readAllLines(inputPath, StandardCharsets.UTF_8);
        int x = 0, y = 0;
        for (String line : lines) {
            boolean inverted = (line.lastIndexOf("not") != -1);
            for (blackColorSynonyms synonym : blackColorSynonyms.values()) {
                blockColor[y][x] = (line.lastIndexOf(String.valueOf(synonym)) != -1) ? !inverted : inverted;
                if (blockColor[y][x]) break;
            }
            if (line.contains("(") || line.contains(")")) {
                blockColor[y][x] = (line.length() < 10);
            }
            if (++x >= blockColor[y].length) {
                ++y;
                x = 0;
            }
        }
    }

    private void createImageAfterParse() {
        Graphics2D g2d = bufferedImage.createGraphics();
        // fill all the image with white
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());

        // create a rectangle with black
        g2d.setColor(Color.black);
        Rectangle bounds = new Rectangle(0, 0, blockWidth, blockHeight);
        for (int y = 0; y < blockColor.length; ++y) {
            for (int x = 0; x < blockColor[y].length; ++x) {
                if (blockColor[y][x]) {
                    g2d.fillRect((int) bounds.getX(), (int) bounds.getY(), blockWidth, blockHeight);
                }
                bounds.setLocation(blockWidth * (x + 1), (int) bounds.getY());
            }
            bounds.setLocation(0, blockHeight * (y + 1));
        }
        // Disposes of this graphics context and releases any system resources that it is using.
        g2d.dispose();
    }

    public void parse() throws IOException {
        parseFile();
        createImageAfterParse();
    }

    // Save as PNG
    public void writeInFilePng(final String outputFile) throws CreateImageException {
        File file = new File(String.valueOf(checkPath(outputFile, "Incorrect path to output file")));
        try {
            ImageIO.write(bufferedImage, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
