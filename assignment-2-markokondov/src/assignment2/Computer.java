package assignment2;

import java.util.ArrayList;
import java.util.List;

public abstract class Computer {
    protected String secretCode;
    protected List<String> prevGuesses;
    protected List<Integer> bulls;
    protected List<Integer> cows;

    public void setSecretCode() {
        this.secretCode = generateRandomCode();
    }

    //Makes a list of all valid numbers, then repeatedly chooses one of these randomly until the code matches the appropriate length, removing it once chosen to ensure no repeats
    public String generateRandomCode() {
        List<Integer> numbers = new ArrayList<>();
        String result = "";
        for (int i = 0; i < 10; i++) {
            numbers.add(i);
        }
        for (int i = 0; i < BullsAndCows.CODE_LENGTH; i++) {
            int index = (int)(Math.random() * (numbers.size()));
            result += numbers.get(index);
            numbers.remove(index);
        }
        return result;
    }

    //Accounts for different ways to make a guess in each difficulty
    public abstract String makeGuess();

    public String getSecretCode() {
        return this.secretCode;
    }

    public List<String> getPrevGuesses() {
        return prevGuesses;
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
