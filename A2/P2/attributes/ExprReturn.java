package attributes;
import java.util.HashMap;

public class ExprReturn 
{
    public String returnTemp;
    public HashMap<String, String> usedTemps;
    public String printString;

    public ExprReturn(String returnTemp)
    {
        this.returnTemp = returnTemp;
        this.usedTemps = new HashMap<String, String>();
        this.printString = "";
    }
}
