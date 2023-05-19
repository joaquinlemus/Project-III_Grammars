import java.util.concurrent.ConcurrentLinkedQueue;

public class gamma {
    public static void main(String[] args) {
        // String rule = "B->bbbbbb";
        // String newRule = "";
        // ConcurrentLinkedQueue<String> factorized = new ConcurrentLinkedQueue<>();
        // char variable = rule.charAt(0);
        // int LENGTH = rule.length() - 3;		
        // for (int i = 1; i <= LENGTH; i++) {
        //     if (i == 1) {
        //         newRule = String.valueOf(variable) + "->" + rule.charAt(i+2) + String.valueOf(variable) + (i);
        //     } else if (i == LENGTH) {
        //         newRule = String.valueOf(variable) + (i-1) + "->" + rule.charAt(i+2);
        //     } else {
        //         newRule = String.valueOf(variable) + (i-1) + "->" + rule.charAt(i+2) + String.valueOf(variable) + (i);
        //     }
        //     factorized.add(newRule);
        // }    
        // System.out.println(factorized);
        String[][] afn = extracted();

        extracted2(afn);
    }

    private static void extracted2(String[][] afn) {
        for (String[] row : afn) {
            for (String elem: row) {
                System.out.print(elem);
            }
            System.out.println();
        }
    }

    private static String[][] extracted() {
        String[][] afn = new String[4][5];
        for (int i = 0; i < afn.length; i++) {
            for (int j = 0; j < afn[i].length; j++) {
                afn[i][j] = "0";
            }
        }
        return afn;
    }
}
