/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package hangmangame;

//------------------------------------------------------------------------------
//Imports
import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;


/**
* @author NWilber2026
**/


//~Start game~

//------------------------------------------------------------------------------
//HANGMAN CLASS STARTS  
//------------------------------------------------------------------------------
public class HangmanGame 
{
    //SETUP SCANNER
    static Scanner input = new Scanner(System.in);


    //--------------------------------------------------------------------------
    //MAIN Method — Entry point; loops back to title screen
    public static void main(String[] args) 
    {
        //Make sure all required data files exist before starting
        initializeFiles();
        //Keep looping back to the title screen until the user quits (allows multuple games)
        boolean running = true;

        while (running) 
        {
        showTitleScreen();
        
        String choice = input.nextLine().trim();
        //Choice 1
            if (choice.equals("1")) 
            { startNewGame(); } 
        
        //Choice 2
            else if (choice.equals("2")) 
            { showCompletedWordList(); } 
        
        //Choice 3
            else if (choice.equals("3")) 
            { showGameRules(); } 
            
        //Go Back (0) = END GAME
            else if (choice.equals("0")) 
            {
                System.out.println("\nEND GAME | Thanks for playing, Goodbye!");
                running = false;
            } 
            
        //Check for real option
            else 
            {
                System.out.println("\nInvalid option. Please enter 1, 2, 3, or 0.");
                pressEnterToContinue();
            }
        }
        //Stop
        input.close();
    }


    //--------------------------------------------------------------------------
    //TITLE SCREEN
    static void showTitleScreen() 
    {
        clearScreen();

        System.out.println("__________________________________________________________________________________");
        System.out.println("");
        System.out.println("    =     =    =======    ==    =    =======    ==   ==    =======    ==     =    ");
        System.out.println("    =     =    =     =    = =   =    =     =    = = = =    =     =    = =    =    ");
        System.out.println("    =     =    =     =    = =   =    =          = = = =    =     =    = =    =    ");
        System.out.println("    =======    =======    =  =  =    =          =  =  =    =======    =  =   =    ");
        System.out.println("    =     =    =     =    =  =  =    =   ===    =     =    =     =    =   =  =    ");
        System.out.println("    =     =    =     =    =   = =    =     =    =     =    =     =    =    = =    ");
        System.out.println("    =     =    =     =    =    ==    =======    =     =    =     =    =     ==    ");
        System.out.println("                                    THE GAME");
        System.out.println("__________________________________________________________________________________");
        System.out.println("");
        System.out.println("                                 \nMAIN MENU");
        System.out.println("");
        System.out.println("1. Start New Game");
        System.out.println("2. View Completed Word List");
        System.out.println("3. Game Rules");
        System.out.println("0. Quit");
        System.out.println("");
        System.out.println("Enter your choice: ");
    }


    //-------------------------------------------------------------------------- 
    //THEME SELECTION
    static void startNewGame() 
    {       
        System.out.println("--------------------------------------------------------------------------");
        System.out.println("");
        System.out.println("CHOOSE A THEME");    
        System.out.println("1. Superheroes");
        System.out.println("2. Restaurants");
        System.out.println("3. Animals");
        System.out.println("");
        System.out.println("Enter your choice (or press Enter to go back): ");
        
    //Ask user
        String choice = input.nextLine().trim();
        String theme = "";

    //Direct to Theme 1
        if (choice.equals("1")) 
        { theme = "superheroes"; } 
        
    //Direct to Theme 2
        else if (choice.equals("2")) 
        { theme = "restaurants"; } 
        
    //Direct to Theme 3
        else if (choice.equals("3")) 
        { theme = "animals"; } 
        
    //Direct to Main Menu (Back)
        else 
        { return; }

        //Selection Direction
        selectWord(theme);
    }


