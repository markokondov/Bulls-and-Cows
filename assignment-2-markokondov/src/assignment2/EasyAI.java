package assignment2;

import java.util.ArrayList;

public class EasyAI extends Computer{

    public EasyAI() {
        setSecretCode();
        prevGuesses = new ArrayList<>();
        bulls = new ArrayList<>();
        cows = new ArrayList<>();
    }

    //Makes a random guess - the same way the code is generated - adds the guess to the prevGuesses list so it can be read later if the user wanted to save the game
    @Override
    public String makeGuess() {
        String guess = generateRandomCode();
        prevGuesses.add(guess);
        return guess;
    }

    @Override
    public String toString() {
        return "Easy";
    }

}
