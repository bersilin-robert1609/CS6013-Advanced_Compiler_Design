package attributes;
import java.util.HashMap;

public class ExprReturn 
{
    public String returnTemp;
    public String returnType;
    public HashMap<String, String> usedTemps;
    public String printString;

    public ExprReturn(String returnTemp, String returnType)
    {
        this.returnTemp = returnTemp;
        this.returnType = returnType;
        this.usedTemps = new HashMap<String, String>();
        this.printString = "";
    }
}
