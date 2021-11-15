import java.util.HashMap;

public class Constraints {
    
    private int timeSlots;
    private Course[] courses;
    private int[] labs;
    private Room[] rooms;
    private Professor[] profs;
    private Professor[] pcPairs;

    public Constraints(int t, int c, int r, int p) {
        this.timeSlots = t;
        this.courses = new Course[c + 1];
        this.rooms = new Room[r + 1];
        this.profs = new Professor[p + 1];
        this.labs = new int[c + 1];
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

    public void addLabs(int n) {
        this.labs = new int[n + 1];
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

    public int[] getLabs() {
        return this.labs;
    }

    public void addRoom(int r, int c) {
        Room newRoom = new Room(r, c);
        this.rooms[r] = newRoom;
    }

    public void addRoomP(int r, int c, boolean p) {
        Room newRoom = new Room(r, c,p);
        this.rooms[r] = newRoom;
    }

    public void addProf(int n, Professor p) {
        this.profs[n] = p;
        this.pcPairs[p.getC1()] = p;
        // this.pcPairs[p.getC2()] = p;;
    }

    public void editProf(int p, int c) {
        // this.pcPairs[profs[p].getC2()] = null;
        this.profs[p].editC2(c);
        this.pcPairs[c] = profs[p];
    }

    public void addLab(int c) {
        this.labs[c] = 1;
    }

    public void rmNulls(int n) {
        Course[] newC = new Course[n];
        System.arraycopy(courses, 0, newC, 0, n);
        this.courses = newC;
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

        /**
     * generates hashmap with time slots enumerated so that we can see start & end times
     * this accomodates extension of taking overlappung slots into account
     * @param t1 number of timeslots that meet in the 1st rotation pattern (TTH)
     * @param t2 number of timeslots that meet in the 2nd rotation pattern (MW)
     * @param t3 number of timeslots that meet in the 3rd rotation pattern (MWF)
     * @param t4 number of night blocks
     */
    public HashMap<Integer, TimeSlot> getSlots(int t1, int t2, int t3, int t4){
        HashMap<Integer, TimeSlot> times = new HashMap<Integer, TimeSlot>();
        double start = 8;
        // 1st rotation
        for (int i = 1; i <= t1; i++) {
            times.put(i, new TimeSlot(start, start+1, 1));
            start += 1.5;
        }
        start = 8;
        // 2nd rotation
        for (int j = (t1 + 1); j <= (t1 + t2); j++) {
            times.put(j, new TimeSlot(start, start+1, 2));
            start += 1.5;
        }
        // 3rd rotation
        start = 8;
        for (int k = (t1 + t2 + 1); k <= (t1 + t2 + t3); k++) {
            times.put(k, new TimeSlot(start, start+1, 3));
            start += 1;
        }
        // these are the night blocks
        start = 19;
        for (int l = (t1 + t2 + t3 + 1); l <= (t1 + t2 + t3 + t4); l++) {
            times.put(l, new TimeSlot(start, start+1, 1));
            start += 3;
        }
        return times;
    }
}
