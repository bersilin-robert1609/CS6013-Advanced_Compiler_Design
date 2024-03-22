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

    public boolean isTop()
    {
        return this.constPropType == LatticePoint.TOP;
    }

    public boolean isBottom()
    {
        return this.constPropType == LatticePoint.BOTTOM;
    }

    public boolean isConstant()
    {
        return this.constPropType == LatticePoint.CONSTANT;
    }

    public void setConstant(String value)
    {
        this.constPropType = LatticePoint.CONSTANT;
        this.value = value;
    }

    public void setTop()
    {
        this.constPropType = LatticePoint.TOP;
    }

    public void setBottom()
    {
        this.constPropType = LatticePoint.BOTTOM;
    }

    public boolean isSame(VarAttr attr)
    {
        // Check all fields
        boolean check = true;
        check = check && this.name.equals(attr.name);
        check = check && this.dtype.equals(attr.dtype);
        check = check && this.constPropType == attr.constPropType;
        check = check && this.varType == attr.varType;
        check = check && this.value.equals(attr.value);
        return check;
    }
}
