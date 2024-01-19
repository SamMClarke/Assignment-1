public class WordSearch 
{
    private char[][] wordSearch = new char[0][0];
    private String[][] wordSearchColor = new String[0][0];

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
