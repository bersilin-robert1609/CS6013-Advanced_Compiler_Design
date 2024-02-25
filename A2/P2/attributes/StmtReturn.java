package attributes;

import java.util.*;

public class StmtReturn 
{
    public String printString;
    public HashMap<String, String> usedTemps;

    public StmtReturn()
    {
        this.printString = "";
        this.usedTemps = new HashMap<>();
    }

    public void addPrintString(String printString)
    {
        this.printString += printString;
    }

    public void addUsedTemp(String tempName, String tempType)
    {
        this.usedTemps.put(tempName, tempType);
    }

    public void addUsedTempMultiple(HashMap<String, String> usedTemps)
    {
        this.usedTemps.putAll(usedTemps);
    }
}