    //--------------------------------------------------------------------------
    //WORD SELECTION SCREEN
    //Shows which words are complete and which are unfinished
    static void selectWord(String theme) 
    {
        //Load all 10 words for this theme and the completed words list
        String[] wordBank = loadWordBank(theme);
        ArrayList<String> completed = loadCompletedWords(theme);

        System.out.println("\n=======================================================");
        System.out.println("         THEME: " + theme.toUpperCase());
        System.out.println("=======================================================");
        System.out.println("");
        System.out.println("Words you have completed are shown. Others are hidden.");
        System.out.println("");

        //Display each word (revealed if completed, underscores if not)
        for (int i = 0; i < wordBank.length; i++) 
        {
            String word = wordBank[i];
            boolean isDone = completed.contains(word.toLowerCase());
            
        //Display conditions
            if (isDone) 
            { System.out.println("   " + (i + 1) + ". [" + word.toUpperCase() + "]  (completed)"); } 
            
            else 
            {
                // FIX: Preserve spaces in hidden display — only hide letters with underscores
                StringBuilder hidden = new StringBuilder();
                for (int j = 0; j < word.length(); j++) 
                {
                    if (word.charAt(j) == ' ') 
                    { hidden.append(' '); } 
                    else 
                    { hidden.append('_'); }
                }
                System.out.println("   " + (i + 1) + ". " + hidden.toString());
            }
        }
        
    //Ask user for choice of word
        System.out.println();
        System.out.print("Pick a word number (1-10) or 0 to go back: ");
       
        while (true) 
        {
            String choice = input.nextLine().trim();

        //Allow going back to the title
            if (choice.equals("0")) 
            { return; }

            int wordIndex = -1;

        //Verify input (Had help with AI for this part)
            try 
            { wordIndex = Integer.parseInt(choice) - 1; } 
            
            catch (NumberFormatException e) 
            { System.out.print("Please enter a number between 1 and 10 (or 0 to go back): ");
              continue;
            }

            //Check the number is in range
            if (wordIndex < 0 || wordIndex >= wordBank.length) 
            { System.out.print("Invalid number. Pick 1-10 (or 0 to go back): ");
              continue;
            }

            String chosenWord = wordBank[wordIndex];

        //Check if the player already completed this word
            if (completed.contains(chosenWord.toLowerCase())) 
            { System.out.print("You already completed that word! Choose an uncompleted one: ");
              continue;
            }

            //Valid uncompleted word — start the game!
            playRound(chosenWord, theme);

        //After the round ends, refresh and come back to word selection
            selectWord(theme);
            return;
        }
    }


    //--------------------------------------------------------------------------
    //COMPLETED WORD LIST SCREEN
    static void showCompletedWordList() 
    {
        System.out.println("\n=======================================================");
        System.out.println("              COMPLETED WORD LIST");
        System.out.println("=======================================================");
        System.out.println();

        String[] themes = {"Superheroes", "Restaurants", "Animals"};

        //Loop through each theme and print its completed words
        for (String theme : themes) 
        {
            ArrayList<String> completed = loadCompletedWords(theme);

            System.out.println("  --- " + theme.toUpperCase() + " ---");

            if (completed.isEmpty()) 
            { System.out.println("      (no words completed yet)"); } 
            
            else 
            {
                for (String word : completed) 
                { System.out.println("      [DONE] " + word.toUpperCase()); }
            }

            System.out.println("");
        }

        System.out.println("=======================================================");
        pressEnterToContinue();
    }


    //--------------------------------------------------------------------------
    //GAME RULES SCREEN
    static void showGameRules() 
    {     
        System.out.println("\n=======================================================");
        System.out.println("                   HOW TO PLAY");
        System.out.println("=======================================================");
        System.out.println("\nGOAL:");
        System.out.println("    - Guess the hidden word before the hangman is complete!");        
        System.out.println("\nSELECTING A WORD:");
        System.out.println("    - Choose a theme: Superheroes, Restaurants, or Animals.");
        System.out.println("    - Each theme has 10 words.");
        System.out.println("    - Pick any uncompleted word to play it.");
        System.out.println("\nDURING THE GAME:");
        System.out.println("    - You get 6 wrong guesses before the game is over.");
        System.out.println("    - Each turn you can:");
        System.out.println("        L  -- guess a single Letter");
        System.out.println("        W  -- guess the entire Word");
        System.out.println("    - Correct letters are revealed in the word.");
        System.out.println("    - If a letter appears more than once, ALL copies");
        System.out.println("      are revealed at the same time.");
        System.out.println("    - Spaces are shown automatically, so only guess letters.");
        System.out.println("    - Wrong guesses are tracked in 'Guessed Letters'.");
        System.out.println("    - Wrong guesses result in one limb being added to the hangman.");
        System.out.println("\nWINNING:");
        System.out.println("    - Reveal every letter  OR  guess the full word.");
        System.out.println("    - The word is added to your Completed Word List!");
        System.out.println("\nLOSING:");
        System.out.println("    - 6 wrong guesses fills the hangman -- game over.");
        System.out.println("    - You can retry the same word or return to the menu.");
        System.out.println("=======================================================");

        pressEnterToContinue();
    }


