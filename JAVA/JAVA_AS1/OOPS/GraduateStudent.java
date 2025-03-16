class GraduateStudent extends Student {
    private String specialization;

    // Constructor
    public GraduateStudent(String name, int rollNumber, double marks, String specialization) {
        super(name, rollNumber, marks);  // Calling parent class constructor
        this.specialization = specialization;
    }

    // Overriding displayInfo Method (Polymorphism)
    @Override
    public void displayInfo() {
        super.displayInfo();  // Calling parent class method
        System.out.println("Specialization: " + specialization);
    }

    // Overloaded Method (Polymorphism)
    public void displayInfo(boolean showMarks) {
        System.out.println("Name: " + getName());
        System.out.println("Roll Number: " + getRollNumber());
        if (showMarks) {
            System.out.println("Marks: " + getMarks());
        }
        System.out.println("Specialization: " + specialization);
    }
}
