package attributes;

public class ClassVarAttr extends VarAttr
{
    public ClassVarAttr(String name, String type)
    {
        this.name = name;
        this.type = type;
        this.constPropType = LatticePoint.BOTTOM;
    }
}
