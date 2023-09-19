package com.mpedroni.streams;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;

public class Main {
    Main() {
        try {
            benchmark();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        new Main();
    }

    private static Path getBookPath(String book) {
        return Paths.get(String.format("src/main/resources/books/%s.txt", book));
    }

    public void benchmark() throws IOException {
        String[] books = {"bible", "the-odyssey"};

        String book = books[0];

        long start;
        long end;

        start = System.currentTimeMillis();
        usingBufferedReader(book);
        end = System.currentTimeMillis();

        System.out.println("BufferedReader: \n" + "  Execution Time (ms): " + (end - start));

        start = System.currentTimeMillis();
        usingStreams(book);
        end = System.currentTimeMillis();

        System.out.println("Stream: \n" + "  Execution Time (ms): " + (end - start));

        start = System.currentTimeMillis();
        usingParallelStreams(book);
        end = System.currentTimeMillis();

        System.out.println("Parallel: \n" + "  Execution Time (ms): " + (end - start));
    }

    private String normalize(String aString) {
        return aString.replaceAll("[^a-zA-Z0-9]|\n", " ").toLowerCase();
    }

    private void usingBufferedReader(String book) throws IOException {
        Map<Character, Set<String>> words = new HashMap<>();
        Path path = getBookPath(book);
        BufferedReader reader = Files.newBufferedReader(path);

        String line;

        while ((line = reader.readLine()) != null) {
            var normalized = normalize(line);

            for (var word : normalized.split(" ")) {
                if (word.isEmpty()) continue;

                var letter = word.charAt(0);

                var group = words.computeIfAbsent(letter, k -> new TreeSet<>());

                group.add(word);
            }
        }

        reader.close();
    }

    private void usingStreams(String book) throws IOException {
        Path path = getBookPath(book);
        Stream<String> stream = Files.lines(path);

        var words = stream
                .map(this::normalize)
                .map(line -> line.split(" "))
                .flatMap(Arrays::stream)
                .filter(w -> !w.isEmpty())
                .collect(groupingBy(word -> word.charAt(0),
                        Collectors.toCollection(TreeSet::new)));

        stream.close();
    }

    private void usingParallelStreams(String book) throws IOException {
        Path path = getBookPath(book);
        Stream<String> parallel = Files.lines(path).parallel();

        var words = parallel.map(this::normalize)
                .map(line -> line.split(" "))
                .flatMap(Arrays::stream)
                .filter(w -> !w.isEmpty())
                .collect(groupingBy(word -> word.charAt(0),
                        Collectors.toCollection(TreeSet::new)));
        parallel.close();
    }

}