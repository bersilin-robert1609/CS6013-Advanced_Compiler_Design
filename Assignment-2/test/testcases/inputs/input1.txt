class T1
{
    public static void main(String[] a)
    {
	    System.out.println(a);
    }
}

class input1
{
    int a;
    int[] b;
    int c;

    public int fun1(int a, int[] b)
    {
        int c;
        int d;
        int e;
        e = d + c; // Error: d and c are not initialized
        d = c + 1; //Error: c is not initialized
        c = 1;
        return c;
    }
}