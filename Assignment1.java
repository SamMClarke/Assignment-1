/*
Authors: Sam Clarke
Date: 1/15/2024
Class: CS 145
Assignment: Assignment #1: Word Search Generator
File: Assignment1.java
Source: Deitel / Deitel
Purpose: Creates and prints word searches created by words from the user
*/

import java.io.*;
import java.io.FileNotFoundException;
import java.util.*;

public class Assignment1
{
    //Shifts array stores all the possible cardinal directions from a point
    private static final int[][] shifts = {{1,1},{1,0},{1,-1},{0,1},{0,-1},{-1,1},{-1,0},{-1,-1}};
    private static final int[] shiftsIndex = {0,1,2,3,4,5,6,7};

    //ColorText stores ANSI codes for various color texts
    private static final String[] colorText = {
    "\u001B[38;5;1m",   //Red
    "\u001B[38;5;2m",   //Green
    "\u001B[38;5;3m",   //Yellow
    "\u001B[38;5;4m",   //Blue
    "\u001B[38;5;5m",   //Magenta
    "\u001B[38;5;198m", //Pink
    "\u001B[38;5;202m", //Orange
    "\u001B[38;5;94m",  //Brown
    "\u001B[38;5;196m", //Bright Red
    "\u001B[38;5;51m"}; //Bright Blue

    public static void main(String[] args)
    {
        //Create two scnanner objects one for lines and one for tokens
        Scanner userInputLines = new Scanner(System.in);
        Scanner userInputTokens = new Scanner(System.in);
        menu(userInputLines, userInputTokens);
    }

    public static void menu(Scanner inputLines, Scanner inputTokens) //Manages the menu and basic program logic
    {
        printIntro();

        //Initialize variables and objects
        boolean hasQuit = false;
        boolean hasWordSearch = false;
        Random random = new Random();
        WordSearch currentWordSearch = new WordSearch();
        char userResponse;
        String colorReset = "\u001B[0m"; //ANSI code that resets text color to default
        
        while (!hasQuit)
        {
            userResponse = printMenu(inputLines);

            switch(userResponse)
            {
                case 'g':
                case 'G':
                    //Generate a word search and allow other inputs to be recognized
                    hasWordSearch = true;
                    currentWordSearch = generate(inputTokens, random);
                    break;
                case 'p':
                case 'P':
                    //Print the word search
                    if (checkInput(hasWordSearch))
                    {
                        printWordSearch(currentWordSearch, random, System.out);
                    }
                    break;
                case 's':
                case 'S':
                    //Show the solution of the word search
                    if (checkInput(hasWordSearch))
                    {
                        printSolution(currentWordSearch, colorReset, true, System.out);
                    }
                    break;
                case 'f':
                case 'F':
                    //Store word search to a file
                    if (checkInput(hasWordSearch))
                    {
                        saveWordSearch(currentWordSearch, inputLines, random, colorReset);
                    }
                    break;
                case 'q':
                case 'Q':
                    //Quit the program
                    hasQuit = true;
                    break;
                default:
                    break;
            }
        }
    }


    public static String[] gatherUserWords(Scanner input) //Manage user word gathering logic
    {
        boolean validAnswer = false;
        char inputType;
        String[] userWordsReturn = new String[0];
        while(!validAnswer) //Keep asking until a valid answer is given
        {
            System.out.print("\nHow would you like to enter your words? Press f for file input. Press m for manual input: ");
            inputType = input.nextLine().charAt(0);
            switch (inputType)
            {
                case 'f':
                case 'F':
                    //Input words via file
                    validAnswer = true;
                    userWordsReturn = fileInput(input);
                    break;
                case 'm':
                case 'M':
                    //Input words via user input
                    validAnswer = true;
                    userWordsReturn = manualInput(input);
                    break;
                default:
                    System.out.println("Invalid input. Please try again.");
                    break;
            }
        }
        //Sort the user words array from largest to smallest
        //Start with large words first because they are the hardest to place
        Arrays.sort(userWordsReturn, (a,b)->Integer.compare(b.length(), a.length()));
        return userWordsReturn;
    }

