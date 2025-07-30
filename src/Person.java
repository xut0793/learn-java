public class Person {
    String name;
    int age;

    enum gender {
        Male,
        Female,
    }

    void hello() {
        System.out.println("I am " + name + ", i am " + age + "year old, i am " + gender.Male);
    }
    int sum(int a, int b) {
        return a + b;
    }
}
