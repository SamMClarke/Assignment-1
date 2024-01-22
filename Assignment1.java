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
    private static final int[][] shifts = {{1,1},{1,0},{1,-1},{0,1},{0,-1},{-1,1},{-1,0},{-1,-1}};
    private static final int[] shiftsIndex = {0,1,2,3,4,5,6,7};
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
        Scanner userInputLines = new Scanner(System.in);
        Scanner userInputTokens = new Scanner(System.in);
        menu(userInputLines, userInputTokens);
    }

    public static void menu(Scanner inputLines, Scanner inputTokens)
    {
        printIntro();

        boolean hasQuit = false;
        boolean hasWordSearch = false;
        Random random = new Random();
        WordSearch currentWordSearch = new WordSearch();
        char userResponse;
        String colorReset = "\u001B[0m";
        
        while (!hasQuit)
        {
            userResponse = printMenu(inputLines);

            switch(userResponse)
            {
                case 'g':
                case 'G':
                    hasWordSearch = true;
                    currentWordSearch = generate(inputTokens, random);
                    break;
                case 'p':
                case 'P':
                    if (checkInput(hasWordSearch))
                    {
                        printWordSearch(currentWordSearch, random, System.out);
                    }
                    break;
                case 's':
                case 'S':
                    if (checkInput(hasWordSearch))
                    {
                        printSolution(currentWordSearch, colorReset, true, System.out);
                    }
                    break;
                case 'f':
                case 'F':
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

    public static void saveWordSearch(WordSearch WordSearch, Scanner input, Random random, String colorReset)
    {
        try
        {
            System.out.print("\nPlease enter the name of the file you would like to save your word search to: ");
            String outputFileName = input.nextLine();
            PrintStream outputFile = new PrintStream(new File(outputFileName));

            printWordSearch(WordSearch, random, outputFile);
            outputFile.println();
            printSolution(WordSearch, colorReset, false, outputFile);

            System.out.println("\nSuccessfully saved the word search to " + outputFileName + "!\n");
        }
        catch (FileNotFoundException e)
        {
            System.out.println(e);
        }
    }

    public static void printLine(int wordSearchLength, PrintStream printType)
    {
        printType.println("=".repeat(wordSearchLength*2+2));
    }

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
                    printType.print(WordSearch.getWordSearchColor()[i][j] + WordSearch.getWordSearch()[i][j] + colorReset + " ");
                }
                else
                {
                    printType.print(WordSearch.getWordSearch()[i][j] + " ");
                }
            }
            printType.println("|");
        }
        printLine(WordSearch.getWordSearch().length, printType);
    }

    public static char printMenu(Scanner input)
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

    public static String[] gatherUserWords(Scanner input)
    {
        boolean validAnswer = false;
        char inputType;
        String[] userWordsReturn = new String[0];
        while(!validAnswer)
        {
            System.out.print("\nHow would you like to enter your words? Press f for file input. Press m for manual input: ");
            inputType = input.nextLine().charAt(0);
            switch (inputType)
            {
                case 'f':
                case 'F':
                    validAnswer = true;
                    userWordsReturn = fileInput(input);
                    break;
                case 'm':
                case 'M':
                    validAnswer = true;
                    userWordsReturn = manualInput(input);
                    break;
                default:
                    System.out.println("Invalid input. Please try again.");
                    break;
            }
        }
        Arrays.sort(userWordsReturn, (a,b)->Integer.compare(b.length(), a.length()));
        return userWordsReturn;
    }

    public static String[] fileInput(Scanner input)
    {
        String[] userWords = new String[0];
        try
        {
            System.out.println("\nText file format needs to have ONE word per line with NO empty lines.");
            System.out.print("Input file name: ");
            String inputFileName = input.nextLine();
            File inputFile = new File(inputFileName);
            int numLines = 0;

            while (!inputFile.exists())
            {
                System.out.print("File not found. Try again: ");
                inputFileName = input.nextLine();
                inputFile = new File(inputFileName);
            }

            Scanner inputFileCount = new Scanner(inputFile);
            Scanner inputFileScanner = new Scanner(inputFile);

            while (inputFileCount.hasNextLine())
            {
                numLines++;
                inputFileCount.nextLine();
            }

            userWords = new String[numLines];
            for (int i = 0; i < numLines; i++)
            {
                userWords[i] = inputFileScanner.nextLine().toUpperCase();
            }
            System.out.println();
        }
        catch (FileNotFoundException e)
        {
            System.out.println(e);
        }
        return userWords;
    }

    public static String[] manualInput(Scanner input)
    {
        System.out.print("\nHow many words would you like to enter into your word search? ");
        String[] userWords = new String[input.nextInt()];
        String userResponse;
        System.out.println("Words must be a minimum of three letters long.\n");

        for (int i = 0; i < userWords.length; i++)
        {
            System.out.print("Please enter word number " + (i+1) + ": ");
            userResponse = input.next().toUpperCase();
            if (userResponse.length() < 3)
            {
                System.out.println("Word is too short! Remember words must be a minimum of three letters long.");
                i--;
            }
            else
            {
                userWords[i] = (userResponse);
                System.out.println();
            }
        }
        return userWords;
    }

    public static WordSearch generate(Scanner inputTokens, Random randomObject)
    {
        String[] userWords = gatherUserWords(inputTokens);
        int totalCharacters = 0;
        for (int i = 0; i < userWords.length; i++)
        {
            totalCharacters += userWords[i].length();
        }
        int wordSearchSize = (int) Math.max(Math.sqrt(totalCharacters*2), userWords[0].length());
        char[][] wordSearch = new char[wordSearchSize][wordSearchSize];
        String[][] wordSearchColor = new String[wordSearchSize][wordSearchSize];
        int[] cells = new int[wordSearchSize*wordSearchSize];
        int randomRow;
        int randomColumn;
        String currentWord;
        int currentWordLength;
        int rowShift;
        int columnShift;
        int maxTries = 100;
        boolean hasEmpty = false;
        boolean hasIntersected = false;
        boolean canFit = true;
        boolean hasPlaced = false;
        int intersectTries = (int) (Math.min(cells.length, maxTries)/2);

        for (int i = 0; i < wordSearchColor.length; i++)
        {
            for (int j = 0; j < wordSearchColor.length; j++)
            {
                wordSearchColor[i][j] = "\u001B[37m";
            }
        }

        emptyArray(wordSearch);

        for (int i = 0; i < cells.length; i++)
        {
            cells[i] = i;
        }

        for (int wordIndex = 0; wordIndex < userWords.length; wordIndex++)
        {
            shuffleArray(cells, randomObject);
            currentWord = userWords[wordIndex];
            currentWordLength = currentWord.length();
            hasPlaced = false;
            for (int tries = 0; tries < Math.min(cells.length, maxTries); tries++)
            {
                randomRow = cells[tries] / wordSearchSize;
                randomColumn = cells[tries] % wordSearchSize;
                shuffleArray(shiftsIndex, randomObject);
                hasEmpty = false;
                hasIntersected = false;

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

                    for (int k = 0; k < shiftsIndex.length; k++)
                    {
                        canFit = true;
                        columnShift = shifts[shiftsIndex[k]][0];
                        rowShift = shifts[shiftsIndex[k]][1];

                        for (int n = 1; n < currentWordLength; n++)
                        {
                            if ((randomRow+(rowShift*n) < 0) || (randomColumn+(columnShift*n) < 0) ||
                            (randomRow+(rowShift*n) > wordSearchSize-1) || (randomColumn+(columnShift*n) > wordSearchSize-1))
                            {
                                canFit = false;
                                break;
                            }

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

                        if (canFit && hasEmpty)
                        {
                            if ((tries < intersectTries && hasIntersected) || (tries >= intersectTries) || (wordIndex == 0))
                            {
                                hasPlaced = true;
                                for (int n = 0; n < currentWordLength; n++)
                                {
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
            if (!hasPlaced)
            {
                System.out.println("Could not place the word " + currentWord + " into the word search");
            }
        }
        System.out.println("Successfully generated word search!\n");

        WordSearch myWordSearch = new WordSearch(wordSearch, wordSearchColor);
        return myWordSearch;
    }

    public static void shuffleArray(int[] array, Random randomObject)
    {
        for (int i = array.length - 1; i > 0; i--) 
        {
            int jc = randomObject.nextInt(i+1);

            int temp = array[i];
            array[i]= array[jc];
            array[jc] = temp;
        }
    }

    public static void emptyArray(char[][] array)
    {
        for (int i = 0; i < array.length; i++)
        {
            for (int j = 0; j < array.length; j++)
            {
                array[i][j] = '*';
            }
        }
    }

    public static boolean checkInput(boolean hasWordSearch)
    {
        boolean checkReturn;
        if (hasWordSearch)
        {
            checkReturn = true;
        }
        else
        {
            checkReturn = false;
            System.out.println("\nInvalid input. Plase generate a word search first.\n");
        }
        return checkReturn;
    }

    public static void printIntro()
    {
        System.out.printf("%s%n%s%n",
        "Welcome to my word search generator!",
        "This program will allow you to generate your own word search puzzle.");
    }
}