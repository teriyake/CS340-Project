import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class TimeSlot {
    double start;
    double end;
    /**
     * rotation options for meeting days: 1- MWF, 2- TTH, 3-MW
     */
    int days;

    public TimeSlot(double startT, double endT, int meetingDays) {
        this.start = startT;
        this.end = endT;
        this.days = meetingDays;
    }
    public double getStart() {
        return this.start;
    }
    public double getEnd() {
        return this.end;
    }
    public double getDays() {
        return this.days;
    }
    public boolean overlaps(TimeSlot t) {
        if (t.getDays() == this.getDays()) {
            if (t.getStart() == this.getStart()) {
                return true;
            }
        } else if (((t.getDays() == 3) && (this.getDays() == 2)) || ((this.getDays() == 3) && (t.getDays() == 2))) {
            if (((t.getStart() >= this.getStart()) && (t.getStart() <= this.getEnd())) || 
            ((this.getStart() >= t.getStart()) && (this.getStart() <= t.getEnd()))) {
                return true;
            }
        }
        return false;
    } 

    public static HashMap<Integer, HashSet<Integer>> overlapping(HashMap<Integer, TimeSlot> t) {
        HashMap<Integer, HashSet<Integer>> overlaps = new HashMap<Integer, HashSet<Integer>>();
        for (Map.Entry<Integer, TimeSlot> t1 : t.entrySet()) {
            for (Map.Entry<Integer, TimeSlot> t2 : t.entrySet()) {
                if (t1.getValue().overlaps(t2.getValue())) {
                    Integer k1 = t1.getKey();
                    Integer k2 = t2.getKey();
                    if (!overlaps.containsKey(t1.getKey())) {
                        overlaps.put(k1, new HashSet<Integer>());
                    }
                    HashSet<Integer> v1 = overlaps.get(k1);
                    v1.add(k2);
                    overlaps.put(k1, v1);
                }
            }
        }
        return overlaps;
    }

}
