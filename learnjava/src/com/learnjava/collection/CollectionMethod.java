package com.learnjava.collection;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Collection 接口不能实例化，所以这里用 List 的实现类 ArrayList 演示
 * add: 添加单个元素
 * remove：删除指定元素
 * contains: 查找是否包含指定元素
 * size: 获取元素个数
 * isEmpty: 判断是否为空
 * clear: 清空整个集合
 * addAll: 添加多个元素
 * containsAll: 查找多个元素是否存在
 * removeAll: 删除多个元素
 */
public class CollectionMethod {
    @SuppressWarnings({"all"})
    public static void main(String[] args) {
        Collection list = new ArrayList();
        // add 添加单个元素
        list.add("a");
        list.add(10);
        list.add(true);
        System.out.println("list = " + list);

        // remove
        // list.remove(0);
        list.remove(true);
        System.out.println("list = " + list);

        boolean isContains = list.contains("a");
        System.out.println("isContains = " + isContains);

        int size = list.size();
        System.out.println("size = " + size);

        boolean isEmpty = list.isEmpty();
        System.out.println("isEmpty = " + isEmpty);

        list.clear();
        System.out.println("list = " + list);

        ArrayList list2 = new ArrayList();
        list2.add("a");
        list2.add("b");

        list.addAll(list2);
        boolean isContainsAll = list.addAll(list2);
        System.out.println("isContainsAll = " + isContainsAll);

        list.removeAll(list2);
        System.out.println("list = " + list);
        System.out.print("lits.size: " + list.size());

    }
}
