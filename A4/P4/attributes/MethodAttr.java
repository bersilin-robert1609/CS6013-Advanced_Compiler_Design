package attributes;

import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.HashSet;

public class MethodAttr 
{
    public String methodName;
    public String className;
    public LinkedHashMap<String, VarAttr> methodParams;
    public HashMap<String, VarAttr> methodVars;
    public String returnType;
    public int paramCount;
    public int localVarCount;

    public HashMap<String, String> allDeclaredVariables; // For final usage

    public HashMap<Integer, CallNode> callGraph;

    public MethodAttr(String methodName, String className, String returnType)
    {
        this.methodName = methodName;
        this.className = className;
        this.methodParams = new LinkedHashMap<String, VarAttr>();
        this.methodVars = new HashMap<String, VarAttr>();
        this.returnType = returnType;
        this.paramCount = 0;
        this.localVarCount = 0;
        this.callGraph = new HashMap<Integer, CallNode>(); 

        this.allDeclaredVariables = new HashMap<String, String>();
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

    public void constructCallGraph(String className, String methodName, int siteIndex, HashMap<String, ClassAttr> symbolTable, boolean inlineable)
    {
        CallNode callNode = new CallNode();

        ClassAttr classAttr = symbolTable.get(className);
        while(!classAttr.methods.containsKey(methodName) && classAttr.parentName != null) classAttr = symbolTable.get(classAttr.parentName);
        callNode.callSigns.add(classAttr.methods.get(methodName));

        classAttr = symbolTable.get(className);
        for(String childName : classAttr.children) constructCallGraphHelper(childName, methodName, symbolTable, callNode);

        callNode.inlineable = inlineable;
        callNode.monomorphic = callNode.callSigns.size() == 1;
        callNode.shouldInline = callNode.monomorphic && callNode.inlineable;

        this.callGraph.put(siteIndex, callNode);
    }

    private void constructCallGraphHelper(String className, String methodName, HashMap<String, ClassAttr> symbolTable, CallNode callGraphEntry)
    {
        if(symbolTable.get(className).methods.containsKey(methodName)) callGraphEntry.callSigns.add(symbolTable.get(className).methods.get(methodName));

        for(String childName: symbolTable.get(className).children) constructCallGraphHelper(childName, methodName, symbolTable, callGraphEntry);
    }

    public void checkRecursion()
    {
        for(CallNode callNode : this.callGraph.values())
        {
            if(callNode.shouldInline)
            {
                HashSet<MethodAttr> visited = new HashSet<MethodAttr>();
                if(checkRecursionHelper(callNode, visited)) callNode.shouldInline = false;
            }
        }
    }
    public boolean checkRecursionHelper(CallNode callNode, HashSet<MethodAttr> visited)
    {
        if(callNode.callSigns.size() > 1) return false;
    
        MethodAttr toCall = callNode.callSigns.iterator().next();
    
        if(visited.contains(toCall)) return true;
    
        visited.add(toCall);
    
        for(CallNode nextCallNode : toCall.callGraph.values()) 
        {
            if(nextCallNode.shouldInline && checkRecursionHelper(nextCallNode, visited)) return true;
        }
    
        visited.remove(toCall);
    
        return false;
    }

    public void printInfo()
    {
        System.out.println("Method: " + this.methodName + " in class: " + this.className);
        
        for(CallNode callNode: callGraph.values()) callNode.printInfo();
    }
}
