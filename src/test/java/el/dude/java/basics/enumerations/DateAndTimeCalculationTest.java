package el.dude.java.basics.enumerations;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.time.Period;
import java.time.chrono.IsoChronology;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Author: Gabriel Inäbnit - 2022-02-03
 */
class DateAndTimeCalculationTest
{

   //@formatter:off
   @ParameterizedTest
   @CsvSource({
           "2022-02-02T12:34:56.789Z,  3,    minutes,    2022-02-02T12:37:56.789Z",
           "2022-02-02T12:34:56.789Z,  3,    days,       2022-02-05T12:34:56.789Z",
           "2022-02-02T12:34:56.789Z,  -3,   days,       2022-01-30T12:34:56.789Z",
           "2022-02-02T12:34:56.789Z,  300,  weeks,      2027-11-03T12:34:56.789Z",
           "2022-02-02T12:34:56.789Z,  3,    months,     2022-05-02T12:34:56.789Z",
           "2022-02-02T12:34:56.789Z,  -3,   months,     2021-11-02T12:34:56.789Z",
           "2022-02-02T12:34:56.789Z,  3,    years,      2025-02-02T12:34:56.789Z",
           // NOTE: gracefully handling wrong data and simply returning the input string
           "2022-02-02T12:34:56.789Z,  3,    boom,       2022-02-02T12:34:56.789Z",
   })
   //@formatter:on
   void add_or_subtract_amount_of_units(final String from, final int amount, final String units, final String expected)
   {
      assertEquals(expected, new DateAndTimeCalculation(from).offsetBy(amount, units));
   }

//   //@formatter:off
//   @ParameterizedTest
//   @CsvSource({
//           "2022-02-02T12:34:56.789Z,  3, monthsAsIs, Unsupported unit: Months",
//           "2022-02-02T12:34:56.789Z,  3, yearsAsIs,  Unsupported unit: Years",
//   })
//   //@formatter:on
//   void add_months_to_date_and_time_exception(final String from, final int amount, final String units,
//           final String expectedErrorMessage)
//   {
//      UnsupportedTemporalTypeException thrown = Assertions.assertThrows(UnsupportedTemporalTypeException.class,
//              () -> DateAndTimeOffsetFunctionImpl.getResultingDateAndTime(from, amount, units));
//      Assertions.assertEquals(expectedErrorMessage, thrown.getMessage());
//   }

   //@Test
   void duration_stuff() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
   {
      // all methods of Duration
      Arrays.stream(Duration.class.getMethods()).forEach(System.out::println);
      // ofMinutes method of Duration
      System.out.println(Duration.class.getMethod("ofMinutes", Long.TYPE));
      // execute ofMinutes method of Duration - notice the first argument of invoke: null = static method
      System.out.println(Duration.class.getMethod("ofMinutes", Long.TYPE).invoke(null, 123));
   }

   //@Test
   void period_stuff() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
   {
      // all methods of Duration
      Arrays.stream(Period.class.getMethods()).forEach(System.out::println);
      // ofMinutes method of Duration
      System.out.println(Period.class.getMethod("ofYears", int.class));
      // execute ofMinutes method of Duration - notice the first argument of invoke: null = static method
      System.out.println(Period.class.getMethod("ofYears", Integer.TYPE).invoke(null, 123));
   }

   /**
    * understanding period representation
    */
   @Test
   void get_days_date_and_time_a_period()
   {
      final Period period = Period.of(1, 2, 3);
      assertEquals(1, period.getYears());
      assertEquals(2, period.getMonths());
      assertEquals(3, period.getDays());
      assertEquals(IsoChronology.INSTANCE, period.getChronology());
      assertEquals("P1Y2M3D", period.toString());
   }

