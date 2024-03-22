package attributes;

import java.util.HashMap;

public class ClassAttr
{
    public String name;
    public String parent;
    public HashMap<String, VarAttr> classVars;
    public HashMap<String, MethodAttr> methods;
    public int classVarCount;

    public ClassAttr(String name, String parent)
    {
        this.name = name;
        this.parent = parent;
        this.classVars = new HashMap<String, VarAttr>();
        this.methods = new HashMap<String, MethodAttr>();
        this.classVarCount = 0;
    }

    public void addClassVar(String name, String type)
    {
        this.classVarCount++;
        VarAttr classVar = new VarAttr(name, type, LatticePoint.BOTTOM, VarType.CLASSVAR);
        this.classVars.put(name, classVar);
    }
}
