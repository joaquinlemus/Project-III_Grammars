import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class AFN {

    private ConcurrentLinkedQueue<String> grammar;
    private ArrayList<String> alphabet;
    private List<String> states, lambdaStates;
    private ConcurrentLinkedQueue<String> unchanged = new ConcurrentLinkedQueue<>();


    public AFN(ConcurrentLinkedQueue<String> grammar, ArrayList<String> alphabet, List<String> states, List<String> lambdaStates) {
        this.grammar = grammar;
        ConcurrentLinkedQueue<String> x = new ConcurrentLinkedQueue<>(grammar);
        while (!x.isEmpty()) {
            unchanged.add(x.poll());
        }
        this.alphabet = alphabet;
        this.states = states;
        this.lambdaStates = lambdaStates;
    }

    
    public ConcurrentLinkedQueue<String> getGrammar() {
        return grammar;
    }


    public ArrayList<String> getAlphabet() {
        return alphabet;
    }


    public List<String> getStates() {
        return states;
    }


    public List<String> getLambdaStates() {
        return lambdaStates;
    }


    public ConcurrentLinkedQueue<String> getUnchanged() {
        return unchanged;
    }


    public String[][] afnMatrix(List<String> states) {
        String[][] afn = new String[states.size()][this.alphabet.size() + 1];
        for (int i = 0; i < afn.length; i++) {
            for (int j = 0; j < afn[i].length; j++) {
                afn[i][j] = "0";
            }
        }
        return afn;
    }

    public void printMatrix(String[][] matrix) {
        for (String[] row : matrix) {
            for (String elem: row) {
                System.out.print(elem);
            }
            System.out.println();
        }
    }


    public List<String> filterStates(ConcurrentLinkedQueue<String> grammar) {
        // link copia grammar y es la que se edita para que zelda trabaje y saque los estados
        ConcurrentLinkedQueue<String> link = new ConcurrentLinkedQueue<>(grammar);
        ConcurrentLinkedQueue<String> zelda = new ConcurrentLinkedQueue<>();
        while (!link.isEmpty()) {
            zelda.add(link.poll());
        }
        List<String> filterStates = new ArrayList<>();
        while (!zelda.isEmpty()) {
            String state = null;
            String x =  zelda.peek();
            String[] y = x.split("");
            int LENGTH = x.length();
            if (LENGTH == 5) {
                if (this.alphabet.contains(y[LENGTH-1])) {
                    state = y[0] + "" + y[1];
                } else {
                    state = y[0];
                }
            } else if (LENGTH == 6) {
                if (y[1].equals("-")) {
                    state = y[0];
                } else {
                    state = y[0] + "" + y[1];
                }
            } else if (LENGTH == 4) {
                state = y[0];
            }
            if (!filterStates.contains(state)) {
                filterStates.add(state);
            }
            zelda.poll();
        }
        return filterStates;
    }


    public List<String> filterInstructions(ConcurrentLinkedQueue<String> grammar) {
        // mario copia grammar y es la que se edita para que peach trabaje y saque las instrucciones
        ConcurrentLinkedQueue<String> mario = new ConcurrentLinkedQueue<>(grammar);
        ConcurrentLinkedQueue<String> peach = new ConcurrentLinkedQueue<>();
        while (!mario.isEmpty()) {
            peach.add(mario.poll());
        }
        List<String> instructions = new ArrayList<>();
        while (!unchanged.isEmpty()) {
            String inst = null;
            String x =  unchanged.peek();
            String[] y = x.split("");
            int LENGTH = x.length();
            if (LENGTH == 5) {
                if (this.alphabet.contains(y[LENGTH-1])) {
                    inst = y[4];
                } else {
                    inst = y[3] + "" + y[4];
                }
            } else if (LENGTH == 6) {
                if (y[1].equals("-")) {
                    inst = y[3] + "" + y[4] + "" + y[5];
                } else {
                    inst = y[4] + "" + y[5];
                }
            } else if (LENGTH == 4) {
                inst = y[3];
            } else if (LENGTH == 7) {
                inst = y[4] + "" + y[5] + "" + y[6];
            }
            
            instructions.add(inst);
            unchanged.poll();
        }
        return instructions;
    }
}
