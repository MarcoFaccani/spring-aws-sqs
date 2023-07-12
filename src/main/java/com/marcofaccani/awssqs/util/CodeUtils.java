package com.marcofaccani.awssqs.util;

import com.marcofaccani.awssqs.model.interfaces.CheckedSupplier;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class CodeUtils {

  public static <S> S executeWithHandling(final CheckedSupplier<S> supplier) {
    try {
      return supplier.get();
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  public static <S> S executeWithHandling(final CheckedSupplier<S> supplier, final String customErrMsg) {
    try {
      return supplier.get();
    } catch (Exception e) {
      final var exceptionMessage = e.getMessage();
      final var errMsg = String.format(customErrMsg, exceptionMessage);
      throw new RuntimeException(errMsg);
    }
  }

}
