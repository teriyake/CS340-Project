import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

public class Scheduling {

    public static Boolean checkProfConflicts(int i, Professor[] pf, Course[] c, HashMap<Integer, HashSet<Integer>> overlaps) {
        Professor p = pf[i];
        if ((p == null) || (c[p.getC1()] == null) || (c[p.getC2()] == null)) {
            return false;
        }
        if ((p.getC1() != 0) && (p.getC2() != 0)) {
            Integer c1Time = c[p.getC1()].getTime();
            Integer c2Time= c[p.getC2()].getTime();
            if (overlaps.containsKey(c1Time) && overlaps.get(c1Time).contains(c2Time)) {
                return true;
            }
        }
        return false;
    }

    public static int resolveProfConflicts(int i, Professor[] pf, Course[] c, int n, int timeSlots, int[][] nca, Room[] rooms) {
        Professor p = pf[i];
        if ((p == null)) {
            return n;
        }

        for (int t = 0; t < 4; i++) {
            //System.out.println("t=" + t);
            for (int r = 0; r < rooms.length; r++) {
                //System.out.println("r=" + r);
                if (nca[t][r] == 0) {
                    if(c[i] == null) {
                        continue;
                    } else {
                        c[i].assignTime(t + timeSlots + 1);
                        c[i].assignRoom(rooms[t]);
                        nca[t][r] = 1;
                        return n;
                    }
                }
            }
            n = n - c[i].getEnrollment();
            c[i].cancel();
        }
            
        return n;
    }

    public static int resolveCapConflicts(int i, Course[] c, Student[] students, int n) {
        if (c[i] == null) {
            return n;
        }
        if (c[i].getRoom().getCapacity() < c[i].getEnrollment()) {
            int overflow = c[i].getEnrollment() - c[i].getRoom().getCapacity();
            n = n - overflow;
            c[i].drop(overflow);
            // for each student over the capacity limit, remove c[i] from their schedule
            int[] currentEnrollment = c[i].getRoster();
            for (int j = 0; j > n; j++) {
                int s = currentEnrollment[currentEnrollment.length - j - 1];
                students[s].removeCourseO(c[i]);
            }
        }
        return n;
    }

    public static int scheduleNightClasses(int i, Professor[] pf, Course[] c, int n, int timeSlots, int[][] nca, Room[] rooms, HashMap<Integer, ArrayList<Student>> ds) {
        Professor p = pf[i];
        if ((p == null)) {
            return n;
        }

        for (int t = 0; t < 4; i++) {
            for (int r = 0; r < rooms.length; r++) {
                if (nca[t][r] == 0) {
                    c[i].assignTime(t + timeSlots + 1);
                    c[i].assignRoom(rooms[t]);
                    ArrayList<Student> studentsToAdd = ds.get(c[i].getID());
                    for (Student s : studentsToAdd) {
                        c[i].enroll(s.getName());
                        s.addCourseO(c[i]);
                    }
                    return c[i].getEnrollment();
                }
            }
            n = n - c[i].getEnrollment();
            c[i].cancel();
        }
            
        return n;
    }

