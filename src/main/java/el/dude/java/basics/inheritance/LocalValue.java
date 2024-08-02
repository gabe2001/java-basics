package el.dude.java.basics.inheritance;

public class LocalValue extends Value implements IValue, ILocalValue
{

   public LocalValue(double value)
   {
      super(value);
   }

   public double getHalfValue()
   {
      return getValue() / 2;
   }

}
