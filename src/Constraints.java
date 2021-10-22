<<<<<<< HEAD
public class Constraints {
    
    private int timeSlots;
    private Course[] courses;
    private Room[] rooms;
    private Professor[] profs;
    private Professor[] pcPairs;

    public Constraints(int t, int c, int r, int p) {
        this.timeSlots = t;
        this.courses = new Course[c + 1];
        this.rooms = new Room[r + 1];
        this.profs = new Professor[p + 1];
    }   

    public Constraints(int t) {
        this(t, 0, 0, 0);
    }

    public Constraints(int t, int r) {
        this(t, 0, r, 0);
    }

    public void addProfCons(int n) {
        this.profs = new Professor[n + 1];
        this.pcPairs = new Professor[2 * n + 1];
    }

    public void addCourses(int n) {
        this.courses = new Course[n + 1];
    }

    public int getTimeSlots() {
        return this.timeSlots;
    }

    public Course[] getCourses() {
        return this.courses;
    }

    public Room[] getRooms() {
        return this.rooms;
    }

    public Professor[] getProfs() {
        return this.profs;
    }

    public Professor[] getPCPairs() {
        return this.pcPairs;
    }

    public void addRoom(int r, int c) {
        Room newRoom = new Room(r, c);
        this.rooms[r] = newRoom;
    }

    public void addProf(int n, Professor p) {
        this.profs[n] = p;
        this.pcPairs[p.getC1()] = p;
        this.pcPairs[p.getC2()] = p;
    }

    public void editProf(int p, int c) {
        this.pcPairs[profs[p].getC2()] = null;
        this.profs[p].editC2(c);
        this.pcPairs[c] = profs[p];
    }

    public int getTotalTimes() {
        return this.timeSlots;
    }

    public int getTotalCourses() {
        return this.courses.length;
    }

    public int getTotalRooms() {
        return this.rooms.length;
    }

    public int getTotalProfs() {
        return this.profs.length;
    }
}
=======
public class Constraints {
    
    private int timeSlots;
    private Course[] courses;
    private Room[] rooms;
    private Professor[] profs;
    private Professor[] pcPairs;

    public Constraints(int t, int c, int r, int p) {
        this.timeSlots = t;
        this.courses = new Course[c + 1];
        this.rooms = new Room[r + 1];
        this.profs = new Professor[p + 1];
    }   

    public Constraints(int t) {
        this(t, 0, 0, 0);
    }

    public Constraints(int t, int r) {
        this(t, 0, r, 0);
    }

    public void addProfCons(int n) {
        this.profs = new Professor[n + 1];
        this.pcPairs = new Professor[2 * n + 1];
    }

    public void addCourses(int n) {
        this.courses = new Course[n + 1];
    }

    public int getTimeSlots() {
        return this.timeSlots;
    }

    public Course[] getCourses() {
        return this.courses;
    }

    public Room[] getRooms() {
        return this.rooms;
    }

    public Professor[] getProfs() {
        return this.profs;
    }

    public Professor[] getPCPairs() {
        return this.pcPairs;
    }

    public void addRoom(int r, int c) {
        Room newRoom = new Room(r, c);
        this.rooms[r] = newRoom;
    }

    public void addProf(int n, Professor p) {
        this.profs[n] = p;
        this.pcPairs[p.getC1()] = p;
        this.pcPairs[p.getC2()] = p;
    }

    public void editProf(int p, int c) {
        this.pcPairs[profs[p].getC2()] = null;
        this.profs[p].editC2(c);
        this.pcPairs[c] = profs[p];
    }

    public int getTotalTimes() {
        return this.timeSlots;
    }

    public int getTotalCourses() {
        return this.courses.length;
    }

    public int getTotalRooms() {
        return this.rooms.length;
    }

    public int getTotalProfs() {
        return this.profs.length;
    }
}
>>>>>>> e388db36ddad8a5307eae84e31464105e8e13523
