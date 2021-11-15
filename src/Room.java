import java.util.ArrayList;
import java.util.HashMap;

public class Room {

    private int number;
    private int capacity;
    private boolean isInPark;
    public ArrayList<Integer> schedule;
    
    public Room(int n, int c, boolean p) {
        this.number = n;
        this.capacity = c;
        this.isInPark = p;
        this.schedule = new ArrayList<Integer>();
    }
    public Room(int c) {
        this(0, c, false);
    }

    public Room(int n, int c) {
        this(n, c, false);
    }

    public int getCapacity() {
        return this.capacity;
    }

    public int getNumber() {
        return this.number;
    }
    public boolean isInPark() {
        return this.isInPark;
    }
    public void addTime(Integer t) {
        this.schedule.add(t);
    }
    public String toString() {
        return String.format("%d (Capacity: %d) Park: %b", this.number, this.capacity, this.isInPark);
    }
}
