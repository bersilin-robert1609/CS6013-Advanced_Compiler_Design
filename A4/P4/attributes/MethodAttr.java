package attributes;

import syntaxtree.*;

import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class MethodAttr 
{
    public Node methodNode;

    public String methodName;
    public String className;
    public LinkedHashMap<String, VarAttr> methodParams;
    public HashMap<String, VarAttr> methodVars;
    public String returnType;
    public int paramCount;
    public int localVarCount;

    public HashMap<String, String> allDeclaredVariables; // For final usage : Variable Name -> Type

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

        this.allDeclaredVariables = new HashMap<String, String>(); // Variable Name -> Type
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
        // For every call node in the method
        for(CallNode callNode : this.callGraph.values())
        {
            if(callNode.shouldInline)
            {
                HashSet<MethodAttr> visited = new HashSet<MethodAttr>();
                
                // Go through all the call signs of the call node
                for(MethodAttr methodAttr : callNode.callSigns)
                {
                    visited.clear();
                    if(checkRecursionHelper(visited, methodAttr)) callNode.shouldInline = false;
                }
            }
        }
    }
    public boolean checkRecursionHelper(HashSet<MethodAttr> visited, MethodAttr methodAttr)
    {
        if(visited.contains(methodAttr)) return true;
        visited.add(methodAttr);

        for(CallNode callNode : methodAttr.callGraph.values())
        {
            if(callNode.shouldInline)
            {
                for(MethodAttr methodAttr2 : callNode.callSigns)
                {
                    if(checkRecursionHelper(visited, methodAttr2)) return true;
                }
            }
        }

        return false;
    }

    public void printInfo()
    {
        System.err.println("Method: " + this.methodName + " in class: " + this.className);
        
        for(Map.Entry<Integer, CallNode> entry: this.callGraph.entrySet())
        {
            System.err.println("  CallSite: " + entry.getKey());
            entry.getValue().printInfo();
        }
    }
}
