import java.util.ArrayList;
import java.util.Arrays;
import java.io.BufferedReader;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class Gramatica {
	
	public static void main(String[] args) {
		List<String> grammar = new ArrayList<>();
		String path = args[0];

        try (FileReader reader = new FileReader(path);
            BufferedReader br = new BufferedReader(reader)) {
            String instruction;
            while ((instruction = br.readLine()) != null) {
                grammar.add(instruction);
            }
		} catch (IOException e) {
			e.printStackTrace();
		}
		ArrayList<String> nonTerminal = new ArrayList<>(Arrays.asList(grammar.get(0).split(",")));
		ConcurrentLinkedQueue<String> rules = productionRules(getRules(grammar), nonTerminal);
		ArrayList<String> alphabet = new ArrayList<>(Arrays.asList(grammar.get(1).split(",")));
		List<String> states = getNewStates(productionRules(getRules(grammar), nonTerminal), nonTerminal);
		List<String> lambda = lambdaStates(productionRules(getRules(grammar), nonTerminal), nonTerminal);

		AFN afn = new AFN(rules, alphabet, states, lambda);
		System.out.println(rules);
		System.out.println(afn.filterStates());
		System.out.println(afn.filterInstructions());
		List<String> y = afn.filterStates();
		
		afn.printMatrix(afn.afnMatrix(y));




		// System.out.println(grammar);
		// System.out.println(getRules(grammar));
		// System.out.println(productionRules(getRules(grammar), nonTerminal));
		// System.out.println(nonTerminal);
		// System.out.println(productionRules(getRules(grammar)).poll().charAt(0));
		// System.out.println(productionRules(getRules(grammar)).poll().charAt(1));
		// System.out.println(getNewStates(productionRules(getRules(grammar)), nonTerminal));
		// System.out.println(alphabet);
		// System.out.println(lambdaStates(productionRules(getRules(grammar)), nonTerminal));
		// System.out.println(filter("W->bbbX", nonTerminal));

	}

	public static void printMatrix(String[][] matrix) {
		for (String[] row : matrix) {
            for (String elem : row) {
                System.out.print(elem + " ");
            }
            System.out.println();
        }
    }
	
	private static ConcurrentLinkedQueue<String> productionRules(List<String> rules, ArrayList<String> nonTerminal) {
		ConcurrentLinkedQueue<String> productionRules = new ConcurrentLinkedQueue<>();
		for (String rule : rules) {
			if (rule.length() > 5) {
				ConcurrentLinkedQueue<String> factorized = factor(rule);
				while (!factorized.isEmpty()) {
					productionRules.add(factorized.poll());
				}
			} else if (filter(rule, nonTerminal) && rule.length() >= 5) {
				ConcurrentLinkedQueue<String> special = specialCase(rule);
				while (!special.isEmpty()) {
					productionRules.add(special.poll());
				}
			} else {
				productionRules.add(rule);
			}
		}
		return productionRules;
	}

	private	static ConcurrentLinkedQueue<String> factor(String rule) {
		ConcurrentLinkedQueue<String> factorized = new ConcurrentLinkedQueue<>();
		String newRule = "";
		char variable = rule.charAt(0); 
		int counter = 0;
		int pos = 0;
		while (counter < numberOfRules(rule)) {
			if (pos == 0) {
				newRule = String.valueOf(variable) + "->" + rule.charAt(counter+3) + String.valueOf(variable) + (pos+1);
			} else if (counter == numberOfRules(rule)-1) {
				newRule = String.valueOf(variable) + pos + "->" + rule.charAt(counter+3) + rule.charAt(rule.length()-1);
			} else if (counter != numberOfRules(rule)-1) {
				newRule = String.valueOf(variable) + pos + "->" + rule.charAt(counter+3) + String.valueOf(variable) + (pos+1);
			}
			factorized.add(newRule);
			pos++;
			counter++;
		}
		return factorized;
	}

	private static ConcurrentLinkedQueue<String> specialCase(String rule) {
		ConcurrentLinkedQueue<String> factorized = new ConcurrentLinkedQueue<>();
		String newRule;
		char variable = rule.charAt(0);
		int LENGTH = rule.length() - 3;		
		for (int i = 1; i <= LENGTH; i++) {
			if (i == 1) {
				newRule = String.valueOf(variable) + "->" + rule.charAt(i+2) + String.valueOf(variable) + (i);
			} else if (i == LENGTH) {
				newRule = String.valueOf(variable) + (i-1) + "->" + rule.charAt(i+2);
			} else {
				newRule = String.valueOf(variable) + (i-1) + "->" + rule.charAt(i+2) + String.valueOf(variable) + (i);
			}
			factorized.add(newRule);
		}
		return factorized;
	}
	
	private static boolean filter(String rule, ArrayList<String> nonTerminal) {
		for (int i = 3; i < rule.length(); i++) {
			if (nonTerminal.contains(String.valueOf(rule.charAt(i)))) {
				return false;
			}
		}
		return true;
	}

	private static int numberOfRules(String rule) {;
		return rule.length() - 4;
	}
	
	private static List<String> getRules(List<String> grammar) {
		List<String> rules = new ArrayList<>();
		for (int i = 3; i < grammar.size(); i++) {
			rules.add(grammar.get(i));
		}
		return rules;
	}

	private static List<String> getNewStates(ConcurrentLinkedQueue<String> states, ArrayList<String> nonTerminal) {
		List<String> newStates = new ArrayList<>();
		while (!states.isEmpty()) {
			String state = states.poll();
			if (nonTerminal.contains(String.valueOf(state.charAt(0)))) {
				if (state.charAt(1) == '-') {
					if (!newStates.contains(String.valueOf(state.charAt(0)))) {
						newStates.add(String.valueOf(state.charAt(0)));
					}
				} else {
					String newState = state.charAt(0) + "" + state.charAt(1);
					if (!newStates.contains(newState)) {
						newStates.add(newState);
					}
				}
			}
		}
		return newStates;
	}

	private static List<String> lambdaStates(ConcurrentLinkedQueue<String> states, ArrayList<String> nonTerminal) {
		List<String> lamda = new ArrayList<>();
		while (!states.isEmpty()) {
			String state = states.poll();
			if (state.length() == 4 && nonTerminal.contains(String.valueOf(state.charAt(3)))) {
				lamda.add(state);
			}
		}
		return lamda;
	}
}