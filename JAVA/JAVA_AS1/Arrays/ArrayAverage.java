import java.util.Scanner;

public class ArrayAverage {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of elements in the array: ");
        int n = scanner.nextInt();

        int[] arr = new int[n];
        int sum = 0;

        System.out.println("Enter the elements of the array:");
        for (int i = 0; i < n; i++) {
            arr[i] = scanner.nextInt();
            sum += arr[i];
        }

        double average = (double) sum / n;
        System.out.printf("The average of the array elements is: %.2f\n", average);

        scanner.close();
    }
}
