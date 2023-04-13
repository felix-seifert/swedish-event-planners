package com.felixseifert.swedisheventplanners.backend.model;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "clients")
@Getter
@Setter
public class Client extends AbstractEntity {

    @Column(nullable = false)
    private String name;

    private String contactDetails;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Client)) {
            return false;
        }
        Client client = (Client) obj;
        return Objects.equals(this.getId(), client.getId()) &&
                Objects.equals(name, client.name) &&
                Objects.equals(contactDetails, client.contactDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId(), name, contactDetails);
    }

    @Override
    public String toString() {
        return "Client{" +
                "name='" + name + '\'' +
                ", contactDetails='" + contactDetails +
                '}';
    }
}
