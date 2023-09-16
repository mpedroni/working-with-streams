package com.mpedroni.streams;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws Exception {
        Path odyssey = Paths.get("src/main/resources/books/the-odyssey.txt");
        Path bible = Paths.get("src/main/resources/books/bible.txt");

        Path path = bible;
        String search = "God";

        BufferedReader reader = Files.newBufferedReader(path);

        String line;

        long count = 0;

        long s = System.currentTimeMillis();

        while((line = reader.readLine()) != null) {
            if(line.contains(search)) count++;
        }

        long e = System.currentTimeMillis();

        reader.close();

        System.out.println("BufferedReader: \n  " + "Result -> " + count + "\n  Time (ms): " + (e - s));

        Stream<String> stream = Files.lines(path);

        s = System.currentTimeMillis();
        count = stream.filter((l) -> l.contains(search)).count();
        e = System.currentTimeMillis();

        System.out.println("Stream: \n  " + "Result -> " + count + "\n  Time (ms): " + (e - s));

        Stream<String> parallel = Files.lines(path).parallel();

        s = System.currentTimeMillis();
        count = parallel.filter((l) -> l.contains(search)).count();
        e = System.currentTimeMillis();

        System.out.println("Parallel: \n  " + "Result -> " + count + "\n  Time (ms): " + (e - s));
    }
}