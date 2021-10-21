import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

public class IO {

    public static int populateCourses(String f, Course[] courses, Conflict[] conflicts) {

        int s = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            s = Integer.parseInt(br.readLine().split("\\s+")[1]);
            String l;

            while ((l = br.readLine()) != null) {
                String[] ls = l.split("\\s+");

                for (int i = 1; i < 5; i++) {
                    if (courses[Integer.parseInt(ls[i])] != null) {
                        //System.out.println(courses[i]);
                        courses[Integer.parseInt(ls[i])].enroll(Integer.parseInt(ls[0]));
                        
                    } else {
                        Course c = new Course(Integer.parseInt(ls[i]), s);
                        courses[Integer.parseInt(ls[i])] = c;
                        c.enroll(Integer.parseInt(ls[0]));
                        
                    }
                }
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }

        return s;
    }

    public static Constraints constraints(String f) {

        Constraints cons = new Constraints(0);

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {

            int t = Integer.parseInt(br.readLine().split("\\s+")[2]);
            int c = 0;
            int r = Integer.parseInt(br.readLine().split("\\s+")[1]);
            int p = 0;
            cons = new Constraints(t, r);

            String l;

            while ((l = br.readLine()) != null) {
                String[] ls = l.split("\\s+");
                if (ls[0].equals("Classes")) {
                    c = Integer.parseInt(ls[1]);
                    break;
                } else {
                    cons.addRoom(Integer.parseInt(ls[0]), Integer.parseInt(ls[1]));
                }
            }

            p = Integer.parseInt(br.readLine().split("\\s+")[1]);

            cons.addProfCons(p);
            cons.addCourses(c);

            while ((l = br.readLine()) != null) {
                String[] ls = l.split("\\s+");
                if (cons.getProfs()[Integer.parseInt(ls[1])] == null) {
                    Professor newProf = new Professor(Integer.parseInt(ls[1]), Integer.parseInt(ls[0]));
                    cons.addProf(Integer.parseInt(ls[1]), newProf);
                } else {
                    cons.editProf(Integer.parseInt(ls[1]), Integer.parseInt(ls[0]));
                }
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cons;
    }

    public static void generateSchedule(Course[] courses) {
        try (Writer bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("schedule"), StandardCharsets.UTF_8))) {
            bw.write("Course\tRoom\tTeacher\tTime\tStudents\n");
            for (Course c : courses) {
                if (c != null) {
                    bw.write(String.format("%d\t%d\t%d\t%d\t%s\n", 
                    c.getID(), c.getRoom().getNumber(), c.getProf(), c.getTime(), c.getRosterStr()));
                }
            }
        }  catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {}
}
