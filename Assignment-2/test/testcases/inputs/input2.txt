class T1
{
    public static void main(String[] a)
    {
	    System.out.println(a);
    }
}

class input2
{
    int a;
    int[] b;
    int c;

    public int fun1(int a)
    {
        int d;
        int e;
        int f;
        int[] g;

        if(a < 0) 
        {
            e = 0;
            d = 0;
        }
        else 
        {
            f = 1;
            d = 1;
            g = new int[10];
        }

        e = d; // init
        f = e;
        a = g[a]; // not init

        return a;
    }
}