package attributes;

import java.util.HashMap;

public class MethodAttr<R, A>
{
    public String name;
    public String returnType;
    public HashMap<String, ParamAttr> params;
    public HashMap<String, LocalVarAttr> localVars;
    public int paramCount;
    public int localVarCount;
    public String startLabel;

    public HashMap<String, CFGNode<R, A>> cfgNodes;
    int labelCount;

    public MethodAttr(String name, String returnType)
    {
        this.name = name;
        this.returnType = returnType;
        this.params = new HashMap<String, ParamAttr>();
        this.localVars = new HashMap<String, LocalVarAttr>();
        this.paramCount = 0;
        this.localVarCount = 0;
        this.startLabel = null;
        this.labelCount = 0;
    }

    public void addParam(String name, String type)
    {
        this.paramCount++;
        ParamAttr param = new ParamAttr(name, type);
        this.params.put(name, param);
    }

    public void addLocalVar(String name, String type)
    {
        this.localVarCount++;
        LocalVarAttr localVar = new LocalVarAttr(name, type);
        this.localVars.put(name, localVar);
    }

    public String getNextLabel()
    {
        return "L" + labelCount++;
    }
}
