import java.util.Arrays;

public class myTest {

    private static double[] remove_value(double[] arr, int value_indx) {
        int newSize = arr.length-1;
        double[] newArr = new double[newSize];
        int i = 0;
        while (i != value_indx) {
            newArr[i] = arr[i];
            i++;
        }
        if(i+1 < arr.length) {
            int j = i+1;
            while(i < newSize) {
                newArr[i] = arr[j];
                i++;
                j++;
            }
        }
        return newArr;
    }

    private static char[][] remove_row(char[][] table, int row_indx) {
        int newSizeRow = table.length-1;
        int colSize = table[0].length;
        char[][] newTable = new char[newSizeRow][colSize];
        int i;
        for(i=0; i<newSizeRow; i++) {
            if(i == row_indx) break;
            for(int j=0; j<colSize; j++) {
                newTable[i][j] = table[i][j];
            }
        }
        if(i+1 < table.length) {
            int r = i+1;
            while(i < newSizeRow) {
                for(int j=0; j<colSize; j++)
                    newTable[i][j] = table[r][j];
                i++;
                r++;
            }
        }
        return newTable;
    }

    public static void main(String[] args) {
        char[][] values = {{'E', 'B', 'A'},
                {'T', 'T', 'T'},
                {'T', 'F', 'T'},
                {'F', 'T', 'T'},
                {'F', 'F', 'T'},
                {'T', 'T', 'F'},
                {'T', 'F', 'F'},
                {'F', 'T', 'F'},
                {'F', 'F', 'F'}};
        double[] prob = {0.95, 0.29, 0.94, 0.001, 0.05, 0.71, 0.06, 0.999};

        values = remove_row(values,3);
        prob = remove_value(prob,2);

        Factor f = new Factor(values, prob);
        //f.visualPrint();

        /*Parser p = new Parser();
        try{
            StringBuilder sb = p.getInput("C:\\Users\\user\\IdeaProjects\\AI_algorithms\\src\\input.txt");
            String [] s =sb.toString().split("Var");
            System.out.println(s[6]);
            int indx = sb.indexOf("Parents:")+9;
            System.out.println(indx);
            String [] as = sb.substring(indx).split("CPT");
            for(int i=0; i<as.length; i++) {
                System.out.println(as[i]);
            }
            //System.out.println(sb.indexOf("CPT"));
        }catch (Exception e){System.out.println(e.getMessage());}*/

        String s = "Parents: A,B,C";
        String[] parents = s.substring(8).split(",");
        for(int i=0; i<parents.length; i++) {
            System.out.println(parents[i]);
        }


    }

}
