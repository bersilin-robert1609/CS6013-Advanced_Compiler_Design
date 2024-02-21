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
}
