package el.dude.java.basics.inheritance;

public class Value implements IValue
{

   private final double val;

   public Value(final double val)
   {
      this.val = val;
   }

   @Override
   public double getValue()
   {
      return val;
   }
}
