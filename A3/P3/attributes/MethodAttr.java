package attributes;

import java.util.HashMap;

public class MethodAttr
{
    public String name;
    public String returnType;
    public HashMap<String, VarAttr> params;
    public HashMap<String, VarAttr> localVars;
    public int paramCount;
    public int localVarCount;
    public String startLabel;

    public HashMap<String, CFGNode> cfgNodes;
    int labelCount;

    public MethodAttr(String name, String returnType)
    {
        this.name = name;
        this.returnType = returnType;
        this.params = new HashMap<String, VarAttr>();
        this.localVars = new HashMap<String, VarAttr>();
        this.paramCount = 0;
        this.localVarCount = 0;
        this.startLabel = null;
        this.labelCount = 0;
        this.cfgNodes = new HashMap<String, CFGNode>();
    }

    public void addParam(String name, String type)
    {
        this.paramCount++;
        VarAttr param = new VarAttr(name, type, LatticePoint.BOTTOM, VarType.PARAM);
        this.params.put(name, param);
    }

    public void addLocalVar(String name, String type)
    {
        this.localVarCount++;
        VarAttr localVar = new VarAttr(name, type, LatticePoint.TOP, VarType.LOCALVAR);
        this.localVars.put(name, localVar);
    }

    public String getNextLabel()
    {
        return "L" + labelCount++;
    }
}
