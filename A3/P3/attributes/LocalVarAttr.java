package attributes;

public class LocalVarAttr extends VarAttr
{
    public String constValue; // Value of the constant

    public LocalVarAttr(String name, String type)
    {
        this.name = name;
        this.type = type;
        this.constPropType = LatticePoint.TOP;
        this.constValue = null;
    }
}
