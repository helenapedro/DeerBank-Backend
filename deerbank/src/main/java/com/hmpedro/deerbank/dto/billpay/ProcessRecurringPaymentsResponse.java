// ProcessRecurringPaymentsResponse.java
package com.hmpedro.deerbank.dto.billpay;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProcessRecurringPaymentsResponse {
    private String message;
    private int processedCount;
}
