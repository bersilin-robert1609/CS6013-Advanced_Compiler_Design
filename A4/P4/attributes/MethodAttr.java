package attributes;

import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.HashMap;

public class MethodAttr 
{
    public String methodName;
    public String className;
    public LinkedHashMap<String, VarAttr> methodParams;
    public HashMap<String, VarAttr> methodVars;
    public String returnType;
    public int paramCount;
    public int localVarCount;

    public HashMap<Integer, CallNode> callSites; // Indexed with global call site number (should be local ?)

    public MethodAttr(String methodName, String className, String returnType)
    {
        this.methodName = methodName;
        this.className = className;
        this.methodParams = new LinkedHashMap<String, VarAttr>();
        this.methodVars = new LinkedHashMap<String, VarAttr>();
        this.returnType = returnType;
        this.paramCount = 0;
        this.localVarCount = 0;

        this.callSites = new HashMap<Integer, CallNode>();
    }

    public void addMethodParam(String paramName, String paramType)
    {
        this.methodParams.put(paramName, new VarAttr(paramName, paramType, VarType.PARAM));
        this.paramCount++;
    }

    public void addMethodVar(String varName, String varType)
    {
        this.methodVars.put(varName, new VarAttr(varName, varType, VarType.LOCALVAR));
        this.localVarCount++;
    }

    public void addCallNode(int globalSiteNumber, int localSiteNumber, String methodName, String varType, boolean inlineable, HashMap<String, ClassAttr> symbolTable, HashMap<String, String> parentMap, HashMap<String, ArrayList<String>> childrenMap)
    {   
        boolean isValid = !(varType.equals("int") || varType.equals("int[]") || varType.equals("boolean"));

        if(isValid)
        {
            CallNode callNode = new CallNode(globalSiteNumber, localSiteNumber, methodName, inlineable);
            callNode.addSignatures(varType, symbolTable, parentMap, childrenMap);
            this.callSites.put(globalSiteNumber, callNode);
        }
        else
        {
            System.out.println("Error: Invalid call to method " + methodName + " in class " + this.className + " from a primitive type variable");
            System.exit(1);
        }
    }
}
