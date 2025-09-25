package com.learnjava.datatype;

public class DataTypeMain {
    public static void main(String[] args) {
        System.out.println("=============类型声明=================");
        DeclarationType.print();

        System.out.println("=============类型转换=================");
        CastType.implicitCast();
        CastType.explicitCast();

        System.out.println("=============数据运算=================");
        OperateType.arithmeticOperators();
        OperateType.comparisonOperators();
        OperateType.logicalOperators();
        OperateType.ternaryOperators();
        OperateType.bitwiseOperators();
        OperateType.compoundOperators();
        OperateType.operatorPriority();
    }
}
