class ForLoop2 {
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

        for(i = 1; i < num; uninit = 1)
        {
            num_aux = num_aux * i;
        }
        return num_aux + uninit;
    }
}