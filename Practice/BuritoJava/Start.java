import syntaxtree.*;
import visitor.*;
import java.util.*;

public class Start 
{
    public static void main(String [] args)
    {
        try
        {
            Node root = new BuritoJavaParser(System.in).Goal();
            GJDepthFirst df = new GJDepthFirst();
            Object value = root.accept(df, null);
            System.out.println("The value is " + Integer.toString((Integer)value));
        }
        catch (ParseException e)
        {
            System.out.println(e.toString());
        }
    }
}