public class Main {
    public static void main(String[] args) {
        System.out.print("Hello and welcome!");
        Person p1 = new Person();
        p1.name = "Tom";
        p1.age = 18;
        p1.hello();
        int sum = p1.sum(1,2);
        System.out.println("sum = " + sum);
    }
}