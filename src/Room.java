public class Room {

    private int number;
    private int capacity;
    private boolean isInPark;
    
    public Room(int n, int c, boolean p) {
        this.number = n;
        this.capacity = c;
        this.isInPark = p;
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

    public String toString() {
        return String.format("%d (Capacity: %d) Park: %b", this.number, this.capacity, this.isInPark);
    }
}
