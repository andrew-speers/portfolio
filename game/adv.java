/**
 * 0 to 360 degrees, all positive values
 * Max 1 new word per statement
 **/
import java.util.Scanner;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Stack;
import javax.script.*;
import java.io.*;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

@SuppressWarnings("unchecked")
public class adv {
    public static void main (String[] args) throws IOException {
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("JavaScript");

        String contents = new String(Files.readAllBytes(Paths.get("init.js"))),
            lang = new String(Files.readAllBytes(Paths.get("words.txt")));

        Hashtable dict = new Hashtable();
        String[] words = lang.split("\n");
        //Build the dictionary and initialize variables in the script engine
        try {
            engine.eval(contents);
            for (String s : words) {
                //noun or verb?
                String[] ele = s.split(",");
                //Noun
                if (ele[1].charAt(0) == 'e') {
                    Word noun = new Word(0);
                    dict.put(ele[0], noun);
                    //engine.put(ele[0], noun);
                    engine.eval("var " + ele[0] + " = {attr : 0};");
                    //engine.eval("print(" + ele[0] + ".getAttr());");
                } else if (ele[1].charAt(0) == 'i') { //Verb
                    String path = "scripts/" + ele[0] + ".js";
                    String[] params = ele[2].split(":");
                    Word verb = new Word(path, params);
                    dict.put(ele[0], verb);
                    engine.eval(new String(Files.readAllBytes(Paths.get(verb.pathName))));
                }
            }
            //engine.eval("elbow.attr = 0.5; print(elbow.attr);");
            //System.out.println(((Invocable) engine).invokeFunction("contract", engine.eval("throat") , engine.eval("elbow")));
        } catch (Exception e) {
            System.err.println(e);
        }


        System.out.println("Statement: ");
        Scanner sc = new Scanner(System.in);
        String in = sc.nextLine();
        String[] cmd = in.split(" ");

        int newWord = -1;//, uniqueNounCount = 0;
        Vector prog = new Vector();
        //Add familiar words to the vector
        for (int i = 0; i < cmd.length; i++) {
            if (!dict.containsKey(cmd[i])) newWord = i;
            else {
                //If it's a noun we haven't seen before
                //if (!prog.contains(cmd[i]) && ((Word) dict.get(cmd[i])).isExplicit)
                //    uniqueNounCount++;
                prog.add(cmd[i]);
            }
        }

        //assume for now all new words are verbs
        //meaning we're writing a script
        if (newWord >= 0) {
            String name = cmd[newWord];
            //Develop param list...
            Vector params = new Vector();
            params.add("dObj");

            StringBuilder out = new StringBuilder("var " + name + " = function " + name + "(");
            while (!params.isEmpty()) out.append((String) params.remove(0) + ", ");
            out.replace(out.length() - 2, out.length(), ") {\n");

            Word current = null, previous = null;
            while (!prog.isEmpty()) {
                String s = (String) prog.remove(0);
                current = (Word) dict.get(s);
                if (previous == null) {
                    if (!current.isExplicit) out.append("\t" + s + "(");
                    //else out.append(s + ", ");
                } else if (previous.isExplicit) {
                    if (!current.isExplicit)
                        out.replace(out.length() - 2, out.length(), ");\n\t" + s + "(");
                    else out.append(s + ", ");
                } else {
                    if (!current.isExplicit) out.append(");\n\t" + s + "(");
                    else out.append(s + ", ");
                }
                previous = current;
            }
            if (previous.isExplicit)
                out.delete(out.length() - 2, out.length());
            out.append(");\n}");

            System.out.println(out.toString());
            System.exit(0);

            /*
            for (Object obj : prog) {
                String[] from = ((Word) dict.get((String) obj)).params;
                if (from == null) continue;
                for (String s : from) {
                    //if (!params.contains(s)) params.add(s);
                    params.add(s);
                }
            }
            */

        }

        /*
        Stack asm = new Stack();
        while (!prog.isEmpty()) {
            Word current = (Word) dict.get(prog.remove(0));
            if (current.isExplicit) {
                asm.push(current);
            }
        }
        */
        /*
        Scanner sc = new Scanner(System.in);
        try {
            engine.eval(contents);
            //language learning, as opposed to problem statements
            System.out.print("LL Statement: ");
            String in = sc.nextLine();
            String[] cmd = in.split(" ");
            Vector prog = new Vector();
            for (int i = 1; i < cmd.length; i++) {
                //if a noun
                if (Character.isUpperCase(cmd[i].charAt(0))) {
                    prog.add(cmd[i - 1] + "" + cmd[i] + "();");

                }
            }
            StringBuilder out = new StringBuilder("var " + cmd[0] + " = function " + cmd[0] + "() {"  + "\n");
            //StringBuilder out = new StringBuilder(arg);
            while (!prog.isEmpty()) {
                String inst = (String) prog.remove(0);
                out.append("\t" + inst + "\n");
                //String noun = (String) prog.remove(0),
                //    verb = (String) prog.remove(0);

            }
            out.append("}\n" + cmd[0] + "();");
            try(PrintWriter pw = new PrintWriter("Compound/" + cmd[0] + ".js")) {
                pw.println(out);
            } catch (FileNotFoundException fnfe) {
                System.err.println(fnfe);
            }
            try {
                Files.write(Paths.get("words.txt"),
                            (cmd[0] + "\n").getBytes(), StandardOpenOption.APPEND);
            } catch (IOException ioe) {
                System.err.println(ioe);
            }
            //System.out.println(out.toString());

            //String in = new String(Files.readAllBytes(Paths.get((String) dict.get(sc.nextLine()))));

            //engine.eval(new String(Files.readAllBytes(Paths.get((String) dict.get(sc.next())))));
            //engine.eval("print(val)");
        } catch (ScriptException e) {
            System.err.println(e);
        }
        */
    }
    public static void tmain (String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter details...");
        float[] range = new float[2];
        float init;
        range[0] = sc.nextFloat();
        range[1] = sc.nextFloat();
        init = sc.nextFloat();
        Joint tmp = new Joint(range, init);

        //Objective: get to minimum
        double bias = 0.5; //lower is rotate CW, greater is rotate CCW
        int k = 0;
        while (k++ < 500) {
            double r = Math.random();
            double choice = 0;

            if (r < bias) {
                tmp.rotate((float) -360);
                choice = 0.01;
            } else {
                tmp.rotate((float) 360);
                choice = -0.01;
            }

            boolean correctDecision = (tmp.clampMax - tmp.currentRot) > (tmp.currentRot - tmp.clampMin);
            if (correctDecision) bias += choice;
            else bias -= choice;

            if (!(bias >= 0 && bias <= 1)) break;

        }

        System.out.println("Bias: " + bias + ", Rotation: " + tmp.currentRot);

    }
}
class Word {
    Boolean isExplicit;
    String pathName;
    String[] params;
    int attr;
    public Word (String a, String[] b) {
        isExplicit = false;
        pathName = a;
        params = b;
    }
    public Word (int a) {
        isExplicit = true;
        attr = a;
    }
    public int getAttr() {
        if (isExplicit) return attr;
        else return -1;
    }
}
class Instruction {
    String inst;
    boolean required;
}
class Joint {
    int df = 1; //for now
    float clampMin, clampMax;
    float currentRot;
    float precision = (float) 0.01;
    public Joint (float[] range, float init) {
        if (range[0] < 0) {
            System.err.println("Cannot have a negative clamp value; taking magnitude of input.");
            range[0] *= (float) -1;
        } else if (range[1] < 0) {
            System.err.println("Cannot have a negative clamp value; taking magnitude of input.");
            range [1] *= (float) -1;
        }
        float min = range[0], max = range[1];
        if (min > max) {
            float tmp = max;
            max = min;
            min = tmp;
        }
        clampMin = min;
        clampMax = max;
        System.out.println("This joint has a range of " + clampMin + " to " + clampMax + " degrees.");
        currentRot = init;
        if (!validateRot(init)) {
            System.err.println("Initial rotation of joint cannot be outside clamped range; setting rotation to minimum legal value for this joint.");
            currentRot = clampMin;
        }
        System.out.println("This joint is rotated at " + currentRot + " degrees.");
    }
    public boolean validateRot(float in) {
        return in >= clampMin && in <= clampMax;
    }
    public void rotate(float in) {
        boolean negative = in < 0;
        float amt = in;
        if (negative) amt *= (float) -1;

        while (amt > 0) {
            if (negative) {
                if (!this.validateRot(currentRot - precision)) {
                    //System.err.println(precision + " degree away from exceeding minimum clamp.");
                    break;
                }
                currentRot -= precision;
            } else {
                if (!this.validateRot(currentRot + precision)) {
                    //System.err.println(precision + " degree away from exceeding maximum clamp.");
                    break;
                }
                currentRot += precision;
            }
            amt -= precision;
        }

        //System.out.println("Joint now rotated at " + currentRot + " degrees.");
    }
}
class myVector {
    int size;
    float clampMin, clampMax;
    float[] data;
}
