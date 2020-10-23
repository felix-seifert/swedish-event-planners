package com.felixseifert.swedisheventplanners.backend.model.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;

@Converter
public class ProposalStatusConverter implements AttributeConverter<ProposalStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ProposalStatus proposalStatus) {
        if(proposalStatus == null) return null;
        return proposalStatus.getDatabaseCode();
    }

    @Override
    public ProposalStatus convertToEntityAttribute(Integer databaseCode) {
        if(databaseCode == null) return null;
        return Arrays.stream(ProposalStatus.values()).filter(type -> type.getDatabaseCode().equals(databaseCode))
                .findFirst().orElseThrow(IllegalArgumentException::new);
    }
}
