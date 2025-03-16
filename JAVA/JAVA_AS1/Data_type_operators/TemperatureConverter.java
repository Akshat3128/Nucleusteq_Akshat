import java.util.Scanner;

public class TemperatureConverter {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Temperature Conversion Menu:");
        System.out.println("1. Celsius to Fahrenheit");
        System.out.println("2. Fahrenheit to Celsius");
        System.out.print("Enter your choice (1/2): ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                System.out.print("Enter temperature in Celsius: ");
                double celsius = scanner.nextDouble();
                double fahrenheit = (celsius * 9 / 5) + 32;
                System.out.printf("%.2f°C is equal to %.2f°F\n", celsius, fahrenheit);
                break;

            case 2:
                System.out.print("Enter temperature in Fahrenheit: ");
                fahrenheit = scanner.nextDouble();
                celsius = (fahrenheit - 32) * 5 / 9;
                System.out.printf("%.2f°F is equal to %.2f°C\n", fahrenheit, celsius);
                break;

            default:
                System.out.println("Invalid choice! Please enter 1 or 2.");
        }

        scanner.close();
    }
}
