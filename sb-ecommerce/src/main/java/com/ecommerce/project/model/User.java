package com.ecommerce.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Table(name = "users",
        uniqueConstraints = {
                    @UniqueConstraint(columnNames = "username"), //also allowed: @UniqueConstraint(columnNames={"username, email})
                    @UniqueConstraint(columnNames = "email")    //applying Unique constraint on DB leve 'Composite column wise' (>1)
                    })                                         //@Column(unique=true) Do same but can't used for Composite columns

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @NotBlank(message = "username can't be blank")
    @Size(max=20, min = 3)
    @Column(name = "username")
    private String userName;

    @Email(message = "Incorrect Email Provided")
    @Size(max=50)
    @Column(name = "email")
    private String email;

    @NotBlank(message = "password can't be blank")
    @Size(max = 100, min = 5, message = "password size should be b/w 5-50")
    @Column(name = "password")
    private String password;

    public User(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    // User is Owning side because @JoinTable is defined here.
    // Hibernate uses this side to manage the users_roles join table.
    // Persist means save role when user is saved
    // Merge means update role when user is updated
    /*
        @JoinTable.name           → Join table name
        @JoinColumn.name          → FK column for owning entity
        @inverseJoinColumn.name   → FK column for associated entity

        **Names can be anything if Hibernate is creating new schema on application startup.
        **Names must match with database schema - if the schema already exists.
     */

    //Only UNI-DIRECTIONAL Mapping, user->role and not role->user | user.getRoles() allowed
    //Therefore no relationship code of user inside Role Class | role.getUser() Not-Allowed
    @ManyToMany(cascade={CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles", //New Join Table Name
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )

    /*
        Bidirectional Many-to-Many If you also want: role.getUsers() also
        then add this in Role:

        @ManyToMany(mappedBy = "roles") --> roles=name of below set!
        private Set<User> users = new HashSet<>();
     */
    private Set<Role> roles = new HashSet<>();


    /* User can be a seller of Products */
    @OneToMany(
            mappedBy = "user", //mapped to user inside Product Entity
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE},
            orphanRemoval = true
            // if user is Detached from product OR user.products.remove(x), then 'x' automatically deleted!
            // ✔️ Deletes a child entity when it is removed from the parent's relationship collection,
            //    "even if the parent still exists".
    )
    @ToString.Exclude
    private Set<Product> products = new HashSet<>();


    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinTable(
            name = "user_addresses",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name = "address_id")
    )
    @ToString.Exclude
    private List<Address> addressList= new ArrayList<>();

    @ToString.Exclude
    @OneToOne(mappedBy = "user",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            orphanRemoval = true
    )
    private Cart cart;
}
