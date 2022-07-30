import java.util.*;



//a = 4 x 3  matrix. It means a has 4 row and 3 columns. There are 12 = 3 x 4 numbers (elements)
//within the matrix A   a[4][3]

//Matrix is often used for graphics, solving equations.
//5 * x + 3 * y + 2 * z = 20
//x + y - z = 5
//2 * x - y + z = 30
//To solve this equation, you have to eliminate x, y, or z step by step  
//For programming, it is hard to perform the elimination operation. Instead,
//it uses matrix product format. 
//The coefficients:
//[5  3  2
//1  1  -1   = A   (3x3)
//2 -1  1]

//[ x
//y  = B  (3x1)
//z]

//[20
//5   = C (3x1)
//30]

//Matrix product:   A * B = C
//For one element (x, y) of C, which means the number at the x-th column and y-th row in C
//C[y][x] = The y-th row of matrix A * the x-th column of matrix B


/*This class implements the Java Runnable interface that will calculate col_idx-th column of the result matrix */
class ColumnCalculator implements Runnable{
	
	Matrix m1;
	Matrix m2;
	Matrix result;
	int col_idx; //specify which column of the result is going to be calculated

	ColumnCalculator(Matrix m_1, Matrix m_2, Matrix r, int col)
	{
		m1 = m_1;
		m2 = m_2;
		result = r;
		col_idx = col;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		//Implementation here...
		//calculating all the elements in the column of index (specified by "col_idx") of the result matrix
                for(int i = 0; i < col_idx; i++){
                    new ColumnCalculator(m1, m2, result, i);
                }
	}
	
}

public class Matrix {
	int rows; // Define the number of rows
	int cols; // Define the number of columns
	double values[][];
	
	Random rand = new Random();

	/*First constructor: with row and column as the input that creates a matrix with the specified size and 
	 * assign each elements with randomly generated number*/
	Matrix(int r, int c) {
		rows = r;
		cols = c;
		values = new double[r][c];
		
		for(int y = 0; y < rows; y++)
		{
			for(int x = 0; x < cols; x++)
			{
				values[y][x] = rand.nextDouble() * 10.0; //generating a double number between 0 and 10
			}
		}

	}

	/*First constructor: with row, column, and a 2D array as the input. Similar to the first constructor
	 * above, but instead of randomly generating, it assigns the elements with the third argument double 2D array.  */
	Matrix(int r, int c, double v[][]) {
		
		//Implementation here...
                rows = r;
                cols = c;
                values = new double[rows][cols];
                
                for(int y = 0; y < rows; y++){
                    for(int x = 0; x < cols; x++){
                        values[y][x] = 0;       //Preset every element to 0, in case v[][] is smaller than values[][]
                    }
                }
                for(int y = 0; y < rows; y++){
                    for(int x = 0; x < cols; x++){
                        values[y][x] = v[y][x]; //Copy over elements from v[][]
                    }
                }
	}
	
	
	/*Output the matrix to the console*/
	void print()
	{
		for(int y = 0; y < rows; y++)
		{
			for(int x = 0; x < cols; x++)
			{
				System.out.print(values[y][x] + ", ");
			}
			System.out.println();
		}	
	}
	
	
	/*Matrix product without thread: let the current matrix times the input argument matrix m
	 * and return the result matrix
	 * Below the multiplication is already provided but you need to add a few lines of code to 
	 * do the dimension check. If the input matrix does not satisfy the dimension requirement for
	 * matrix product, you can print out a message and return a null matrix*/
	Matrix multiplyBy(Matrix m)
	{
		//Implementation here...
            if(cols != m.rows){
                Matrix n = null;
                System.out.println("The matrices cannot be multiplied");
                return n;
            }
            
            
		Matrix result = new Matrix(rows, m.cols); //Initialize the result matrix
		
		for(int y = 0; y < result.rows; y++)
		{
			for(int x = 0; x < result.cols; x++)
			{
				result.values[y][x] = 0; //the yth row of current matrix x the xth column of m
				for(int i = 0; i < cols; i++)
				{
					result.values[y][x] += values[y][i] * m.values[i][x];
				}
				
				
//				A        *         B         = result
//			   [5, 3            [1, 2, 3        [? ? (y = 0, x = 1) ?
//                          2, 0]           4, 5, 6]        ? ? ?]
						
				
			}
		}	
		
		return result;
	}
	
	
	/*Implementation: instead using loops above to calculate each elements, 
	 * here you will use threads to accomplish the matrix product task.
	 * Similar to the "multiplyBy()" above, the input matrix m represents
	 * the second matrix that you will use the current matrix to times. The
	 * returned Matrix will be the product result matrix.  
	 * The code below is just an example, which is not the real solution. 
	 * You need to create multiple threads to do the multiplication with each thread
	 * computing one column within the result matrix*/
	Matrix multiplyByThreads(Matrix m)
	{
		//Implementation here...
		
		Matrix result = null;
		
               List<Thread> threads = new ArrayList<>();
               
               for(int i = 0; i < 10; i++){
                   Runnable r = new ColumnCalculator(this,m,result,i);
                   Thread thread = new Thread(r);
                   thread.start();
               }
               for(Thread thread: threads){
                   try{
                       thread.join();
                   }
                   catch(InterruptedException e){
                       e.printStackTrace();
                   }
               }
		
		
		return result; 
	}
	
	
	/* The main function for evaluation purpose*/
	public static void main(String[] args)
	{
		Matrix m1 = new Matrix(250, 150);

		
		Matrix m2 = new Matrix(150, 200);
		

		System.out.println("Begin matrix product");
                long time2 = System.currentTimeMillis();

		Matrix result = m1.multiplyBy(m2);
		
                long totaltime2 = System.currentTimeMillis() - time2;
                
		System.out.println("Ending matrix product");
                System.out.println("Total milliseconds required: " + totaltime2 + "\n");
		
		
		//Implementation here...
		//Test your multiplyByThreads() in both accuracy and time performance
                
                System.out.println("Begin threaded matrix product");
                long time1 = System.currentTimeMillis();
                
                Matrix result2 = m1.multiplyByThreads(m2);
                
                
                long totaltime1 = System.currentTimeMillis() - time1;
                
                System.out.println("Ending threaded matrix product");
                System.out.println("Total milliseconds required: " + totaltime1);
		
		
		
		
	}
}