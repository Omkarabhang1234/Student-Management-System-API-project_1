import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

       Scanner sc = new Scanner(System.in);
       FileManager file = new FileManager();
        while (true) {

            System.out.println("\n====================================");
            System.out.println("   STUDENT MANAGEMENT SYSTEM");
            System.out.println("====================================");
            System.out.println("1. Add Student");
            System.out.println("2. View Students");
            System.out.println("3. Search Student");
            System.out.println("4. Update Student");
            System.out.println("5. Delete Student");
            System.out.println("6. Exit");
            System.out.print("Enter Your Choice: ");

            int choice = sc.nextInt();
            sc.nextLine(); // Consume newline

            switch (choice) {

                // ---------------- ADD STUDENT ----------------
                case 1:

                    System.out.println("\n===== Add Student =====");

                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();

                    System.out.print("Enter Age: ");
                    int age = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter Course: ");
                    String course = sc.nextLine();

                    System.out.print("Enter Email: ");
                    String email = sc.nextLine();

                    Student student = new Student(name, age, course, email);

                    file.addStudent(student);

                    break;

                // ---------------- VIEW STUDENTS ----------------
                case 2:

                    System.out.println("\n===== Student List =====");

                    file.viewStudents();

                    break; 

                // ---------------- SEARCH STUDENT ----------------
                case 3:

                    System.out.print("\nEnter Student ID: ");

                    int searchId = sc.nextInt();

                    file.searchStudent(searchId);

                    break;

                // ---------------- UPDATE STUDENT ----------------
                case 4:

                    System.out.print("\nEnter Student ID to Update: ");
                    int updateId = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter New Name: ");
                    String newName = sc.nextLine();

                    System.out.print("Enter New Age: ");
                    int newAge = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter New Course: ");
                    String newCourse = sc.nextLine();

                    System.out.print("Enter New Email: ");
                    String newEmail = sc.nextLine();

                    file.updateStudent(updateId, newName, newAge, newCourse, newEmail);
                    break;

                // ---------------- DELETE STUDENT ----------------
                case 5:

                    System.out.print("\nEnter Student ID to Delete: ");

                    int deleteId = sc.nextInt();

                    file.deleteStudent(deleteId);
                    break;

                // ---------------- EXIT ----------------
                case 6:

                    System.out.println("\n====================================");
                    System.out.println(" Thank You for Using the Project");
                    System.out.println(" Project Developed by Omkar");
                    System.out.println("====================================");

                    sc.close();
                    System.exit(0);

                // ---------------- INVALID ----------------
                default:

                    System.out.println("\nInvalid Choice! Please Try Again.");
            }
        }
    }
}