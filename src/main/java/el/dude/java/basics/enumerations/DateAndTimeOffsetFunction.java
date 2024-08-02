package el.dude.java.basics.enumerations;

import java.time.temporal.TemporalAmount;

/**
 * Author: Gabriel Inäbnit - 2022-02-04
 */
@FunctionalInterface
public interface DateAndTimeOffsetFunction
{
   TemporalAmount offsetBy(final int amount);
}
