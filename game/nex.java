/**
 * Start by designing the state machine, which goes...
 * 1) I offer sensory input
 * 2) The thing returns some behavioral output
 * Because of x, do y, and remember that you did y because of x and it worked out
 * z.
 * So remember you did y because x, and remember that you got z because y because x.
 * It's always a somewhat random decision, but the command hallucination
 * kicks in when it doesn't know what to do.  When a situation is too novel.
 * Acting completely erratically is biologically stupid.
 * Has anything "like" this ever happened before?  Would be the question to ask
 * before a command is required.
 **/
import java.util.Scanner;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Random;

public class nex {
    public static void main(String[] args) {
        Scanner sc;
        Vector<Integer> memory = new Vector<Integer>();
        Hashtable<Integer, Integer> iodict = new Hashtable<Integer, Integer>();
        Hashtable<String, Integer> ioodict = new Hashtable<String, Integer>();
        Hashtable<String, Integer> langdict = new Hashtable<String, Integer>();
        int current = (new Random()).nextInt(10),
            input = -21;
        while (true) {
            sc = new Scanner(System.in);
            System.out.println(current); //Current behavior
            int in = sc.nextInt(); //Sensory input

            if (in == 4) { //Mapping
                System.out.print("Mapping: ");
                langdict.put(sc.next(), current);
                continue;
            } else if (in == 0) { //Resolve scenario
                if (input == -21) {
                    System.err.println("No input!");
                    continue;
                }
                System.out.print("Resolution: ");
                iodict.put(input, current);
                ioodict.put(new Pair(input, current).toString(), sc.nextInt());
                current = (new Random()).nextInt(10);
                memory.add(input);
                continue;
            } else if (in == -20) break;
            else {
                input = in;
            }

            if (iodict.containsKey(input)) { //Have I ever experienced this input before?
                int prevo = iodict.get(input),
                    outcome = ioodict.get(new Pair(input, prevo).toString());
                //If so, is what I did in response back then a good choice?
                if (outcome == 1) { //If so, do it again.
                    current = prevo;
                } else {
                    System.out.print("-> Command: ");
                    String cmd = sc.next();
                    current = langdict.get(cmd);
                }
            } else { //If not, command.
                    System.out.print("Command: ");
                    String cmd = sc.next();
                    current = langdict.get(cmd);
            }
        }
    }
}
class Pair {
    int[] data;
    public Pair (int a, int b) {
        data = new int[2];
        data[0] = a;
        data[1] = b;
    }
    public String toString() {
        return "[" + data[0] + ", " + data[1] + "]";
    }
}
