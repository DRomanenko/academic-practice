import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;

import static org.opencv.imgcodecs.Imgcodecs.imwrite;
import static org.opencv.imgproc.Imgproc.INTER_CUBIC;

public class ImageProcessing {
    private static void scalingImage(int scalingFactor) {
        nu.pattern.OpenCV.loadShared();
        System.loadLibrary(org.opencv.core.Core.NATIVE_LIBRARY_NAME);
        Mat mat = Imgcodecs.imread("src/main/resources/blandwh.png");
        Mat resizedMat = new Mat();
        double width = mat.cols(), height = mat.rows();
        Size sz = new Size(width * scalingFactor, height * scalingFactor);
        Imgproc.resize(mat, resizedMat, sz, 0, 0, INTER_CUBIC);
        Imgproc.blur(mat, resizedMat, new Size(2, 2));
        imwrite("src/main/resources/improveBlandwh.png", resizedMat);
    }

    public static void main(String[] args) {
        //scalingImage(2);
        try {
            if (args.length != 1) {
                throw new IllegalArgumentException("Expected gen or parse.");
            }
            if (args[0].equals("ocr")) {
                BlockImageOCR blockImageOCR = new BlockImageOCR("src/main/resources/blandwh.png", "src/main/out/image.txt", 100, 100);
                blockImageOCR.parseImg();
                blockImageOCR.printInFile();
            }
            if (args[0].equals("gen")) {
                ParserAndCreatorImageFile parserAndCreatorImageFile = new ParserAndCreatorImageFile("src/main/out/image.txt", 3700, 3700, 100, 100);
                parserAndCreatorImageFile.parse();
                parserAndCreatorImageFile.writeInFilePng("src/main/out/qrCode.png");
            }
        } catch (CreateImageException | IOException e) {
            e.printStackTrace();
        }
    }
}
