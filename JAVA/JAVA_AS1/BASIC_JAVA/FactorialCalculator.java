import java.util.Scanner;

public class FactorialCalculator {

    
    public static long factorial(int n) {
        if (n == 0 || n == 1) {
            return 1;  
        }
        return n * factorial(n - 1);  // Recursive call
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Taking user input
        System.out.print("Enter a positive integer: ");
        int number = scanner.nextInt();

        if (number < 0) {
            System.out.println("Factorial is not defined for negative numbers.");
        } else {
            long result = factorial(number);
            System.out.println("Factorial of " + number + " is: " + result);
        }

        scanner.close(); 
    }
}
