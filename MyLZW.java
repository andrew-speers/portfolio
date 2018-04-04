/*************************************************************************
 *  Compilation:  javac MyLZW.java
 *  Execution:    java MyLZW - [n | r | m] < input.txt > output.lzw  (compress)
 *  Execution:    java MyLZW + < input.lzw > output.whatever   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *
 *  Compress or expand binary input from standard input using MyLZW.
 *  Andrew Speers, 2/14/2017
 *
 *  While it is the case that each mode builds on top of another in terms
 *  of complexity, the expansion and compression steps in their three modes,
 *  respectively, have been separated into 6 different methods altogether.
 *************************************************************************/
public class MyLZW {
    private static final int R = 256;        // number of input chars
    private static int W = 9;          //codeword width
    private static int L = 512;       // number of codewords = 2^W
    private static final double zeus = 1.1; //the ratio of ratios

    private static void ncompress() {

        BinaryStdOut.write('n' , 8);

        String input = BinaryStdIn.readString();
        TST<Integer> st = new TST<Integer>();
        for (int i = 0; i < R; i++)
            st.put("" + (char) i, i);
        int code = R+1;  // R is codeword for EOF

        while (input.length() > 0) {
            //The "do nothing" step, modifying W and L to accommodate a variable
            //length codeword width.
            if (code >= L && W != 16) {
                W++;
                L *= 2;
            }

            String s = st.longestPrefixOf(input);  // Find max prefix match s.
            BinaryStdOut.write(st.get(s), W);      // Print s's encoding.
            int t = s.length();
            if (t < input.length() && code < L)    // Add s to symbol table.
                st.put(input.substring(0, t + 1), code++);
            input = input.substring(t);            // Scan past s in input.
        }
        BinaryStdOut.write(R, W);
        BinaryStdOut.close();
    }

    private static void rcompress() {

        BinaryStdOut.write('r' , 8);

        String input = BinaryStdIn.readString();
        TST<Integer> st = new TST<Integer>();
        for (int i = 0; i < R; i++)
            st.put("" + (char) i, i);
        int code = R+1;  // R is codeword for EOF

        while (input.length() > 0) {

            //The "do nothing" step, as above.
            if (code >= L && W != 16) {
                W++;
                L *= 2;
            }

            //The "reset" step, which we encounter when the codebook is full and
            //therefore time to throw it out.  It's simply a matter of mimicking
            //what happened in the set up before we entered this while loop.
            if (code == 65536) {
                st = new TST<Integer>();
                for (int i = 0; i < R; i++)
                    st.put("" + (char) i, i);
                W = 9;
                L = 512;
                code = R + 1;
            }

            String s = st.longestPrefixOf(input);  // Find max prefix match s.
            BinaryStdOut.write(st.get(s), W);      // Print s's encoding.
            int t = s.length();
            if (t < input.length() && code < L)    // Add s to symbol table.
                st.put(input.substring(0, t + 1), code++);
            input = input.substring(t);            // Scan past s in input.
        }
        BinaryStdOut.write(R, W);
        BinaryStdOut.close();
    }

    private static void mcompress() {

        BinaryStdOut.write('m' , 8);

        //Monitoring variables
        boolean timeToMonitor = false;
        long unprocessedData = 0, processedData = 0;
        double compRatio = 0, oldRatio = 0;

        String input = BinaryStdIn.readString();
        TST<Integer> st = new TST<Integer>();
        for (int i = 0; i < R; i++)
            st.put("" + (char) i, i);
        int code = R+1;  // R is codeword for EOF

        while (input.length() > 0) {

            //The "do nothing" step.
            if (code >= L && W != 16) {
                W++;
                L *= 2;
            }

            //The "ratio" step, and we make a check to avoid divide by zero errors.
            if (processedData != 0) compRatio = (double) unprocessedData / (double) processedData;

            //The "reset" step, only now we maintain a flag to run instructions
            //when it's time to begin monitoring.
            if (code == 65536 || timeToMonitor) {
                if (timeToMonitor) {
                    if (oldRatio / compRatio > zeus) {
                        st = new TST<Integer>();
                        for (int i = 0; i < R; i++)
                            st.put("" + (char) i, i);
                        W = 9;
                        L = 512;
                        code = R + 1;

                        //Unique to the monitor step.
                        timeToMonitor = false;
                        processedData = 0;
                        unprocessedData = 0;
                    } //else System.err.print((char) 13);
                } else {
                    oldRatio = compRatio;
                    timeToMonitor = true;
                }
            }

            String s = st.longestPrefixOf(input);  // Find max prefix match s.
            BinaryStdOut.write(st.get(s), W);      // Print s's encoding.
            int t = s.length();

            //
            unprocessedData += 8 * t;
            processedData   += W;
            //

            if (t < input.length() && code < L)    // Add s to symbol table.
                st.put(input.substring(0, t + 1), code++);
            input = input.substring(t);            // Scan past s in input.
        }
        BinaryStdOut.write(R, W);
        BinaryStdOut.close();
    }

