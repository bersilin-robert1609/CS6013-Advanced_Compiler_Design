package attributes;
import syntaxtree.*;
import visitor.*;
import java.util.HashMap;

public class CFGNode<R, A>
{
    public StatementType type;
    public Node node;
    public String parent;
    public String label; // Label for the Node : is of form L1, L2, L3, ...
    public String posNext; // Label for the Node when the condition is true
    public String negNext; // Label for the Node when the condition is false

    public HashMap<String, LocalVarAttr> in;
    public HashMap<String, LocalVarAttr> out;

    public CFGNode()
    {
        this.type = null;
        this.node = null;
        this.label = null;
        this.posNext = null;
        this.negNext = null;
        this.in = new HashMap<String, LocalVarAttr>();
        this.out = new HashMap<String, LocalVarAttr>();
    }

    public R processNode(GJVisitor<R, A> v, A argu)
    {
        return this.node.accept(v, argu);
    }
}