    public static String[] fileInput(Scanner input) //Gather words from an input file
    {
        String[] userWords = new String[0];
        try
        {
            String inputFileName;
            File inputFile;
            int numLines = 0;
            boolean validFormat = false;

            while (!validFormat) //Keep asking for an input file until the right format is given
            {
                validFormat = true;
                System.out.println("\nText file format needs to have ONE word (3 letters or more) per line.");
                System.out.print("Input file name: ");
                inputFileName = input.nextLine();
                inputFile = new File(inputFileName);
                numLines = 0;

                while (!inputFile.exists()) //Check is file exists
                {
                    System.out.print("File not found. Try again: ");
                    inputFileName = input.nextLine();
                    inputFile = new File(inputFileName);
                }

                //Create two scanner objects for the file:
                Scanner inputFileCount = new Scanner(inputFile); //One to count the lines of the file
                Scanner inputFileScanner = new Scanner(inputFile); //One to add the words to the userWords array

                while (inputFileCount.hasNextLine()) //Count how many lines are in the file
                {
                    numLines++;
                    inputFileCount.nextLine();
                }

                if (validFormat)
                {
                    userWords = new String[numLines];
                    for (int i = 0; i < numLines; i++)
                    {
                        userWords[i] = inputFileScanner.nextLine().toUpperCase();
                        if (userWords[i].length() < 3) //If words are less than 3 letters long -> try again
                        {
                            validFormat = false;
                            System.out.print("\nFile does not fit the intended format. Please edit the file and try again.");
                            break;
                        }
                    }
                    System.out.println();
                }
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println(e);
        }
        return userWords;
    }

    public static String[] manualInput(Scanner input) //Ask user to manually input words
    {
        System.out.print("\nHow many words would you like to enter into your word search? ");
        String[] userWords = new String[input.nextInt()];
        String userResponse;
        System.out.println("Words must be a minimum of three letters long.\n");

        for (int i = 0; i < userWords.length; i++)
        {
            System.out.print("Please enter word number " + (i+1) + ": ");
            userResponse = input.next().toUpperCase();
            if (userResponse.length() < 3) //If word is less than three letters long -> try again
            {
                System.out.println("Word is too short! Remember words must be a minimum of three letters long.");
                i--;
            }
            else
            {
                userWords[i] = (userResponse); //Fill the userWords array with user's words
                System.out.println();
            }
        }
        return userWords;
    }


    public static WordSearch generate(Scanner inputTokens, Random randomObject) //Generates a word search
    {
        String[] userWords = gatherUserWords(inputTokens); //Gets user words
        int totalCharacters = 0;
        for (int i = 0; i < userWords.length; i++)
        {
            totalCharacters += userWords[i].length();
        }

        //The word search size is based on the number of total characters the user had in their words
        //The user words should make up roughly 50% of the entire word search, or be long enough to fit the longest word
        int wordSearchSize = (int) Math.max(Math.sqrt(totalCharacters*2), userWords[0].length());

        //Initialize three word search arrays:
        char[][] wordSearch = new char[wordSearchSize][wordSearchSize]; //Text
        String[][] wordSearchColor = new String[wordSearchSize][wordSearchSize]; //Color
        int[] cells = new int[wordSearchSize*wordSearchSize]; //Total number of cells

        int randomRow;
        int randomColumn;
        int currentWordLength;
        int rowShift;
        int columnShift;
        int maxTries = 100;
        int intersectTries = (int) (Math.min(cells.length, maxTries)/2);

        String currentWord;

        boolean hasEmpty = false;
        boolean hasIntersected = false;
        boolean canFit = true;
        boolean hasPlaced = false;
        
        for (int i = 0; i < wordSearchColor.length; i++) //Fill color array with default color (white)
        {
            for (int j = 0; j < wordSearchColor.length; j++)
            {
                wordSearchColor[i][j] = "\u001B[37m";
            }
        }

        for (int i = 0; i < cells.length; i++) //Fill cells array with digits 1 to total cells
        {
            cells[i] = i;
        }

        emptyArray(wordSearch); //Fill word search array with *'s

        for (int wordIndex = 0; wordIndex < userWords.length; wordIndex++) //For each word
        {
            shuffleArray(cells, randomObject); //Shuffle cells array
            currentWord = userWords[wordIndex];
            currentWordLength = currentWord.length();
            hasPlaced = false;

            for (int tries = 0; tries < Math.min(cells.length, maxTries); tries++) //Try placing word 100 times
            {
                //Pick random cell
                randomRow = cells[tries] / wordSearchSize;
                randomColumn = cells[tries] % wordSearchSize;

                shuffleArray(shiftsIndex, randomObject);
                hasEmpty = false;
                hasIntersected = false;

                //If cell is empty or has the same letter as the first letter of the word
                if (wordSearch[randomRow][randomColumn] == '*' || 
                wordSearch[randomRow][randomColumn] == currentWord.charAt(0))
                {
                    if (wordSearch[randomRow][randomColumn] == '*') 
                    {
                        hasEmpty = true;
                    }
                    else
                    {
                        hasIntersected = true;
                    }

                    for (int k = 0; k < shiftsIndex.length; k++) //For each direction, try placing the word
                    {
                        canFit = true;
                        //Set shifts
                        columnShift = shifts[shiftsIndex[k]][0];
                        rowShift = shifts[shiftsIndex[k]][1];

                        for (int n = 1; n < currentWordLength; n++) //Until the end of the word is reached
                        {
                            //If the word goes out of the bounds of the array
                            if ((randomRow+(rowShift*n) < 0) || (randomColumn+(columnShift*n) < 0) ||
                            (randomRow+(rowShift*n) > wordSearchSize-1) || (randomColumn+(columnShift*n) > wordSearchSize-1))
                            {
                                canFit = false;
                                break;
                            }

                            //If the cell is empty or equal to the current letter
                            if (wordSearch[randomRow+(rowShift*n)][randomColumn+(columnShift*n)] == '*' ||
                            wordSearch[randomRow+(rowShift*n)][randomColumn+(columnShift*n)] == currentWord.charAt(n))
                            {
                                if (wordSearch[randomRow+(rowShift*n)][randomColumn+(columnShift*n)] == '*') 
                                {
                                    hasEmpty = true;
                                }
                                else
                                {
                                    hasIntersected = true;
                                }
                            }
                            else
                            {
                                canFit = false;
                                break;
                            }
                        }

                        if (canFit && hasEmpty) //If the word can fit and has at least one "unique" cell -> place the word
                        {
                            //For the first 50% of the tries, force the word to have intersected with at least one other word
                            //This creates a more interesting word search
                            if ((tries < intersectTries && hasIntersected) || (tries >= intersectTries) || (wordIndex == 0))
                            {
                                hasPlaced = true;
                                for (int n = 0; n < currentWordLength; n++)
                                {
                                    //Place word search into array and place random color into the same spots of the color array
                                    wordSearch[randomRow+(rowShift*n)][randomColumn+(columnShift*n)] = currentWord.charAt(n);
                                    wordSearchColor[randomRow+(rowShift*n)][randomColumn+(columnShift*n)] = colorText[wordIndex % colorText.length];
                                }
                                break;
                            }
                        }
                    }
                }
                if (hasPlaced)
                {
                   break;
                }
            }
            if (!hasPlaced) //If after all the tries have expired and the word hasn't been placed -> omit the word
            {
                System.out.println("Could not place the word " + currentWord + " into the word search");
            }
        }
        System.out.println("Successfully generated word search!\n");

        //Create word search object that contains both the text array and the color array, then return this object
        WordSearch myWordSearch = new WordSearch(wordSearch, wordSearchColor);
        return myWordSearch;
    }

    public static void emptyArray(char[][] array) //Fills word search array with *'s (empty)
    {
        for (int i = 0; i < array.length; i++)
        {
            for (int j = 0; j < array.length; j++)
            {
                array[i][j] = '*';
            }
        }
    }

    public static void shuffleArray(int[] array, Random randomObject) //Randomly shuffles the elements of an array
    {
        for (int i = array.length - 1; i > 0; i--) 
        {
            int jc = randomObject.nextInt(i+1);

            int temp = array[i];
            array[i]= array[jc];
            array[jc] = temp;
        }
    }


    //Saves the word search and its solution to a given file
    public static void saveWordSearch(WordSearch WordSearch, Scanner input, Random random, String colorReset)
    {
        try 
        {
            System.out.print("\nPlease enter the name of the file you would like to save your word search to: ");
            String outputFileName = input.nextLine();
            PrintStream outputFile = new PrintStream(new File(outputFileName)); 

            printWordSearch(WordSearch, random, outputFile); //Print word search to file
            outputFile.println();
            printSolution(WordSearch, colorReset, false, outputFile); //Print solution to file

            System.out.println("\nSuccessfully saved the word search to " + outputFileName + "!\n");
        }
        catch (FileNotFoundException e)
        {
            System.out.println(e);
        }
    }


    //Prints the unsolved word search
    public static void printWordSearch(WordSearch WordSearch, Random random, PrintStream printType)
    {
        char[][] wordSearchPrint = WordSearch.getWordSearch();
        printLine(wordSearchPrint.length, printType);

        for (int i = 0; i < wordSearchPrint.length; i++)
        {
            printType.print("| ");
            for (int j = 0; j < wordSearchPrint.length; j++)
            {
                if (wordSearchPrint[i][j] == '*')
                {
                    //Fills in *'s with random letters
                    printType.print(((char)(random.nextInt(26)+'a') + " ").toUpperCase());
                }
                else
                {
                    printType.print(wordSearchPrint[i][j] + " ");
                }
            }
            printType.println("|");
        }
        printLine(wordSearchPrint.length, printType);
    }

    //Prints the solution to the word search
    public static void printSolution(WordSearch WordSearch, String colorReset, boolean printColor, PrintStream printType)
    {
        printLine(WordSearch.getWordSearch().length, printType);
        for (int i = 0; i < WordSearch.getWordSearch().length; i++)
        {
            printType.print("| ");
            for (int j = 0; j < WordSearch.getWordSearch().length; j++)
            {
                if (printColor)
                {
                    //Prints out the array with the color codes (for console output)
                    printType.print(WordSearch.getWordSearchColor()[i][j] + WordSearch.getWordSearch()[i][j] + colorReset + " ");
                }
                else
                {
                    //Prints out just the array without the color codes (for file output since txt files can't have color text)
                    printType.print(WordSearch.getWordSearch()[i][j] + " ");
                }
            }
            printType.println("|");
        }
        printLine(WordSearch.getWordSearch().length, printType);
    }

    public static void printLine(int wordSearchLength, PrintStream printType) //Prints line based on word search length
    {
        printType.println("=".repeat(wordSearchLength*2+3));
    }


    public static boolean checkInput(boolean hasWordSearch) //Checks if a word search has been created yet
    {
        boolean checkReturn;
        if (hasWordSearch)
        {
            checkReturn = true; //Has word search
        }
        else
        {
            checkReturn = false; //Doesn't have word search
            System.out.println("\nInvalid input. Plase generate a word search first.\n");
        }
        return checkReturn;
    }

    public static char printMenu(Scanner input) //Prints the menu to the program
    {
        System.out.printf("%s%n%s%n%s%n%s%n%s%n%s%n",
        "Please select an option:",
        "Generate a new word search (g)",
        "Print out your word search (p)",
        "Show the solution to your word search (s)",
        "Save current word search and its solution to a file (f)",
        "Quit the program (q)");
        return input.nextLine().charAt(0);
    }

    public static void printIntro() //Prints an intro to the program
    {
        System.out.printf("%s%n%s%n",
        "Welcome to my word search generator!",
        "This program will allow you to generate your own word search puzzle.");
    }
}