    public static void main(String[] args) {
 
        long startT = System.currentTimeMillis();

        String constraints = "./tests/constraints";
        String studentPrefs = "./tests/studentprefs";
        String outputFile = "./tests/schedule";
        String tmpSchedule = "./tests/~schedule";

        if (args.length != 0) {
            constraints = "./data/c_" + args[0];
            studentPrefs = "./data/s_" + args[0];
            outputFile = "./output/schedule_" + args[0];
        } 

        Constraints cons = IO.constraints(constraints);
        //int timeSlots = cons.getTimeSlots();
        int timeSlots = 19;
        HashMap<Integer, TimeSlot> slotList = cons.getSlots(5, 5, 8, 4);
        HashMap<Integer, HashSet<Integer>> overlaps = TimeSlot.overlapping(slotList);
        Course[] courses = cons.getCourses();
        int[] labs = cons.getLabs();
        Room[] rooms = cons.getRooms();
        Student[] students = new Student[IO.getStudents(studentPrefs) + 1];
        Professor[] profAvailability = Arrays.copyOfRange(cons.getPCPairs(), 1, courses.length);
        Conflict[] conflicts = new Conflict[courses.length];

        int spv = IO.populateCourses(studentPrefs, courses, labs, conflicts, students) * 4;
        double spvu = spv;

        Room[] npr = new Room[rooms.length];
        Room[] pr = new Room[rooms.length];
        for (int i = 0; i < rooms.length; i++) {
            if (rooms[i] != null) {
                if(!rooms[i].isInPark()) {
                    npr[i] = rooms[i];
                } else {
                    pr[i] = rooms[i];
                }
            }
        }

        HashMap<Integer, ArrayList<Student>> droppedStudents = new HashMap<Integer, ArrayList<Student>>();

        Arrays.sort(npr, Comparator.nullsLast(new RoomComparator()));
        Arrays.sort(pr, Comparator.nullsLast(new RoomComparator()));
        Arrays.sort(courses, Comparator.nullsLast(new CourseEnrollmentComparator()));
        Arrays.sort(rooms, Comparator.nullsLast(new RoomComparator()));

        System.out.println(overlaps.toString());

        int timeAvailability = timeSlots;
        // int currentRoom = 0;
        int currentNPRoom = 0;
        int currentPRoom = 0;
        int[][] nightClassesAvailability = new int[4][rooms.length];
        
        for (int i = 0; i < courses.length; i++) {
            //emily balch sems are always scheduled in time 2 - (always parsed from 001 or 002)
            if (courses[i] != null) {
                if (timeAvailability == 0) {
                    currentNPRoom++;
                    timeAvailability = timeSlots;
                }             
                boolean flag = false;
                for (int r = 0; r < rooms[currentNPRoom].schedule.size(); r++) {              
                    int slot = rooms[currentNPRoom].getSchedule().get(r);
                    if ((overlaps.get(timeAvailability)).contains(slot)) {
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    i--;
                    timeAvailability--;
                    continue;
                } 
                if ((courses[i].getID() == 1) || (courses[i].getID() == 2)) {

                    courses[i].assignRoom(rooms[currentNPRoom]);
                    timeAvailability--;
                    currentNPRoom++;
                    courses[i].assignTime(2);
                    rooms[currentNPRoom].schedule.add(2);
                    courses[i].assignProf(profAvailability[courses[i].getID() - 1].getName());
                    profAvailability[courses[i].getID() - 1].addCourse(courses[i]);
                    continue;
                } 

                if (courses[i].hasLab()) {
                    if (timeAvailability > 0) {
                        courses[i].assignRoom(rooms[currentPRoom]);
                        timeAvailability--;
                        courses[i].assignTime(timeSlots - timeAvailability);
                        courses[i].assignProf(profAvailability[courses[i].getID() - 1].getName());
                        profAvailability[courses[i].getID() - 1].addCourse(courses[i]);
                    } else if (currentPRoom >= (pr.length - 1)) {
                        break;
                    } else {
                        currentPRoom++;
                        timeAvailability = timeSlots;
                        courses[i].assignRoom(rooms[currentPRoom]);
                        timeAvailability--;
                        courses[i].assignTime(timeSlots - timeAvailability);
                        courses[i].assignProf(profAvailability[courses[i].getID() - 1].getName());
                        profAvailability[courses[i].getID() - 1].addCourse(courses[i]);
                    }   
                 } else {
                    if (timeAvailability > 0) {
                        courses[i].assignRoom(rooms[currentNPRoom]);
                        timeAvailability--;
                        courses[i].assignTime(timeSlots - timeAvailability);
                        courses[i].assignProf(profAvailability[courses[i].getID() - 1].getName());
                        profAvailability[courses[i].getID() - 1].addCourse(courses[i]);
                    } else if (currentNPRoom >= (npr.length - 1)) {
                        break;
                    } else {
                        currentNPRoom++;
                        timeAvailability = timeSlots;
                        courses[i].assignRoom(rooms[currentNPRoom]);
                        timeAvailability--;
                        courses[i].assignTime(timeSlots - timeAvailability);
                        courses[i].assignProf(profAvailability[courses[i].getID() - 1].getName());
                        profAvailability[courses[i].getID() - 1].addCourse(courses[i]);
                    }
                }
                rooms[currentNPRoom].addTime(timeSlots - timeAvailability);
            }  
        }

        for (int i = 0; i < courses.length; i++) {
            if (courses[i] != null) {
                if (checkProfConflicts(i, profAvailability, courses, overlaps)) {
                    int nspv = resolveProfConflicts(i, profAvailability, courses, spv, timeSlots, nightClassesAvailability, rooms);
                    if (nspv == spv) {
                    }
                    spv = nspv;
                }
                if (courses[i].getRoom() != null) {
                    spv = resolveCapConflicts(i, courses, students, spv);
                }
            }

        }

        Arrays.sort(courses, Comparator.nullsLast(new CourseTimeComparator()));
        
        HashMap<Integer, HashSet<Integer>> numSet = new HashMap<Integer, HashSet<Integer>>();
        int currentTime = 1;
        int ttttt = 0;
        for(int k = 1; k < (timeSlots + 4); k++) {
            numSet.put(k, new HashSet<Integer>());
        }
        for (int i = 1; i < courses.length - 1; i++) {
            if (courses[i].getTime() == currentTime) {
                for(int s : courses[i].getRoster()){
                    for (Integer t : overlaps.get(currentTime)) {
                        if(!(numSet.get(t)).add(s)) {
                            if (!droppedStudents.containsKey(courses[i].getID())) {
                                ArrayList<Student> nds = new ArrayList<>();
                                nds.add(students[s]);
                                droppedStudents.put(courses[i].getID(), nds);
                            } else {
                                droppedStudents.get(courses[i].getID()).add(students[s]);
                            }
                            courses[i].unenroll(s);
                            students[s].removeCourseO(courses[i]);
                            spv--;
                            ttttt++;
                        }
                    }
                    /*if(!(numSet.get(currentTime)).add(s)){
                        if (!droppedStudents.containsKey(courses[i].getID())) {
                            ArrayList<Student> nds = new ArrayList<>();
                            nds.add(students[s]);
                            droppedStudents.put(courses[i].getID(), nds);
                        } else {
                            droppedStudents.get(courses[i].getID()).add(students[s]);
                        }
                        courses[i].unenroll(s);
                        students[s].removeCourseO(courses[i]);
                        spv--;
                        ttttt++;
                        //System.out.printf("%d\tStudent #%d dropped due to conflict at time %d course %d\n", ttttt, students[s].getName(), currentTime, courses[i].getID());
                    }*/
                }
            } else {
                currentTime = courses[i].getTime();
                for (int s : courses[i].getRoster()) {
                    HashSet<Integer> n = numSet.get(currentTime);
                    n.add(s);
                    numSet.put(currentTime, n);
                }
            }
            
        }

        IO.generateSchedule(courses, tmpSchedule);

        // if a class is cancelled due to all students having time conflicts,
        // it is rescheduled as a night class
        for (int i = 0; i < courses.length; i++) {
            if (courses[i] != null) {
                if (courses[i].getEnrollment() == 0) {
                    int nspv = scheduleNightClasses(i, profAvailability, courses, spv, timeSlots, nightClassesAvailability, rooms, droppedStudents);
                    spv += nspv;
                }
            }

        }

        IO.generateSchedule(courses, outputFile);
        System.out.println(String.format("Student Preference Value: %d (%.2f)\n", spv, (spv / spvu)));

        long endT = System.currentTimeMillis();
        System.out.println(endT - startT);

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

class RoomTypeComparator implements Comparator<Room> {
    @Override
    public int compare(Room r1, Room r2) {
        return Boolean.compare(r2.isInPark(), r1.isInPark());
    }
}

class ProfComparator implements Comparator<Professor> {
    @Override
    public int compare(Professor p1, Professor p2) {
        return p1.getName() - p2.getName();
    }
}