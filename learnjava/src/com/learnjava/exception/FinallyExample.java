package com.learnjava.exception;

public class FinallyExample {
  /**
   * 这个函数的返回值是0，而不是2。finally 语句中不能改变返回值
   * 实际执行过程是：在执行到try内的return
   * ret；语句前，会先将返回值ret保存在一个临时变量中，然后才执行finally语句，最后try再返回那个临时变量，finally中对ret的修改不会被返回。
   * 
   * @return
   */
  public static int finallyModifyVariable() {
    int ret = 0;
    try {
      return ret;
    } finally {
      ret = 2;
    }
  }

  /**
   * 这个函数的返回值是2，而不是0。
   * finally中有return 会覆盖try和catch内的返回值。
   */
  public static int finallyReturn() {
    int ret = 0;
    try {
      return ret;
    } finally {
      ret = 2;
    }
  }

  /**
   * 5/0 会触发ArithmeticException
   * 这个函数如果 finally 中使用 return，那么最终返回值是2，而不是抛出异常 ArithmeticException。
   * 如果 finally中抛出新异常，则会覆盖try和catch内的异常，新异常向上传递。
   */
  public static int finallyThrow() {
    int ret = 0;
    try {
      int a = 5 / 0;
      return ret;
    } catch (ArithmeticException e) {
      return 1;
    } finally {
      // return 2;
      throw new RuntimeException("finally throw exception");
    }
  }

  public static void main(String[] args) {
    System.out.println("finallyModifyVariable() 返回值: " + finallyModifyVariable()); // 0
    System.out.println("finallyReturn() 返回值: " + finallyReturn()); // 2
    try {
      System.out.println("finallyThrow() 返回值: " + finallyThrow());
    } catch (RuntimeException e) {
      System.out.println("捕获到异常: " + e.getMessage());
    }
  }
}
