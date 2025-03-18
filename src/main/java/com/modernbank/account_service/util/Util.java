package com.modernbank.account_service.util;

import java.time.LocalDate;
import java.util.UUID;

public class Util {
    public static String generateIban(){
        LocalDate localDate = LocalDate.now();
        int year = localDate.getYear();
        int month = localDate.getMonth().getValue();
        int day = localDate.getDayOfMonth();
        return "MB"+
                year +
                UUID.randomUUID().toString().replace("-", "") +
                month +
                UUID.randomUUID().toString().replace("-","").substring(0,10) +
                day;
    }
}