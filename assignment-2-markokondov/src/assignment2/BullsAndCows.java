package assignment2;

import java.io.*;
import java.util.*;

public class BullsAndCows {
    private int round;
    private Computer computer;
    private User user;
    private String secretCodeFromComputer;
    private String secretCodeFromUser;
    //**ADVANCED CONFIGURATION FOR EASY AI** - Default = 7, adjust this value to change the number of rounds
    public static final int MAX_ROUNDS = 7;
    //**ADVANCED CONFIGURATION FOR EASY AI** - Default = 4, adjust this value to change the length of the code
    //Note - a code length different to the default is NOT compatible with the Hard difficulty
    public static final int CODE_LENGTH = 4;
    private boolean isFinished;
    private boolean playFromFile;
    private File preWrittenInput;

    public BullsAndCows() {
        round = 1;
    }

    //Asks the user if they would like to use a pre-written guesses from a file. Checks that the user has entered an appropriate response, then updates the playFromFile field appropriately
    public void setPlayFromFile(){
        System.out.println("Would you like to use pre-made guesses from a file?");
        boolean validInput = false;
        while (!validInput){
            String selection = Keyboard.readInput();
            if (selection.toLowerCase().charAt(0) == 'y') {
                this.playFromFile = true;
                validInput = true;
            } else if (selection.toLowerCase().charAt(0) == 'n') {
                this.playFromFile = false;
                validInput = true;
            } else {
                System.out.println("Please enter valid input - yes or no");
            }
        }
    }

    //Checks the user has entered a valid file name, for pre-written guesses - will only be called if the above function resulted in the playFromFile field being true
    public void processFile(){
        boolean validFileName = false;
        while (!validFileName) {
            System.out.print("Enter the file name (not including .txt): ");
            String fileName = Keyboard.readInput();
            fileName += ".txt";
            File file = new File(fileName);
            if (file.exists()) {
                this.preWrittenInput = file;
                validFileName = true;
            } else {
                System.out.println("Please enter a valid file name");
            }
        }
    }

    //Creates a user object with the users name (if entered, otherwise a default value if not), and sets it to the user field
    public void createUser() {
        System.out.print("Please enter your name: ");
        this.user = new User(Keyboard.readInput());
        if (user.getName().equals("")) {
            System.out.println("Are you sure you don't want to enter a name?");
            String response = "";
            while (true) {
                response = Keyboard.readInput();
                if (response.equals("")) {
                    System.out.println("Please enter a response");
                }else if (response.charAt(0) == 'y') {
                    user.setName("User who refused to set a name");
                    break;
                } else if (response.charAt(0) == 'n') {
                    createUser();
                    break;
                } else {
                    System.out.println("Sorry didn't quite get that.");
                }
            }
        }
    }

    //Asks the user preferred difficulty for the game, then creates a new object appropriately and sets it to the computer field
    public void setDifficulty() {
        boolean valid = false;
        while (!valid) {
            System.out.print("Please enter difficulty: Easy, Medium, Hard: ");
            String difficultyString = Keyboard.readInput().toLowerCase();
            if (difficultyString.equals("easy") || difficultyString.equals("e")) {
                this.computer = new EasyAI();
                valid = true;
            } else if (difficultyString.equals("medium") || difficultyString.equals("m")) {
                this.computer = new MediumAI();
                valid = true;
            } else if (difficultyString.equals("hard") || difficultyString.equals("h")) {
                this.computer = new HardAI();
                valid = true;
            } else {
                System.out.println("Invalid input - please enter \"Easy\", \"Medium\" or \"Hard\"");
            }
        }
        System.out.println("You chose: " + computer);
    }

    //Checks a guess (passed as a parameter) against a code (also a parameter) for either bulls or cows (checked by the boolean parameter - true for bulls, false for cows) - returns the amount
    public static int numberOfBullsAndCows(String guess, String code, boolean isBull) {
        int result = 0;
        for (int i = 0; i < guess.length(); i++) {
            for (int j = 0; j < guess.length(); j++) {
                if (guess.charAt(i) == code.charAt(j) && (isBull == (i == j))) {
                    result++;
                    break;
                }
            }
        }
        return result;
    }

    //The computer makes a guess, then the number of bulls and number of cows is passed to a list stored in the computer object - if the guess equals the users code, then return true (and the game will end as in the start function)
    public boolean processComputerGuess(){
        String computerGuess = computer.makeGuess();
        System.out.println("Computers guess: ");
        System.out.println(computerGuess);
        computer.addBull(numberOfBullsAndCows(computerGuess, secretCodeFromUser, true));
        computer.addCow(numberOfBullsAndCows(computerGuess, secretCodeFromUser, false));
        if (compareGuess(computerGuess, secretCodeFromUser)) {
            System.out.println("The computer wins!! :(");
            isFinished = true;
            return true;
        }
        return false;
    }

