package com.marcofaccani.awssqs.model.interfaces;

/*
  used to allow to use within lambdas methods that throw checked exceptions
 */
@FunctionalInterface
public interface CheckedSupplier<T> {
  T get() throws Exception;
}