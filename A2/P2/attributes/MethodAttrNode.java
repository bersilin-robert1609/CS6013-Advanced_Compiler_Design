package attributes;
import java.util.*;

public class MethodAttrNode 
{
    public String methodName;
    public String className;
    public int methodVarCount;
    public int paramCount;
    public HashMap<String, ParamAttrNode> paramMap;
    public HashMap<String, VarAttrNode> methodVarMap;
    public String returnType;

    public MethodAttrNode(String methodName, String className, String returnType)
    {
       this.methodName = methodName;
       this.className = className;
       this.returnType = returnType;

       this.paramMap = new HashMap<String, ParamAttrNode>();
       this.methodVarMap = new HashMap<String, VarAttrNode>();

       this.methodVarCount = 0;
       this.paramCount = 0;
    }

    public void addParam(String paramName, String type)
    {
       this.paramCount++;
       this.paramMap.put(paramName, new ParamAttrNode(paramName, type));
    }

    public void addVar(String varName, String type)
    {
       this.methodVarCount++;
       this.methodVarMap.put(varName, new VarAttrNode(varName, type));
    }
 }