package com.marcofaccani.awssqs.model;

import java.math.BigDecimal;


// just a dummy data class
public record WireTransferMessage(
    String transactionId,
    String recipientFullName,
    String recipientEmail,
    String recipientIban,
    BigDecimal amount,
    String currency,
    String description
) {

}
