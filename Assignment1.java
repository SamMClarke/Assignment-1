import java.util.*;


public class Assignment1
{
    public static void main(String[] args)
    {
        String[] test = new String[]{"Then", "Though", "Spin", "The"};
        int[][] shifts = new int[][]{{1,1},{1,0},{1,-1},{0,1},{0,-1},{-1,1},{-1,0},{-1,-1}};
        int[] order = new int[]{0,1,2,3,4,5,6,7};
        int[] cells = new int[100];
        Random random = new Random();

        for (int i = 0; i < cells.length; i++)
        {
            cells[i] = i;
        }

        for (int i = cells.length - 1; i > 0; i--) 
        {
            int jc = random.nextInt(i+1);

            int tempc = cells[i];
            cells[i]= cells[jc];
            cells[jc] = tempc;
        }

        System.out.println(Arrays.toString(cells));
        int cellnum;
        int cellrow;
        int cellcol;
        int sideLength = (int) Math.sqrt(cells.length);

        for (int i = 0; i < cells.length; i++)
        {
            cellnum = cells[i];
            cellrow = cellnum/sideLength;
            cellcol = cellnum%sideLength;
            System.out.println("(" + cellrow + "," + cellcol + ")");
        }

        for (int i = order.length - 1; i > 0; i--) 
        {
            int j = random.nextInt(i+1);

            int temp = order[i];
            order[i]= order[j];
            order[j] = temp;
        }
        

        for (int i = 0; i < order.length; i++)
        {
            System.out.println(shifts[order[i]][0] + ", " + shifts[order[i]][1]);
        }

        Arrays.sort(test, (a,b)->Integer.compare(b.length(), a.length()));
        System.out.println(Arrays.toString(test));
    }
}