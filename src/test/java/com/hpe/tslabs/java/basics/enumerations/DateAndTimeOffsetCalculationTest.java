// (c) Copyright 2022 Hewlett-Packard Development Company, L.P.
package com.hpe.tslabs.java.basics.enumerations;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.Period;
import java.time.ZonedDateTime;
import java.time.chrono.IsoChronology;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * com.hpe.tslabs.java.basics.enumerations
 * <p>
 * Author: Gabriel Inäbnit - 2022-02-03
 */
class DateAndTimeOffsetCalculationTest
{

   final static private Logger logger = LoggerFactory.getLogger(DateAndTimeOffsetCalculationTest.class);

   final String dateAndTime = "2022-02-02T12:34:56.789Z";

   @Test
   void add_3_minutes_to_date_and_time()
   {
      final String resultingDateAndTime = DateAndTimeOffsetCalculation.getResultingDateAndTime(dateAndTime, 3,
              "minutes");
      assertEquals("2022-02-02T12:37:56.789Z", resultingDateAndTime);
   }

   @Test
   void add_3_days_to_date_and_time()
   {
      final String resultingDateAndTime = DateAndTimeOffsetCalculation.getResultingDateAndTime(dateAndTime, 3, "days");
      assertEquals("2022-02-05T12:34:56.789Z", resultingDateAndTime);
   }

   @Test
   void subtract_3_days_to_date_and_time()
   {
      final String resultingDateAndTime = DateAndTimeOffsetCalculation.getResultingDateAndTime(dateAndTime, -3, "days");
      assertEquals("2022-01-30T12:34:56.789Z", resultingDateAndTime);
   }

   @Test
   void add_300_weeks_to_date_and_time()
   {
      final String resultingDataAndTime = DateAndTimeOffsetCalculation.getResultingDateAndTime(dateAndTime, 300,
              "weeks");
      assertEquals("2027-11-03T12:34:56.789Z", resultingDataAndTime);
   }

   @Test
   void add_3_months_to_date_and_time()
   {
      final String resultingDataAndTime = DateAndTimeOffsetCalculation.getResultingDateAndTime(dateAndTime, 3,
              "months");
      assertEquals("2022-05-02T12:34:56.789Z", resultingDataAndTime);
   }

   @Test
   void subtract_3_months_to_date_and_time()
   {
      final String resultingDataAndTime = DateAndTimeOffsetCalculation.getResultingDateAndTime(dateAndTime, -3,
              "months");
      assertEquals("2021-11-02T12:34:56.789Z", resultingDataAndTime);
   }

   @Test
   void add_3_years_to_date_and_time()
   {
      final String resultingDataAndTime = DateAndTimeOffsetCalculation.getResultingDateAndTime(dateAndTime, 3, "years");
      assertEquals("2025-02-02T12:34:56.789Z", resultingDataAndTime);
   }

   @Test
   void add_months_to_date_and_time_exception()
   {
      UnsupportedTemporalTypeException thrown = Assertions.assertThrows(UnsupportedTemporalTypeException.class,
              () -> DateAndTimeOffsetCalculation.getResultingDateAndTime(dateAndTime, 3, "monthsAsIs"));
      Assertions.assertEquals("Unsupported unit: Months", thrown.getMessage());
   }

   /**
    * NOTE: gracefully handling wrong data and simply returning the input string
    */
   @Test
   void no_such_method_exception()
   {
      assertEquals(dateAndTime, DateAndTimeOffsetCalculation.getResultingDateAndTime(dateAndTime, 3, "boom"));
   }

   @Test
   void add_years_to_date_and_time_exception()
   {
      UnsupportedTemporalTypeException thrown = Assertions.assertThrows(UnsupportedTemporalTypeException.class,
              () -> DateAndTimeOffsetCalculation.getResultingDateAndTime(dateAndTime, 3, "yearsAsIs"));
      Assertions.assertEquals("Unsupported unit: Years", thrown.getMessage());
   }

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

   @Test
   void add_14_hours_to_date_and_time()
   {
      assertEquals("2022-02-03T02:34:56.789Z",
              DateAndTimeOffsetCalculation.getResultingDateAndTime(dateAndTime, "PT14H", "duration"));

   }

   @Test
   void subtract_14_hours_to_date_and_time()
   {
      assertEquals("2022-02-01T22:34:56.789Z",
              DateAndTimeOffsetCalculation.getResultingDateAndTime(dateAndTime, "PT-14H", "duration"));

   }

   /**
    * observe that adding 1 month to January 31st results in returning the last day of February
    */
   @Test
   void add_1_month_to_last_day_in_january()
   {
      assertEquals("2022-02-28T12:24:56Z",
              DateAndTimeOffsetCalculation.getResultingDateAndTime("2022-01-31T12:24:56Z", "P1M", "period"));

   }

   /**
    * observe the taking care of the leap year
    */
   @Test
   void add_1_month_to_last_day_in_january_of_2020()
   {
      assertEquals("2020-02-29T12:24:56Z",
              DateAndTimeOffsetCalculation.getResultingDateAndTime("2020-01-31T12:24:56Z", "P1M", "period"));

   }

   /**
    * lenient handling of wrong input and simply returning the input value
    */
   @Test
   void add_non_parseable_period()
   {
      assertEquals(dateAndTime,
              DateAndTimeOffsetCalculation.getResultingDateAndTime(dateAndTime, "I cannot be parsed", "period"));

   }

   /**
    * NOTE: java.time.Period can only parse the date part. The time part will cause a parse error.
    */
   @Test
   void add_iso_period()
   {
      assertEquals(dateAndTime,
              DateAndTimeOffsetCalculation.getResultingDateAndTime(dateAndTime, "P1Y2M3DT4H5M6" + ".789S", "period"));

   }

   @Test
   void iso_8601_period()
   {
      final String isoPeriod = "P1Y2M3DT4H5M6.789S";
      assertEquals("2023-04-05T16:40:03.578Z",
              DateAndTimeOffsetCalculation.getResultingDateAndTime(dateAndTime, isoPeriod));
   }

   @Test
   void iso_8601_period_duration_only()
   {
      final String isoPeriod = "PT4H5M6.789S";
      assertEquals("2022-02-02T16:40:03.578Z",
              DateAndTimeOffsetCalculation.getResultingDateAndTime(dateAndTime, isoPeriod));
   }

   @Test
   void iso_8601_period_only()
   {
      final String isoPeriod = "P1Y2M3D";
      assertEquals("2023-04-05T12:34:56.789Z",
              DateAndTimeOffsetCalculation.getResultingDateAndTime(dateAndTime, isoPeriod));
   }

   @Test
   void iso_8601_not_even_close()
   {
      final String isoPeriod = "A1B2C3D4E5F6";
      assertEquals(dateAndTime, DateAndTimeOffsetCalculation.getResultingDateAndTime(dateAndTime, isoPeriod));
   }

   @Test
   void iso_8601_too_many_ts()
   {
      final String isoPeriod = "P1Y2M3DT4H5M6.789STas";
      assertEquals(dateAndTime, DateAndTimeOffsetCalculation.getResultingDateAndTime(dateAndTime, isoPeriod));
   }

   @Test
   void iso_8601_too_many_ts_but_no_harm()
   {
      final String isoPeriod = "P1Y2M3DT4H5M6.789ST";
      assertEquals("2023-04-05T16:40:03.578Z",
              DateAndTimeOffsetCalculation.getResultingDateAndTime(dateAndTime, isoPeriod));
   }

}
