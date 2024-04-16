package attributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class CallNode 
{
    public int globalSiteNumber;
    public int localSiteNumber;
    public String methodName;
    public HashSet<CallSignature> callSigns;
    public boolean inlineable;

    public CallNode(int globalSiteNumber, int localSiteNumber, String methodName, boolean inlineable)
    {
        this.globalSiteNumber = globalSiteNumber;
        this.localSiteNumber = localSiteNumber;
        this.methodName = methodName;
        this.callSigns = new HashSet<CallSignature>();
        this.inlineable = inlineable;
    }

    public void addSignatures(String varType, HashMap<String, ClassAttr> symbolTable, HashMap<String, String> parentMap, HashMap<String, ArrayList<String>> childrenMap)
    {
        ClassAttr classAttr = symbolTable.get(varType); // First availability of the function in the parent side of the hierarchy
        while(!classAttr.methods.containsKey(this.methodName) && classAttr.parentName != null)
        {
            classAttr = symbolTable.get(parentMap.get(classAttr.parentName));
        }
        callSigns.add(new CallSignature(classAttr.className, this.methodName));

        addSignaturesTemp(varType, symbolTable, childrenMap);
    }

    public void addSignaturesTemp(String currClass, HashMap<String, ClassAttr> symbolTable, HashMap<String, ArrayList<String>> childrenMap)
    {
        if(currClass == null) return;

        if(symbolTable.get(currClass).methods.containsKey(this.methodName))
        {
            callSigns.add(new CallSignature(currClass, this.methodName));
        }
        
        for(String childName: childrenMap.get(currClass))
        {
            addSignaturesTemp(childName, symbolTable, childrenMap);
        }
    }
}
