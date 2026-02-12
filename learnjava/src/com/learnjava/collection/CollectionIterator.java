package com.learnjava.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class CollectionIterator {
    @SuppressWarnings("all")
    public static void main(String[] args) {
        Collection list = new ArrayList();
        list.add(new Book("三国演义", "罗贯中", 10.1));
        list.add(new Book("红楼梦", "曹雪芹", 34.6));
        list.add(new Book("西游记", "吴承恩", 23.5));
        list.add(new Book("水浒传", "施耐庵", 23.67));

        // System.out.println("list = " + list);

        // 快捷生成语句 list.iterator().var
        final Iterator iterator = list.iterator();
        // Iterator iterator = list.iterator();

        // 循环遍历迭代器的快捷键 itit
        // while (iterator.hasNext()) {
        //     // Object obj = iterator.next();
        //     Book book = (Book) iterator.next();
        //         System.out.println(book);
        // }

        for (Object book : list) {
            System.out.println("book = " + book);
        }
    }
}

class Book {
    private String title;
    private String author;
    private double price;

    public Book(String title, String author, double price) {
        this.title = title;
        this.author = author;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", price=" + price +
                '}';
    }
}
