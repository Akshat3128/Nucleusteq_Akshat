import java.util.Scanner;

public class PatternPrinter {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Choose a pattern to print:");
        System.out.println("1. Right-Angled Triangle");
        System.out.println("2. Square");
        System.out.println("3. Pyramid");
        System.out.print("Enter your choice (1/2/3): ");
        int choice = scanner.nextInt();

        System.out.print("Enter the number of rows/size: ");
        int size = scanner.nextInt();

        switch (choice) {
            case 1:
                printTriangle(size);
                break;

            case 2:
                printSquare(size);
                break;

            case 3:
                printPyramid(size);
                break;

            default:
                System.out.println("Invalid choice! Please enter 1, 2, or 3.");
        }

        scanner.close();
    }

    // Method to print Right-Angled Triangle
    public static void printTriangle(int rows) {
        System.out.println("Right-Angled Triangle:");
        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= i; j++) {
                System.out.print("* ");
            }
            System.out.println();
        }
    }

    // Method to print Square
    public static void printSquare(int size) {
        System.out.println("Square:");
        for (int i = 1; i <= size; i++) {
            for (int j = 1; j <= size; j++) {
                System.out.print("* ");
            }
            System.out.println();
        }
    }

    // Method to print Pyramid
    public static void printPyramid(int rows) {
        System.out.println("Pyramid:");
        for (int i = 1; i <= rows; i++) {
            for (int j = i; j < rows; j++) {
                System.out.print(" ");
            }
            for (int j = 1; j <= (2 * i - 1); j++) {
                System.out.print("*");
            }
            System.out.println();
        }
    }
}
