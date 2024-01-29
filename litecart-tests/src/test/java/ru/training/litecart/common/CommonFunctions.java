package ru.training.litecart.common;

import java.util.List;

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
}
