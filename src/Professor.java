<<<<<<< HEAD
public class Professor {

    private int name;
    private int c1;
    private int c2;
    private Course[] courses;

    public Professor(int p, int c1, int c2) {
        this.name = p;
        this.c1 = c1;
        this.c2 = c2;
        this.courses = new Course[2];
    }

    public Professor(int p, int c1) {
        this(p, c1, 0);
    }

    public int getC1() {
        return this.c1;
    }

    public int getC2() {
        return this.c2;
    }

    public int getName() {
        return this.name;
    }

    public Course[] getCourses() {
        return this.courses;
    }

    public void editC1(int newC) {
        this.c1 = newC;
    }

    public void editC2(int newC) {
        this.c2 = newC;
    }

    // return false if prof is unavailable
    public boolean addCourse(Course c) {
        if (this.courses[0] == null) {
            this.courses[0] = c;
            return true;
        } else if (this.courses[1] == null) {
            this.courses[1] = c;
            return true;
        }
        return false;
    }

    public void editCourse(int n, Course c) {
        this.courses[n] = c;
    }

    public String toString() {
        return String.format("Professor: %d\nCourses: %d, %d", this.name, this.c1, this.c2);
    }
}
=======
public class Professor {

    private int name;
    private int c1;
    private int c2;
    private Course[] courses;

    public Professor(int p, int c1, int c2) {
        this.name = p;
        this.c1 = c1;
        this.c2 = c2;
        this.courses = new Course[2];
    }

    public Professor(int p, int c1) {
        this(p, c1, 0);
    }

    public int getC1() {
        return this.c1;
    }

    public int getC2() {
        return this.c2;
    }

    public int getName() {
        return this.name;
    }

    public Course[] getCourses() {
        return this.courses;
    }

    public void editC1(int newC) {
        this.c1 = newC;
    }

    public void editC2(int newC) {
        this.c2 = newC;
    }

    // return false if prof is unavailable
    public boolean addCourse(Course c) {
        if (this.courses[0] == null) {
            this.courses[0] = c;
            return true;
        } else if (this.courses[1] == null) {
            this.courses[1] = c;
            return true;
        }
        return false;
    }

    public void editCourse(int n, Course c) {
        this.courses[n] = c;
    }

    public String toString() {
        return String.format("Professor: %d\nCourses: %d, %d", this.name, this.c1, this.c2);
    }
}
>>>>>>> e388db36ddad8a5307eae84e31464105e8e13523
