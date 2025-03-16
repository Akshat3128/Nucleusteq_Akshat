public class SumOfEvenNumbers {

    public static void main(String[] args) {
        int sum = 0;
        int num = 1;

        while (num <= 10) {
            if (num % 2 == 0) {
                sum += num;
            }
            num++;
        }

        System.out.println("Sum of even numbers from 1 to 10 is: " + sum);
    }
}
