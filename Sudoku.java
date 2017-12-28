
import java.util.*;
import java.io.*;


class Sudoku
{
    /* SIZE is the size parameter of the Sudoku puzzle, and N is the square of the size.  For 
     * a standard Sudoku puzzle, SIZE is 3 and N is 9. */
    int SIZE, N;

    /* The grid contains all the numbers in the Sudoku puzzle.  Numbers which have
     * not yet been revealed are stored as 0. */
    int Grid[][];


    /* The solve() method should remove all the unknown characters ('x') in the Grid
     * and replace them with the numbers from 1-9 that satisfy the Sudoku puzzle. */
    
    public void solve()
    {     
        for( int i = 0; i < N; i++ ) {
            for( int j = 0; j < N; j++ ) {
                // Check if the value is unknowm (has value of 0)
                if (Grid[i][j] == 0){
                if(solvePuzzle(i,j) ) return;// Exit the method when we reach the last slot of the puzzle assuming it is not empty
                }
            }
        } 
    }
    
    public boolean solvePuzzle (int i, int j)
    {    
        //base case: Puzzle is solved
        if (isSolved() ) return true;
        
        //if cell is empty
        if (Grid[i][j] == 0)
        {
            //value that will be put in one of the unknown spaces
            for( int val = 1; val < N+1; val++ ) 
            {          
            //check if the value is taken in the same row/column/box, if not, give the empty slot that value
                if (checkRow(val,i,j) && checkCol(val,i,j) && checkBox(val,i,j))
                {
                    Grid[i][j] = val;
                    //recursively solve (for the next element in line) the rest of the board
                    if (solvePuzzle(  i+(j/(N-1)) , (j+1)%(N) ) ) return true; 
                } 
            }
            Grid[i][j] = 0; //if the grid cannot be filled: backtrack and try next value
        }
        //if cell has a predefined number (usefull during recursion call)
        else 
        {
            if(solvePuzzle( i+(j/(N-1)) ,(j+1)%(N) ) ) return true;
        } 
        return false;
    }

    //Determines if puzzle is solved (no '0' in any slot)
    public boolean isSolved(){
        for(int i = 0 ; i < N ; i++){
            for(int j = 0 ; j < N ; j++){
                if(Grid[i][j] == 0) return false;
            }
        }
        return true;
    }
    //Check there are no duplicates in some row of the puzzle
    public boolean checkRow(int num, int row, int col){
        int value = num;
        for( int i = 0; i < Grid.length; i++ ) {
            if (i == col) continue; //don't compare the value with the slot from which it was taken
            if (value == Grid[row][i]) return false;
        }
        return true;
    }
    
    //Check there are no duplicates in some column of the puzzle
    public  boolean checkCol(int num, int row, int col){
        int value = num;
        for( int i = 0; i < Grid.length; i++ ) {
            if (i == row) continue; //don't compare the value with the slot from which it was taken
            if (value == Grid[i][col]) return false;
        }
        return true;
    }
    
    //Check there are no duplicates in some box of the puzzle containing the element at index [row][col]:
    /*Loop through the 'box' delimited by SIZE   
        1.Find the box containing the element (using floor division of the index of the element with the size of the grid)
        2.Multiply the row index of the box by 'grid size' to get the index of the 1st element in that box
          (do the same to find the column)
        3.Loop from the 1st element of the box to the last element of the box (shifting the index of the Row and Col by 'size' and 
          compare the value of the number with the element in question*/
    public boolean checkBox(int num, int row, int col){
        int value = num;
        int size = (int) Math.sqrt(Grid.length);
        int boxRow = row/size;
        int boxCol = col/size;
      
        for( int i = boxRow*size; i < boxRow*size+size; i++ ) {
            for( int j = boxCol*size; j < boxCol*size+size; j++ ) {
                if (Grid[i][j] == value) return false;
            }
        }
        return true;
    }

    /*****************************************************************************/
    /* NOTE: YOU SHOULD NOT HAVE TO MODIFY ANY OF THE FUNCTIONS BELOW THIS LINE. */
    /*****************************************************************************/
 
    /* Default constructor.  This will initialize all positions to the default 0
     * value.  Use the read() function to load the Sudoku puzzle from a file or
     * the standard input. */
    public Sudoku( int size )
    {
        SIZE = size;
        N = size*size;

        Grid = new int[N][N];
        for( int i = 0; i < N; i++ ) 
            for( int j = 0; j < N; j++ ) 
                Grid[i][j] = 0;
    }


