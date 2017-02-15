import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

// Store the frequency of words per segment
public class SingleCounter implements Callable<Map<String, Integer>>{
    private Map<String, Integer> _frequentDict;
    private String[] _words;
    private int _start;
    private int _end;
    private int _threadNumber;

    public SingleCounter(String[]words,int start,int end, int threadNumber){
        _words = words;
        _frequentDict = new HashMap<>();
        _start = start;
        _end = end;
        _threadNumber = threadNumber;
    }

    @Override
    public Map<String, Integer> call() throws Exception {
        System.out.println("Thread " + _threadNumber + " starts working!");
        for (int i = _start; i < _end; i++) {
            _frequentDict.put(_words[i], _frequentDict.getOrDefault(_words[i], 0) + 1);
        }
        System.out.println("Thread " + _threadNumber + " finished work!");
        return _frequentDict;
    }
}

