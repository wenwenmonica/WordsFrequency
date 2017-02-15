import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CountingProcess {
    public static void main(String[] args) throws IOException {
        // Read words
        String filename = "./src/TestFile.txt";
        int numberOfThreads = 5;
        if (args.length > 0) {
            filename = args[0];
        }

        if (args.length > 1) {
            numberOfThreads = Integer.parseInt(args[1]);
        }

        BufferedReader br = new BufferedReader(new FileReader(filename));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(" " + line.trim());
        }
        String[] words = sb.toString().trim().split(" ");
        br.close();

        WordCounter wordCounter = new WordCounter(words, numberOfThreads);
        wordCounter.count();
        System.out.println();
        System.out.println("Final Result :");
        System.out.println(wordCounter.toString());
    }
}
