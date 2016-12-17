/*
 * Edmund Korley, 2016
 * A simple Java program that simulates a game of Cee-Lo (also known as 456) between the user and the computer.
 */

// We import the Scanner module responsible for receiving input from the user.
import java.util.Scanner;
// We import the Arrays module for the Arrays.sort method.
import java.util.Arrays;
// We import the random number generator that we will be using the generate dice numbers.
import java.util.Random;

// Our CeeLo class that groups together all the logic responsible for the game,
// as well as the execution logic.
public class CeeLo {
	/*
	 * We keep track of win, loses, and ties in order to print statistics after each turn.
	 */
	public static int wins = 0;
	public static int loses = 0;
	public static int ties = 0;

	/*
	 * Resolve the values of a dice roll to a single number.
	 *
	 * Invalid rolls = 0
	 * Rolls with two of the same number = the other number, [2-2-4] = 4, [6-5-5] = 6, and so on ...
	 * Rolls with all three number the same = the number + 6, [1-1-1] = 1 + 6 = 7, [5-5-5] = 5 + 6 = 11
	 * A [4-5-6] roll = 13
	 * 
	 * Once we have this absolute scale, it is easy to see who is the winner or if it is a tie.
	 */
	public static int resolveRoll(int[] diceRoll) {

		Arrays.sort(diceRoll); // Sort array for easier resolving
		if (validRoll(diceRoll)) {
			// [4-5-6] special case
			if (diceRoll[0] == 4 && diceRoll[1] == 5 && diceRoll[2] == 6) return 13;
			// If a triplet, return the number + 6
			if (diceRoll[0] == diceRoll[1] && diceRoll[1] == diceRoll[2]) return 6 + diceRoll[0];
			// Check for where the duplicate lies and return the other number
			if (diceRoll[0] == diceRoll[1]) return diceRoll[2];
			if (diceRoll[1] == diceRoll[2]) return diceRoll[0];
		}
		return 0;
	}

	/*
	 * Determines who won a given dice roll, or whether there was a tie.
	 * Because there are 3 outcomes (lose, tie, win), we use an integer the represent the different outcomes (-1, 0, 1).
	 */
	public static int determineWinner(int[] userRoll, int[] computerRoll) {
		int userOutcome;
		int computerOutcome;

		userOutcome = resolveRoll(userRoll);
		computerOutcome = resolveRoll(computerRoll);
		if (userOutcome == computerOutcome) {
			ties++;
			return 0;
		} else if (userOutcome < computerOutcome) {
			loses++;
			return -1;
		} else {
			wins++;
			return 1;
		}
	}

	/*
	 * Generates three random numbers.
	 */
	public static int[] rollDice(Random diceNumber) {
		int[] diceNumbers = new int[3]; // Using an array to keep track of the values of different dice.

		// Storing output of dice rolls (a random number between 1 and 6 inclusive)
		diceNumbers[0] = diceNumber.nextInt(6) + 1;
		diceNumbers[1] = diceNumber.nextInt(6) + 1;
		diceNumbers[2] = diceNumber.nextInt(6) + 1;
		return diceNumbers;
	}

	/*
	 * Roll the dice (both user and computer) and evaluate game.
	 */
	public static void playTurn(Random diceNumber) {
		int[] userDiceRoll = null; // Storing user dice rolls
		int[] computerDiceRoll = null; // Storing computer dice rolls

		// Giving both players chances to get a valid dice roll
		for (int i = 0; i < 3; ++i) {
			userDiceRoll = rollDice(diceNumber);
			if (validRoll(userDiceRoll)) break;
			System.out.println(String.format("You rolled a [%d-%d-%d], which is not valid. Attempt #%d.", userDiceRoll[0], userDiceRoll[1], userDiceRoll[2], i + 1));
			if (i == 2) System.out.println("You are out of turns and you rolled no valid numbers.");
		}
		System.out.println(String.format("Your final roll is a [%d-%d-%d].\n", userDiceRoll[0], userDiceRoll[1], userDiceRoll[2]));
		for (int i = 0; i < 3; ++i) {
			computerDiceRoll = rollDice(diceNumber);
			if (validRoll(computerDiceRoll)) break;
			System.out.println(String.format("The computer rolled a [%d-%d-%d], which is not valid. Attempt #%d.", computerDiceRoll[0], computerDiceRoll[1], computerDiceRoll[2], i + 1));
			if (i == 2) System.out.println("The computer is out of turns and the computer rolled no valid numbers.");
		}
		System.out.println(String.format("The computer's final roll is a [%d-%d-%d].\n", computerDiceRoll[0], computerDiceRoll[1], computerDiceRoll[2]));
		// After both players roll, we determine the winner of that round.
		int outcome = determineWinner(userDiceRoll, computerDiceRoll);
		if (outcome == 0) {
			System.out.println(String.format("It's a tie between [%d-%d-%d] and [%d-%d-%d]! So much suspence ...", userDiceRoll[0], userDiceRoll[1], userDiceRoll[2], computerDiceRoll[0], computerDiceRoll[1], computerDiceRoll[2]));
		} else if (outcome == 1) {
			System.out.println(String.format("Your [%d-%d-%d] beat the computer's [%d-%d-%d]! Great work :D", userDiceRoll[0], userDiceRoll[1], userDiceRoll[2], computerDiceRoll[0], computerDiceRoll[1], computerDiceRoll[2]));
		} else {
			System.out.println(String.format("Ah dang, your [%d-%d-%d] lost to the computer's [%d-%d-%d]! Roll again :|", userDiceRoll[0], userDiceRoll[1], userDiceRoll[2], computerDiceRoll[0], computerDiceRoll[1], computerDiceRoll[2]));
		}
		// We print statistics and reminder of menu commands.
		printStatistics();
		printHelp(false, true);
	}

