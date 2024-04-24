package attributes;

// Called as y = a.foo() in the code (a is of static type A)
import java.util.HashMap;

public class InlineCallerInfo 
{
    public String classVarName; // a
    public String classVarType; // A
    public Integer callSiteIndex; // int
    public String methodName; // foo

    public String setVar; // y

    public String callerClass;
    public String callerMethod;

    public Integer depth;
    
    public HashMap<String, String> renamedVariables;

    public InlineCallerInfo(String classVarName, String methodName, int depth)
    {
        this.classVarName = classVarName;
        this.methodName = methodName;
        this.depth = depth;
        this.renamedVariables = new HashMap<String, String>();
        this.setVar = null;
    }

    public void addVariable(VarAttr varAttr)
    {
        String varName = varAttr.varName;
        String varType = "";

        if(varAttr.varType == VarType.LOCALVAR) varType = "local";
        else if(varAttr.varType == VarType.PARAM) varType = "param";

        String new_name = varName + "_" + this.methodName + "_" + this.callSiteIndex.toString() + "_" + this.depth.toString() + "_" + varType;
        this.renamedVariables.put(varName, new_name);
    }
}
