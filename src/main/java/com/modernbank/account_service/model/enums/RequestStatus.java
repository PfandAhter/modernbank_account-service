package com.modernbank.account_service.model.enums;

public enum RequestStatus {
    OPEN,            // Açık
    IN_PROGRESS,     // İşlemde
    WAITING_USER,    // Kullanıcı yanıtı bekleniyor
    RESOLVED,        // Çözüldü
    CLOSED           // Kapatıldı
}