package assignment2;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;
    private String secretCode;
    private List<String> prevGuesses;
    private List<Integer> bulls;
    private List<Integer> cows;

    //Initiates the Lists, and sets the users name
    public User(String name) {
        this.name = name;
        prevGuesses = new ArrayList<>();
        bulls = new ArrayList<>();
        cows = new ArrayList<>();
    }

    public void setName(String name){
        this.name = name;
    }

    //Sets the users secret code if valid (defined by another function) - if not valid, will ask again.
    public void setCode() {
        System.out.println("Please enter your secret code:");
        String secretCode = "";
        while(true) {
            secretCode = Keyboard.readInput();
            if (validCode(secretCode)){
                break;
            }
        }
        this.secretCode = secretCode;
        System.out.println("---");
    }

    public String getSecretCode() {
        return this.secretCode;
    }

    //called to make a guess (if the code is valid), if not valid, will inform why and ask again
    public String makeGuess() {
        String guess = "";
        while (true) {
            guess = Keyboard.readInput();
            if (validCode(guess)) {
                break;
            }
        }
        prevGuesses.add(guess);
        return guess;
    }

    //Checks for the right length (prompting the user if too short or long), for any number repeats, and for any non-digits - only returns true if valid code
    private boolean validCode(String guess) {
        if (guess.length() > BullsAndCows.CODE_LENGTH) {
            System.out.println("Your code is too long - please enter a " + BullsAndCows.CODE_LENGTH + " digit code");
            return false;
        } else if (guess.length() < BullsAndCows.CODE_LENGTH) {
            System.out.println("Your code is too short - please enter a " + BullsAndCows.CODE_LENGTH + " digit code");
            return false;
        } else if (areRepeats(guess)) {
            System.out.println("Each code must be unique - please try again");
            return false;
        } else if (!allNumbers(guess)) {
            System.out.println("Each character must be a digit - please try again");
            return false;
        } else {
            return true;
        }
    }

    //Checks if the String is all numbers by trying to parse them into an int
    private boolean allNumbers(String guess) {
        try {
            Integer.parseInt(guess);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    //Checks if any of the characters in the guess are repeats of each other
    private boolean areRepeats(String guess) {
        for (int i = 0; i < guess.length(); i++) {
            for (int j = 0; j < guess.length(); j++) {
                if (guess.charAt(i) == guess.charAt(j) && i != j) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<String> getPrevGuesses() {
        return prevGuesses;
    }

    public void addPreviousGuessFromFile(String guess) {
        this.prevGuesses.add(guess);
    }

    public String getName() {
        return this.name;
    }

    public List<Integer> getBulls() {
        return bulls;
    }

    public void addBull(Integer bull) {
        this.bulls.add(bull);
    }

    public List<Integer> getCows() {
        return cows;
    }

    public void addCow(Integer cow) {
        this.cows.add(cow);
    }
}
