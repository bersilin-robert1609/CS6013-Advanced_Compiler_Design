class M_MegaInheritance {
    public static void main(String[] a) {
        A __tempcaller__1_2;
        int t_foo_2_1_local;
        A TEMP1;
        int TEMP2;
        TEMP1 = new C();
        {
            __tempcaller__1_2 = TEMP1;
            t_foo_2_1_local = __tempcaller__1_2.init();
            TEMP2 = t_foo_2_1_local;
        }
        System.out.println(TEMP2);
    }
}

class A {
    int x;

    public int foo() {
        int t;
        t = this.init();

        return t;
    }

    public int init() {
        int TEMP2;
        TEMP2 = 2;
        x = TEMP2;

        return x;
    }
}

class B extends A {
    int y;

    public int init() {
        int TEMP3;
        TEMP3 = 3;
        y = TEMP3;

        return y;
    }
}

class C extends A {
    int z;

    public int init() {
        int TEMP4;
        TEMP4 = 4;
        z = TEMP4;

        return z;
    }
}

class D extends B {
    int w;

    public int init() {
        int TEMP5;
        D __tempcaller__1_1;
        int t_foo_1_1_local;
        {
            __tempcaller__1_1 = this;
            t_foo_1_1_local = __tempcaller__1_1.init();
            TEMP5 = t_foo_1_1_local;
        }
        w = TEMP5;

        return w;
    }
}
