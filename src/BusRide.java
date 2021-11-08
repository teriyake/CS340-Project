public class BusRide {
    private int departTime;
    private int arriveTime;
    private String destination;
    private String depart;
    private BusRide(int t1, int t2, String l1, String l2) {
        this.departTime = t1;
        this.arriveTime = t2;
        this.destination = l1;
        this.depart = l2;
    }

    public int depart() {
        return this.departTime;
    }
    public int arrive() {
        return this.arriveTime;
    }
    public String to() {
        return this.destination;
    }
    public String from() {
        return this.depart;
    }
}
