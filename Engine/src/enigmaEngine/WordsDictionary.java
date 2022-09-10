package enigmaEngine;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class WordsDictionary {
    private final Set<String> words;
    private final Set<Character> excludedCharacters;

    public WordsDictionary(String dictionaryWords, String _excludedCharacters) {
        this.excludedCharacters = new HashSet<>();
        this.words = new HashSet<>(Arrays.asList(dictionaryWords.toUpperCase().split(" ")));
        this.excludedCharacters.addAll(_excludedCharacters.toUpperCase().chars().mapToObj(c -> (char) c).collect(Collectors.toList()));
        this.words.forEach(word -> this.words.add(word.replaceAll("[" + excludedCharacters + "]", "")));
    }

    public List<String> candidateWords(String processedText) {
        List<String> cleanedProcessedText = cleanText(processedText.toUpperCase());
        return words.stream().filter(cleanedProcessedText::contains).collect(Collectors.toList());
    }

    private List<String> cleanText(String processedText) {
        return Arrays.stream(processedText.split(" ")).
                map(word -> word.replaceAll("[" + excludedCharacters + "]", "")).
                collect(Collectors.toList());
    }
}
