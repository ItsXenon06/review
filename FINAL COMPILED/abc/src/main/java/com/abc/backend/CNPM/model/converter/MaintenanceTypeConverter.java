package com.abc.backend.CNPM.model.converter;

import com.abc.backend.CNPM.model.enums.MaintenanceType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class MaintenanceTypeConverter implements AttributeConverter<MaintenanceType, String> {

    @Override
    public String convertToDatabaseColumn(MaintenanceType attribute) {
        if (attribute == null) return null;
        return switch (attribute) {
            case OilChange -> "Oil Change";
            default        -> attribute.name();
        };
    }

    @Override
    public MaintenanceType convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        return switch (dbData) {
            case "Oil Change" -> MaintenanceType.OilChange;
            default           -> MaintenanceType.valueOf(dbData);
        };
    }
}
