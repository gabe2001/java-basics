package el.dude.java.basics.enumerations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.Period;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAmount;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Author: Gabriel Inäbnit - 2022-02-03
 * <p>
 * {@link java.time.Duration}: {@code PnDTnHnMn.nS}
 * <p>
 * {@link java.time.Period}: {@code PnYnMnD}
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
public enum DateAndTimeOffsetCalculationNonLambdaApproach
{

   //@formatter:off
   NANOS          ("nanos",      Duration.class,   "ofNanos",     Long.TYPE),
   MILLIS         ("millis",     Duration.class,   "ofMillis",    Long.TYPE),
   SECONDS        ("seconds",    Duration.class,   "ofSeconds",   Long.TYPE),
   MINUTES        ("minutes",    Duration.class,   "ofMinutes",   Long.TYPE),
   HOURS          ("hours",      Duration.class,   "ofHours",     Long.TYPE),
   DAYS           ("days",       Period.class,     "ofDays",      Integer.TYPE),
   WEEKS          ("weeks",      Period.class,     "ofWeeks",     Integer.TYPE),
   MONTHS         ("months",     Period.class,     "ofMonths",    Integer.TYPE),
   YEARS          ("years",      Period.class,     "ofYears",     Integer.TYPE),
   DURATION       ("duration",   Duration.class,   "parse",       CharSequence.class),
   PERIOD         ("period",     Period.class,     "parse",       CharSequence.class),
   // This will simply log an error
   BOOM           ("boom",       Period.class,     "boom",        CharSequence.class),
   ;
   //@formatter:on

   private static final Logger logger = LoggerFactory.getLogger(DateAndTimeOffsetCalculationNonLambdaApproach.class);

   private static final Map<String, DateAndTimeOffsetCalculationNonLambdaApproach> lookup = new HashMap<>();

   static
   {
      for (final DateAndTimeOffsetCalculationNonLambdaApproach lambda : EnumSet.allOf(
              DateAndTimeOffsetCalculationNonLambdaApproach.class))
      {
         lookup.put(lambda.units, lambda);
      }
   }

   private final String units;
   private final Method method;

   /**
    * Enum constructor
    *
    * @param units          name of unit
    * @param clazz          java.time class
    * @param methodName     to be invoked on clazz
    * @param parameterTypes method signature
    */
   DateAndTimeOffsetCalculationNonLambdaApproach(final String units, final Class<?> clazz, final String methodName,
           final Class<?>... parameterTypes)
   {
      Method establishMethod;
      this.units = units;
      try
      {
         establishMethod = clazz.getMethod(methodName, parameterTypes);
      }
      catch (final NoSuchMethodException e)
      {
         establishMethod = null;
         final Logger constructorLogger = LoggerFactory.getLogger(DateAndTimeOffsetCalculationNonLambdaApproach.class);
         constructorLogger.error("Class {} has no method named {}", clazz.getName(), methodName);
      }
      this.method = establishMethod;
   }

   /**
    * We're hiding (maybe) the complexity of the choice between Duration and Period
    *
    * @param from   ISO date and time
    * @param amount of units (positive or negative)
    * @param units  name
    * @return ISO date and time with offset applied
    */
   public static String getResultingDateAndTime(final String from, final int amount, final String units)
   {
      final DateAndTimeOffsetCalculationNonLambdaApproach function = lookup.get(units);
      try
      {
         Objects.requireNonNull(function.method);
         return ZonedDateTime.parse(from).plus((TemporalAmount) function.method.invoke(null, amount)).toString();
      }
      catch (final NullPointerException e)
      {
         logger.error("No methods exists for units: {}", units);
      }
      catch (final IllegalAccessException | InvocationTargetException e)
      {
         logger.error("Error {} offsetting by {} {}: {}", from, amount, units, e.getMessage());
      }
      return from;
   }

   /**
    * Offsetting a given date and time with a period or duration
    *
    * @param from   ISO data and time
    * @param period {@link java.time.Duration} {@code PnDTnHnMn.nS} or {@link java.time.Period} {@code PnYnMnD}
    * @param units  duration or period
    * @return ISO date and time with offset applied
    */
   public static String getResultingDateAndTime(final String from, final String period, final String units)
   {
      final DateAndTimeOffsetCalculationNonLambdaApproach function = lookup.get(units);
      try
      {
         switch (function.units)
         {
            case "duration":
            case "period":
               return ZonedDateTime.parse(from).plus((TemporalAmount) function.method.invoke(null, period)).toString();
            default:
               return from;
         }
      }
      catch (final NullPointerException e)
      {
         logger.error("No parse method exists for {}", units);
      }
      catch (final IllegalAccessException | InvocationTargetException e)
      {
         logger.error("\"{}\" {}", period, e.getCause().getMessage());
      }
      return from;
   }

   /**
    * Offsetting a given ISO date and time by a given ISO period.
    * Note: java.time.Period and java.time.Duration are used to handle the full ISO period specification.
    * <p>
    * Example ISO periods:
    * <pre>
    *    P1Y2M3DT4H5M6.789S
    *    P1Y2M3D
    *    P1Y2M3D
    * </pre>
    *
    * @param from      ISO date and time string
    * @param isoPeriod ISO period string
    * @return ISO date and time string with period applied
    */
   public static String getResultingDateAndTime(final String from, final String isoPeriod)
   {
      if (isoPeriod == null || !(isoPeriod.startsWith("P")))
      {
         logger.error("\"{}\" is not a ISO 8601 formatted period string", isoPeriod);
         return from;
      }
      try
      {
         ZonedDateTime result = ZonedDateTime.parse(from);
         final String[] periodParts = isoPeriod.split("T");
         // period and duration
         if (periodParts.length == 2)
         {
            final String period = periodParts[0];
            final String duration = periodParts[1];
            if (period.length() > 1)
            {
               result = result.plus(Period.parse(period));
            }
            if (duration.length() > 1)
            {
               result = result.plus(Duration.parse("PT" + duration));
            }
         }
         // period or duration
         else if (periodParts.length == 1)
         {
            final String part = periodParts[0];
            if (part.startsWith("PT"))
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
         logger.error("Error {} offsetting by {}: {}", from, isoPeriod, e.getMessage());
      }
      return from;
   }

   /**
    * Simple enum method
    *
    * @return units name
    */
   public String getUnits()
   {
      return this.units;
   }

}
