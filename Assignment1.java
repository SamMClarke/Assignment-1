/*
Authors: Sam Clarke
Date: 1/15/2024
Class: CS 145
Assignment: Assignment #1: Word Search Generator
File: Assignment1.java
Source: Deitel / Deitel
Purpose: Creates and prints word searches created by words from the user
*/

import java.util.*;

import javax.management.InstanceNotFoundException;

public class Assignment1
{
    private static final int[][] shifts = {{1,1},{1,0},{1,-1},{0,1},{0,-1},{-1,1},{-1,0},{-1,-1}};
    private static final int[] shiftsIndex = {0,1,2,3,4,5,6,7};

    public static void main(String[] args)
    {
        Scanner userInputLines = new Scanner(System.in);
        Scanner userInputTokens = new Scanner(System.in);
        printIntro();
        menu(userInputLines, userInputTokens);
    }

    public static void menu(Scanner inputLines, Scanner inputTokens)
    {
        boolean hasQuit = false;
        boolean hasWordSearch = false;
        char userResponse;
        char[][] currentWordSearch = new char[0][0];
        Random random = new Random();

        while (!hasQuit)
        {
            System.out.printf("%s%n%s%n%s%n%s%n%s%n",
            "Please select an option:",
            "Generate a new word search (g)",
            "Print out your word search (p)",
            "Show the solution to your word search (s)",
            "Quit the program (q)");
            userResponse = inputLines.nextLine().charAt(0);

            switch(userResponse)
            {
                case 'g':
                case 'G':
                    //Generate new word search
                    hasWordSearch = true;
                    currentWordSearch = generate(inputTokens, random);
                    break;
                case 'p':
                case 'P':
                    if (checkInput(hasWordSearch))
                    {
                        System.out.println(Arrays.deepToString(currentWordSearch));
                        //Print word search
                    }
                    break;
                case 's':
                case 'S':
                    if (checkInput(hasWordSearch))
                    {
                        //Solve the word search
                    }
                    break;
                case 'q':
                case 'Q':
                    //Quit the program
                    hasQuit = true;
                    break;
            }
        }
    }

    //MAKE 3 LETTER WORDS MINIMUM
    public static String[] gatherUserWords(Scanner input)
    {
        System.out.print("\nHow many words would you like to enter into your word search? ");
        String[] userWords = new String[input.nextInt()];

        for (int i = 0; i < userWords.length; i++)
        {
            System.out.print("Please enter word number " + (i+1) + ": ");
            userWords[i] = input.next();
            System.out.println();
        }

        Arrays.sort(userWords, (a,b)->Integer.compare(b.length(), a.length()));
        return userWords;
    }

    public static char[][] generate(Scanner inputTokens, Random randomObject)
    {
        String[] userWords = gatherUserWords(inputTokens);
        int wordSearchSize = userWords[0].length() + 1;
        char[][] wordSearch = new char[wordSearchSize][wordSearchSize];
        int[] cells = new int[wordSearchSize*wordSearchSize];
        int randomRow;
        int randomColumn;
        String currentWord;
        int currentWordLength;
        int rowShift;
        int columnShift;
        boolean hasEmpty = false;
        boolean canFit = true;
        int tries = 100;

        emptyArray(wordSearch);

        for (int i = 0; i < cells.length; i++)
        {
            cells[i] = i;
        }

        for (int i = 0; i < userWords.length; i++)
        {
            shuffleArray(cells, randomObject);
            currentWord = userWords[i];
            currentWordLength = currentWord.length();
            for (int j = 0; j < Math.min(tries, cells.length); j++)
            {
                randomRow = cells[j] / wordSearchSize;
                randomColumn = cells[j] % wordSearchSize;
                shuffleArray(shiftsIndex, randomObject);

                if (wordSearch[randomRow][randomColumn] == '*' || 
                wordSearch[randomRow][randomColumn] == currentWord.charAt(0))
                {
                    if (wordSearch[randomRow][randomColumn] == '*') 
                    {
                        hasEmpty = true;
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
                                if (wordSearch[randomRow+(rowShift*n)][randomColumn+(columnShift*n)] == '*' && !hasEmpty) 
                                {
                                    hasEmpty = true;
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
                            for (int n = 0; n < currentWordLength; n++)
                            {
                                wordSearch[randomRow+(rowShift*n)][randomColumn+(columnShift*n)] = currentWord.charAt(n);
                            }
                            break;
                        }
                    }
                }
                if (canFit && hasEmpty)
                {
                    break;
                }
            }
        }
        System.out.println("Successfully generated word search!\n");
        return wordSearch;
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
        "This program will allow you to generate your own word search puzzle");
    }
}