package assignment2;

import java.util.*;

//Note HardAI will not work if BullsAndCows.CODE_LENGTH is different to 4 (default value)
public class HardAI extends Computer{
    private List<String> possibleCodes= new ArrayList<>();

    public HardAI() {
        generateAllPossibleCodes();
        setSecretCode();
        prevGuesses = new ArrayList<>();
        bulls = new ArrayList<>();
        cows = new ArrayList<>();
    }

    //Fills the list possibleCodes with all possible codes - first adds all possible numbers starting with 0, then adds all other codes (regardless if valid or not up to 9876 (highest possible number)
    //Then, iterates through each code, removing it if invalid (using function below)
    private void generateAllPossibleCodes() {
        for (int i = 100; i < 1000; i++) {
            possibleCodes.add("0" + i);
        }
        for (int i = 1023; i < 9877; i++) {
            possibleCodes.add("" + i);
        }
        Iterator<String> myIterator = possibleCodes.iterator();
        while (myIterator.hasNext()) {
            String code = myIterator.next();
            if (!isValidCode(code)) {
                myIterator.remove();
            }
        }
    }

    //Checks if the code passed is valid (used in the generation of all possible codes)
    private boolean isValidCode(String code) {
        if (code.length() != 4) {
            return false;
        }
        for (int i = 0; i < code.length(); i ++) {
            for (int j = 0; j < code.length(); j++) {
                if (code.charAt(i) == code.charAt(j) && j != i) {
                    return false;
                }
            }
        }
        return true;
    }

    //If it is the first guess, selects a random possible code. Otherwise, uses the prune function to narrow down the possible codes based on previous bulls and cows, and then picks from those.
    //Also removes the made guess, so it is not a possible guess for the next round (as either will be correct or wrong)
    @Override
    public String makeGuess() {
        String guess;
        if (prevGuesses.size() == 0) {
            int randomIndex = (int)(Math.random()*possibleCodes.size());
            guess = possibleCodes.get(randomIndex);
        } else {
            prune(prevGuesses.get(prevGuesses.size()-1), bulls.get(bulls.size() - 1), cows.get(cows.size()-1));
            int randomIndex = (int)(Math.random()*possibleCodes.size());
            guess = possibleCodes.get(randomIndex);
        }
        prevGuesses.add(guess);
        possibleCodes.remove(guess);
        return guess;
    }

    //Compares all possible codes to the previous guess, and removes them from possible guesses if the number of bulls or cows don't match
    private void prune(String code, int numBulls, int numCows) {
        Iterator<String> myIterator = possibleCodes.iterator();
        while (myIterator.hasNext()) {
            String possibleGuess = myIterator.next();
            if ((BullsAndCows.numberOfBullsAndCows(possibleGuess, code, true) != numBulls) || (BullsAndCows.numberOfBullsAndCows(possibleGuess, code, false) != numCows)) {
                myIterator.remove();
            }
        }
    }

    @Override
    public String toString() {
        return "Hard";
    }
}
