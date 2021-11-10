import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class Utils {

    public static boolean isInt (String str) {

        if (str == null) {
            return false;
        }

        int length = str.length();
        if (length == 0) {
            return false;
        }

        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }

        return true;
    }

    public static void getDepts(String f, ArrayList<String> d) {
        HashSet<String> set = new HashSet<>();

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            br.readLine();
            String l;

            while ((l = br.readLine()) != null) {
                String[] ls = l.split(",");

                if (!set.contains(ls[2])) {
                    d.add(ls[2]);
                    set.add(ls[2]);
                }
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void getLabs(String f, ArrayList<String> lc) {
        HashSet<String> set = new HashSet<>();

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            br.readLine();
            String l;

            while ((l = br.readLine()) != null) {
                String[] ls = l.split(",");

                if (!isInt(ls[4])) {
                    if (!set.contains(ls[1])) {
                        //System.out.println(ls[6]);
                        lc.add(ls[1]);
                        set.add(ls[1]);
                    }
                }
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static int getTimes(String f, ArrayList<Double> t) {
        HashSet<String> setS = new HashSet<>();
        HashSet<String> setE = new HashSet<>();

        int n = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            br.readLine();
            String l;

            while ((l = br.readLine()) != null) {
                String[] ls = l.split(",");
                String startHM;
                String endHM;
                int[] start = new int[2];
                int[] end = new int[2];
                    // 12 14

                if (ls[12].equals("")) {
                    continue;
                }
                //System.out.println(ls[1]);
                if (ls[12].endsWith("AM")) {
                    startHM = ls[12].split("AM")[0].trim();
                    String[] shm = startHM.split(":");
                    start[0] = Integer.parseInt(shm[0]);
                    start[1] = Integer.parseInt(shm[1]);
                } else {
                    startHM = ls[12].split("PM")[0].trim();
                    String[] shm = startHM.split(":");
                    start[0] = Integer.parseInt(shm[0]) + 12;
                    start[1] = Integer.parseInt(shm[1]);
                }
                if (ls[14].endsWith("AM")) {
                    endHM = ls[14].split("AM")[0].trim();
                    String[] ehm = endHM.split(":");
                    end[0] = Integer.parseInt(ehm[0]);
                    end[1] = Integer.parseInt(ehm[1]);
                } else {
                    endHM = ls[14].split("PM")[0].trim();
                    String[] ehm = endHM.split(":");
                    end[0] = Integer.parseInt(ehm[0]) + 12;
                    end[1] = Integer.parseInt(ehm[1]);
                }
                String startHMHash = startHM.replaceAll(":", "");
                String endHMHash = endHM.replaceAll(":", "");

                if ((setS.contains(startHMHash)) && (setE.contains(endHMHash))) {
                    continue;
                } else {
                    n = n + 1;
                    System.out.printf("Course: %s\tStart: %s\tEnd: %s\n", ls[6], ls[12], ls[14]);
                    Double startTime = start[0] + (double) start[1] / 60;
                    t.add(startTime);
                    System.out.println(setS.add(startHMHash));
                    Double endTime = end[0] + (double) end[1] / 60;
                    t.add(endTime);
                    System.out.println(setE.add(endHMHash));
                    Double duration = endTime - startTime;
                    t.add(duration);
                }
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        return n;
    }
    public static void main(String[] args) {
        String enrollmentData = "./data/Spring2015.csv";
        ArrayList<String> departments = new ArrayList<String>();
        ArrayList<String> labs = new ArrayList<String>();
        ArrayList<Double> times = new ArrayList<Double>();
        int timeSlots = 0;

        getDepts(enrollmentData, departments);
        for (String dept : departments) {
            System.out.println(dept);
        }

        getLabs(enrollmentData, labs);

        timeSlots = getTimes(enrollmentData, times);
        System.out.println(timeSlots);
        for (Double t : times) {
            System.out.println(t);
        }

    }
}
