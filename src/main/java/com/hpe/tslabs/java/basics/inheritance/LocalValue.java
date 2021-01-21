/**
 * (C) Copyright 2021 Hewlett Packard Enterprise Development LP
 *
 * @author Gabriel Inäbnit - 2021-01-21
 */

package com.hpe.tslabs.java.basics.inheritance;

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
