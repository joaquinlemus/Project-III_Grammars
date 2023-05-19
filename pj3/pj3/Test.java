import java.util.Arrays;

public class Test {
    public static void main(String[] args) {
        String x =  "W->b";
        String[] y = x.split("");
        String state;
        String inst;
        String $ = null;
        int LENGTH = x.length();
        if (LENGTH == 5) {
            if (y[LENGTH-1].equals("b")) {
                // W->b
                state = y[0] + "" + y[1];
                inst = y[4];
            } else {
                state = y[0];
                inst = y[3] + "" + y[4];
            }
        } else if (LENGTH == 6) {
            state = y[0] + "" + y[1];
            inst = y[4] + "" + y[5];
        } else if (LENGTH == 4) {
            state = y[0];
            inst = y[3];
            $ = state;
        }
    }    

    public static void printMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }
}
