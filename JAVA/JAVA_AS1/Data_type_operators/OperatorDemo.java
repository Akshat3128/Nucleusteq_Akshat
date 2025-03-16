import java.util.Scanner;

public class OperatorDemo {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter first number (a): ");
        int a = scanner.nextInt();

        System.out.print("Enter second number (b): ");
        int b = scanner.nextInt();

        // Arithmetic Operators
        System.out.println("\n--- Arithmetic Operators ---");
        System.out.println("a + b = " + (a + b));
        System.out.println("a - b = " + (a - b));
        System.out.println("a * b = " + (a * b));
        System.out.println("a / b = " + (a / b));
        System.out.println("a % b = " + (a % b));

        // Relational Operators
        System.out.println("\n--- Relational Operators ---");
        System.out.println("a == b : " + (a == b));
        System.out.println("a != b : " + (a != b));
        System.out.println("a > b  : " + (a > b));
        System.out.println("a < b  : " + (a < b));
        System.out.println("a >= b : " + (a >= b));
        System.out.println("a <= b : " + (a <= b));

        // Logical Operators
        System.out.println("\n--- Logical Operators ---");
        boolean condition1 = (a > 0);
        boolean condition2 = (b > 0);

        System.out.println("Condition1 (a > 0): " + condition1);
        System.out.println("Condition2 (b > 0): " + condition2);

        System.out.println("condition1 AND condition2 : " + (condition1 && condition2));
        System.out.println("condition1 OR condition2  : " + (condition1 || condition2));
        System.out.println("NOT condition1           : " + (!condition1));

        scanner.close();
    }
}
