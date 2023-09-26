# working-with-streams

The purpose of this repository is to practice studies about Java Streams. The main idea here is to implement equivalent operations using streams (both serial and parallel) and the Collection framework.

The application reads a huge text file (e.g. the Bible), group all the words by the first letter, then remove the duplicates and sort the created groups.

To comparison, there are three different implementations:

 - Using a serial Java stream
 - Using a parallel Java stream
 - Using the Collection framework with a BufferedReader to read the file

The execution results are presented below:

| Implementation  | Execution Time (ms) |
|-----------------|---------------------|
| BufferedReader  | 239.8               |
| Serial Stream   | 200.0               |
| Parallel Stream | 81.59               |

All execution times are an average of five executions.

For info about the used tools and hardware, check the [environment](#environment) section.

## Conclusion

All the three methods were implemented on the easiest way that I found. Besides the better results, using streams is easier rather the Collection framework (once you learn how to work the streams API) and we reach a better code readability.

So, despite the Collection framework is more intuitive than streams at first glance, worth it a lot to spend some time learning how to use streams, specially when it is necessary make complex operations and/or transformations over a data group, because the idea of pipeline (sequential operations, over each element, as a factory production line) which the stream brings to the code makes it easier to create complex data manipulations without losing the code readability. 

## Environment

- IntelliJ IDEA 2023.2.2 (Community Edition)
- Java 17
- MacBook Air M1, 8 GB (2020)  
