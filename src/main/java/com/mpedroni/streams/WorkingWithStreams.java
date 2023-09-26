package com.mpedroni.streams;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;

public class WorkingWithStreams {

    static final int NUMBER_OF_ITERATIONS = 5;

    WorkingWithStreams() throws Exception {
        init();
    }

    public static void main(String[] args) throws Exception {
        new WorkingWithStreams();
    }

    private static Path getBookPath(String book) {
        return Paths.get(String.format("src/main/resources/books/%s.txt", book));
    }

    public void init() throws Exception {
        String[] books = {"bible", "the-odyssey"};

        Path book = getBookPath(books[0]);

        var bufferedReader = benchmark(() -> {
            usingBufferedReader(book);
            return null;
        });

        var stream = benchmark(() -> {
            usingStreams(book);
            return null;
        });

        var parallelStream = benchmark(() -> {
            usingParallelStreams(book);
            return null;
        });

        printResult("BufferedReader", bufferedReader);
        printResult("Stream", stream);
        printResult("Parallel Stream", parallelStream);
    }

    private double benchmark(Callable<?> callable) throws Exception {
        long time = 0;

        for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
            var start = System.currentTimeMillis();
            callable.call();
            var end = System.currentTimeMillis();

            time += end - start;
        }


        double avg = (double) time / NUMBER_OF_ITERATIONS;

        return Math.floor(avg * 100) / 100;
    }

    /**
     * Given a string, return a new string removing everything that is not a letter, a number or the new line character
     */
    private String normalize(String aString) {
        return aString.replaceAll("[^a-zA-Z0-9]|\n", " ").toLowerCase();
    }

    private void usingBufferedReader(Path path) throws IOException {
        Map<Character, Set<String>> words = new HashMap<>();
        BufferedReader reader = Files.newBufferedReader(path);

        String line;

        while ((line = reader.readLine()) != null) {
            var normalized = normalize(line);

            for (var word : normalized.split(" ")) {
                if (word.isEmpty()) continue;

                var letter = word.charAt(0);

                // get the letter group if present, else create a new one
                var group = words.computeIfAbsent(letter, k -> new TreeSet<>());

                group.add(word);
            }
        }

        reader.close();
    }

    private void usingStreams(Path path) throws IOException {
        Stream<String> stream = Files.lines(path);

        var words = stream
                .map(this::normalize)
                .map(line -> line.split(" "))
                .flatMap(Arrays::stream)
                .filter(w -> !w.isEmpty())
                .collect(groupingBy(word -> word.charAt(0),
                        // set a TreeSet as collector to remove duplicated words;
                        // the default collector is a List, which accepts duplicated values
                        Collectors.toCollection(TreeSet::new)));

        stream.close();
    }

    private void usingParallelStreams(Path path) throws IOException {
        Stream<String> parallel = Files.lines(path).parallel();

        var words = parallel.map(this::normalize)
                .map(line -> line.split(" "))
                .flatMap(Arrays::stream)
                .filter(w -> !w.isEmpty())
                .collect(groupingBy(word -> word.charAt(0),
                        Collectors.toCollection(TreeSet::new)));

        parallel.close();
    }

    private void printResult(String label, double result) {
        System.out.println(label + ": \n" + "  Execution Time (ms): " + result);
    }
}