    //--------------------------------------------------------------------------
    //PLAY GAME (lots of sub-sections)
 
    // Maximum number of wrong guesses allowed before the player loses
    static final int MAX_WRONG_GUESSES = 6;

    static void playRound(String word, String theme) 
    {
        //Setup start of game necessary stuff
        
        //Convert the word to uppercase so comparisons aren't messed up
        String targetWord = word.toUpperCase();
        int wrongGuesses = 0;

        // Track which letters have been guessed incorrectly
        ArrayList<String> wrongLetters = new ArrayList<>();

        // FIX: Build the display array — underscores for letters, spaces for spaces
        // e.g. for "IRON MAN" -> ['_', '_', '_', '_', ' ', '_', '_', '_']
        char[] displayWord = new char[targetWord.length()];
        for (int i = 0; i < displayWord.length; i++) 
        {
            if (targetWord.charAt(i) == ' ') 
            { displayWord[i] = ' '; }   // spaces are revealed immediately
            else 
            { displayWord[i] = '_'; }
        }

        //Win Conditions
        boolean playerWon  = false;
        boolean playerLost = false;


        //----------------------------------------------------------------------
        //Main loop = go until win or lose
        //----------------------------------------------------------------------
        while (!playerWon && !playerLost) 
        {
            // Draw the current game state to the screen
            displayGameScreen(targetWord, displayWord, wrongGuesses, wrongLetters, theme);

            // Ask what kind of guess the player wants to make
            System.out.println();
            System.out.println("  [L] Guess a Letter     [W] Guess the Word");
            System.out.print("Your choice: ");

            String action = input.nextLine().trim().toUpperCase();

            //------------------------------------------------------------------
            //Letter Guess           
            if (action.equals("L")) 
            {

                System.out.print("  Enter a letter: ");
                String letterInput = input.nextLine().trim().toUpperCase();

                // Validate: must be exactly one alphabetic character
                if (letterInput.length() != 1 || !Character.isLetter(letterInput.charAt(0))) 
                {
                    System.out.println("  Invalid input. Please enter a single letter.");
                    pressEnterToContinue();
                    continue;
                }

                char guessedLetter = letterInput.charAt(0);

                //Check if this letter was already guessed (Help from AI on this)
                if (wrongLetters.contains(String.valueOf(guessedLetter)) ||
                    isLetterRevealed(guessedLetter, displayWord)) 
                {                   
                    System.out.println("  You already guessed '" + guessedLetter + "'! Try a different letter.");
                    pressEnterToContinue();
                    continue;
                }

                // Check if the letter is in the target word
                if (targetWord.indexOf(guessedLetter) >= 0) 
                {
                    // Reveal ALL occurrences of that letter in the display word
                    revealLetter(guessedLetter, targetWord, displayWord);
                    System.out.println("  Great guess! '" + guessedLetter + "' is in the word.");
                } 
                
                else // Wrong guess = add to the wrong list
                {                    
                    wrongGuesses++;
                    wrongLetters.add(String.valueOf(guessedLetter));
                    System.out.println("  Sorry, '" + guessedLetter + "' is not in the word.");
                }
                
                pressEnterToContinue();
            }
            
            //------------------------------------------------------------------
            //Word Guess                                    
            else if (action.equals("W")) 
            {
                System.out.print("Guess the full word: ");
                String wordGuess = input.nextLine().trim().toUpperCase();

                //Correct Guess
                if (wordGuess.equals(targetWord)) 
                {               
                    for (int i = 0; i < targetWord.length(); i++) 
                    { displayWord[i] = targetWord.charAt(i); }
                    System.out.println("CORRECT! You guessed the word!");
                } 
                
                //Wrong Guess
                else 
                {                                    
                    wrongGuesses++;
                    System.out.println("Wrong word! You lost a guess.");
                }

                pressEnterToContinue();

            } 
            
            //Check for real input on guesses
            else 
            {
                //Unrecognized input = ask again without penalizing the player
                System.out.println("Please type L to guess a letter or W to guess the word.");
                pressEnterToContinue();
                continue;
            }


            //------------------------------------------------------------------
            //Check for win/loss on each guess
                //Win
                    if (!containsUnderscore(displayWord)) 
                    { playerWon = true; }
                //Lose
                    if (wrongGuesses >= MAX_WRONG_GUESSES) 
                    { playerLost = true; }

        }

        //----------------------------------------------------------------------
        //END OF ROUND (show win or lose)
        if (playerWon) 
        { showWinScreen(word, theme); } 
        
        else 
        { showLoseScreen(word, theme); }
    }


