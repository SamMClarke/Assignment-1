public class WordSearch 
{
    //Each word search has two arrays:
    private char[][] wordSearch = new char[0][0]; //One that stores the actual letters of the word search
    private String[][] wordSearchColor = new String[0][0]; //One that stores the ANSI color codes of the word search

    public WordSearch()
    {
        
    }

    public WordSearch(char[][] wordSearchArray, String[][] wordSearchColorArray)
    {
        this.wordSearch = wordSearchArray;
        this.wordSearchColor = wordSearchColorArray;
    }

    public char[][] getWordSearch()
    {
        return this.wordSearch;
    }

    public String[][] getWordSearchColor()
    {
        return this.wordSearchColor;
    }
}
