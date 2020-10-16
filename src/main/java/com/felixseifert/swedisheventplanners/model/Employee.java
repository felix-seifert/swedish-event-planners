package com.felixseifert.swedisheventplanners.model;

import com.felixseifert.swedisheventplanners.model.enums.Role;
import com.felixseifert.swedisheventplanners.model.enums.RoleConverter;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
public class Employee extends AbstractEntity {

    private String name;

    @ElementCollection
    @CollectionTable(name = "roles_of_employees", joinColumns = @JoinColumn(name = "employee_id"))
    @Column(columnDefinition = "smallint")
    @Convert(converter = RoleConverter.class)
    private Set<Role> roles = new HashSet<>();

    public void addRole(Role role) {
        roles.add(role);
    }

    public void removeRole(Role role) {
        roles.remove(role);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return 13;
    }
}
