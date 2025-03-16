import java.util.Scanner;

public class demonstration {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Creating a Student object
        Student student = new Student("Alice", 101, 87.5);
        System.out.println("Student Details:");
        student.displayInfo();

        // Creating a GraduateStudent object (Inheritance)
        GraduateStudent gradStudent = new GraduateStudent("Bob", 102, 92.3, "Computer Science");
        System.out.println("\nGraduate Student Details:");
        gradStudent.displayInfo();

        // Demonstrating Method Overloading (Polymorphism)
        System.out.println("\nGraduate Student Details (With Marks):");
        gradStudent.displayInfo(true);

        // Demonstrating Encapsulation by modifying marks using setter method
        student.setMarks(90.0);
        System.out.println("\nModified Student Marks:");
        student.displayInfo();

        scanner.close();
    }
}
