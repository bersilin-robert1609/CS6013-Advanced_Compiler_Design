class ForLoop {
    public static void main(String[] a){
        System.out.println(new Fac().ComputeFac(10));
    }
}

class Fac {
    public int ComputeFac(int num){
        int num_aux ;
        int uninit;
        int i;

        num_aux = 1;

        for(i = 1; i < num; i = i+1)
        {
            num_aux = num_aux * i;
            uninit = 1;
        }
        return num_aux + uninit;
    }
}