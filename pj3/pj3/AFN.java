import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.xml.stream.events.StartElement;

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
        int counter = 1;
        for (int i = 0; i < afn.length; i++) {
            for (int j = 0; j < afn[i].length; j++) {
                if (j == afn[i].length - 1) {
                    afn[i][j] = String.valueOf(counter);
                } else {
                    afn[i][j] = "0";
                }
            }
            counter++;
        }
        return afn;
    }
    
    
    public void fillAFNMatrix(String[][] matrix, String instruction, List<String> states, ArrayList<String> alphabet) {


        // W->B -> 4
        // W->b -> 4
        // W->aX -> 5
        // W->aW1 -> 6
        // W1->aX -> 6
        // W1->aW2 -> 7

        if (instruction.length() == 4) {
            
        } else if (instruction.length() == 5) {
            String row = String.valueOf(instruction.charAt(0)); 
            String column = String.valueOf(instruction.charAt(3)); 
            String state = String.valueOf(instruction.charAt(4));
            changeAFNMatrix(matrix, states.indexOf(state)+1, states.indexOf(row), alphabet.indexOf(column));
        }
    }

    // [W->aW1, W1->bX, X->bX, X->b, X->cY, Y->cY, Y->c, Y->d]
    // [W, W1, X, Y]
    // 3 estados nuevos -> X->b; Y->c; Y->d
    // X->b => X -> J
    // Y->c => Y -> K
    // Y->d => Y -> Z
    // [W, W1, X, J, Y, K, Z]

    public List<String> integrateNewStates(ConcurrentLinkedQueue<String> grammar, List<String> states, List<String> alphabet) {
        ConcurrentLinkedQueue<String> zelda = new ConcurrentLinkedQueue<>(grammar);
        List<String> integratedStates = new LinkedList<>(states);
        List<String> instructions = new ArrayList<>();
        List<String> link = new ArrayList<>();
        int counter = 0;
        char green_goblin = 74;
        while (!zelda.isEmpty()) {
            instructions.add(zelda.poll());
        }

        for (String inst : instructions) {
            if (inst.length() == 4 && alphabet.contains(String.valueOf(inst.charAt(3)))) {
                link.add(inst);
                counter++;
            }
        }

        if (counter == 0) {
            return null;
        } else {
            String state = null;
            for (String hen : link) {
                if (hen.charAt(1) == '-') {
                    state = String.valueOf(hen.charAt(0));
                } else {
                    state = String.valueOf(hen.charAt(0)) + "" + String.valueOf(hen.charAt(hen.charAt(1)));
                }
                String newState = String.valueOf(green_goblin);
                System.out.println(states.indexOf(state));
                integratedStates.add(states.indexOf(state)+1, newState);
                green_goblin++;
            }
        }
        return integratedStates;
    }


    public void changeAFNMatrix(String[][] matrix, int state, int row, int column) {
        matrix[row][column] = String.valueOf(state);
    }


    public void printMatrix(String[][] matrix) {
        for (String[] row : matrix) {
            for (String elem : row) {
                System.out.print(elem + " ");
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


    public List<String> convertStringStatesToNumbers(List<String> states) {
        List<String> statesAsNumbers = new ArrayList<>();
        int counter = 1;
        for (String state : states) {
            state = String.valueOf(counter);
            statesAsNumbers.add(String.valueOf(state));
            counter++;
        }
        return statesAsNumbers;
    }
}
