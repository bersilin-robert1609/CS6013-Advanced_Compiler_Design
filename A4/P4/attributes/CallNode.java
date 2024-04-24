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
        System.out.println("CallNode:");
        System.out.println("  Inlineable: " + this.inlineable);
        System.out.println("  Monomorphic: " + this.monomorphic);
        System.out.println("  ShouldInline: " + this.shouldInline);
        System.out.println("  CallSigns:");
        for(MethodAttr method : this.callSigns)
        {
            System.out.println("    Method: " + method.methodName + " in class: " + method.className);
        }
    }
}