    //--------------------------------------------------------------------------
    //GAME SCREEN    
    static void displayGameScreen(String targetWord, char[] displayWord, 
    int wrongGuesses, ArrayList<String> wrongLetters, String theme) 
    {        
        System.out.println("\n\n\n\n\n");
        System.out.println("=======================================================");
        System.out.println("   HANGMAN  |  Theme: " + theme.toUpperCase());
        System.out.println("=======================================================");
        System.out.println("");
        
        //Had help with this part
        // Print the hangman on number of wrong guesses
        printHangman(wrongGuesses);

        System.out.println();
        System.out.println("Wrong guesses left: " + (MAX_WRONG_GUESSES - wrongGuesses));
        System.out.println();

        //Print the word with spaces between each letter/underscore
        System.out.print("Word: ");
        for (char c : displayWord) 
        { System.out.print(c + " "); }
            System.out.println();
            System.out.println();

        //List of wrong letters guessed so far
            System.out.print("Guessed Letters: ");
        
            if (wrongLetters.isEmpty()) 
            { System.out.print("none"); } 
        
            else 
            {
                for (String letter : wrongLetters) 
                { System.out.print(letter + " "); }
            }

        System.out.println("");
        System.out.println("=======================================================");
    }


    //--------------------------------------------------------------------------
    //HANGMAN CONSTRUCTION
    //Builds the figure one limb at a time
    //Wrong guess key: 
    //    1 = head 
    //    2 = body 
    //    3 = left arm
    //    4 = right arm 
    //    5 = left leg 
    //    6 = right leg
    static void printHangman(int wrongGuesses) 
    {
    //Printing Layout
        //Frame top (always visible)
        System.out.println("         +---+");
        System.out.println("         |   " + (wrongGuesses >= 1 ? "|" : " "));

        //Head (Guess 1)
        System.out.println("         |   " + (wrongGuesses >= 1 ? "O" : " "));
        
        //Body Arm (Guess 2)
        String body     = (wrongGuesses >= 2) ? "|" : " ";        
    
    //Arms    
        //Left Arm (Guess 3)
        String leftArm  = (wrongGuesses >= 3) ? "/" : " ";                        
        //Right Arm (Guess 4)
        String rightArm = (wrongGuesses >= 4) ? "\\" : " ";
        System.out.println("         |  " + leftArm + body + rightArm);
    
    //Legs
        //Left Leg (Guess 5)
        String leftLeg  = (wrongGuesses >= 5) ? "/" : " ";
        //Right Leg (Guess 6)
        String rightLeg = (wrongGuesses >= 6) ? "\\" : " ";
        System.out.println("         |  " + leftLeg + " " + rightLeg);

        // Ground (always visible)
        System.out.println("         |");
        System.out.println("      =========");
    }


