import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileRead {
    public static void main(String[] args) {
        String filePath = "example.txt";  // Make sure the file is present in the same directory

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file: " + e.getMessage());
        }
    }
}
