import java.io.*;
import java.util.*;

public class FileManager {

    private final String FILE_NAME = "students.csv";

    // Generate Next ID
    private int getNextId() {

        int id = 1;

        try {

            File file = new File(FILE_NAME);

            if (!file.exists()) {
                return 1;
            }

            BufferedReader br = new BufferedReader(new FileReader(file));

            String line;

            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty())
                    continue;

                String[] data = line.split(",");

                id = Integer.parseInt(data[0]) + 1;
            }

            br.close();

        } catch (Exception e) {
            System.out.println(e);
        }

        return id;
    }

    // ================= ADD =================
    public void addStudent(Student student) {

        try {

            BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME, true));

            int id = getNextId();

            bw.write(id + "," +
                    student.getName() + "," +
                    student.getAge() + "," +
                    student.getCourse() + "," +
                    student.getEmail());

            bw.newLine();

            bw.close();

            System.out.println("\nStudent Added Successfully.");

        } catch (Exception e) {

            System.out.println(e);
        }
    }

    // ================= VIEW =================
    public void viewStudents() {

        try {

            File file = new File(FILE_NAME);

            if (!file.exists()) {

                System.out.println("No Records Found.");
                return;
            }

            BufferedReader br = new BufferedReader(new FileReader(file));

            String line;

            while ((line = br.readLine()) != null) {

                String[] data = line.split(",");

                System.out.println("----------------------------");
                System.out.println("ID      : " + data[0]);
                System.out.println("Name    : " + data[1]);
                System.out.println("Age     : " + data[2]);
                System.out.println("Course  : " + data[3]);
                System.out.println("Email   : " + data[4]);
            }

            br.close();

        } catch (Exception e) {

            System.out.println(e);
        }
    }

    // ================= SEARCH =================
    public void searchStudent(int id) {

        boolean found = false;

        try {

            BufferedReader br = new BufferedReader(new FileReader(FILE_NAME));

            String line;

            while ((line = br.readLine()) != null) {

                String[] data = line.split(",");

                if (Integer.parseInt(data[0]) == id) {

                    System.out.println("\nStudent Found");
                    System.out.println("----------------------------");
                    System.out.println("ID      : " + data[0]);
                    System.out.println("Name    : " + data[1]);
                    System.out.println("Age     : " + data[2]);
                    System.out.println("Course  : " + data[3]);
                    System.out.println("Email   : " + data[4]);

                    found = true;
                    break;
                }
            }

            br.close();

            if (!found) {

                System.out.println("Student Not Found.");
            }

        } catch (Exception e) {

            System.out.println(e);
        }
    }

    // ================= UPDATE =================
    public void updateStudent(int id, String name, int age, String course, String email) {

        ArrayList<String> list = new ArrayList<>();

        boolean found = false;

        try {

            BufferedReader br = new BufferedReader(new FileReader(FILE_NAME));

            String line;

            while ((line = br.readLine()) != null) {

                String[] data = line.split(",");

                if (Integer.parseInt(data[0]) == id) {

                    list.add(id + "," + name + "," + age + "," + course + "," + email);

                    found = true;
                } else {

                    list.add(line);
                }
            }

            br.close();

            BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME));

            for (String s : list) {

                bw.write(s);
                bw.newLine();
            }

            bw.close();

            if (found) {

                System.out.println("Student Updated Successfully.");
            } else {

                System.out.println("Student Not Found.");
            }

        } catch (Exception e) {

            System.out.println(e);
        }
    }

    // ================= DELETE =================
    public void deleteStudent(int id) {

        ArrayList<String> list = new ArrayList<>();

        boolean found = false;

        try {

            BufferedReader br = new BufferedReader(new FileReader(FILE_NAME));

            String line;

            while ((line = br.readLine()) != null) {

                String[] data = line.split(",");

                if (Integer.parseInt(data[0]) == id) {

                    found = true;
                    continue;
                }

                list.add(line);
            }

            br.close();

            BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME));

            for (String s : list) {

                bw.write(s);
                bw.newLine();
            }

            bw.close();

            if (found) {

                System.out.println("Student Deleted Successfully.");
            } else {

                System.out.println("Student Not Found.");
            }

        } catch (Exception e) {

            System.out.println(e);
        }
    }
}