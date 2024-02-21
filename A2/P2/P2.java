import syntaxtree.*;
import visitor.*;

public class P2
{
    public static void main(String[] args)
    {
        try
        {
            Node root = new BuritoJavaParser(System.in).Goal();
            GJDepthFirst df = new GJDepthFirst();
            Object value = root.accept(df, null);
        }
        catch (ParseException e)
        {
            System.out.println(e.toString());
        }
    }
}