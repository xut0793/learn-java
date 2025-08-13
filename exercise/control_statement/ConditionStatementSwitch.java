package exercise.control_statement;

public class ConditionStatementSwitch {
  // #region switchDefault
  public static void switchDefault() {
    int dayOfWeek = 3;
    switch (dayOfWeek) {
      case 1:
        System.out.println("星期一");
        break;
      case 2:
        System.out.println("星期二");
        break;
      case 3:
        System.out.println("星期三");
        break;
      case 4:
        System.out.println("星期四");
        break;
      case 5: {
        // 可以在 case 中使用代码块
        System.out.println("星期五");
        break;
      }
      case 6: {
        System.out.println("星期六");
        break;
      }
      case 7: {
        System.out.println("星期日");
        break;
      }
      default: {
        System.out.println("非 1-7 之间的数字");
        break;
      }
    }
  }
  // #endregion switchDefault

  // switch case 穿透现象
  // #region switchPenetrate
  public static void switchPenetrate() {
    int dayOfWeek = 3;
    switch (dayOfWeek) {
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
        System.out.println("工作日");
        break;
      case 6:
      case 7: {
        System.out.println("休息日");
        break;
      }
      default: {
        System.out.println("非 1-7 之间的数字");
        break;
      }
    }
  }
  // #endregion switchPenetrate

  // #region switchArrow
  public static void switchArrow() {
    int dayOfWeek = 3;
    switch (dayOfWeek) {
      case 1 -> System.out.println("星期一");
      case 2 -> System.out.println("星期二");
      case 3 -> System.out.println("星期三");
      case 4 -> System.out.println("星期四");
      case 5 -> System.out.println("星期五");
      case 6 -> System.out.println("星期六");
      case 7 -> System.out.println("星期日");
      default -> System.out.println("非 1-7 之间的数字");
    }
  }
  // #endregion switchArrow

  // #region switchArrowMerge
  public static void switchArrowMerge() {
    int dayOfWeek = 3;
    switch (dayOfWeek) {
      case 1, 2, 3 -> System.out.println("工作日");
      case 4, 5 -> System.out.println("工作日");
      case 6, 7 -> System.out.println("休息日");
      default -> System.out.println("非 1-7 之间的数字");
    }
  }
  // #endregion switchArrowMerge

  // #region switchYieldDefault
  public static void switchYieldDefault() {
    int dayOfWeek = 3;
    String dayText = switch (dayOfWeek) {
      case 1: {
        // 可以在 case 中使用代码块
        yield "星期一";
      }
      case 2: {
        yield "星期二";
      }
      case 3: {
        yield "星期三";
      }
      case 4: {
        yield "星期四";
      }
      case 5: {
        yield "星期五";
      }
      case 6: {
        yield "星期六";
      }
      case 7: {
        yield "星期日";
      }
      default: {
        yield "非 1-7 之间的数字";
      }
    };
    System.out.println(dayText);
  }
  // #endregion switchYieldDefault

  // #region switchYieldArrow
  public static void switchYieldArrow() {
    int dayOfWeek = 3;
    String dayText = switch (dayOfWeek) {
      case 1 -> "星期一";
      case 2 -> "星期二";
      case 3 -> "星期三";
      case 4 -> "星期四";
      case 5 -> "星期五";
      case 6 -> "星期六";
      case 7 -> "星期日";
      default -> "非 1-7 之间的数字";
    };
    System.out.println(dayText);
  }
  // #endregion switchYieldArrow

  // #region switchYieldArrowBlock
  public static void switchYieldArrowBlock() {
    int dayOfWeek = 3;
    String dayText = switch (dayOfWeek) {
      case 1 -> {
        // 可以在 case 中使用代码块
        yield "星期一";
      }
      case 2 -> {
        yield "星期二";
      }
      case 3 -> {
        yield "星期三";
      }
      case 4 -> {
        yield "星期四";
      }
      case 5 -> {
        yield "星期五";
      }
      case 6 -> {
        yield "星期六";
      }
      case 7 -> {
        yield "星期日";
      }
      default -> {
        yield "非 1-7 之间的数字";
      }
    };
    System.out.println(dayText);
  }
  // #endregion switchYieldArrowBlock

}
