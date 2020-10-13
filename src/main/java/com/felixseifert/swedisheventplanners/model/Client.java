package com.felixseifert.swedisheventplanners.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
@ToString
public class Client extends AbstractEntity {

    private String name;

    private String contactDetails;
}
