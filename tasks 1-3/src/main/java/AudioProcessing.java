import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class AudioProcessing {
    static final HashMap<Character, String> encodeMap = new HashMap<>();
    static final HashMap<String, Character> decodeMap = new HashMap<>();

    static final char[] english = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
            'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

    static final String[] morse = {".-", "-...", "-.-.", "-..", ".", "..-.", "--.", "....", "..", ".---", "-.-", ".-..",
            "--", "-.", "---", ".--.", "--.-", ".-.", "...", "-", "..-", "...-", ".--", "-..-", "-.--", "--.."};

    public static List<File> sort(List<File> fileList) {
        ArrayList<File> res = new ArrayList<>(fileList.size());
        res.addAll(fileList);
        res.sort(Comparator.comparingInt(f -> Integer.parseInt(f.getName().substring(0, f.getName().indexOf('-')))));
        return res;
    }

    public static void initMorse() {
        for (int i = 0; i < english.length; i++) {
            encodeMap.put(english[i], morse[i]);
            decodeMap.put(morse[i], english[i]);
        }
    }

    public static String decode(String input) {
        String[] letters = input.split(" ");
        StringBuilder ret = new StringBuilder();
        for (String letter : letters) {
            if (decodeMap.get(letter) != null) {
                ret.append(decodeMap.get(letter));
            } else {
                System.err.println("UnknownCharacter: " + letter);
                System.err.println("I think it's: \"yb\"");
                ret.append("yb");
            }
        }
        return ret.toString();
    }

    public static void main(String[] args) {
        initMorse();
        File folder = new File("src/main/resources/cutAudio");
        StringBuilder ans = new StringBuilder();
        for (File entry : sort(Arrays.asList(Objects.requireNonNull(folder.listFiles())))) {
            if (entry.length() < 7000) {
                ans.append(" ");
            }
            if (entry.length() > 7000 && entry.length() < 25000) {
                ans.append(".");
            }
            if (entry.length() > 25000) {
                ans.append("-");
            }
        }
        try (FileWriter writer = new FileWriter(String.valueOf("src/main/out/audio.txt"), StandardCharsets.UTF_8)) {
            writer.append(decode(String.valueOf(ans)));
            writer.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