    //--------------------------------------------------------------------------
    //WIN SCREEN
    static void showWinScreen(String word, String theme) 
    {
        System.out.println("=======================================================");
        System.out.println("                  YOU WIN!  :D");
        System.out.println("=======================================================");
        System.out.println("");
        System.out.println("   The word was: " + word.toUpperCase());
        System.out.println("");
        System.out.println("   '" + word.toUpperCase() + "' has been added to your completed word list!");
        System.out.println("");
        System.out.println("=======================================================");

        //Save the completed word to the file so it persists between sessions (acount managing)
        saveCompletedWord(word, theme);

        pressEnterToContinue();
    }


    //--------------------------------------------------------------------------
    //LOSE SCREEN
    static void showLoseScreen(String word, String theme) 
    {        
        System.out.println("=======================================================");
        System.out.println("                  GAME OVER  :(");
        System.out.println("=======================================================");
        System.out.println();

        //Show the fully completed hangman figure
        printHangman(MAX_WRONG_GUESSES);
        
        System.out.println("");
        System.out.println("What would you like to do?");
        System.out.println("   1. Try this word again");
        System.out.println("   2. Return to the main menu");
        System.out.println("");
        System.out.print("Your choice: ");
        //Ask user for next step (back or quit)
        String choice = input.nextLine().trim();

        //Replay method
        if (choice.equals("1")) 
        { playRound(word, theme); }        
    }

    
    //--------------------------------------------------------------------------
    //GAMEPLAY NUANCES
    
    //Show all instances of a letter   
    static void revealLetter(char letter, String targetWord, char[] displayWord) 
    {
        for (int i = 0; i < targetWord.length(); i++) 
        {
            if (targetWord.charAt(i) == letter) 
            { displayWord[i] = letter; }
        }
    }

    //No dupe letters
    static boolean isLetterRevealed(char letter, char[] displayWord) 
    {
        for (char c : displayWord) 
        { if (c == letter) return true; }
            return false;
    }

    //Checks word completion (still underscores)
    static boolean containsUnderscore(char[] displayWord) 
    {
        for (char c : displayWord) 
        { if (c == '_') return true; }
            return false;
    }


    //--------------------------------------------------------------------------
    //FILE MANAGEMENT (inside folder)
    static final String FILE_SUPERHEROES = "superheroes.txt";
    static final String FILE_RESTAURANTS = "restaurants.txt";
    static final String FILE_ANIMALS     = "animals.txt";
    static final String FILE_COMPLETED   = "completed.txt";


    //--------------------------------------------------------------------------
    //  INITIALIZE FILES    
    static void initializeFiles() 
    {
        createFileIfMissing(FILE_SUPERHEROES, getDefaultWords("superheroes"));
        createFileIfMissing(FILE_RESTAURANTS, getDefaultWords("restaurants"));
        createFileIfMissing(FILE_ANIMALS,     getDefaultWords("animals"));
        createFileIfMissing(FILE_COMPLETED,   ""); //(starts empty)
    }


    //--------------------------------------------------------------------------    
    //WORD BANK
    static String getDefaultWords(String theme) 
    {        
        //Superheros List
        if (theme.equals("superheroes")) 
        {
        return
            "Superman\n" +   
            "Batman\n" +   
            "Spiderman\n" +  
            "Iron Man\n" +   
            "Wonder Woman\n" +
            "Thor\n" +   
            "Flash\n" +
            "Black Widow\n" + 
            "Hulk\n" + 
            "Captain America\n";
        }
        
        // FIX: Changed "Fast Food Restaurants" to "restaurants" to match the theme variable
        else if (theme.equals("restaurants")) 
        {
        return
            "McDonalds\n" +
            "Wendys\n" +
            "Burger King\n" +
            "Subway\n" +
            "Kentucky Fried Chicken\n" +
            "Taco Bell\n" +
            "Pizza Hut\n" +
            "Chipotle\n" +
            "Jersey Mikes\n" +
            "Starbucks\n";
        }
        
        //Animals List
        else 
        { 
        return
            "Elephant\n" +
            "Zebra\n" +
            "Lion\n" +
            "Cheetah\n" +
            "Polar Bear\n" +
            "Hippo\n" +
            "Seagull\n" +
            "Monkey\n" +
            "Dog\n" +
            "Giraffe\n";
        }
    }


