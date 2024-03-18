package attributes;

public class ParamAttr extends VarAttr
{
    public ParamAttr(String name, String type)
    {
        this.name = name;
        this.type = type;
        this.constPropType = LatticePoint.BOTTOM;
    }
}
