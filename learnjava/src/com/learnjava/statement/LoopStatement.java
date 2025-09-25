package com.learnjava.statement;

import java.util.ArrayList;
import java.util.List;

public class LoopStatement {
    // #region while
    public static void whileLoop() {
        int i = 0;
        while (i < 10) {
            System.out.println(i);
            i++;
        }
    }
    // #endregion while

    // #region doWhile
    public static void doWhileLoop() {
        int i = 0;
        do {
            System.out.println(i);
            i++;
        } while (i < 10);
    }
    // #endregion doWhile

    // #region for
    public static void forLoop() {
        for (int i = 0; i < 10; i++) {
            System.out.println(i);
        }
    }
    // #endregion for

    // #region foreach
    public static void foreachLoop() {
        // 遍历数组
        int[] arr = { 1, 2, 3, 4, 5 };
        for (int i : arr) {
            System.out.println(i);
        }

        // 遍历集合
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        for (String s : list) {
            System.out.println(s);
        }
    }
    // #endregion foreach
}