	/*
	 * Determines if a given dice roll is valid.
	 */
	public static boolean validRoll(int[] diceRoll) {

		Arrays.sort(diceRoll); // Sort the array for easier validity checking
		// Special [4-5-6] winning case is valid
		if (diceRoll[0] == 4 && diceRoll[1] == 5 && diceRoll[2] == 6) {
			return true;
		}
		// Two elements in array are the same (array is sorted so they will be right next to each other),
		// we have a valid roll.
		if (diceRoll[0] == diceRoll[1] || diceRoll[1] == diceRoll[2]) {
			return true;
		}
		// If all elements are the same, we have a valid roll.
		if (diceRoll[0] == diceRoll[1] && diceRoll[1] == diceRoll[2]) {
			return true;
		}
		// All other situations are not valid.
		return false;
	}

	/*
	 * Prints statistics of wins, loses, and ties for both the user and the computer.
	 */
	public static void printStatistics() {
		System.out.println(String.format("\nYou have %d wins, %d loses, and %d ties so far.", wins, loses, ties));
		System.out.println(String.format("The computer has %d wins, %d loses, and %d ties so far.\n", loses, wins, ties));
	}

	/*
	 * Prints help or game instructions conditionally.
	 * 
	 * When the withGameInstructions boolean is true, it will print the game instructions,
	 * otherwise it prints the 
	 */
	public static void printHelp(boolean withGameInstructions, boolean shortVersion) {
		if (withGameInstructions == true && shortVersion == false) {
			System.out.println("Cee-Lo (also known as 456) is a dice game played between multiple people originating from mainland China.\n");
			System.out.println("=============================================================================");
			System.out.println("It is very simple to play. You have 3 6-sided dice.\nOne player rolls and gets three numbers, then the second player rolls gets three numbers.\n");
			System.out.println("If you get [4-5-6], then you automatically win!\nIf you get two of the same numbers, then the third different number is the number\nthat will use to compare against the other player's number.\n");
			System.out.println("For example, I roll [2-2-3] and you roll [6-6-1], so we are comparing [3] vs. [1],\nso my [3] is bigger than your [1] so I win that round.");
			System.out.println("Another example, if you roll three of the same number, that is better than any combination of two of the same number.\nEG: [2-2-2] is better than [3-3-6]");
			System.out.println("Again, if you roll a [4-5-6] then you automatically win!");
			System.out.println("You only have three chances to roll any of these patterns,\notherwise it is the computers turn to roll and even if it gets a 1 (i.e. [6-6-1]) it beats you.");
			System.out.println("Make sense? Well then go play and emerge a winner!\n");

			// ASCII art by R. Shawn Butler on http://www.chris.com/ascii/index.php?art=objects/dice
			System.out.print("" +
			"   _______\n" +
			"  /\\ o o o\\ \n" +
			" /o \\ o o o\\_______ \n" +
			"<    >------>   o /| \n" +
			" \\ o/  o   /_____/o| \n" +
			"  \\/______/     |oo| \n" +
			"        |   o   |o/  \n" +
			"        |_______|/ \n\n");
			
			// Recursively call a different configuration of our printHelp method.
			printHelp(false, true); 
		} else if (withGameInstructions == false && shortVersion == true) {
			System.out.println("Type (1) to shake dice, (2) to roll dice, (3) to print help, (4) for how to play, or (5) to quit game.");
		} else {
			System.out.println("============================");
			System.out.println("|   Let's play Cee-Lo      |");
			System.out.println("============================");
			System.out.println("| Options:                 |");
			System.out.println("|        1. Shake dice     |");
			System.out.println("|        2. Roll dice      |");
			System.out.println("|        3. Print help     |");
			System.out.println("|        4. How to play    |");
			System.out.println("|        5. Quit game      |");
			System.out.println("============================");
		}
	}

	/*
	 * Execution and user interaction logic.
	 */
	public static void main(String[] args) {
		int userChoice; // This will store the user choice as integer, representing different menu options as numbers.
		Scanner commandLineReader = new Scanner(System.in); // We instance the Scanner object, which will later parse user input.
		printHelp(false, false); // We print the initial help menu.
		Random diceNumber = new Random(); // Instance the Random object, for generating random numbers (for dice rolls).

		// This do-while loop runs the game logic (at least once) until the user selects to quit the game.
		do {
			// Get choice from user.
			System.out.print(">> ");
			userChoice = commandLineReader.nextInt();

			// Process user choice into game logic.
			switch (userChoice) {
				case 1: // shake dice
					System.out.println("Shaking dice. Good luck. Ready to roll?");
					rollDice(diceNumber); // Perform dummy roll of dice to turn off probabilities.
					printHelp(false, true); // Print shorter help text
					break;
				case 2: // roll dice
					System.out.println("Rolling dice.\n");
					playTurn(diceNumber);
					break;
				case 3: // print help
					printHelp(false, false);
					break;
				case 4: // print how to play
					printHelp(true, false);
					break;
				case 5: // quit game
					System.out.println("Thank you for playing. Have a great day!");
					break;
				default: // invalid response
					System.out.println("Please select a valid option.");
					printHelp(false, true);
			}
		} while (userChoice != 5); // while user has not chosen to quit game, run the above game logic

		// We tell the Scanner class that there will be no more input.
		commandLineReader.close();
	}

}
