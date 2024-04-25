class M_ClassVar_Adopted {
    public static void main(String[] args) {
        int a;
        int one_init_9_2_local;
        Foo __tempcaller__1_2;
        Foo f;
        Foo y_init_9_2_local;
        Foo y_bar_2_1_local;
        Foo __tempcaller__2_7;
        Foo __tempcaller__2_8;
        Foo y_init_8_2_local;
        Foo __tempcaller__2_9;
        int one_bar_2_1_local;
        int z_bar_2_1_local;
        int a_init_8_2_local;
        int t_bar_2_1_local;
        Foo y2_bar_2_1_local;
        Foo y_init_7_2_local;
        int a_init_7_2_local;
        int t_init_7_2_local;
        Foo y3_bar_2_1_local;
        int one_init_7_2_local;
        int t_init_8_2_local;
        int t_init_9_2_local;
        int one_init_8_2_local;
        int a_init_9_2_local;
        f = new Foo();
        {
            __tempcaller__1_2 = f;
            one_bar_2_1_local = 1;
            __tempcaller__1_2.x = 0;
            y3_bar_2_1_local = __tempcaller__1_2;
            t_bar_2_1_local = y3_bar_2_1_local.x;
            t_bar_2_1_local = t_bar_2_1_local + one_bar_2_1_local;
            y_bar_2_1_local = __tempcaller__1_2;
            {
                __tempcaller__2_7 = y_bar_2_1_local;
                one_init_7_2_local = 1;
                __tempcaller__2_7.x = 1;
                y_init_7_2_local = __tempcaller__2_7;
                t_init_7_2_local = y_init_7_2_local.x;
                t_init_7_2_local = t_init_7_2_local + one_init_7_2_local;
                a_init_7_2_local = 1;
                z_bar_2_1_local = a_init_7_2_local;
            }
            {
                __tempcaller__2_8 = __tempcaller__1_2;
                one_init_8_2_local = 1;
                __tempcaller__2_8.x = 1;
                y_init_8_2_local = __tempcaller__2_8;
                t_init_8_2_local = y_init_8_2_local.x;
                t_init_8_2_local = t_init_8_2_local + one_init_8_2_local;
                a_init_8_2_local = 1;
                z_bar_2_1_local = a_init_8_2_local;
            }
            {
                __tempcaller__2_9 = new Foo();
                one_init_9_2_local = 1;
                __tempcaller__2_9.x = 1;
                y_init_9_2_local = __tempcaller__2_9;
                t_init_9_2_local = y_init_9_2_local.x;
                t_init_9_2_local = t_init_9_2_local + one_init_9_2_local;
                a_init_9_2_local = 1;
                z_bar_2_1_local = a_init_9_2_local;
            }
            y2_bar_2_1_local = new Foo();
            z_bar_2_1_local = y2_bar_2_1_local.init();
            z_bar_2_1_local = __tempcaller__1_2.init();
            z_bar_2_1_local = new Foo().init();
            a = z_bar_2_1_local;
        }
        System.out.println(a);
    }
}

class Foo {
    int x;

    public int init() {
        int a;
        int t;
        int one;
        Foo y;
        one = 1;
        x = 1;
        y = this;
        t = y.x;
        t = t + one;
        a = 1;

        return a;
    }

    public int bar() {
        int one;
        Foo __tempcaller__1_7;
        Foo __tempcaller__1_8;
        Foo __tempcaller__1_9;
        int t_init_7_1_local;
        int one_init_7_1_local;
        int a_init_7_1_local;
        Foo y_init_9_1_local;
        Foo y_init_8_1_local;
        int t_init_8_1_local;
        int a_init_9_1_local;
        int t;
        Foo y_init_7_1_local;
        int a_init_8_1_local;
        int one_init_8_1_local;
        Foo y;
        Foo y2;
        int z;
        Foo y3;
        int t_init_9_1_local;
        int one_init_9_1_local;
        one = 1;
        x = 0;
        y3 = this;
        t = y3.x;
        t = t + one;
        y = this;
        {
            __tempcaller__1_7 = y;
            one_init_7_1_local = 1;
            __tempcaller__1_7.x = 1;
            y_init_7_1_local = __tempcaller__1_7;
            t_init_7_1_local = y_init_7_1_local.x;
            t_init_7_1_local = t_init_7_1_local + one_init_7_1_local;
            a_init_7_1_local = 1;
            z = a_init_7_1_local;
        }
        {
            __tempcaller__1_8 = this;
            one_init_8_1_local = 1;
            __tempcaller__1_8.x = 1;
            y_init_8_1_local = __tempcaller__1_8;
            t_init_8_1_local = y_init_8_1_local.x;
            t_init_8_1_local = t_init_8_1_local + one_init_8_1_local;
            a_init_8_1_local = 1;
            z = a_init_8_1_local;
        }
        {
            __tempcaller__1_9 = new Foo();
            one_init_9_1_local = 1;
            __tempcaller__1_9.x = 1;
            y_init_9_1_local = __tempcaller__1_9;
            t_init_9_1_local = y_init_9_1_local.x;
            t_init_9_1_local = t_init_9_1_local + one_init_9_1_local;
            a_init_9_1_local = 1;
            z = a_init_9_1_local;
        }
        y2 = new Foo();
        z = y2.init();
        z = this.init();
        z = new Foo().init();

        return z;
    }
}
