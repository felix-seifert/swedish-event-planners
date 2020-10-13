package com.felixseifert.swedisheventplanners.model.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;

@Converter
public class EventTypeConverter implements AttributeConverter<EventType, Byte> {

    @Override
    public Byte convertToDatabaseColumn(EventType eventType) {
        if(eventType == null) return null;
        return eventType.getDatabaseCode();
    }

    @Override
    public EventType convertToEntityAttribute(Byte databaseCode) {
        if(databaseCode == null) return null;
        return Arrays.stream(EventType.values()).filter(type -> type.getDatabaseCode().equals(databaseCode))
                .findFirst().orElseThrow(IllegalArgumentException::new);
    }
}
