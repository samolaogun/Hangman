/**
 *	Hangman
 *
 *	A version of Hangman built with a functional philosophy. A few
 *	pattern that you might notice are, recursive definitions, pure functions,
 *	and dependency injection.
 *
 *	@author Sam Olaogun
 */
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Arrays;
import java.io.FileReader;
import java.io.BufferedReader;

class Hangman {

	public static final String ROOT_PATH = "../lib/";

	// Consolidate dictionary and implementation
	private enum Dictionaries {
		COUNTRIES("countries.txt"),
		FRUITS("fruits.txt");

		private String path;

		private Dictionaries(String path) {
			this.path = path;
		}

		public String getPath() {
			return this.path;
		}
	}

	public static void main(String[] args) {
		ArrayList<String> wordChoices = new ArrayList<>();

		// TODO: Consolidate try/catch and dictionary
		try {
			// Load Dictionary
			FileReader fr = new FileReader(ROOT_PATH + Dictionaries.COUNTRIES.getPath());

			// Tokenize file contents
			BufferedReader br = new BufferedReader(fr);

			// Read file and pass on to wordChoices List
			read(br, wordChoices);
		} catch (Exception e) {
			System.out.println(e);
		}

		// Shall we begin
		play(new Scanner(System.in), wordChoices);
	}

	/**
	 *	Recursive definition of the play feature
	 *
	 *	@param scn          Console IO
	 *	@param wordChoices  Passes the library to the guess function
	 */
	private static void play(Scanner scn, ArrayList<String> wordChoices) {
		// This is where the state of each character is managed
		LinkedHashMap<Character, Boolean> model = new LinkedHashMap<>();

		// A tokenized form of the chosen string
		String word = wordChoices.get((int) Math.floor(Math.random() * wordChoices.size())).toLowerCase();
		char[] view = word.toCharArray();
		fillFalse(view, model, 0);

		// Evaluate length without spaces
		int diff = word.length() - word.replace(" ", "").length();
		int len = model.size() - diff;

		// Begin guessing
		guess(view, model, scn, wordChoices, 0, len);
	}

	/**
	 *	Handle each guess by tracking the guess count and
	 *	checking for completion
	 *
	 *	@param view         Orignial array of character values
	 *	@param model        Associative array without tautologies
	 *	@param scn          Console IO
	 *	@param count        Manages of correct guesses
	 *	@param wordChoices  Passes the library to the play function
	 */
	private static void guess(char[] view, LinkedHashMap<Character, Boolean> model, Scanner scn, ArrayList<String> wordChoices, int count, int len) {
		System.out.print(">>> Guess a character: ");

		// Adds each correct guess to a count
		count += picked(scn.next().charAt(0), model, view);

		// If the count of correct char matches the amount of possible chars, exit
		if (count >= len) {
			System.out.printf(">>> You tried %d times. Would you like to play again? Enter Y or N ", count);

			if (scn.next().equalsIgnoreCase("Y")) {
				play(scn, wordChoices);
			} else {
				System.exit(0);
			}
		} else {
			guess(view, model, scn, wordChoices, count, len);
		}
	}

	/**
	 *	Check the entered character against a set of conditionals
	 *
	 *	@param chr    Character being tested
	 *	@param model  Associative array without tautologies
	 *	@param view   Orignial array of character values
	 *	@return       Returns no value if no condition is met
	 */
	private static int picked(char chr, LinkedHashMap<Character, Boolean> model, char[] view) {
		// Prompts user and returns 1 if the guess is correct, else returns 0
		if (model.get(chr) != null) {
			if ((boolean) model.get(chr)) {
				System.out.printf("'%c' was already picked. Try something else ", chr);
			} else {
				System.out.printf("'%c'? Correct guess! ", chr);
				model.put(chr, true);
				view(view, model, 0);
				return 1;
			}
		} else {
			System.out.printf(chr + " Is not correct, try again ");
			model.put(chr, true);
		}
		view(view, model, 0);
		return 0;
	}

	/**
	*		Recursively fills the model with false because no values have been selected
	*
	*		@param view
	* 	@param model
	*		@param count
	*/
	private static void fillFalse(char[] view, LinkedHashMap<Character, Boolean> model, int count) {
		if (count < view.length) {
			model.put(view[count], false);
			fillFalse(view, model, ++count);
		}
	}

	/**
	 *	Recursively parse the buffer and add the product
	 *	to the {@code wordChoices} list
	 *
	 *	@param br           Buffer Stream
	 *	@param wordChoices  Library
	 */
	private static void read(BufferedReader br, ArrayList<String> wordChoices) {
		try {
			String ln;
			if ((ln = br.readLine()) != null) {
				wordChoices.add(ln);
				read(br, wordChoices);
			} else {
				br.close();
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 *	Recursively display the model if the position in the view array is less than
	 *	the length
	 *
	 *	@param view   Orignial array of character values
	 *	@param model  Associative array without tautologies
	 *	@param count  Position in the recursive call
	 */
	private static void view(char[] view, LinkedHashMap<Character, Boolean> model, int count) {
		if (count < view.length) {
			if (model.get(view[count])) {
				System.out.print(view[count]);
			} else if (view[count] == ' ') {
				System.out.print(" ");
			} else {
				System.out.print('*');
			}
			view(view, model, ++count);
		} else {
			System.out.println();
		}
	}

}
