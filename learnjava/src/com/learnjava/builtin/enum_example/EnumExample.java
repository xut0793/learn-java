package com.learnjava.builtin.enum_example;

import java.util.Arrays;

public class EnumExample {
    public static void main(String[] args) {
        enumBasic();
    }

    // #region enumBasic
    /**
     * 枚举的基本用法
     */
    public static void enumBasic() {
        enum Size {
            Small, Medium, Large
        }
        Size size = Size.Medium;
        System.out.println("size: " + size);

        // 继承的实例方法
        System.out.println("Size name: " + size.name()); // Medium
        System.out.println("Size ordinal: " + size.ordinal()); // 1

        // Enum 类的静态方法
        System.out.println("Size valueOf: " + Size.valueOf("Medium")); // Medium
        System.out.println("Size values: " + Arrays.toString(Size.values())); // [Small, Medium, Large]

        // 枚举的比较
        Size s1 = Size.Small;
        Size s2 = Size.Medium;
        boolean isEqual = s1 == s2; // false
        boolean isDifferent = s1 != s2; // true

        // 也可以使用 equals 方法
        boolean isEqual2 = s1.equals(s2); // true

        System.out.println("isEqual: " + isEqual);
        System.out.println("isDifferent: " + isDifferent);
        System.out.println("isEqual2: " + isEqual2);

    }
    // #endregion enumBasic

    // #region enumExtend
    /**
     * 枚举的扩展用法
     */
    public static void enumExtend() {
        enum Size {
            SMALL("S", "小号"),
            MEDIUM("M", "中号"),
            LARGE("L", "大号");

            private String abbr;
            private String title;

            private Size(String abbr, String title) {
                this.abbr = abbr;
                this.title = title;
            }

            public String getAbbr() {
                return abbr;
            }

            public String getTitle() {
                return title;
            }

            public static Size getSizeByAbbr(String abbr) {
                for (Size size : Size.values()) {
                    if (size.getAbbr().equals(abbr)) {
                        return size;
                    }
                }
                return null;
            }
        }

        // 使用
        Size s = Size.SMALL;
        System.out.println("s: " + s);
        System.out.println("s.getAbbr(): " + s.getAbbr()); // S
        System.out.println("s.getTitle(): " + s.getTitle()); // 小号

        // 按 abbr 获取枚举实例
        Size s2 = Size.getSizeByAbbr("M");
        System.out.println("s2: " + s2); // MEDIUM
    }
    // #endregion enumExtend

    // #region enumExtendAbstract
    /**
     * 枚举的扩展用法：抽象方法
     */
    public static void enumEXtendAbstract() {
        enum Operation {
            ADD {
                @Override
                public double calculate(double a, double b) {
                    return a + b;
                }
            },
            SUBTRACT {
                @Override
                public double calculate(double a, double b) {
                    return a - b;
                }
            },
            MULTIPLY {
                @Override
                public double calculate(double a, double b) {
                    return a * b;
                }
            },
            DIVIDE {
                @Override
                public double calculate(double a, double b) {
                    if (b != 0) {
                        return a / b;
                    }
                    throw new IllegalArgumentException("除数不能为零");
                }
            };

            public abstract double calculate(double a, double b);
        }

        // 使用
        double result = Operation.ADD.calculate(5, 3);
        System.out.println("result: " + result); // 8.0

        result = Operation.SUBTRACT.calculate(5, 3);
        System.out.println("result: " + result); // 2.0

        result = Operation.MULTIPLY.calculate(5, 3);
        System.out.println("result: " + result); // 15.0

        result = Operation.DIVIDE.calculate(5, 3);
        System.out.println("result: " + result); // 1.6666666666666667
    }

    // #endregion enumExtendAbstract

    // #region enumExtendInterface
    /**
     * 枚举的扩展用法：实现接口
     */
    public interface Describable {
        String getDescription();
    }

    public static void enumExtendInterface() {
        // 枚举实现接口
        enum Priority implements Describable {
            HIGH("高优先级"),
            MEDIUM("中优先级"),
            LOW("低优先级");

            private final String description;

            private Priority(String description) {
                this.description = description;
            }

            @Override
            public String getDescription() {
                return description;
            }
        }

        // 使用
        Priority p = Priority.HIGH;
        System.out.println("p: " + p); // HIGH
        System.out.println("p.getDescription(): " + p.getDescription()); // 高优先级
    }
    // #endregion enumExtendInterface

    // #region enumSwitch
    /**
     * 枚举与 switch 语句配合使用
     */
    public static void enumSwitch() {
        enum Season {
            SPRING, SUMMER, AUTUMN, WINTER
        }

        Season season = Season.SUMMER;

        switch (season) {
            case SPRING:
                System.out.println("春暖花开");
                break;
            case SUMMER:
                System.out.println("夏日炎炎");
                break;
            case AUTUMN:
                System.out.println("秋高气爽");
                break;
            case WINTER:
                System.out.println("雪花纷飞");
                break;
        }
    }
    // #endregion enumSwitch
}
