import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class WordCounter {
    public static final String DEFAULT_OUTPUT_FILE_NAME = "Result.txt";
    public static final String DEFAULT_ENCODE = "UTF-8";
    private int _segNumber;
    private int _wordsPerSeg;
    private ExecutorService _executorService;
    private String[] _words;
    private Map<String, Integer> _frequentMap;

    public WordCounter(String[] words, int segNumber) {
        _words = words;
        _segNumber = segNumber;
        _wordsPerSeg = words.length / segNumber + 1;
        _executorService = Executors.newFixedThreadPool(_segNumber);
        _frequentMap = new HashMap<>();
    }

    public void count() {
        List<FutureTask<Map<String, Integer>>> taskList = new ArrayList<>();

        // Start chunk data into segments and assign tasks
        for (int i = 0 ; i < _segNumber; i++) {
            SingleCounter singleCounter = new SingleCounter(_words, _wordsPerSeg * i,
                    _wordsPerSeg * (i + 1) > _words.length ? _words.length : _wordsPerSeg * (i + 1), i);
            FutureTask<Map<String, Integer>> task = new FutureTask<>(singleCounter);
            taskList.add(task);
            _executorService.submit(task);
        }

        // Loop the submitted tasks to collect final result
        for (FutureTask<Map<String, Integer>> task : taskList) {
            try {
                _frequentMap.putAll(task.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String word : _frequentMap.keySet()) {
            sb.append(word + ":" + _frequentMap.get(word) + '\n');
        }
        return sb.toString();
    }

    public void generateResult(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            fileName = DEFAULT_OUTPUT_FILE_NAME;
        }
        try (PrintWriter writer = new PrintWriter(fileName, DEFAULT_ENCODE)) {
            writer.println(toString());
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