    private static void nexpand() {
        String[] st = new String[65536];
        int i; // next available codeword value

        // initialize symbol table with all 1-character strings
        for (i = 0; i < R; i++)
            st[i] = "" + (char) i;
        st[i++] = "";                        // (unused) lookahead for EOF

        int codeword = BinaryStdIn.readInt(W);
        if (codeword == R) return;           // expanded message is empty string
        String val = st[codeword];

        while (true) {

            //The "do nothing" step, same as it was in the compression step.
            if (i >= L - 1 && W != 16) {
                W++;
                L *= 2;
            }

            BinaryStdOut.write(val);
            codeword = BinaryStdIn.readInt(W);
            if (codeword == R) break;
            String s = st[codeword];
            if (i == codeword) s = val + val.charAt(0);   // special case hack
            if (i < L) st[i++] = val + s.charAt(0);
            val = s;
        }
        BinaryStdOut.close();
    }

    private static void rexpand() {
        String[] st = new String[65536];
        int i; // next available codeword value

        // initialize symbol table with all 1-character strings
        for (i = 0; i < R; i++)
            st[i] = "" + (char) i;
        st[i++] = "";                        // (unused) lookahead for EOF

        int codeword = BinaryStdIn.readInt(W);
        if (codeword == R) return;           // expanded message is empty string
        String val = st[codeword];

        while (true) {

            //The "reset" check, which now "preempts" filling the entire codebook
            //as a consequence of the first run through the expansion not
            //producing a codeword.
            if (i == 65535) {

                //Reset sizes
                W = 9;
                L = 512;

                //New codebook with first 256 elements
                st = new String[65536];
                for (i = 0; i < R; i++)
                    st[i] = "" + (char) i;
                st[i++] = "";

                //Mimicking what happened just before we entered this while loop
                codeword = BinaryStdIn.readInt(W);
                val = st[codeword];
            }

            //The "do nothing" check.
            if (i >= L - 1 && W != 16) {
                W++;
                L *= 2;
            }

            BinaryStdOut.write(val);
            codeword = BinaryStdIn.readInt(W);
            if (codeword == R) break;
            String s = st[codeword];
            if (i == codeword) s = val + val.charAt(0);   // special case hack
            if (i < L) st[i++] = val + s.charAt(0);
            val = s;
        }
        BinaryStdOut.close();
    }

    //The trick here is setting our bit counts to 8 and 9, respectively, as a
    //consequence of expansion always being one step behind compression, right up until
    //the bitter end.  We must also, on each reset, set bit counts back to 8 and 9,
    //instead of back to 0 & 0: a reset asks us to mimic the steps leading up to
    //the while loop.
    private static void mexpand() {
        //My variables
        boolean timeToMonitor = false; //Self-explanatory
        long unprocessedData = 8, processedData = 9;
        double compRatio = 0, oldRatio = 0;

        String[] st = new String[65536];
        int i; // next available codeword value

        //initialize symbol table with all 1-character strings
        for (i = 0; i < R; i++)
            st[i] = "" + (char) i;
        st[i++] = "";                        // (unused) lookahead for EOF

        int codeword = BinaryStdIn.readInt(W);
        if (codeword == R) return;           // expanded message is empty string
        String val = st[codeword];

        while (true) {

            if (processedData != 0) compRatio = (double) unprocessedData / (double) processedData;

            //The "do nothing" check.
            if (i >= L - 1 && W != 16) {
                W++;
                L *= 2;
            }

            //
            BinaryStdOut.write(val);
            //

            //The "reset" check.
            if (i == 65535 || timeToMonitor) {
                if (timeToMonitor) {
                    if (oldRatio / compRatio > zeus) {
                        //Reset sizes
                        W = 9;
                        L = 512;

                        //New codebook with first 256 elements
                        st = new String[65536];
                        for (i = 0; i < R; i++)
                            st[i] = "" + (char) i;
                        st[i++] = "";

                        //Mimicking what happened just before we entered this while loop
                        codeword = BinaryStdIn.readInt(W);
                        val      = st[codeword];

                        //And here's what's unique to mexpand
                        timeToMonitor   = false;
                        unprocessedData = 8;
                        processedData   = 9;

                        continue;
                    }
                } else {
                    //The "true" beginning of the monitor step.
                    oldRatio      = compRatio;
                    timeToMonitor = true;
                }
            }

            //
            codeword = BinaryStdIn.readInt(W);
            processedData += W;
            //

            if (codeword == R) break;
            String s = st[codeword];
            if (i == codeword) s = val + val.charAt(0);   // special case hack
            if (i < L) st[i++] = val + s.charAt(0);
            val = s;

            //
            unprocessedData += 8 * val.length();
            //
        }
        BinaryStdOut.close();
    }

    private static void routeExpand() {
        char option = BinaryStdIn.readChar(8);
        if (option == 'n') nexpand();
        else if (option == 'r') rexpand();
        else mexpand();
    }

    private static void routeCompress(char option) {
        if (option == 'n') ncompress();
        else if (option == 'r') rcompress();
        else mcompress();
    }

    public static void main(String[] args) {
        if      (args[0].equals("-") && (args[1].charAt(0) == 110 || args[1].charAt(0) == 114 || args[1].charAt(0) == 109))
            routeCompress(args[1].charAt(0));
        else if (args[0].equals("+")) routeExpand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }

}
