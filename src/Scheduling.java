import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Scheduling {

    public static Return checkProfConflicts(int i, Professor[] pf, Course[] c) {
        Return ret = new Return(0, false);
        Professor p = pf[c[i].getID() - 1];
        if (p == null) {
            // no prof available
            int a = c[i].getEnrollment();
            c[i].cancel(); //* decrease enrollment 
            ret.updateSPV(a);
            ret.updateConflict(true);
            return ret;
        }
        c[i].assignProf(p.getName());
        p.addCourse(c[i]);
        if ((p.getCourses()[0] != null) && (p.getCourses()[1] != null)) {
            if (p.getCourses()[0].getTime() == p.getCourses()[1].getTime()) {
                ret.updateConflict(true);
                return ret;
            }
        }
        return ret;
    }

    public static int resolveProfConflicts(int i, Professor[] pf, Course[] c, int n) {
        Professor p = pf[c[i].getID() - 1];
        if ((p.getCourses()[0].getEnrollment()) < (p.getCourses()[1].getEnrollment())) {
            n = n - p.getCourses()[0].getEnrollment();
            p.getCourses()[0].cancel();
        } else {
            n = n - p.getCourses()[1].getEnrollment();
            p.getCourses()[0].cancel();
        }
        return n;
    }

    public static int resolveCapConflicts(int i, Course[] c, Student[] students, int n) {
        if (c[i].getRoom().getCapacity() < c[i].getEnrollment()) {
            int overflow = c[i].getEnrollment() - c[i].getRoom().getCapacity();
            n = n - overflow;
            c[i].drop(overflow);
            // for each student over the capacity limit, remove c[i] from their schedule
            int[] currentEnrollment = c[i].getRoster();
            for (int j = 0; j > n; j++) {
                int s = currentEnrollment[currentEnrollment.length - i - 1];
                students[s].removeCourseO(c[i]);
            }
        }
        return n;
    }

    public static void main(String[] args) {
 
        long startT = System.currentTimeMillis();

        String constraints = "./tests/constraints";
        String studentPrefs = "./tests/studentprefs";
        String outputFile = "./tests/schedule";

        if (args.length != 0) {
            constraints = "./data/c_" + args[0];
            studentPrefs = "./data/s_" + args[0];
            outputFile = "./output/schedule_" + args[0];
        } 

        Constraints cons = IO.constraints(constraints);
        // int timeSlots = cons.getTimeSlots();
        int timeSlots = 17;
        Course[] courses = cons.getCourses();
        int[] labs = cons.getLabs();
        //System.out.println(Arrays.toString(labs));

        Room[] rooms = cons.getRooms();

        //Professor[] profs = cons.getProfs();
        Student[] students = new Student[IO.getStudents(studentPrefs) + 1];

        Professor[] profAvailability = Arrays.copyOfRange(cons.getPCPairs(), 1, courses.length);
        Conflict[] conflicts = new Conflict[courses.length];

        int spv = IO.populateCourses(studentPrefs, courses, labs, conflicts, students) * 4;
        double spvu = spv;

        // Arrays.sort(rooms, Comparator.nullsLast(new RoomTypeComparator()));
        // for (Room r : rooms) {
        //     if (r != null) {
        //         System.out.println(r.toString());
        //     }
        // }

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
        Arrays.sort(npr, Comparator.nullsLast(new RoomComparator()));
        Arrays.sort(pr, Comparator.nullsLast(new RoomComparator()));
        // for (Room r : npr) {
        //     if (r != null) {
        //         System.out.println(r.toString());
        //     }
        // }
        // System.out.println("===============");
        // for (Room r : pr) {
        //     if (r != null) {
        //         System.out.println(r.toString());
        //     }
        // }


        Arrays.sort(courses, Comparator.nullsLast(new CourseEnrollmentComparator()));
        Arrays.sort(rooms, Comparator.nullsLast(new RoomComparator()));

        int timeAvailability = timeSlots;
        int currentRoom = 0;
        int currentNPRoom = 0;
        int currentPRoom = 0;
        for (int i = 0; i < courses.length; i++) {
            if (courses[i] != null) {

                //emily balch sems are always scheduled in time 2
                if (courses[i] != null && ((courses[i].getID() == 001) || (courses[i].getID() == 002))) {
                    courses[i].assignRoom(rooms[currentNPRoom]);
                    timeAvailability--;
                    courses[i].assignTime(2);
                } else if (!courses[i].hasLab()) {
                    if (timeAvailability > 0) {
                        courses[i].assignRoom(rooms[currentNPRoom]);
                        timeAvailability--;
                        courses[i].assignTime(timeSlots - timeAvailability);
                        Return ret = checkProfConflicts(i, profAvailability, courses);
                        if (ret.hasConflict()) {
                            if (ret.getSPV() == 0) {
                                spv = resolveProfConflicts(i, profAvailability, courses, spv);
                            } else {
                                spv = spv - ret.getSPV();
                            }
                            
                        }
    
                        spv = resolveCapConflicts(i, courses, students, spv);

                    } else if (currentNPRoom >= (npr.length - 1)) {
                        break;
                    } else {
                        currentNPRoom++;
                        timeAvailability = timeSlots;
                        courses[i].assignRoom(rooms[currentNPRoom]);
                        timeAvailability--;
                        courses[i].assignTime(timeSlots - timeAvailability);
    
                        Return ret = checkProfConflicts(i, profAvailability, courses);
                        if (ret.hasConflict()) {
                            if (ret.getSPV() == 0) {
                                spv = resolveProfConflicts(i, profAvailability, courses, spv);
                            } else {
                                spv = spv - ret.getSPV();
                            }
                            
                        }
    
                        spv = resolveCapConflicts(i, courses, students, spv);
                    }
                } else {
                    if (timeAvailability > 0) {
                        courses[i].assignRoom(rooms[currentPRoom]);
                        timeAvailability--;
                        courses[i].assignTime(timeSlots - timeAvailability);

                        Return ret = checkProfConflicts(i, profAvailability, courses);
                        if (ret.hasConflict()) {
                            if (ret.getSPV() == 0) {
                                spv = resolveProfConflicts(i, profAvailability, courses, spv);
                            } else {
                                spv = spv - ret.getSPV();
                            }
                            
                        }
    
                        spv = resolveCapConflicts(i, courses, students, spv);

                    } else if (currentPRoom >= (pr.length - 1)) {
                        break;
                    } else {
                        currentPRoom++;
                        timeAvailability = timeSlots;
                        courses[i].assignRoom(rooms[currentPRoom]);
                        timeAvailability--;
                        courses[i].assignTime(timeSlots - timeAvailability);
    
                        Return ret = checkProfConflicts(i, profAvailability, courses);
                        if (ret.hasConflict()) {
                            if (ret.getSPV() == 0) {
                                spv = resolveProfConflicts(i, profAvailability, courses, spv);
                            } else {
                                spv = spv - ret.getSPV();
                            }
                            
                        }
    
                        spv = resolveCapConflicts(i, courses, students, spv);
                    }
                }
            }
        }


        Arrays.sort(courses, Comparator.nullsLast(new CourseTimeComparator()));
        
        Set<Integer> numSet = new HashSet<Integer>();
        int currentTime = 1;
        for (int i = 1; i < courses.length - 1; i++) {
            //System.out.println(Arrays.toString(courses[i].getRoster()));
            if (courses[i].getTime() == currentTime) {
                for(int s : courses[i].getRoster()){
                    if(!numSet.add(s)){
                        courses[i].unenroll(s);
                        students[s].removeCourseO(courses[i]);
                        spv--;
                    }
                }
            } else {
                currentTime = courses[i].getTime();
                numSet = new HashSet<Integer>();
                for (int s : courses[i].getRoster()) {
                    numSet.add(s);
                }
            }
            
        }

        //* testing
        // System.out.println(Arrays.toString(students));
        // for (Course c : students[7].getCoursesO()) {
        //     if (c != null) {
        //         System.out.println(c.toString());
        //     }
        //     if (c == null) {
        //         System.out.println(c);
        //     }
        // }

       
        // Arrays.sort(profAvailability, Comparator.nullsLast(new ProfComparator()));

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