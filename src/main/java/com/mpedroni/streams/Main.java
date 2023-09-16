package com.mpedroni.streams;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws Exception {
        String[] books = {"bible", "the-odyssey"};

        String search = "God";
        String book = books[0];

        long start;
        long end;

        start = System.currentTimeMillis();
        var bufferedReader = usingBufferedReader(book, search);
        end = System.currentTimeMillis();

        System.out.println("BufferedReader: \n  " + "Result -> " + bufferedReader + "\n  Time (ms): " + (end - start));

        start = System.currentTimeMillis();
        var stream = usingStreams(book, search);
        end = System.currentTimeMillis();

        System.out.println("Stream: \n  " + "Result -> " + stream + "\n  Time (ms): " + (end - start));

        start = System.currentTimeMillis();
        var parallel = usingParallelStreams(book, search);
        end = System.currentTimeMillis();

        System.out.println("Parallel: \n  " + "Result -> " + parallel + "\n  Time (ms): " + (end - start));
    }

    private static int usingBufferedReader(String book, String search) throws IOException {
        Path path = getBookPath(book);
        BufferedReader reader = Files.newBufferedReader(path);

        String line;

        int count = 0;

        while ((line = reader.readLine()) != null) {
            if (line.contains(search)) count++;
        }

        reader.close();

        return count;
    }

    private static int usingStreams(String book, String search) throws IOException {
        Path path = getBookPath(book);
        Stream<String> stream = Files.lines(path);

        var count = stream.filter((l) -> l.contains(search)).count();
        stream.close();

        return (int) count;
    }

    private static int usingParallelStreams(String book, String search) throws IOException {
        Path path = getBookPath(book);
        Stream<String> parallel = Files.lines(path).parallel();

        var count = parallel.filter((l) -> l.contains(search)).count();
        parallel.close();

        return (int) count;
    }

    private static Path getBookPath(String book) {
        return Paths.get(String.format("src/main/resources/books/%s.txt", book));
    }

}