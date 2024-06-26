package attributes;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class MethodAttr
{
    public String name;
    public String returnType;
    public LinkedHashMap<String, VarAttr> params;
    public HashMap<String, VarAttr> localVars;
    public int paramCount;
    public int localVarCount;
    public CFGNode startNode;
    public CFGNode endNode;

    public HashMap<String, CFGNode> labelToNode;

    int labelCount;

    public MethodAttr(String name, String returnType)
    {
        this.name = name;
        this.returnType = returnType;
        this.params = new LinkedHashMap<String, VarAttr>();
        this.localVars = new HashMap<String, VarAttr>();
        this.paramCount = 0;
        this.localVarCount = 0;
        this.startNode = null;
        this.endNode = null;
        this.labelCount = 0;

        this.labelToNode = new HashMap<String, CFGNode>();
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

    public void setStartNode(CFGNode startNode)
    {
        this.startNode = startNode;
    }
}
