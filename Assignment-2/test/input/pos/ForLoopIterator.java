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
        
        for(e = 3; e < 4; e = e+1)
        {
            d = 1;
            c = 2;
            e = 4;
        }

        a = e;  // Valid
        // b = d;
        return 0;
    }
}