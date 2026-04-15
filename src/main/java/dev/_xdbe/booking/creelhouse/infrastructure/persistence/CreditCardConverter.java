package dev._xdbe.booking.creelhouse.infrastructure.persistence;


import javax.crypto.IllegalBlockSizeException;
import javax.crypto.BadPaddingException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.beans.factory.annotation.Autowired;

import dev._xdbe.booking.creelhouse.infrastructure.persistence.CryptographyHelper;


@Converter
public class CreditCardConverter implements AttributeConverter<String, String> {

    @Autowired
    private CryptographyHelper cryptographyHelper;

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null || attribute.isBlank()) {
            return attribute;
        }

        try {
            return CryptographyHelper.encryptData(attribute);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return dbData;
        }

        String pan;
        try {
            pan = cryptographyHelper.decryptData(dbData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return panMasking(pan);
    }

    private String panMasking(String pan) {
        if (pan == null || pan.length() < 4) {
            return pan;
        }

        int visibleDigits = 4;
        int maskedLength = pan.length() - visibleDigits;

        return "*".repeat(maskedLength) + pan.substring(maskedLength);
    }

    
}
