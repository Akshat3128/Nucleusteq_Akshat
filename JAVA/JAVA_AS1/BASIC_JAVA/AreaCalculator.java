import java.util.Scanner;

public class AreaCalculator {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Area Calculator");
        System.out.println("Choose a shape to calculate area:");
        System.out.println("1. Circle");
        System.out.println("2. Rectangle");
        System.out.println("3. Triangle");

        System.out.print("Enter your choice (1/2/3): ");
        int choice = scanner.nextInt();

        double area = 0;

        switch (choice) {
            case 1: // Circle
                System.out.print("Enter the radius of the circle: ");
                double radius = scanner.nextDouble();
                area = Math.PI * radius * radius;
                System.out.println("Area of the Circle: " + area);
                break;

            case 2: // Rectangle
                System.out.print("Enter the length of the rectangle: ");
                double length = scanner.nextDouble();
                System.out.print("Enter the width of the rectangle: ");
                double width = scanner.nextDouble();
                area = length * width;
                System.out.println("Area of the Rectangle: " + area);
                break;

            case 3: // Triangle
                System.out.print("Enter the base of the triangle: ");
                double base = scanner.nextDouble();
                System.out.print("Enter the height of the triangle: ");
                double height = scanner.nextDouble();
                area = 0.5 * base * height;
                System.out.println("Area of the Triangle: " + area);
                break;

            default:
                System.out.println("Invalid choice! Please enter 1, 2, or 3.");
        }

        scanner.close();
    }
}
