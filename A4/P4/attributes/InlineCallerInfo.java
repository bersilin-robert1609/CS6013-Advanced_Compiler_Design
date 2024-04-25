package attributes;

// Called as y = a.foo() in the code (a is of static type A)
import java.util.HashMap;
import java.util.ArrayList;

public class InlineCallerInfo 
{
    public String classVarName; // a or this or new A() -> always change to a temporary variable -> classVarName is that temp
    public String classVarType; // A
    public Integer callSiteIndex; // int
    public String methodName; // foo

    public String setVar; // y
    public Integer localStmtNumber;
    public Integer depth;
    public HashMap<String, String> renamedVariables; // Old Name -> New Name
    public ArrayList<String> arguments; // Arguments to the function call

    // public String callerClass;
    // public String callerMethod;

    public InlineCallerInfo(String classVarName, String classVarType, Integer callSiteIndex, String methodName, Integer depth)
    {
        this.classVarName = classVarName;
        this.classVarType = classVarType;
        this.callSiteIndex = callSiteIndex;
        this.methodName = methodName;
        this.depth = depth;
        this.renamedVariables = new HashMap<String, String>();
        this.setVar = null;
        this.localStmtNumber = 0;
        this.arguments = new ArrayList<String>();
    }

    public void addAndRenameVariable(VarAttr varAttr)
    {
        String varName = varAttr.varName;
        String varType = "";

        if(varAttr.varType == VarType.LOCALVAR) varType = "local";
        else if(varAttr.varType == VarType.PARAM) varType = "param";

        String newVarName = varName + "_" + this.methodName + "_" + this.callSiteIndex.toString() + "_" + this.depth.toString() + "_" + varType;
        this.renamedVariables.put(varName, newVarName);
    }
}
