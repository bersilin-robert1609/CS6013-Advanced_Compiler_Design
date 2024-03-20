package attributes;
import syntaxtree.*;
import java.util.HashMap;

public class CFGNode
{
    public StatementType type;
    public Node node;
    public String parent;
    public String label; // Label for the Node : is of form L1, L2, L3, ...
    public String posNext; // Label for the Node when the condition is true
    public String negNext; // Label for the Node when the condition is false

    public HashMap<String, VarAttr> in;
    public HashMap<String, VarAttr> out;

    public CFGNode(StatementType type, Node node, String label)
    {
        this.type = type;
        this.node = node;
        this.label = label;
        this.parent = null;
        this.posNext = null;
        this.negNext = null;
        this.in = new HashMap<String, VarAttr>();
        this.out = new HashMap<String, VarAttr>();
    }
}
