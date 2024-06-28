import java.util.Scanner;
import java.util.Random;

public class Numbergame {
    public static void main(String args[]) {
        Scanner s = new Scanner(System.in);
        System.out.println("Welcome to the Number Guessing Game!");
        boolean playAgain;
        int totalRounds = 0;
        int totalScore = 0;

        do {

            Game game = new Game();
            game.playGame();
            totalRounds++;
            totalScore += game.getScore();

            System.out.print("Do you want to play again? (yes/no): ");
            playAgain = s.next().equalsIgnoreCase("yes");
        } while (playAgain);

        System.out.println("\nThank you for playing!");
        System.out.println("Total rounds played: " + totalRounds);
        System.out.println("Total score: " + totalScore);

        s.close();
    }
}

class Game {
    int numberToGuess;
    int maxAttempts = 10;
    int attemptsLeft;
    int score;
    Random random;
    Scanner scanner;

    public Game() {
        random = new Random();
        scanner = new Scanner(System.in);
        numberToGuess = random.nextInt(100) + 1;
        attemptsLeft = maxAttempts;
        score = 0;
    }

    public void playGame() {
        System.out.println("\nA new round has started! Try to guess the number between 1 and 100.");
        System.out.println("You have " + maxAttempts + " attempts.");

        boolean hasGuessedCorrectly = false;
        while (attemptsLeft > 0 && !hasGuessedCorrectly) {
            System.out.print("Enter your guess: ");
            int userGuess = scanner.nextInt();
            attemptsLeft--;
            float ratio = (float) numberToGuess / userGuess;

            if (ratio == 1) {
                score = attemptsLeft + 1;
                System.out.println(
                        "Correct! You've guessed the number in " + (maxAttempts - attemptsLeft) + " attempts.");
                hasGuessedCorrectly = true;
            } else if (0.75 < ratio && ratio < 1) {
                System.out.println("high");
            } else if (0 < ratio && ratio <= 0.75) {
                System.out.println("too high");
            } else if (1 < ratio && ratio <= 1.5) {
                System.out.println("low");
            } else if (1.5 < ratio) {
                System.out.println("too low");
            }

            if (!hasGuessedCorrectly && attemptsLeft == 0) {
                System.out.println("Sorry, you've used all your attempts. The number was " + numberToGuess + ".");
            }
        }
    }

    public int getScore() {
        return score;
    }

}