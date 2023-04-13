package com.felixseifert.swedisheventplanners.backend.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;

import com.felixseifert.swedisheventplanners.backend.model.enums.Role;
import com.felixseifert.swedisheventplanners.backend.model.enums.RoleConverter;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Employee)) {
            return false;
        }
        Employee employee = (Employee) obj;
        return Objects.equals(this.getId(), employee.getId()) &&
                Objects.equals(name, employee.name) &&
                Objects.equals(roles, employee.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId(), name, roles);
    }
}
