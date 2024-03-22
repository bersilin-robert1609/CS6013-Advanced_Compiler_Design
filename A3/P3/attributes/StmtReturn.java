package attributes;

public class StmtReturn 
{
    public CFGNode beginNode;
    public CFGNode endNode;

    public StmtReturn(CFGNode beginNode, CFGNode endNode)
    {
        this.beginNode = beginNode;
        this.endNode = endNode;
    }
}