    //Reads and processes each line of the file (which is checked by the processFile function) - and updates the user objects bulls and cows appropriately - similar to processComputerGuess()
    public boolean processFileGuess(File file){
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("Round: " + this.round);
                System.out.println("The file guessed: ");
                System.out.println(line);
                user.addPreviousGuessFromFile(line);
                user.addBull(numberOfBullsAndCows(line, secretCodeFromComputer, true));
                user.addCow(numberOfBullsAndCows(line, secretCodeFromComputer, false));
                if (compareGuess(line, secretCodeFromComputer)) {
                    System.out.println(user.getName() + " wins!! :)");
                    isFinished = true;
                    return true;
                }
                System.out.println();
                if (processComputerGuess()) {
                    return true;
                }
                this.round++;
                System.out.println("---");
            }
            this.playFromFile = false;
            return false;
        } catch (IOException e) {
            System.out.println("Input output error");
            return false;
        }
    }


    //If there is a current file to read - then process the guesses using above function, else get a guess from the user and update the users fields appropriately
    public boolean processUserGuess(boolean isFile) {
        if (isFile) {
            return processFileGuess(preWrittenInput);
        } else {
            System.out.println(user.getName() + "'s guess: ");
            String userGuess = user.makeGuess();
            user.addBull(numberOfBullsAndCows(userGuess, secretCodeFromComputer, true));
            user.addCow(numberOfBullsAndCows(userGuess, secretCodeFromComputer, false));
            if (compareGuess(userGuess, secretCodeFromComputer)) {
                System.out.println(user.getName() + " wins!! :)");
                isFinished = true;
                return true;
            }
            return false;
        }
    }

    //Called at the end of the program - prompts the user to enter a file name, and writes all prior guesses, bulls and cows to the file, using objects pre-defined and written fields
    public void saveGame() {
        System.out.print("What would you like to call your file? (don't add .txt at the end): ");
        String fileName = Keyboard.readInput();
        fileName += ".txt";
        File gameFile = new File("saves/" + fileName);
        if (!gameFile.exists()) {
            try {
                gameFile.createNewFile();
            } catch (IOException e) {
                System.out.println("IOException in save game file creation");
            }
        }
        try (PrintWriter writer = new PrintWriter(new FileWriter(gameFile))) {
            writer.println("Bulls and Cows game - " + user.getName() + " vs the computer");
            writer.println("Selected difficulty: " + computer);
            writer.println("Your code: " + secretCodeFromUser);
            writer.println("Computer's code: " + secretCodeFromComputer);
            writer.println("---");
            List<String> userGuesses = user.getPrevGuesses();
            List<String> computerGuesses = computer.getPrevGuesses();
            for (int i = 0; i < user.getBulls().size(); i++) {
                writer.println("Round " + (i + 1));
                writer.println(user.getName() + " guessed: " + userGuesses.get(i));
                writer.println("They got " + user.getBulls().get(i) + " bulls and " + user.getCows().get(i) + " cows");
                if (user.getBulls().get(i) == CODE_LENGTH) {
                    writer.println(user.getName() + " won!");
                    break;
                }
                writer.println("The computer guessed: " + computerGuesses.get(i));
                writer.println("They got " + computer.getBulls().get(i) + " bulls and " + computer.getCows().get(i) + " cows");
                if (computer.getBulls().get(i) == CODE_LENGTH) {
                    writer.println("The computer won!");
                }
                writer.println("---");
                if (i + 1 == MAX_ROUNDS) {
                    writer.println("Game ended in a draw.");
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error in file writer in save game function");
        }

    }

    //Prints the bulls and cows to the program and finishes the game if the guess equals the code.
    public boolean compareGuess(String guess, String code) {
        int noOfBulls = numberOfBullsAndCows(guess, code, true);
        int noOfCows = numberOfBullsAndCows(guess, code, false);
        System.out.println(noOfBulls + " Bulls and " + noOfCows + " Cows");
        if (guess.equals(code)) {
            this.isFinished = true;
            return true;
        } else {
            return false;
        }
    }

    //Uses above functions to process a round starting with a file (if playFromFile is true), then the computer - if at any point the guess is true, the round will finish
    public void playRound() {
        if (playFromFile) {
            if (processUserGuess(true)) {
                isFinished = true;
                return;
            }
        }
        System.out.println("Round: " + round);
        if (processUserGuess(false)) {
            isFinished = true;
            return;
        }
        System.out.println();
        if (processComputerGuess()) {
            isFinished = true;
            return;
        }
        System.out.println("---");
        this.round++;
    }

    //Runs previous functions, in the appropriate order, with prompts.
    public void start() {
        System.out.println("Welcome to Bulls and Cows");
        setPlayFromFile();
        if (playFromFile) {
            processFile();
        }
        createUser();
        System.out.println("Welcome " + user.getName());
        setDifficulty();
        user.setCode();
        this.secretCodeFromUser = user.getSecretCode();
        this.secretCodeFromComputer = computer.getSecretCode();
        while (!isFinished && this.round <= MAX_ROUNDS) {
            playRound();
        }
        if (round > MAX_ROUNDS) {
            System.out.println("The game has ended in a draw.");
        }
        System.out.println("Would you like to save this game to a file?");
        String answer = Keyboard.readInput().toLowerCase();
        if (answer.charAt(0) == 'y') {
            saveGame();
        }
        System.out.println("Hope you enjoyed - please play again soon :)");
    }

    public static void main(String[] args) {
        new BullsAndCows().start();
    }
}
