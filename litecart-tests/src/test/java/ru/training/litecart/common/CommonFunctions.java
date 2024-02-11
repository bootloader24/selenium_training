package ru.training.litecart.common;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommonFunctions {
    // метод проверки правильности сортировки списка строк
    // возвращает true, если сортировка корректна
    public static boolean checkSorting(List<String> strings) {
        String previous = "";
        for (var current : strings) {
            if (current.compareTo(previous) < 0)
                return false;
            previous = current;
        }
        return true;
    }

    public static String randomString(int n) {
        var rnd = new Random();
        Supplier<Integer> randomNumbers = () -> rnd.nextInt(26);
        return Stream.generate(randomNumbers)
                .limit(n)
                .map(i -> 'a' + i)
                .map(Character::toString)
                .collect(Collectors.joining());
    }


    public static String randomFile(String dir) {
        var fileNames = new File(dir).list();
        var rnd = new Random();
        var index = rnd.nextInt(fileNames.length);
        return Paths.get(dir, fileNames[index]).toString();
    }
}
