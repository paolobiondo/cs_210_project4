import stdlib.StdOut;

public class Ramanujan1 {
    // Entry point.
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);

        for (int a = 1; (a * a * a) <= n; a++) {
            int aCube = a * a * a;

            for (int b = a; (b * b * b) <= (n - aCube); b++) {
                int bCube = b * b * b;

                for (int c = a + 1; (c * c * c) <= n; c++) {
                    int cCube = c * c * c;

                    for (int d = c; (d * d * d) <= (n - cCube); d++) {
                        int dCube = d * d * d;

                        if (cCube + dCube == aCube + bCube) {
                            StdOut.print((cCube + dCube) + " = " + a + "^3 + " + b + "^3 = ");
                            StdOut.print(c + "^3 + " + d + "^3\n");
                        }
                    }
                }
            }
        }
    }
}
