import java.util.Scanner;

public class StudentGradeCalculator {

    public static void main(String args[]) {
        Scanner s = new Scanner(System.in);
        System.out.print("Enter number of subjects: ");
        int numofsubjects = s.nextInt();
        int[] marks = new int[numofsubjects];
        int total = 0;

        for (int i = 0; i < numofsubjects; i++) {
            System.out.print("\nEnter marks of subject " + (i + 1) + ": ");
            marks[i] = s.nextInt();
            if (marks[i] > 100 || marks[i] < 0) {
                System.err.print("Error: Enter marks out of 100.\n");
                i--;
            } else {
                total = total + marks[i];
            }
        }

        float average = (float) total / numofsubjects;

        char grade;
        if (average >= 90) {
            grade = 'A';
        } else if (average >= 80) {
            grade = 'B';
        } else if (average >= 70) {
            grade = 'C';
        } else if (average >= 60) {
            grade = 'D';
        } else {
            grade = 'F';
        }

        System.out.println("\nTotal marks: " + total + " out of " + numofsubjects * 100);
        System.out.println("Average marks: " + average);
        System.out.println("Grade: " + grade);
        s.close();
    }
}
