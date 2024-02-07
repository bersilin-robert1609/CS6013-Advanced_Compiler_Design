class NestedIfElse {
    public static void main(String[] a){
        System.out.println(new Fac().ComputeFac(10));
    }
}

class Fac {
    public int ComputeFac(int num){
        
        int ans;

        if(1 < 2)
        {
            if(2 < 1)
            {
                ans = 1;
            }
            else
            {
                ans = 2;
            }
        }
        else
        {
            if(2 < 1)
            {
                ans = 1;
            }
            else
            {
                System.out.println(1);
            }
        }

        return ans;
    }
}