    //--------------------------------------------------------------------------
    //CREATE FILE IF MISSING    
    static void createFileIfMissing(String fileName, String defaultContent) 
    {
        File file = new File(fileName);

        if (!file.exists()) 
        {
            try 
            {
                FileWriter writer = new FileWriter(file);
                writer.write(defaultContent);
                writer.close();
            } 
            
            catch (IOException e) 
            {
                System.out.println("Error creating file: " + fileName);
                System.out.println(e.getMessage());
            }
        }
    }


    //--------------------------------------------------------------------------
    //LOAD WORD BANK    
    static String[] loadWordBank(String theme) 
    {
        // Pick the correct file name based on the theme
        String fileName;

        if (theme.equals("superheroes")) 
            { fileName = FILE_SUPERHEROES; } 
        
        else if (theme.equals("restaurants")) 
            { fileName = FILE_RESTAURANTS; } 
        
        else 
            { fileName = FILE_ANIMALS; }
        
        
        //Problem conditionals
        ArrayList<String> words = new ArrayList<>();

        try 
        {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;

            while ((line = reader.readLine()) != null) 
            {
                line = line.trim();
                if (!line.isEmpty()) 
                { words.add(line); }
            }
            reader.close();
        } 
        
        catch (IOException e) 
        {
            System.out.println("Error reading file: " + fileName);
            System.out.println(e.getMessage());
        }

        //Convert the list into array of 10 slots
        String[] wordArray = new String[10];

        for (int i = 0; i < 10; i++) 
        {
            if (i < words.size()) 
            { wordArray[i] = words.get(i); } 
            
            else 
            { wordArray[i] = "______"; }
        }

        return wordArray;
    }


    //--------------------------------------------------------------------------
    //LOAD COMPLETED WORDS   
    static ArrayList<String> loadCompletedWords(String theme) 
    {
        ArrayList<String> completedWords = new ArrayList<>();
        try 
        {
            BufferedReader reader = new BufferedReader(new FileReader(FILE_COMPLETED));
            String line;

            while ((line = reader.readLine()) != null) 
            {
                line = line.trim();

                // Split on the colon to separate the theme from the word
                if (line.contains(":")) 
                {
                    String[] parts      = line.split(":", 2);
                    String   savedTheme = parts[0].trim().toLowerCase();
                    String   savedWord  = parts[1].trim().toLowerCase();

                    // Only add words that belong to the requested theme
                    if (savedTheme.equals(theme.toLowerCase())) 
                    { completedWords.add(savedWord); }
                }
            }
            reader.close();
        } 
        
        catch (IOException e) 
        {
            // File doesn't exist yet — just return an empty list
        }

        return completedWords;
    }


    //--------------------------------------------------------------------------
    //SAVE WINNING WORD (Send to completed)
    static void saveCompletedWord(String word, String theme) 
    {
        //Avoid duplicates (check)
        ArrayList<String> alreadyDone = loadCompletedWords(theme);

        if (alreadyDone.contains(word.toLowerCase())) 
            { return; }

        //Add to file under proper theme (theme creation )
        try 
        {
            FileWriter writer = new FileWriter(FILE_COMPLETED, true);
            writer.write(theme.toLowerCase() + ":" + word.toLowerCase() + "\n");
            writer.close();
        } 
        
        //Error Backup
        catch (IOException e)        
        {
            System.out.println("Error saving completed word.");
            System.out.println(e.getMessage());
        }
    }


    //--------------------------------------------------------------------------
    //QUALITY OF LIFE ADD ONS (TEDDY'S IDEA)      
    
    //Major indent
    static void clearScreen() 
    { System.out.println("\n\n\n\n\n\n\n\n\n\n"); }

    // Pauses and waits for user
    static void pressEnterToContinue() 
    {       
        System.out.println();
        System.out.print("Press Enter to continue: ");
        //Wait for user
        input.nextLine();
    }
}