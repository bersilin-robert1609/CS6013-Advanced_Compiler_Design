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

    public void addPrintString(String printString)
    {
        this.printString += printString;
    }

    public void addUsedTemp(String tempName, String tempType)
    {
        this.usedTemps.put(tempName, tempType);
    }

    public void setNewReturnTemp(String newTemp, String newType)
    {
        this.returnTemp = newTemp;
        this.returnType = newType;
    }

    public void addUsedTempMultiple(HashMap<String, String> usedTemps)
    {
        this.usedTemps.putAll(usedTemps);
    }
}
