# Assignment-1: Word Search

# Menu:
-keep showing menu until the user has quit

## Generate new word search (g):
-ask the user how many words they want to enter
-ask the user to enter this many words

## Print out your word search (p):
-print out generated word search

## Show the solution to your word search (s):
-print solution by replacing all random letters with X's (*'s might be better)

## Quit the program (q):
-end the program

shifts array
[1,1]
[1,0]
[1,-1]
[0,1]
[0,-1]
[-1,-1]
[-1,1]
[-1,0]

1. store user entered words into a 1d array and sort from longest to shortest.
2. create 2d word search array with size being the length of the longest word + some constant maybe and fill it with *'s
3. Find random cell by using a 1d array from 0-length^2-1. shuffle this array randomly.
4. grab the first number and use div to find which row we are on, and then moldulos -1  to find the collum.
5. starting with the first letter, if this random cell is either that starting letter or a * start the word there.
6. shuffle the shifts array and start at the top to find the direction to try first. then check the second letter if it fits in the cell or it is empty. Note: keep a boolean that the word must have one blank to be "leagal".
7. Keep going  in that direction until either it can't or it finishes the word. Then check the empty space boolean
8. Then try all the other directions. If it cant place the word go to another random cell and shuffle the shifts array again.
9. If a word can be placed, and it has at least one empty space, go to the next word. If we try 100 different random cells with one word, omit the word.
10. If a word can be placed, with at least one empty space, start from the ending cell and place the last letter there, then shift the cell backwords and place the second to last letter, and so on. So we don't place the word until we know it is a valid spot and remove the hassle of having to delete our placed letters if it is an illegal place.
