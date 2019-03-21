package util;

import data.Movie;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Calculator {

    private Calculator(){}

    static Map<String, Double> statistics(List<Movie> movies, Predicate<Movie> predicate){
        List<Movie> sample = movies.stream().filter(predicate).collect(Collectors.toList());
        Supplier<Stream<Movie>> streamSupplier = sample::stream;
        Map<String, Double> results = new HashMap<>(
                createSummaryStatistics(streamSupplier.get(), Movie::numericValueOfRuntime, "runtime"));
        results.putAll(createSummaryStatistics(
                streamSupplier.get(), item -> Double.parseDouble(item.getMetascore()), "metascore"));
        results.putAll(createSummaryStatistics(
                streamSupplier.get(), item -> Double.parseDouble(item.getImdbRating()), "imdbRating"));
        results.putAll(createSummaryStatistics(
                streamSupplier.get(), item -> Double.parseDouble(item.getImdbVotes().replaceAll(",","")), "imdbVotes"));
        return results;
    }

    static Map<String, Double> createSummaryStatistics(Stream<Movie> movies,
                                                               ToDoubleFunction<Movie> doubleFunction,
                                                               String mapPrefix){
        DoubleSummaryStatistics summaryStatistics =  movies
                .mapToDouble(doubleFunction)
                .summaryStatistics();
        Map<String, Double> hashSummary = new HashMap<>();
        hashSummary.put(mapPrefix + "Avg", summaryStatistics.getAverage());
        hashSummary.put(mapPrefix + "Max", summaryStatistics.getMax());
        hashSummary.put(mapPrefix + "Min", summaryStatistics.getMin());
        hashSummary.put(mapPrefix + "Sum", summaryStatistics.getSum());
        return hashSummary;
    }
}
