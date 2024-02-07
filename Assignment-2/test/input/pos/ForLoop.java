class ForLoop {
    public static void main(String[] a){
        System.out.println(new Fac().ComputeFac(10));
    }
}

class Fac {
    public int ComputeFac(int num){
        int num_aux ;
        int init;
        int i;

        num_aux = 1;
        i = 1;

        for(init = 1; i < num; i = i+1)
        {
            num_aux = num_aux * i;
        }
        return num_aux + init;
    }
}