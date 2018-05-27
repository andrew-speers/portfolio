/**
 * -1 - No information available
 * 0 - End scenario
 * 1 -
 * 2 -
 * 3 - Break stress threshold
 * 4 - Mapping
 **/
import java.util.Hashtable;
import java.util.Scanner;
import java.util.Vector;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

@SuppressWarnings("unchecked")
public class red {

    private static Hashtable<String, Node> index;
    private static Hashtable<String, Word> dict;
    private static Hashtable<Word, String> revDict;

    public static void main (String[] args) throws IOException {
        int actions = 10;
        Scanner sc = new Scanner(System.in);
        index   = new Hashtable<Word, Node>();
        dict    = new Hashtable<String, Word>();
        revDict = new Hashtable<Word, String>();
        Vector<Integer> input = new Vector<Integer>(),
            buffer = new Vector<Integer>();
        Word current = new Word(0),
            p = new Word(1),
            s = new Word(0);
        index.put(p, new Node());
        index.put(s, new Node());
        while (true) {

            sc = new Scanner(System.in);
            //Print current state
            if (revDict.containsKey(current))
                System.out.println(revDict.get(current));
            else System.out.println(current.toString());

            buffer.add(curent);

            int in = sc.nextInt();
            if (in == -20) break;
            if (in != 4) input.add(in);
            else { //Mapping
                System.out.print("Action count: ");
                /*
                Vector<Integer> path = new Vector<Integer>();
                for (int i = buffer.size() - 1 - sc.nextInt(); i < buffer.size(); i++) {
                    path.add(buffer.get(i));
                }
                */
                System.out.print("Mapping: ");
                String tmp = sc.nextLine();
                dict.put(tmp, new Word(
            }

        }
    }
}
class Input extends Word {
    public Input(int[] a) {
        super(a);
    }
    public Input(int a) {
        super(a);
    }
}
class Node {
    Vector<Node> paths;
    public Node (Vector<Node> v) {
        paths = v;
    }
    public Node () {
        paths = new Vector<Node>();
    }
}
class Word {
    Vector<Integer> path;
    public int size() {
        return path.size();
    }
    public Word(int[] a) {
        path = new Vector<Integer>();
        for (int x : a) path.add(x);
    }
    public Word(int a) {
        path = new Vector<Integer>();
        path.add(a);
    }
    public String toString() {
        return path.toString();
    }
}
