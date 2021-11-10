import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.Writer;
import java.net.URI;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.imageio.plugins.bmp.BMPImageWriteParam;

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
        HashMap<String, Integer> deptCounts = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            br.readLine();
            String l;

            while ((l = br.readLine()) != null) {
                String[] ls = l.split(",");

                if (!set.contains(ls[2])) {
                    d.add(ls[2]);
                    set.add(ls[2]);
                    deptCounts.put(ls[2], 1);
                } else {
                    int n = deptCounts.get(ls[2]) + 1;
                    deptCounts.put(ls[2], n);
                }
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(deptCounts.toString());

    }

    public static void getLabs(String f, ArrayList<String> lc, String o) {
        HashSet<String> set = new HashSet<>();

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            br.readLine();
            String l;

            while ((l = br.readLine()) != null) {
                String[] ls = l.split(",");

                if (!isInt(ls[4])) {
                    if (!set.contains(ls[1])) {
                        System.out.println(ls[6]);
                        lc.add(ls[1]);
                        set.add(ls[1]);
                    }
                }
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (Writer bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(o), StandardCharsets.UTF_8))) {
            for (String c : lc) {
                bw.write(c + "\n");
                }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static int getCourses(String f, ArrayList<String> lc) {
        HashSet<String> set = new HashSet<>();
        int ret = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            br.readLine();
            String l;

            while ((l = br.readLine()) != null) {
                String[] ls = l.split(",");
                if (!set.contains(ls[1])) {
                    //System.out.println(ls[6]);
                    lc.add(ls[1]);
                    set.add(ls[1]);
                    ret++;
                }
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ret;
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
                    //System.out.printf("Course: %s\tStart: %s\tEnd: %s\n", ls[6], ls[12], ls[14]);
                    Double startTime = start[0] + (double) start[1] / 60;
                    t.add(startTime);
                    setS.add(startHMHash);
                    Double endTime = end[0] + (double) end[1] / 60;
                    t.add(endTime);
                    setE.add(endHMHash);
                    Double duration = endTime - startTime;
                    t.add(duration);
                }
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        return n;
    }

    public static void findLabs(ArrayList<String> c, ArrayList<String> l, String o) {

        try (Writer bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(o), StandardCharsets.UTF_8))) {
            for (int i = 0; i < c.size(); i++) {
                String w = "";
                // System.out.println(c.size() + "\t" + l.size());
                if (l.isEmpty()) {
                    break;
                }
                for (int j = 0; j < l.size(); j++) {
                    if (c.get(i).equals(l.get(j))) {
                        // w = i + "L" + "\t" + c.get(i) + "\n";
                        w = i + " " + "L" + "\n";
                        l.remove(j);
                        break;
                    }
                }
                if (w != "") {
                    bw.write(w);
                } else {
                    bw.write(i + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addLabs(String cons, String labs) {
        try {

            BufferedReader[] brs = new BufferedReader[2];
            brs[0] = new BufferedReader(new FileReader(cons));
            brs[1] = new BufferedReader(new FileReader(labs));
            BufferedWriter bw = new BufferedWriter(new FileWriter("~labs"));

            String l;
            String ll;
            
            // sb1.append(brs[0].readLine());
            // sb1.append(System.getProperty("line.separator"));
            // r.append(brs[0].readLine());

            while ((l = brs[0].readLine()) != null) {
                bw.write(l);
                bw.newLine();
                // sb2.append(l);
                // sb2.append(System.getProperty("line.separator"));
                String[] ls = l.split("\\s+");

                if (ls[0].equals("Teachers")) {
                    break;
                } 
                
            }

            brs[1].readLine();

            boolean eof = false;
            
            while (!eof) {
                l = brs[0].readLine();
                ll = brs[1].readLine();
                
                if ((l == null) || (ll == null)) {
                    eof = true;
                    break;
                }

                // sb2.append(l);
                // sb2.append(System.getProperty("line.separator"));

                String[] lls = ll.split("\\s+");
                String[] ls = l.split("\\s+");

                if (ls.length > 2) {
                    bw.write(l);
                    bw.newLine();
                    continue;
                }

                if (lls.length > 1) {
                    if (lls[0].equals(ls[0])) {
                        bw.write(l + " L");
                        bw.newLine();
                    }
                } else {
                    bw.write(l);
                    bw.newLine();
                }
            }

            bw.close();   
            brs[0].close();

            File consL = new File("~labs");
            Path p = Paths.get(cons);
            Files.delete(p);
            if(consL.renameTo (new File(cons))) {
                consL.delete();
                // System.out.println("k");
            } else {
                // System.out.println("ERrOr");
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static int findParkRooms(String f, String c, HashMap<Integer, Integer> rc) {
        int ret = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            // BufferedWriter bw = new BufferedWriter(new FileWriter("tmp_rooms", StandardCharsets.UTF_8));
            // RandomAccessFile raf = new RandomAccessFile(c, "rw");
            br.readLine();
            String l;
            int n = ret;
            int id = 0;
            while ((l = br.readLine()) != null) {
                String[] ls = l.split("\\s+");
                if (ls[0].equals("Rooms")) {
                    break;
                }
            }

            while ((l = br.readLine()) != null) {
                id++;
                String[] ls = l.split("\\s+");
                if (ls[0].equals("Classes")) {
                    break;
                }
                if (ls[0].startsWith("PK")) {
                    rc.put(id, Integer.parseInt(ls[1]));
                    // System.out.println(ls[0]);
                    n++;
                }
            }

            // System.out.println(n);
            // bw.write(String.format("%d\n", n));
            // bw.close();

            // FileChannel channel = raf.getChannel();
            // ByteBuffer b = ByteBuffer.wrap(String.format("%d", n).getBytes(StandardCharsets.UTF_8));
            // channel.write(b, 5);
            // raf.close();

            ret = n;
            System.out.println(rc.toString());
            
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ret;
    }

    public static void updateRooms(String f, int r, HashMap<Integer, Integer> rc) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            BufferedWriter bw = new BufferedWriter(new FileWriter("~rooms"));
            String l;

            while ((l = br.readLine()) != null) {
                String[] ls = l.split("\\s+");
                if (!isInt(ls[0])) {
                    bw.write(l);
                    bw.newLine();
                } else {
                    if (ls.length > 2) {
                        bw.write(l);
                        bw.newLine();
                        continue;
                    } else {
                        if (rc.containsKey(Integer.parseInt(ls[0]))) {
                            bw.write(l + " P");
                            bw.newLine();
                        } else {
                            bw.write(l);
                            bw.newLine();
                        }
                    }
                }
            }

            br.close();
            bw.close();

            File consR = new File("~rooms");
            Path p = Paths.get(f);
            Files.delete(p);
            if(consR.renameTo (new File(f))) {
                consR.delete();
                // System.out.println("k");
            } else {
                // System.out.println("ERrOr");
            }
        
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        String enrollmentData = "./data/Spring2015.csv";
        String preprocessedCons = "./data/c_s15";
        String outputLabs = "./data/l_s15";
        String cons = "./data/c_5";
        ArrayList<String> departments = new ArrayList<String>();
        ArrayList<String> labs = new ArrayList<String>();
        ArrayList<Double> times = new ArrayList<Double>();
        ArrayList<String> courses = new ArrayList<String>();
        HashMap<Integer, Integer> parkRC = new HashMap<Integer, Integer>();
        int timeSlots = 0;
        int parkRooms = 0;
        // StringBuilder sb1 = new StringBuilder();
        // StringBuilder sb2 = new StringBuilder();
        // StringBuilder r = new StringBuilder();

        getDepts(enrollmentData, departments);
        // for (String dept : departments) {
        //     System.out.println(dept);
        // }

        getLabs(enrollmentData, labs, outputLabs);

        timeSlots = getTimes(enrollmentData, times);
        System.out.println(timeSlots);
        // for (Double t : times) {
        //     System.out.println(t);
        // }
        System.out.println(getCourses(enrollmentData, courses));
        findLabs(courses, labs, outputLabs);

        addLabs(cons, outputLabs);
    
        parkRooms = findParkRooms(preprocessedCons, cons, parkRC);

        // System.out.println(sb1.toString());
        // String pr = r.toString() + " " + parkRooms + "\n";
        // System.out.println(sb2.toString());
        
        updateRooms(cons, parkRooms, parkRC);
    }
}
