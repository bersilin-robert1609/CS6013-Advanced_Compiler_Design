package attributes;

import java.util.HashMap;

public class ClassAttr<R, A>
{
    public String name;
    public String parent;
    public HashMap<String, ClassVarAttr> classVars;
    public HashMap<String, MethodAttr<R, A>> methods;

    public ClassAttr(String name, String parent)
    {
        this.name = name;
        this.parent = parent;
        this.classVars = new HashMap<String, ClassVarAttr>();
        this.methods = new HashMap<String, MethodAttr<R, A>>();
    }

    public void addClassVar(String name, String type)
    {
        ClassVarAttr classVar = new ClassVarAttr(name, type);
        this.classVars.put(name, classVar);
    }
}
