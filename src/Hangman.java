/**
 *	Hangman
 *
 *	A version of Hangman built with a functional philosophy. A few
 *	pattern that you might notice are, recursive functions, pure functions,
 *	higher order functions and dependency injection. Btw, htf do you make a build system
 *	because bash is totally wrong.
 *
 *	@author Sam Olaogun
 */

import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileReader;
import java.io.BufferedReader;

class Hangman {

	public static final String ROOT_PATH = "../lib/";

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

		// Reads the "words.txt" file from same directory
		try {
			FileReader fr = new FileReader(ROOT_PATH + Dictionaries.COUNTRIES.getPath());
			BufferedReader br = new BufferedReader(fr);
			read(br, wordChoices);
		} catch (Exception e) {
			System.out.println(e);
		}

		// Shall we begin
		play(new Scanner(System.in), wordChoices);
	}

	/**
	 *	Higher order function to handle gameplay
	 *
	 *	@param scn          Console IO
	 *	@param wordChoices  Passes the library to the guess function
	 */
	private static void play(Scanner scn, ArrayList<String> wordChoices) {
		// This is where the state of each character is managed
		LinkedHashMap<Character, Boolean> model = new LinkedHashMap<>();

		// A tokenized form of the chosen string
		char[] view = wordChoices.get((int) Math.floor(Math.random() * wordChoices.size())).toLowerCase().toCharArray();
		fillFalse(view, model, 0);

		final int len = model.size();
		
		// Begins guessing
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
		count += picked(scn.next().toLowerCase().charAt(0), model, view);

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

	private static void fillFalse(char[] chars, LinkedHashMap<Character, Boolean> model, int count) {
		if (count < chars.length) {
			model.put(chars[count], false);
			fillFalse(chars, model, ++count);
		}
	}

	/**
	 *	Recursively parse the Buffer and add product
	 *	to the {@code wordChoices} List
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
			} else {
				System.out.print('*');
			}
			view(view, model, ++count);
		} else {
			System.out.println();
		}
	}

}
