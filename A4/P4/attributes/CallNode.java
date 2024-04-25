package attributes;

import java.util.HashSet;

public class CallNode 
{
    public HashSet<MethodAttr> callSigns;
    public boolean inlineable;
    public boolean monomorphic;
    public boolean shouldInline;

    public CallNode()
    {
        this.callSigns = new HashSet<MethodAttr>();
        this.inlineable = false;
        this.monomorphic = false;
        this.shouldInline = false;
    }

    public void printInfo()
    {
        System.err.println("CallNode:");
        System.err.println("  Inlineable: " + this.inlineable);
        System.err.println("  Monomorphic: " + this.monomorphic);
        System.err.println("  ShouldInline: " + this.shouldInline);
        System.err.println("  CallSigns:");
        for(MethodAttr method : this.callSigns)
        {
            System.err.println("    Method: " + method.methodName + " in class: " + method.className);
        }
    }
}
