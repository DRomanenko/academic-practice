import javafx.util.Pair;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static org.opencv.core.CvType.CV_32FC1;
import static org.opencv.imgcodecs.Imgcodecs.imwrite;
import static org.opencv.imgproc.Imgproc.*;

public class VideoProcessing {
    static void matchingMethod() {
        Mat frame = new Mat(), result = new Mat();
        VideoCapture video = new VideoCapture("src/main/resources/task.mp4");
        video.read(frame);
        int startSec = 10, fps = 24, qtyFrames = 895 - startSec;
        for (int i = 0; i < startSec * fps; ++i) {
            video.read(frame);
        }

        File folder = new File("src/main/resources/letters");
        File[] folderEntries = folder.listFiles();

        try (FileWriter writer = new FileWriter("src/main/out/qtyLetters.txt", StandardCharsets.UTF_8)) {
            int scaleFactor = 4, currFrame = 0;
            int[] counter = new int[26];
            // If next video frame is available
            while (currFrame / fps < qtyFrames && video.read(frame)) {
                if (currFrame % fps == 0) {
                    System.out.println(currFrame / fps);
                    cvtColor(frame, frame, COLOR_RGB2GRAY);
                    imwrite("src/main/resources/frames/" + (currFrame / fps) + ".png", frame);
                    Pair<Double, String> pair = new Pair<>(-1e-9, "");
                    Mat resizedFrame = new Mat();
                    for (File entry : Objects.requireNonNull(folderEntries)) {
                        frame.copyTo(resizedFrame);
                        Mat pattern = Imgcodecs.imread(entry.getAbsolutePath(), Imgcodecs.IMREAD_GRAYSCALE);
                        while (resizedFrame.width() - pattern.width() - scaleFactor > 0 || resizedFrame.height() - pattern.height() - scaleFactor > 0) {
                            result.create(resizedFrame.width() - pattern.width() + 1, resizedFrame.height() - pattern.height() + 1, CV_32FC1);
                            Imgproc.matchTemplate(resizedFrame, pattern, result, Imgproc.TM_CCOEFF_NORMED);
                            Core.MinMaxLocResult mmr = Core.minMaxLoc(result);
                            pair = (pair.getKey() < mmr.maxVal ? new Pair<>(mmr.maxVal, entry.getName()) : pair);
                            int diffWidth = resizedFrame.width() - (resizedFrame.width() - pattern.width() - scaleFactor > 0 ? scaleFactor : 0);
                            int diffHeight = resizedFrame.height() - (resizedFrame.height() - pattern.height() - scaleFactor > 0 ? scaleFactor : 0);
                            Imgproc.resize(resizedFrame, resizedFrame, new Size(diffWidth, diffHeight), 0, 0, INTER_AREA);
                        }
                    }
                    writer.append(pair.getValue().toUpperCase().charAt(0)).append(" ").append(String.valueOf(pair.getKey())).append("\n");
                    ++counter[pair.getValue().toLowerCase().charAt(0) - 'a'];
                }
                if ((currFrame / fps) % 50 == 0) {
                    writer.flush();
                }
                ++currFrame;
            }
            StringBuilder ans = new StringBuilder();
            for (int i = 0; i < counter.length; ++i) {
                if (counter[i] > 0) {
                    ans.append((char) (i + 'A')).append(counter[i]);
                }
            }
            writer.append(ans);
            writer.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void scalingPattern(double scalingFactor) {
        File folder = new File("src/main/resources/letters");
        File[] folderEntries = folder.listFiles();
        for (File entry : Objects.requireNonNull(folderEntries)) {
            Mat pattern = Imgcodecs.imread(entry.getAbsolutePath(), Imgcodecs.IMREAD_GRAYSCALE);
            Imgproc.resize(pattern, pattern, new Size(scalingFactor * pattern.width(), scalingFactor * pattern.height()), 0, 0, INTER_AREA);
            imwrite("src/main/resources/letters/" + entry.getName(), pattern);
        }
    }

    public static void main(String[] args) {
        nu.pattern.OpenCV.loadShared();
        System.loadLibrary(org.opencv.core.Core.NATIVE_LIBRARY_NAME);
        // scalingImage(0.3);
        matchingMethod();
    }
}
