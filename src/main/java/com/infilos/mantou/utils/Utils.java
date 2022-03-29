package com.infilos.mantou.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {

    public static List<Integer> skipSequences(List<Integer> dataList) {
        return groupBySeq(dataList).stream().map(list -> list.get(0)).collect(Collectors.toList());
    }

    public static List<List<Integer>> groupBySeq(List<Integer> dataList) {
        return dataList.stream().sorted().collect(ArrayList::new,
            (acc, val) -> {
                if (acc.isEmpty()) {
                    acc.add(new ArrayList<>());
                }
                List<Integer> lastGroup = acc.get(acc.size() - 1);
                if (lastGroup.isEmpty() || val - lastGroup.get(lastGroup.size() - 1) == 1) {
                    lastGroup.add(val);
                } else {
                    ArrayList<Integer> newGroup = new ArrayList<>();
                    newGroup.add(val);
                    acc.add(newGroup);
                }
            }, (lists, lists2) -> {
            });
    }
}
