import java.io.*;
import java.util.*;

public class WordleClone {

    // Set to hold all 5-letter words from the dictionary file
    private static final Set<String> DICTIONARY = new HashSet<>();

    public static void main(String[] args) {
        // Load the dictionary
        loadDictionary("words.txt");

        // Ensure dictionary is loaded
        if (DICTIONARY.isEmpty()) {
            System.out.println("Error: No words loaded from the dictionary. Please check the file.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        // Select a random word from the dictionary
        String targetWord = getRandomWord();

        System.out.println("Welcome to Wordle! Guess the 5-letter word.");
        System.out.println("You have 6 attempts. Good luck!\n");

        int attempts = 6;
        boolean isGuessed = false;
        int attemptsUsed = 0;

        while (attempts > 0 && !isGuessed) {
            System.out.printf("Attempts remaining: %d%n", attempts);
            System.out.print("Enter your guess: ");
            String guess = scanner.nextLine().toLowerCase();

            // Validate the guess
            if (guess.length() != 5) {
                System.out.println("Invalid input! Please enter a 5-letter word.\n");
                continue;
            }

            if (!DICTIONARY.contains(guess)) {
                System.out.println("Word not in dictionary!\n");
                continue;
            }

            attemptsUsed++;

            // Check the guess and provide feedback
            if (guess.equals(targetWord)) {
                System.out.println();
                switch (attemptsUsed) {
                    case 1:
                        System.out.println("Super Impressive!!!");
                        break;
                    case 2:
                        System.out.println("Great Work!");
                        break;
                    case 3:
                        System.out.println("Awesome!");
                        break;
                    case 4:
                        System.out.println("Its about time");
                        break;
                    case 5:
                        System.out.println("That was a nail biter!");
                        break;
                    case 6:
                        System.out.println("Next time, try not to cut it so close");
                        break;
                }
                isGuessed = true;
            } else {
                provideFeedback(guess, targetWord);
                attempts--;
            }
        }

        if (!isGuessed) {
            System.out.println("\nThe word was: " + targetWord);
            System.out.println("Better luck next time, if there even is one ig...");
        }

        scanner.close();
    }

    private static void loadDictionary(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String word;
            while ((word = reader.readLine()) != null) {
                word = word.trim().toLowerCase();
                if (word.length() == 5 && word.matches("[a-z]+")) {
                    DICTIONARY.add(word);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading dictionary file: " + e.getMessage());
        }
    }

    private static String getRandomWord() {
        int index = new Random().nextInt(DICTIONARY.size());
        return new ArrayList<>(DICTIONARY).get(index);
    }

    private static void provideFeedback(String guess, String targetWord) {
        // Define ANSI color codes
        final String GREEN_BG = "\u001B[42m";
        final String YELLOW_BG = "\u001B[43m";
        final String GRAY_BG = "\u001B[100m";
        final String RESET = "\u001B[0m";
        
        StringBuilder feedback = new StringBuilder("Feedback: ");
        boolean[] targetUsed = new boolean[5];
        
        // First pass: mark exact matches
        char[] letters = guess.toCharArray();
        String[] colors = new String[5];
        for (int i = 0; i < 5; i++) {
            if (letters[i] == targetWord.charAt(i)) {
                colors[i] = GREEN_BG;
                targetUsed[i] = true;
            } else {
                colors[i] = GRAY_BG;
            }
        }
        
        // Second pass: mark yellow matches
        for (int i = 0; i < 5; i++) {
            if (colors[i].equals(GRAY_BG)) {
                for (int j = 0; j < 5; j++) {
                    if (!targetUsed[j] && letters[i] == targetWord.charAt(j)) {
                        colors[i] = YELLOW_BG;
                        targetUsed[j] = true;
                        break;
                    }
                }
            }
        }
        
        // Build feedback string
        for (int i = 0; i < 5; i++) {
            feedback.append(colors[i])
                   .append(letters[i])
                   .append(RESET);
        }
        
        System.out.println(feedback.toString());
        System.out.println();
    }
}