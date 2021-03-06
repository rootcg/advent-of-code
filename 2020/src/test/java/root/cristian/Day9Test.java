package root.cristian;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import utils.FilesUtilities;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

class Day9Test {

    @Test
    final void exampleFirst() {
        final String data = """
                35
                20
                15
                25
                47
                40
                62
                55
                65
                95
                102
                117
                150
                182
                127
                219
                299
                277
                309
                576""";

        final Day9 day9 = new Day9();
        final long result = day9.first(data.lines().collect(Collectors.toList()), 5);
        Assertions.assertEquals(127, result);
    }

    @Test
    final void exampleSecond() {
        final String data = """
                35
                20
                15
                25
                47
                40
                62
                55
                65
                95
                102
                117
                150
                182
                127
                219
                299
                277
                309
                576""";

        final List<String> dataList = data.lines().collect(Collectors.toList());
        final Day9 day9 = new Day9();
        final long result = day9.second(dataList, day9.first(dataList, 5));
        Assertions.assertEquals(62, result);
    }

    @Test
    final void first() throws IOException {
        final List<String> data = readInput();
        final Day9 day9 = new Day9();
        final long result = day9.first(data, 25);
        System.out.println("Result is: " + result);
    }

    @Test
    final void second() throws IOException {
        final List<String> data = readInput();
        final Day9 day9 = new Day9();
        final long result = day9.second(data, day9.first(data, 25));
        System.out.println("Result is: " + result);
    }

    private List<String> readInput() throws IOException {
        return Files.lines(FilesUtilities.getResource("Day9")).collect(Collectors.toList());
    }

}
