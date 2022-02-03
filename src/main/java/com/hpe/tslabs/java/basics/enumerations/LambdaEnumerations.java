// (c) Copyright 2022 Hewlett-Packard Development Company, L.P.
package com.hpe.tslabs.java.basics.enumerations;

import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;
import java.time.Period;
import java.time.temporal.TemporalAmount;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * com.hpe.tslabs.java.basics.enumerations
 * <p>
 * Author: Gabriel Inäbnit - 2022-02-03
 */
public enum LambdaEnumerations
{

   //@formatter:off
   MINUTES        ("minutes", "java.time.Duration", "ofMinutes"),
   DAYS           ("days",    "java.time.Period", "ofDays"),
   ;
   //@formatter:on

   private static final Map<String, LambdaEnumerations> lookup = new HashMap<>();

   static
   {
      for (final LambdaEnumerations lambda : EnumSet.allOf(LambdaEnumerations.class))
      {
         lookup.put(lambda.units, lambda);
      }
   }

   private final String units;
   private final String className;
   private final String methodName;

   LambdaEnumerations(final String units, final String className, final String methodName)
   {
      this.units = units;
      this.className = className;
      this.methodName = methodName;
   }

   /**
    * We're hiding the complexity (maybe) of the choice between Duration and Period
    *
    * @param from   ISO date and time
    * @param amount of units (positive or negative)
    * @param units  name
    * @return
    */
   public static String getResultingDateAndTime(final String from, final Long amount, final String units) throws Exception
   {
      final Instant fromInstant = Instant.parse(from);
      final LambdaEnumerations lamba = lookup.get(units);
      final Class clazz = Class.forName(lamba.className);
      final Method method = clazz.getMethod(lamba.methodName);
      return fromInstant.plus((TemporalAmount) method.invoke(amount)).toString();
   }

}
