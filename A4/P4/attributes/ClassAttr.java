package attributes;

import java.util.HashMap;
import java.util.HashSet;

public class ClassAttr 
{
    public String className;
    public String parentName;
    public HashMap<String, VarAttr> classVars;
    public HashMap<String, MethodAttr> methods;
    public int classVarCount;
    public HashSet<String> children;

    public ClassAttr(String className, String parentName)
    {
        this.className = className;
        this.parentName = parentName;
        this.classVars = new HashMap<String, VarAttr>();
        this.methods = new HashMap<String, MethodAttr>();
        this.classVarCount = 0;
        this.children = new HashSet<String>();
    }

    public void addClassVar(String varName, String varType)
    {
        this.classVars.put(varName, new VarAttr(varName, varType, VarType.CLASSVAR));
        this.classVarCount++;
    }

    public void checkRecursion()
    {
        for(MethodAttr method: methods.values()) method.checkRecursion();
    }

    public void printInfo()
    {
        for(MethodAttr method : methods.values()) method.printInfo();
    }
}
