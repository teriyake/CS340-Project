import java.util.Arrays;

public class Student {
    
    private int name;
    private int[] courses;
    private int[] times;

    public Student(int n, int[] c, int[] t) {
        this.name = n;
        this.courses = c;
        this.times = t;
    }

    public Student(int n) {
        this(n, new int[4], new int[2]);
    }

    public int getName() {
        return this.name;
    }

    public int[] getCourses() {
        return this.courses;
    }

    public boolean addCourse(int c) {
        for (int i = 0; i < 4; i++) {
            if (this.courses[i] == 0) {
                this.courses[i] = c;
                return true;
            }
        }
        return false; 
    }

    // return true if conflict
    public boolean addTime(int t) {
        /*
        for (int i = 0; i < 2; i++) {
            if (this.times[i] == 0) {
                this.times[i] = t;
            }
        }
        return false; 
        */
        if (this.times[0] == 0) {
            this.times[0] = t;
            return false;
        }
        if (this.times[1] == 0) {
            this.times[1] = t;
            if (this.times[0] == this.times[1]) {
                return true;
            }
        }
        return false;
    }

    public boolean removeCourse(int c) {
        for (int i = 0; i < 4; i++) {
            if (this.courses[i] == c) {
                this.courses[i] = 0;
                return true;
            }
        }
        return false;
    }

    public boolean removeTime(int t) {
        for (int i = 0; i < 2; i++) {
            if (this.times[i] == t) {
                this.times[i] = 0;
                return true;
            }
        }
        return false;
    }

    public boolean editCourse(int c1, int c2) {
        for (int i = 0; i < this.courses.length; i++) {
            if (c1 == this.courses[i]) {
                this.courses[i] = c2;
                return true;
            }
        }
        return false;
    }

    public String toString() {
        return this.getName() + "\t" + Arrays.toString(this.courses);
    }
}

