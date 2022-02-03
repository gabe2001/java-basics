// (c) Copyright 2022 Hewlett-Packard Development Company, L.P.
package com.hpe.tslabs.java.basics.enumerations;

import java.time.temporal.TemporalAmount;

/**
 * com.hpe.tslabs.java.basics.enumerations
 * <p>
 * Author: Gabriel Inäbnit - 2022-02-03
 */
//@FunctionalInterface
public interface DateAndTimeFunctionsInt extends DateAndTimeFunctions
{
   TemporalAmount getTemporalAmount(final Integer amount);
}
