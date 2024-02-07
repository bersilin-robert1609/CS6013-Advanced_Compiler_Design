class WhileCondition {
    public static void main(String[] a){
        System.out.println(new Fac().ComputeFac(10));
    }
}

class Fac {
    public int ComputeFac(int num){
        int num_aux ;

        num_aux = 0;

        while(num_aux < 10)
        {
            num_aux = num_aux + 1;
        }
        return 0;
    }
}