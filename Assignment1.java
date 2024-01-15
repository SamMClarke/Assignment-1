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

public class Assignment1
{
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
                    generate(inputTokens);
                    break;
                case 'p':
                case 'P':
                    if (checkInput(hasWordSearch))
                    {
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

    public static void generate(Scanner input)
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

        System.out.println(Arrays.toString(userWords));
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