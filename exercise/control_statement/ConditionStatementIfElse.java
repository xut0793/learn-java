package exercise.control_statement;

public class ConditionStatementIfElse {
  // #region conditionStatement
  public static void ifStatement() {
    int a = 10;
    if (a > 0) {
      System.out.println("a 大于 0");
    }

    // if-else 语句
    if (a > 0) {
      System.out.println("a 大于 0");
    } else {
      System.out.println("a 不大于 0");
    }

    // if-elseif 语句
    if (a < 0) {
      System.out.println("a 小于 0");
    } else if (a == 0) {
      System.out.println("a 等于 0");
    } else {
      System.out.println("a 大于 0");
    }
  }
  // #endregion conditionStatement
}
