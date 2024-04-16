package attributes;

public class VarAttr 
{
    public String varName;
    public String dataType;
    public VarType varType;

    public VarAttr(String varName, String dataType, VarType varType)
    {
        this.varName = varName;
        this.dataType = dataType;
        this.varType = varType;
    }

    public boolean isClassVar()
    {
        return this.varType == VarType.CLASSVAR;
    }

    public boolean isLocalVar()
    {
        return this.varType == VarType.LOCALVAR;
    }

    public boolean isParam()
    {
        return this.varType == VarType.PARAM;
    }
}
