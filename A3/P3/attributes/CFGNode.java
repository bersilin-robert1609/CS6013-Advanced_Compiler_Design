package attributes;
import syntaxtree.*;

import java.util.HashMap;

public class CFGNode 
{
    public StatementType type;
    public Node node;
    String label; // Only for debugging

    public CFGNode parent1;
    public CFGNode parent2;

    public CFGNode posNext;
    public CFGNode negNext;

    public CFGNode ifDummy; // Dummy node for if-else statements

    public HashMap<String, VarAttr> in;
    public HashMap<String, VarAttr> out;

    public CFGNode(StatementType type, Node node, String currClass, String currMethod, HashMap<String, ClassAttr> symbolTable)
    {
        this.type = type;
        this.node = node;
        this.label = symbolTable.get(currClass).methods.get(currMethod).getNextLabel();
        this.parent1 = null;
        this.parent2 = null;
        this.posNext = null;
        this.negNext = null;
        this.in = new HashMap<String, VarAttr>();
        this.out = new HashMap<String, VarAttr>();

        for(VarAttr varAttr : symbolTable.get(currClass).methods.get(currMethod).localVars.values())
        {
            this.in.put(varAttr.name, new VarAttr(varAttr));
            this.out.put(varAttr.name, new VarAttr(varAttr));
        }

        symbolTable.get(currClass).methods.get(currMethod).labelToNode.put(this.label, this);
    }

    public void setParent1(CFGNode parent1) 
    {
        this.parent1 = parent1;
    }

    public void setParent2(CFGNode parent2)
    {
        this.parent2 = parent2;
    }

    public void setParents(CFGNode parent1, CFGNode parent2)
    {
        this.parent1 = parent1;
        this.parent2 = parent2;
    }

    public void setPosNext(CFGNode posNext)
    {
        this.posNext = posNext;
    }

    public void setNegNext(CFGNode negNext)
    {
        this.negNext = negNext;
    }

    public void setNexts(CFGNode posNext, CFGNode negNext)
    {
        this.posNext = posNext;
        this.negNext = negNext;
    }

    public void dataFlowMeet()
    {
        if(this.parent1 == null) return;

        // Meet operator from parent1 and parent2
        this.in.clear();
        if(parent2 == null)
        {
            for(VarAttr varAttr : parent1.out.values()) this.in.put(varAttr.name, new VarAttr(varAttr));
            return;
        }

        for(VarAttr var1 : parent1.out.values())
        {
            VarAttr var2 = parent2.out.get(var1.name);
            int condCode = -1; // 0 - TOP, 1 - CONST, 2 - BOTTOM
            String constValue = null;

            if(var1.isTop() && var2.isTop()) condCode = 0;
            else if(var1.isTop() && var2.isConstant())
            {
                condCode = 1;
                constValue = var2.value;
            }
            else if(var1.isConstant() && var2.isTop())
            {
                condCode = 1;
                constValue = var1.value;
            }
            else if(var1.isConstant() && var2.isConstant())
            {
                if(var1.value.equals(var2.value))
                {
                    condCode = 1;
                    constValue = var1.value;
                }
                else condCode = 2;
            }
            else if(var1.isBottom() || var2.isBottom()) condCode = 2;

            this.in.put(var1.name, new VarAttr(var1));
            VarAttr var = this.in.get(var1.name);

            if(condCode == 0) var.setTop();
            else if(condCode == 1) var.setConstant(constValue);
            else if(condCode == 2) var.setBottom();
        }
        return;
    }

    public void printCFGNode()
    {
        // FILL IN
        return;
    }
}
