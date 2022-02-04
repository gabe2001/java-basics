// (c) Copyright 2022 Hewlett-Packard Development Company, L.P.
package com.hpe.tslabs.java.basics.enumerations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Period;
import java.time.temporal.TemporalAmount;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * com.hpe.tslabs.java.basics.enumerations
 * <p>
 * Author: Gabriel Inäbnit - 2022-02-03
 * <p>
 * {@link Duration}: {@code PnDTnHnMn.nS}
 * <p>
 * {@link Period}: {@code PnYnMnD}
 * <p>
 * We cannot add/subtract months or years this way. When attempted we get this exception:
 * java.time.temporal.UnsupportedTemporalTypeException: Unsupported unit: Months
 * <p>
 * Explanation why we need to make an extra effort to calculate years and months and inspiration for this
 * implementation:
 *
 * @see <a href="https://stackoverflow.com/questions/39907925/why-instant-does-not-support-operations-with-chronounit-years/49252497">why-instant-does-not-support-operations-with-chronounit-years</a>
 * @see <a href="https://stackoverflow.com/questions/30833582/how-to-calculate-the-number-of-days-in-a-period">how-to-calculate-the-number-of-days-in-a-period</a>
 */
public enum DateAndTimeOffsetFunctionImpl implements DateAndTimeOffsetFunction
{

   //@formatter:off
   DAYS           ("days",       Period::ofDays),
   WEEKS          ("weeks",      Period::ofWeeks),
   MONTHS         ("months",     Period::ofMonths),
   YEARS          ("years",      Period::ofYears),
   NANOS          ("nanos",      Duration::ofNanos),
   MILLIS         ("millis",     Duration::ofMillis),
   SECONDS        ("seconds",    Duration::ofSeconds),
   MINUTES        ("minutes",    Duration::ofMinutes),
   HOURS          ("hours",      Duration::ofHours),
   ;
   //@formatter:on

   private static final Logger logger = LoggerFactory.getLogger(DateAndTimeOffsetFunctionImpl.class);

   private static final Map<String, DateAndTimeOffsetFunctionImpl> lookup = new HashMap<>();

   static
   {
      for (final DateAndTimeOffsetFunctionImpl function : EnumSet.allOf(
              DateAndTimeOffsetFunctionImpl.class))
      {
         lookup.put(function.units, function);
      }
   }

   private final String                    units;
   private final DateAndTimeOffsetFunction function;


   /**
    * Enum constructor
    *
    * @param units name of unit
    */
   DateAndTimeOffsetFunctionImpl(final String units, final DateAndTimeOffsetFunction function)
   {
      this.units = units;
      this.function = function;
   }

   @Override
   public TemporalAmount offsetBy(final int amount)
   {
      return function.offsetBy(amount);
   }

   public static DateAndTimeOffsetFunction getFunction(final String units)
   {
      return lookup.get(units);
   }

}
