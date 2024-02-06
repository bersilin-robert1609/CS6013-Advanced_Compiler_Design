import syntaxtree.*;
import visitor.*;
import java.util.*;

public class Main {
    public static void main(String[] args) 
    {
        try 
        {
            Node root = new BuritoJavaParser(System.in).Goal();
            GJDepthFirst df = new GJDepthFirst();
            Object value = root.accept(df, null);
            
            if(df.varDeclared)
            {
                System.out.println("No uninitialized variables.");
            }
            else
            {
                System.out.println("Uninitialized variable found.");
            }
        } 
        catch (ParseException e) 
        {
            System.out.println(e.toString());
        }
    }
}