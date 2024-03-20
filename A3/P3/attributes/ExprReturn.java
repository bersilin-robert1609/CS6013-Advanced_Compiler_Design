package attributes;

public class ExprReturn 
{
    public boolean isConst;
    public String value; //can also be a printing statement

    public ExprReturn(boolean isConst, String value)
    {
        this.isConst = isConst;
        this.value = value;
    }
}
