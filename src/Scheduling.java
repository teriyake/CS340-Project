import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Stream;

public class Scheduling {

    public static boolean checkProfConflicts(int i, Professor[] pf, Course[] c) {
        Professor p = pf[c[i].getID() - 1];
        c[i].assignProf(p.getName());
        p.addCourse(c[i]);
        if ((p.getCourses()[0] != null) && (p.getCourses()[1] != null)) {
            if (p.getCourses()[0].getTime() == p.getCourses()[1].getTime()) {
                return true;
            }
        }
        return false;
    }

    public static int resolveProfConflicts(int i, Professor[] pf, Course[] c, int n) {
        Professor p = pf[c[i].getID() - 1];
        if ((p.getCourses()[0].getEnrollment()) < (p.getCourses()[1].getEnrollment())) {
            n = n - p.getCourses()[0].getEnrollment();
            p.getCourses()[0].assignProf(0);
            p.editCourse(0, null);
        } else {
            n = n - p.getCourses()[1].getEnrollment();
            p.getCourses()[1].assignProf(0);
            p.editCourse(1, null);
        }
        return n;
    }

    public static int resolveCapConflicts(int i, Course[] c, int n) {
        if (c[i].getRoom().getCapacity() < c[i].getEnrollment()) {
            int overflow = c[i].getEnrollment() - c[i].getRoom().getCapacity();
            n = n - overflow;
            c[i].drop(overflow);
        }
        return n;
    }

    public static void checkStudentTimeConflicts(Course[] cc, Conflict[] cf, HashMap<Integer,Conflict> map) {
        int i = 1;
        String ret = "";
        Course ccc = cc[cc.length - 2];
        for (Course c : cc) {
            if (c != null) {
                if (c.getTime() == i) {
                    ret = ret + c.getRosterStr();
                } else {
                    i++;

                    
                    int[] e = Stream.of(ret.split("\\s+")).mapToInt(Integer::parseInt).toArray(); 
                    System.out.println(Arrays.toString(e));
                    HashSet<Integer> conflicts = new HashSet<Integer>();
                    for (Integer n : e) {
                        if (!conflicts.add(n)) {
                            System.out.println("Conflict Student: " + n);
                    
                                if (map.containsKey(n)) {
                                    map.get(n).addConf(n);
                                } else {
                                    Conflict newCf = new Conflict(c);
                                    newCf.addConf(n);
                                    map.put(n, newCf);
                                }
                        
                        }
                    }
                    
                    System.out.println(ret);
                    
                    ret = c.getRosterStr();
                }
            }
        }
        int[] e = Stream.of(ret.split("\\s+")).mapToInt(Integer::parseInt).toArray(); 
        System.out.println(Arrays.toString(e));
        HashSet<Integer> conflicts = new HashSet<Integer>();
        for (Integer n : e) {
            if (!conflicts.add(n)) {
                System.out.println("Conflict Student: " + n);
        
                    if (map.containsKey(n)) {
                        map.get(n).addConf(n);
                    } else {
                        Conflict newCf = new Conflict(ccc);
                        newCf.addConf(n);
                        map.put(n, newCf);
                    }
            
            }
        }
    
    }

    public static void resolveStudentConflicts() {
        //calculate total number of conflicts for course c
        //reschedule c if conflict > popularity
        //else unenroll students
    }


    public static void main(String[] args) {

        String constraints = "./tests/constraints";
        String studentPrefs = "./tests/studentprefs";

        Constraints cons = IO.constraints(constraints);
        int timeSlots = cons.getTimeSlots();
        Course[] courses = cons.getCourses();
        Room[] rooms = cons.getRooms();
        //Professor[] profs = cons.getProfs();
        Professor[] profAvailability = Arrays.copyOfRange(cons.getPCPairs(), 1, courses.length);
        Conflict[] conflicts = new Conflict[courses.length];
        int spv = IO.populateCourses(studentPrefs, courses, conflicts) * 4;
        double spvu = spv;
        int[] studentConflicts = new int[cons.getCourses()[1].getRoster().length];
        HashMap<Integer,Conflict> STPairs = new HashMap<Integer,Conflict>();

        Arrays.sort(courses, Comparator.nullsLast(new CourseEnrollmentComparator()));
        Arrays.sort(rooms, Comparator.nullsLast(new RoomComparator()));

        int timeAvailability = timeSlots;
        int currentRoom = 0;

        for (int i = 0; i < courses.length; i++) {
            if (courses[i] != null) {
                if (timeAvailability > 0) {

                    courses[i].assignRoom(rooms[currentRoom]);
                    timeAvailability--;
                    courses[i].assignTime(timeSlots - timeAvailability);


                    if (checkProfConflicts(i, profAvailability, courses)) {
                        spv = resolveProfConflicts(i, profAvailability, courses, spv);
                    }

                    spv = resolveCapConflicts(i, courses, spv);

                    
                } else if (currentRoom >= (rooms.length - 1)) {
                    //should not happen unless some timeslot @ some room is unused
                    //or when a course is cancelled
                    //System.out.println("No more room... ");
                    //TODO
                    break;
                } else {

                    currentRoom++;
                    timeAvailability = timeSlots;
                    courses[i].assignRoom(rooms[currentRoom]);
                    timeAvailability--;
                    courses[i].assignTime(timeSlots - timeAvailability);

                    if (checkProfConflicts(i, profAvailability, courses)) {
                        spv = resolveProfConflicts(i, profAvailability, courses, spv);
                    }

                    spv = resolveCapConflicts(i, courses, spv);


                    

                }
            }
        }

        for (int k : studentConflicts) {
            System.out.println(k);
        }

        Arrays.sort(courses, Comparator.nullsLast(new CourseTimeComparator()));
        
        checkStudentTimeConflicts(courses, conflicts, STPairs);

        System.out.println(STPairs);

        IO.generateSchedule(courses);
        System.out.println(String.format("Student Preference Value: %d (%.2f)\n", spv, (spv / spvu)));

    }
}


class CourseEnrollmentComparator implements Comparator<Course> {
    @Override
    public int compare(Course c1, Course c2) {
        return c2.getEnrollment() - c1.getEnrollment();
    }
}

class CourseRoomComparator implements Comparator<Course> {
    @Override
    public int compare(Course c1, Course c2) {
        return c1.getRoom().getNumber() - c2.getRoom().getNumber();
    }
}

class CourseTimeComparator implements Comparator<Course> {
    @Override
    public int compare(Course c1, Course c2) {
        return c1.getTime() - c2.getTime();
    }
}

class RoomComparator implements Comparator<Room> {
    @Override
    public int compare(Room r1, Room r2) {
        return r2.getCapacity() - r1.getCapacity();
    }
}