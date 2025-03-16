import java.util.Scanner;

public class FibonacciSequence {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of terms to generate in Fibonacci sequence: ");
        int terms = scanner.nextInt();

        if (terms <= 0) {
            System.out.println("Please enter a positive integer.");
        } else {
            System.out.println("Fibonacci sequence up to " + terms + " terms:");
            printFibonacci(terms);
        }

        scanner.close(); 
    }

    
    public static void printFibonacci(int terms) {
        int first = 0, second = 1;

        for (int i = 1; i <= terms; i++) {
            System.out.print(first + " ");

            int next = first + second;
            first = second;
            second = next;
        }
        System.out.println(); 
    }
}
