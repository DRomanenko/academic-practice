import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import javax.imageio.ImageIO;

public class BlockImageOCR {
    private final Path outputPath;
    private BufferedImage bufferedImage;
    private final int blockHeight;
    private final int blockWidth;
    private String[][] blocks;

    BlockImageOCR(final String inputFile, final String outputFile, final int blockHeight, final int blockWidth) throws CreateImageException {
        outputPath = checkPath(outputFile, "Incorrect path to output file");
        try {
            bufferedImage = ImageIO.read(new File(String.valueOf(inputFile)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        blocks = new String[bufferedImage.getHeight() / blockHeight][bufferedImage.getWidth() / blockWidth];
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

    public void parseImg() {
        Tesseract tesseract = new Tesseract();
        try {
            tesseract.setDatapath("src/main/resources/tessdata");
            tesseract.setLanguage("eng");
            Rectangle bounds = new Rectangle(0, 0, blockWidth, blockHeight);
            for (int y = 0; y < blocks.length; ++y) {
                for (int x = 0; x < blocks[y].length; ++x) {
                    blocks[y][x] = tesseract.doOCR(bufferedImage, bounds);
                    blocks[y][x] = blocks[y][x].replaceAll("\\s+", "").toLowerCase();
                    bounds.setLocation(blockWidth * (x + 1), (int) bounds.getY());
                }
                bounds.setLocation(0, blockHeight * (y + 1));
            }
        } catch (TesseractException e) {
            e.printStackTrace();
        }
    }

    public void printInFile() {
        try (FileWriter writer = new FileWriter(String.valueOf(outputPath), StandardCharsets.UTF_8)) {
            for (String[] block : blocks) {
                for (String s : block) {
                    writer.append(s).append("\n");
                }
            }
            writer.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
