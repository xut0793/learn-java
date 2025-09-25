package com.learnjava.statement;

public class StatementMain {
    public static void main(String[] args) {
        System.out.println("=============条件语句=================");
        ConditionStatement.ifElse();
        ConditionStatement.switchDefault();
        ConditionStatement.switchPenetrate();
        ConditionStatement.switchArrow();
        ConditionStatement.switchArrowMerge();
        ConditionStatement.switchYieldDefault();
        ConditionStatement.switchYieldArrow();
        ConditionStatement.switchYieldArrowBlock();
        // 循环结构
        System.out.println("=======================循环结构=======================");
        LoopStatement.whileLoop();
        LoopStatement.doWhileLoop();
        LoopStatement.forLoop();
        LoopStatement.foreachLoop();
        // 跳转结构
        System.out.println("=======================跳转结构=======================");
        JumpStatement.breakStatement();
        JumpStatement.continueStatement();
        JumpStatement.returnStatement();
    }
}
