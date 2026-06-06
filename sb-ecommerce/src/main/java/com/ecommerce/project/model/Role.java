package com.ecommerce.project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor //??can we skip this
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="role_id") //Giving name to column having roleId
    private Integer roleId;

    @Column(name="role_name") //Giving name to DB column having roleName
    @Enumerated(EnumType.STRING) //Store This ENUM IN DB as String Not Default behaviour of store OrdinalIndex - Int
    private AppRoleName roleName;

    /* Only Single parameter constructor needed */
    public Role(AppRoleName roleName){
        this.roleName=roleName;
    }

}
