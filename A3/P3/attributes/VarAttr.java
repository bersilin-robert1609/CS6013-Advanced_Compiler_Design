package attributes;

public class VarAttr 
{
    public String name;
    public String dtype; // Data type of the variable
    public LatticePoint constPropType; // Type of the constant
    public VarType varType; // Type of the variable
    public String value; // Value of the variable

    public VarAttr(String name, String dtype, LatticePoint constPropType, VarType varType)
    {
        this.name = name;
        this.dtype = dtype;
        this.constPropType = constPropType;
        this.varType = varType;
    }

    public VarAttr(VarAttr attr)
    {
        // Copy constructor
        this.name = attr.name;
        this.dtype = attr.dtype;
        this.constPropType = attr.constPropType;
        this.varType = attr.varType;
        this.value = attr.value;
    }
}
