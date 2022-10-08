package com.securitytest.securitytest.util;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;
@Slf4j
public class DateValidator {
    public static void ValidateDateRange(Date fromDate, Date toDate){
        try {
            log.info("Validating Date Range  - {} : {} ",fromDate,toDate);
            if (!fromDate.before(toDate)) throw new RuntimeException("invalid Date interval");
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }
}
