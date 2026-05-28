package com.abc.backend.CNPM.model.converter;

import com.abc.backend.CNPM.model.enums.FuelLevel;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class FuelLevelConverter implements AttributeConverter<FuelLevel, String> {

    @Override
    public String convertToDatabaseColumn(FuelLevel attribute) {
        if (attribute == null) return null;
        return switch (attribute) {
            case Full           -> "Full";
            case THREE_QUARTERS -> "3/4";
            case HALF           -> "1/2";
            case ONE_QUARTER    -> "1/4";
            case Empty          -> "Empty";
        };
    }

    @Override
    public FuelLevel convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        return switch (dbData) {
            case "Full"  -> FuelLevel.Full;
            case "3/4"   -> FuelLevel.THREE_QUARTERS;
            case "1/2"   -> FuelLevel.HALF;
            case "1/4"   -> FuelLevel.ONE_QUARTER;
            case "Empty" -> FuelLevel.Empty;
            default -> throw new IllegalArgumentException("Unknown fuel level: " + dbData);
        };
    }
}