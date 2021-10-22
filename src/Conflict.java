import java.util.Arrays;

public class Conflict {
    private int num;
    private Course course;
    private int[] students;

    public Conflict(Course c) {
        this.num = 1;
        this.course = c;
        this.students = new int[c.getEnrollment() * 2];
    }

    public Course getCourse() {
        return this.course;
    }

    public int[] getStudents() {
        return this.students;
    }

    public void addConf(int s) {
        this.students[num] = s;
        num++;
    }

    public String toString() {
        return String.format("%d\n", this.course.getID());
    }
}
