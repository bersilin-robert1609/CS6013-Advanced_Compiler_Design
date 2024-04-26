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

    public Integer localStmtNumber;
    public Integer depth;
    public HashMap<String, String> renamedLocalVars; // Old Name -> New Name
    public HashMap<String, String> renamedParams; // OldParamName -> NewParamName
    public ArrayList<String> arguments; // Arguments to the function call

    public String _retString; // The return identifier from the function body after inlining

    // public String callerClass;
    // public String callerMethod;

    public InlineCallerInfo(String classVarName, String classVarType, Integer callSiteIndex, String methodName, Integer depth)
    {
        this.classVarName = classVarName;
        this.classVarType = classVarType;
        this.callSiteIndex = callSiteIndex;
        this.methodName = methodName;
        this.depth = depth;
        this.renamedLocalVars = new HashMap<String, String>();
        this.renamedParams = new HashMap<String, String>();
        this._retString = null;
        this.localStmtNumber = 0;
        this.arguments = new ArrayList<String>();
    }

    public void addAndRenameLocalvar(VarAttr varAttr)
    {
        String varName = varAttr.varName;
        String newVarName = varName + "_" + this.methodName + "_" + this.callSiteIndex.toString() + "_" + this.depth.toString() + "_" + "local";
        this.renamedLocalVars.put(varName, newVarName);
    }

    public void addAndRenameParam(VarAttr varAttr)
    {
        String varName = varAttr.varName;
        String newVarName = varName + "_" + this.methodName + "_" + this.callSiteIndex.toString() + "_" + this.depth.toString() + "_" + "param";
        this.renamedParams.put(varName, newVarName);
    }
}
