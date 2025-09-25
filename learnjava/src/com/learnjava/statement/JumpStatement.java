package com.learnjava.statement;

public class JumpStatement {
    // #region continue
    public static void continueStatement() {
        // while
        int i = 0;
        while (i < 10) {
            i++;
            if (i == 5) {
                continue;
            }
            System.out.println(i); // 输出：0 1 2 3 4 6 7 8 9
        }

        // do-while
        i = 0;
        do {
            i++;
            if (i == 5) {
                continue;
            }
            System.out.println(i); // 输出：0 1 2 3 4 6 7 8 9
        } while (i < 10);

        // for
        for (int j = 0; j < 10; j++) {
            if (j == 5) {
                continue;
            }
            System.out.println(j); // 输出：0 1 2 3 4 6 7 8 9
        }
    }
    // #endregion continue

    // #region break
    public static void breakStatement() {
        // while
        int i = 0;
        while (i < 10) {
            i++;
            if (i == 5) {
                break;
            }
            System.out.println(i); // 输出：0 1 2 3 4
        }

        // do-while
        i = 0;
        do {
            i++;
            if (i == 5) {
                break;
            }
            System.out.println(i); // 输出：0 1 2 3 4
        } while (i < 10);

        // for
        for (int j = 0; j < 10; j++) {
            if (j == 5) {
                break;
            }
            System.out.println(j); // 输出：0 1 2 3 4
        }

        // switch
        switch (i) {
            case 1:
                System.out.println("1");
                break;
            default:
                System.out.println("default");
                break;
        }
    }
    // #endregion break

    // #region return
    public static void returnStatement() {
        for (int i = 0; i < 10; i++) {
            if (i == 5) {
                return;
            }
            System.out.println(i); // 输出：0 1 2 3 4
        }
        System.out.println("return 后面的代码"); // 不会执行
    }
    // #endregion return
}
