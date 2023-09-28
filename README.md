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

## Why are streams faster?

When using streams, under the hood, before effectively executing the pipeline, there are a series of analysis in order to create an optimized execution plan to find the better way to do such operations over the data. These optimizations ranges from change the operations order (to apply as many as possible operations in the same iteration, for example) to remove some useless steps (like removing duplicates from a `Tree`, which doesn't have duplicated data at all). This execution plan may improve significantly the execution time in opposite with the Collection framework.

There are two types of operations: stateless and stateful. Stateless operations don't need to know about the other elements on the list to be applied. `map` and `filter` are examples of stateless operations. In the other hand, stateful operations need to access other elements on the group to be evaluated, such as `distinct` and `sorted`.

Think about it for a moment... if a stream pipeline has a sequence of stateless operations, it implies once all the operations which must be applied are known, it is possible to apply it over each element independently, **at the same time**, and that is exactly what happens when using parallel streams.

## Conclusion

Besides the better results, using streams can be easier to use rather the Collection framework when we think in streams as a pipeline of operations and transformations over each element of a collection, which allows us to create complex transformations with a great readability at the same time. 

The downside of streams is, IMO, the learning curve. The Collection framework have a familiar and intuitive API (probably because all we need to know is how to iterate over all elements, and get/delete a specific one). In the other hand, the stream has a quite particular API, which demands spending some time over Java docs and StackOverflow to figure out how to make it work and get used to it.

Feel free to comment or contribute with better and more optimized implementations. Any ideas are welcome. You can check the source code [here](./src/main/java/com/mpedroni/streams/WorkingWithStreams.java).

## Further reading

Here are two resources about Java Stream. The first one is an overview with some usage examples, and the second one explores streams in a deeper way, addressing how it works under the hood:

- [The Java 8 Stream API Tutorial](https://www.baeldung.com/java-8-streams)
- [Understanding Java Streams](https://theboreddev.com/understanding-java-streams/)

And, of course, the Java Stream [official documentation](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/stream/Stream.html).

## Environment

- IntelliJ IDEA 2023.2.2 (Community Edition)
- Java 17
- MacBook Air M1, 8 GB (2020)  
