package com.felixseifert.swedisheventplanners.model.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;

@Converter
public class PreferencesConverter implements AttributeConverter<Preference, Byte> {

    @Override
    public Byte convertToDatabaseColumn(Preference preference) {
        if(preference == null) return null;
        return preference.getDatabaseCode();
    }

    @Override
    public Preference convertToEntityAttribute(Byte databaseCode) {
        if(databaseCode == null) return null;
        return Arrays.stream(Preference.values()).filter(preference -> preference.getDatabaseCode().equals(databaseCode))
                .findFirst().orElseThrow(IllegalArgumentException::new);
    }
}