   /**
    * understanding duration representation
    */
   @Test
   void get_days_date_and_time_a_duration()
   {
      final Duration duration = Duration.ofMillis(1234567890);
      assertEquals(1234567, duration.getSeconds());
      assertEquals("PT342H56M7.89S", duration.toString());
   }

//   //@formatter:off
//   @ParameterizedTest
//   @CsvSource({
//           "2022-02-02T12:34:56.789Z,  PT14H,               duration,   2022-02-03T02:34:56.789Z",
//           "2022-02-02T12:34:56.789Z,  PT-14H,              duration,   2022-02-01T22:34:56.789Z",
//           // observe that adding 1 month to January 31st results in returning the last day of February
//           "2022-01-31T12:24:56Z,      P1M,                 period,     2022-02-28T12:24:56Z",
//           // observe the taking care of the leap year
//           "2020-01-31T12:24:56Z,      P1M,                 period,     2020-02-29T12:24:56Z",
//           // lenient handling of wrong input and simply returning the input value
//           "2022-02-02T12:34:56.789Z,  I cannot be parsed,  period,     2022-02-02T12:34:56.789Z",
//           // NOTE: java.time.Period can only parse the date part. The time part will cause a parse error.
//           "2022-02-02T12:34:56.789Z,  P1Y2M3DT4H5M6.789S,  period,     2022-02-02T12:34:56.789Z",
//   })
//   //@formatter:on
//   void test_period_method_one_calculations(final String from, final String period, final String units,
//           final String expected)
//   {
//      assertEquals(expected, DateAndTimeOffsetFunctionImpl.getResultingDateAndTime(from, period, units));
//   }

   //@formatter:off
   @ParameterizedTest
   @CsvSource({
           "2022-02-02T12:34:56.789Z,  PT14H,                  2022-02-03T02:34:56.789Z",
           "2022-02-02T12:34:56.789Z,  PT-14H,                 2022-02-01T22:34:56.789Z",
           // observe that adding 1 month to January 31st results in returning the last day of February
           "2022-01-31T12:24:56Z,      P1M,                    2022-02-28T12:24:56Z",
           // observe the taking care of the leap year
           "2020-01-31T12:24:56Z,      P1M,                    2020-02-29T12:24:56Z",
           // lenient handling of wrong input and simply returning the input value
           "2022-02-02T12:34:56.789Z,  I cannot be parsed,    2022-02-02T12:34:56.789Z",
           // NOTE: java.time.Period can only parse the date part. The time part will cause a parse error.
           "2022-02-02T12:34:56.789Z,  P1Y2M3DT4H5M6.789S,     2023-04-05T16:40:03.578Z",
           "2022-02-02T12:34:56.789Z,  PT4H5M6.789S,           2022-02-02T16:40:03.578Z",
           "2022-02-02T12:34:56.789Z,  P1Y2M3D,                2023-04-05T12:34:56.789Z",
           "2022-02-02T12:34:56.789Z,  P1Y2M3DT4H5M6.789STas,  2022-02-02T12:34:56.789Z",
           // Trailing "T" does not do any harm
           "2022-02-02T12:34:56.789Z,  P1Y2M3DT4H5M6.789ST,    2023-04-05T16:40:03.578Z",
   })
   //@formatter:on
   void test_iso_8601_period_calculations(final String from, final String period, final String expected)
   {
      assertEquals(expected, new DateAndTimeCalculation(from).offsetBy(period));
   }

   @ParameterizedTest
   @EnumSource(DateAndTimeOffsetFunctionImpl.class)
   void do_something_with_the_enums(final DateAndTimeOffsetFunctionImpl theEnum)
   {
      assertTrue(theEnum.name().length() > 0);
   }

   /**
    * The CSV source automatically converts the first item to the enum type
    *
    * @param theEnum   enumeration
    * @param theString expected string value
    */
   //@formatter:off
   @ParameterizedTest
   @CsvSource({
           "NANOS,         nanos",
           "MILLIS,        millis",
           "SECONDS,       seconds",
           "MINUTES,       minutes",
           "HOURS,         hours",
           "DAYS,          days",
           "WEEKS,         weeks",
           "MONTHS,        months",
           "YEARS,         years",
   })
   //@formatter:on
   void validate_the_unit_strings(final DateAndTimeOffsetFunctionImpl theEnum, final String theString)
   {
      assertEquals(theString.toUpperCase(), theEnum.name());
   }

}
