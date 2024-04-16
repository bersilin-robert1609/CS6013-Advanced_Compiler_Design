import syntaxtree.*;
import visitor.*;

public class P4
{
    public static void main(String[] args)
    {
        try
        {
            Node root = new FunkyTacoJavaParser(System.in).Goal();
            GJDepthFirst df = new GJDepthFirst();
            Object value = root.accept(df, null);
        }
        catch (ParseException e)
        {
            System.out.println(e.toString());
        }
    }
}