    /* readInteger is a helper function for the reading of the input file.  It reads
     * words until it finds one that represents an integer. For convenience, it will also
     * recognize the string "x" as equivalent to "0". */
    static int readInteger( InputStream in ) throws Exception
    {
        int result = 0;
        boolean success = false;

        while( !success ) {
            String word = readWord( in );

            try {
                result = Integer.parseInt( word );
                success = true;
            } catch( Exception e ) {
                // Convert 'x' words into 0's
                if( word.compareTo("x") == 0 ) {
                    result = 0;
                    success = true;
                }
                // Ignore all other words that are not integers
            }
        }

        return result;
    }


    /* readWord is a helper function that reads a word separated by white space. */
    static String readWord( InputStream in ) throws Exception
    {
        StringBuffer result = new StringBuffer();
        int currentChar = in.read();
 String whiteSpace = " \t\r\n";
        // Ignore any leading white space
        while( whiteSpace.indexOf(currentChar) > -1 ) {
            currentChar = in.read();
        }

        // Read all characters until you reach white space
        while( whiteSpace.indexOf(currentChar) == -1 ) {
            result.append( (char) currentChar );
            currentChar = in.read();
        }
        return result.toString();
    }


    /* This function reads a Sudoku puzzle from the input stream in.  The Sudoku
     * grid is filled in one row at at time, from left to right.  All non-valid
     * characters are ignored by this function and may be used in the Sudoku file
     * to increase its legibility. */
    public void read( InputStream in ) throws Exception
    {
        for( int i = 0; i < N; i++ ) {
            for( int j = 0; j < N; j++ ) {
                Grid[i][j] = readInteger( in );
            }
        }
    }


    /* Helper function for the printing of Sudoku puzzle.  This function will print
     * out text, preceded by enough ' ' characters to make sure that the printint out
     * takes at least width characters.  */
    void printFixedWidth( String text, int width )
    {
        for( int i = 0; i < width - text.length(); i++ )
            System.out.print( " " );
        System.out.print( text );
    }


    /* The print() function outputs the Sudoku grid to the standard output, using
     * a bit of extra formatting to make the result clearly readable. */
    public void print()
    {
        // Compute the number of digits necessary to print out each number in the Sudoku puzzle
        int digits = (int) Math.floor(Math.log(N) / Math.log(10)) + 1;

        // Create a dashed line to separate the boxes 
        int lineLength = (digits + 1) * N + 2 * SIZE - 3;
        StringBuffer line = new StringBuffer();
        for( int lineInit = 0; lineInit < lineLength; lineInit++ )
            line.append('-');

        // Go through the Grid, printing out its values separated by spaces
        for( int i = 0; i < N; i++ ) {
            for( int j = 0; j < N; j++ ) {
                printFixedWidth( String.valueOf( Grid[i][j] ), digits );
                // Print the vertical lines between boxes 
                if( (j < N-1) && ((j+1) % SIZE == 0) )
                    System.out.print( " |" );
                System.out.print( " " );
            }
            System.out.println();

            // Print the horizontal line between boxes
            if( (i < N-1) && ((i+1) % SIZE == 0) )
                System.out.println( line.toString() );
        }
    }


    /* The main function reads in a Sudoku puzzle from the standard input, 
     * unless a file name is provided as a run-time argument, in which case the
     * Sudoku puzzle is loaded from that file.  It then solves the puzzle, and
     * outputs the completed puzzle to the standard output. */
    public static void main( String args[] ) throws Exception {
        InputStream in;
        if( args.length > 0 ) 
            in = new FileInputStream( args[0] );
        else
            in = System.in;

        // The first number in all Sudoku files must represent the size of the puzzle.  See
        // the example files for the file format.
        int puzzleSize = readInteger( in );
        if( puzzleSize > 100 || puzzleSize < 1 ) {
            System.out.println("Error: The Sudoku puzzle size must be between 1 and 100.");
            System.exit(-1);
        }

        Sudoku s = new Sudoku( puzzleSize );

        // read the rest of the Sudoku puzzle
        s.read( in );

        // Solve the puzzle.  We don't currently check to verify that the puzzle can be
        // successfully completed.  You may add that check if you want to, but it is not
        // necessary.
        s.solve();

        // Print out the (hopefully completed!) puzzle
        s.print();
    }
}

