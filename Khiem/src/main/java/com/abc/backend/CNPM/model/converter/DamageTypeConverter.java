package com.abc.backend.CNPM.model.converter;
import com.abc.backend.CNPM.model.enums.DamageType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class DamageTypeConverter implements AttributeConverter<DamageType, String> {

    @Override
    public String convertToDatabaseColumn(DamageType attribute) {
        if (attribute == null) return null;
        return switch (attribute) {
            case TotalLoss -> "Total Loss";
            default        -> attribute.name();
        };
    }

    @Override
    public DamageType convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        return switch (dbData) {
            case "Total Loss" -> DamageType.TotalLoss;
            default           -> DamageType.valueOf(dbData);
        };
    }
}
