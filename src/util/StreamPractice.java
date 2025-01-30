package util;

import domain.Employee;
import model.Position;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class StreamPractice {

    public static List<Integer> removeDuplicates(List<Integer> original) {

        return original.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    public static Integer findThirdLargestNumber(List<Integer> original) {

        return original.stream()
                .sorted(Comparator.reverseOrder())
                .skip(2)
                .findFirst()
                .orElse(null);
    }

    public static Integer findThirdUniqueLargestNumber(List<Integer> original) {

        return findThirdLargestNumber(removeDuplicates(original));
    }

    public static List<String> getTopThreeOldestEngineerNames(List<Employee> employees) {
        return employees.stream()
                .filter(e -> e.getPosition().equals(Position.ENGINEER))
                .sorted(Comparator.comparingInt(Employee::getAge).reversed())
                .limit(3)
                .map(Employee::getName)
                .collect(Collectors.toList());
    }

    public static double getAverageEngineerAge(List<Employee> employees) {
        return employees.stream()
                .filter(e -> e.getPosition().equals(Position.ENGINEER))
                .mapToInt(Employee::getAge)
                .average()
                .orElse(0);
    }

    public static String findLongestWord(List<String> words) {
        return words.stream()
                .max(Comparator.comparingInt(String::length))
                .orElse(null);
    }

    public static Map<String, Long> countWordOccurrences(String string) {
        return Stream.of(string.split(" "))
                .collect(Collectors.groupingBy(w -> w, Collectors.counting()));
    }

    public static List<String> sortWordsInString(List<String> string) {
        return string.stream()
                .sorted(
                        Comparator.comparing(String::length)
                                .thenComparing(Comparator.naturalOrder()))
                .collect(Collectors.toList());
    }

    public static String findLongestWord(String[] strings) {
        return Stream.of(strings)
                .flatMap(s -> Arrays.stream(s.split(" ")))
                .max(Comparator.comparing(String::length))
                .orElse(null);
    }
}
