public class Room {

    private int number;
    private int capacity;
    
    public Room(int n, int c) {
        this.number = n;
        this.capacity = c;
    }

    public Room(int c) {
        this(0, c);
    }

    public int getCapacity() {
        return this.capacity;
    }

    public int getNumber() {
        return this.number;
    }

    public String toString() {
        return String.format("%d (Capacity: %d)", this.number, this.capacity);
    }
}
