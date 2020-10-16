package com.felixseifert.swedisheventplanners.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Objects;
import javax.persistence.Table;

@Entity
@Table(name = "clients")
@Getter
@Setter
public class Client extends AbstractEntity {

    @Column(nullable = false)
    private String name;

    private String contactDetails;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return 22;
    }

    @Override
    public String toString() {
        return "Client{" +
                "name='" + name + '\'' +
                ", contactDetails='" + contactDetails +
                '}';
    }
}
