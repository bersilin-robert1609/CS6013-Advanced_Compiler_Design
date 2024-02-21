package attributes;
import java.util.*;

public class ClassAttrNode
{
    public int methodCount;
    public int varCount;
    public HashMap<String, MethodAttrNode> methodMap;
    public HashMap<String, VarAttrNode> varMap;

    public String parent;
    public String selfName;

    public ClassAttrNode(String className, String parentName)
    {
        this.selfName = className;
        this.parent = parentName;
        this.methodMap = new HashMap<String, MethodAttrNode>();
        this.varMap = new HashMap<String, VarAttrNode>();
        this.methodCount = 0;
        this.varCount = 0;
    }

    public void addVar(String varName, String typeName)
    {
        this.varCount++;
        this.varMap.put(varName, new VarAttrNode(varName, typeName));
    }

    public void addMethod(String methodName, String className, String returnType)
    {
        MethodAttrNode methodAttrNode = new MethodAttrNode(methodName, className, returnType);
        this.methodMap.put(methodName, methodAttrNode);
        this.methodCount++;
    }
}