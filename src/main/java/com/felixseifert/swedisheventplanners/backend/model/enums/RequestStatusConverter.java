package com.felixseifert.swedisheventplanners.backend.model.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;

@Converter
public class RequestStatusConverter implements AttributeConverter<RequestStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(RequestStatus requestStatus) {
        if(requestStatus == null) return null;
        return requestStatus.getDatabaseCode();
    }

    @Override
    public RequestStatus convertToEntityAttribute(Integer databaseCode) {
        if(databaseCode == null) return null;
        return Arrays.stream(RequestStatus.values()).filter(type -> type.getDatabaseCode().equals(databaseCode))
                .findFirst().orElseThrow(IllegalArgumentException::new);
    }
}
