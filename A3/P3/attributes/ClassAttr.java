package attributes;

import java.util.HashMap;

public class ClassAttr
{
    public String name;
    public String parent;
    public HashMap<String, VarAttr> classVars;
    public HashMap<String, MethodAttr> methods;

    public ClassAttr(String name, String parent)
    {
        this.name = name;
        this.parent = parent;
        this.classVars = new HashMap<String, VarAttr>();
        this.methods = new HashMap<String, MethodAttr>();
    }

    public void addClassVar(String name, String type)
    {
        VarAttr classVar = new VarAttr(name, type, LatticePoint.BOTTOM, VarType.CLASSVAR);
        this.classVars.put(name, classVar);
    }
}
