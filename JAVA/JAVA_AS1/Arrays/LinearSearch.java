import java.util.Scanner;

public class LinearSearch {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of elements in the array: ");
        int n = scanner.nextInt();

        int[] arr = new int[n];

        System.out.println("Enter the elements of the array:");
        for (int i = 0; i < n; i++) {
            arr[i] = scanner.nextInt();
        }

        System.out.print("Enter the element to search: ");
        int key = scanner.nextInt();

        boolean found = false;
        for (int i = 0; i < n; i++) {
            if (arr[i] == key) {
                System.out.println("Element " + key + " found at index: " + i);
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Element " + key + " not found in the array.");
        }
        
        scanner.close();
    }
}
