import domain.Employee;
import model.Position;

import java.util.List;
import java.util.Map;

import static util.StreamPractice.*;

public class Main {
    public static void main(String[] args) {
        List<Integer> list = List.of(5, 2, 10, 9, 4, 3, 10, 1, 13);
        List<Employee> employees = List.of(
                new Employee("Bob", 18, Position.ENGINEER),
                new Employee("Rob", 45, Position.DIRECTOR),
                new Employee("Mark", 30, Position.MANAGER),
                new Employee("Liza", 35, Position.ENGINEER),
                new Employee("Sofia", 40, Position.ENGINEER),
                new Employee("Claire", 25, Position.ENGINEER)
        );
        List<String> numbers = List.of("One", "Two", "Three", "Four", "Five");
        String string = "one two two three three three";
        List<String> fruits = List.of("banana", "apple", "peach", "orange", "avocado", "lemon");
        String[] numberArr = {
                "one two three four five",
                "six seven eight nine ten",
                "eleven twelve thirteen fourteen fifteen"
        };

        List<Integer> uniqueElements = removeDuplicates(list);
        System.out.println("uniqueElements: " + uniqueElements);

        Integer thirdLargestNumber = findThirdLargestNumber(list);
        System.out.println("thirdLargestNumber: " + thirdLargestNumber);

        Integer thirdUniqueLargestNumber = findThirdUniqueLargestNumber(list);
        System.out.println("thirdUniqueLargestNumber: " + thirdUniqueLargestNumber);

        List<String> topThreeOldestEngineerNames = getTopThreeOldestEngineerNames(employees);
        System.out.println("topThreeOldestEngineerNames: " + topThreeOldestEngineerNames);

        double averageEngineerAge = getAverageEngineerAge(employees);
        System.out.println("averageEngineerAge: " + averageEngineerAge);

        String longestWord1 = findLongestWord(numbers);
        System.out.println("longestWord1: " + longestWord1);

        Map<String, Long> wordOccurrenceMap = countWordOccurrences(string);
        System.out.println("wordOccurrenceMap: " + wordOccurrenceMap);

        List<String> sortedList = sortWordsInString(fruits);
        System.out.println("sortedList: " + sortedList);

        String longestWord2 = findLongestWord(numberArr);
        System.out.println("longestWord2: " + longestWord2);
    }
}
