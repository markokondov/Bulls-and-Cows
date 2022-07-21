package assignment2;

import java.util.ArrayList;

public class MediumAI extends Computer{

    public MediumAI() {
        setSecretCode();
        prevGuesses = new ArrayList<>();
        bulls = new ArrayList<>();
        cows = new ArrayList<>();
    }

    //Makes a random guess the same way that the code is generated, if the code is the same as a previous guess (checked by checking the list), then keep guessing until unique
    @Override
    public String makeGuess() {
        boolean valid = false;
        String guess = null;
        while (!valid) {
            guess = generateRandomCode();
            if (!prevGuesses.contains(guess)) {
                valid = true;
            }
        }
        prevGuesses.add(guess);
        return guess;
    }
    
    @Override
    public String toString() {
        return "Medium";
    }
}
