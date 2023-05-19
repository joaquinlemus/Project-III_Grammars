import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * AFD.java
 * 
 * @author Joaquín Lemus 
 * @author Ernesto Aragón
 * @author Jorge de Mata
 * 
 * Date: 21-05-2023
 */

public class AFD {

    // private int number_of_states = 0;
    private ArrayList<String> plainFinalState;
    private ArrayList<String> alphabet;
    private ArrayList<String> plain_text_matrix;
    private ArrayList<Integer> finalState;


	public AFD(String path) {
        plain_text_matrix = new ArrayList<>();
        try {
            File afd = new File(path);
            Scanner sc = new Scanner(afd);
            // number_of_states = Integer.parseInt(sc.nextLine());
            plainFinalState = new ArrayList<>(Arrays.asList(sc.next().split(",")));
            finalState = getIntegerArray(plainFinalState);
            alphabet = new ArrayList<>(Arrays.asList(sc.next().split(",")));
            while (sc.nextLine() != null) {
                plain_text_matrix.add(sc.next());
            }
            sc.close();
        } catch (Exception e) {
        }
	}


	public int getTransition(int currentState, char symbol) {
		char[][] stateMatrix = getStateMatrix(plain_text_matrix);
        int s = getRowIndex(alphabet, symbol);
		return Character.getNumericValue(stateMatrix[s][currentState]);
	}


	public boolean evaluate(String string) {
		int LENGTH = string.length();
        int currentState = 1;
        for (int i = 0; i < LENGTH; i++) {
            char actualCharacter = string.charAt(i);
            currentState = getTransition(currentState, actualCharacter);
        }
        if (isFinal(currentState)) {
            return true;       
        }
		return false;
	}


	public boolean[] evaluateMany(String[] strings) {
        boolean[] evaluation = new boolean[strings.length];
        int index = 0;
        for (String string : strings) {
            evaluation[index] = evaluate(string);
            index++;
        }
		return evaluation;	
	}


	public boolean isFinal(int currentState) {
		return finalState.contains(currentState);
	}


	private char[][] getStateMatrix(ArrayList<String> plain_text_matrix) {
        char[][] stateMatrix = new char[plain_text_matrix.size()][plain_text_matrix.get(0).length()];
        int k = 0;
        for (int i = 0; i < plain_text_matrix.size(); i++) {
            String state = plain_text_matrix.get(i);
            int l = 0;
            for (int j = 0; j < state.length(); j++) {
                if (state.charAt(j) != ',') {
                    stateMatrix[k][l] = state.charAt(j);
                    l++;
                }
            }
            k++;        
        }
        return stateMatrix;
    }

	public int getRowIndex(ArrayList<String> alphabet, char symbol) {
        String charToString = String.valueOf(symbol);
        return alphabet.indexOf(charToString);
    }

	private ArrayList<Integer> getIntegerArray(ArrayList<String> stringArray) {
        ArrayList<Integer> IntegerArray = new ArrayList<Integer>();
        for(String stringValue : stringArray) {
            if (stringValue.equals(",")){
                continue;
            } else {
                IntegerArray.add(Integer.parseInt(stringValue));
            }
        }       
        return IntegerArray;
	}
}