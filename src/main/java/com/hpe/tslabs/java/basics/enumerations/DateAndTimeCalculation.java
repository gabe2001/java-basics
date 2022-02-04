// (c) Copyright 2022 Hewlett-Packard Development Company, L.P.
package com.hpe.tslabs.java.basics.enumerations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.DateTimeException;
import java.time.Duration;
import java.time.Period;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.Objects;

/**
 * com.hpe.tslabs.java.basics.enumerations
 * <p>
 * Author: Gabriel Inäbnit - 2022-02-04
 * <p>
 * We're hiding the tyranny of the choice between Duration and Period.
 * <p>
 * Offset a given ISO date and time value by an amount of units or by a ISO period
 * <p>
 * We're limiting the type to integer for the amount values. For the purpose of the namespace object, lifespans in
 * the sub-seconds realm are not required.
 */
public class DateAndTimeCalculation
{

   private static final Logger logger = LoggerFactory.getLogger(DateAndTimeCalculation.class);

   private static final String PERIOD_DURATION_SEPARATOR = "T";
   private static final String DURATION_ONLY_PREFIX      = "PT";

   private final String        dateAndTime;
   private final ZonedDateTime parsedDateAndTime;

   DateAndTimeCalculation(final String dateAndTime) throws DateTimeParseException
   {
      this.dateAndTime = dateAndTime;
      this.parsedDateAndTime = ZonedDateTime.parse(dateAndTime);
   }

   /**
    * We're hiding the complexity of the choice between Duration and Period
    *
    * @param amount of units (positive or negative)
    * @param units  name
    * @return ISO date and time with offset applied
    */
   public String offsetBy(final int amount, final String units)
   {
      final DateAndTimeOffsetFunction function = DateAndTimeOffsetFunctionImpl.getFunction(units);
      try
      {
         Objects.requireNonNull(function);
         return parsedDateAndTime.plus(function.offsetBy(amount)).toString();
      }
      catch (final NullPointerException e)
      {
         logger.error("No methods exists for units: {}", units);
      }
      return dateAndTime;
   }

   /**
    * Offsetting a given ISO date and time by a given ISO period.
    * Under the hood {@code java.time.Period} and {@code java.time.Duration} are used to handle the full ISO period specification.
    * To fully apply a ISO period, both classes may have to be employed.
    * <p>
    * Example ISO 8601 periods:
    * <pre>
    *    P1Y2M3DT4H5M6.789S -- 1 year, 2 months, 3 days, 4 hours, 5 minutes, 6.789 seconds
    *    P1Y2M3D            -- 1 year, 2 months, 3 days
    *    PT4H5M6.789S       -- 4 hours, 5 minutes, 6.789 seconds
    *    PT-4H-5M-6.789S    -- minus 4 hours, minus 5 minutes, minus 6.789 seconds
    *    PT-4H5M-6.789S     -- minus 4 hours, plus 5 minutes, minus 6.789 seconds
    * </pre>
    *
    * @param period ISO string
    * @return ISO date and time string with period applied
    */
   public String offsetBy(final String period)
   {
      if (period == null || !(period.startsWith("P")))
      {
         logger.error("\"{}\" is not a ISO 8601 formatted period string", period);
         return dateAndTime;
      }
      try
      {
         ZonedDateTime result = parsedDateAndTime;
         final String[] periodParts = period.split(PERIOD_DURATION_SEPARATOR);
         // period and duration
         if (periodParts.length == 2)
         {
            final String periodPart = periodParts[0];
            final String durationPart = periodParts[1];
            if (periodPart.length() > 1)
            {
               result = result.plus(Period.parse(periodPart));
            }
            if (durationPart.length() > 1)
            {
               result = result.plus(Duration.parse(DURATION_ONLY_PREFIX + durationPart));
            }
         }
         // period or duration
         else if (periodParts.length == 1)
         {
            final String part = periodParts[0];
            if (part.startsWith(DURATION_ONLY_PREFIX))
            {
               result = result.plus(Duration.parse(part));
            }
            else
            {
               result = result.plus(Period.parse(part));
            }
         }
         return result.toString();
      }
      catch (final DateTimeException | ArithmeticException e)
      {
         logger.error("Error {} offsetting by {}: {}", dateAndTime, period, e.getMessage());
      }
      return dateAndTime;
   }

}
