package com.kata.bootfetch.kata_fetch.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Set;

// Для того, чтобы в дальнейшим использовать класс User в Spring Security, он должен реализовывать интерфейс UserDetails.
// UserDetails можно представить, как адаптер между БД пользователей и тем что требуется Spring Security внутри SecurityContextHolder

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", unique = true, nullable = false)
    @Size(min = 2, max = 20)
    private String firstName; // уникальное значение

    @Column(name = "last_name")
    @Size(min = 2, max = 20)
    private String lastName;

    @Column(name = "age")
    private byte age;

    @Column(name = "password", nullable = false)
    @Size(min = 2, max = 20)
    private String password;

    @Column(name = "email", unique = true, nullable = false)
    @Size(min = 7, max = 20)
    private String email;

    //@JsonBackReference //убираем циклические ссылки
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
//    @JsonIdentityInfo(
//            generator = ObjectIdGenerators.PropertyGenerator.class,
//            property = "id"
//    )
    private Set<Role> roles;

    public User() {
    }

    public User(String firstName, String lastName, byte age, String password, String email, Set<Role> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.password = password;
        this.email = email;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String name) {
        this.firstName = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public byte getAge() {
        return age;
    }

    public void setAge(byte age) {
        this.age = age;
    }


    /*
    используется в классе class LoginSuccessHandler для получения списка ролей
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return firstName;
    }

    //Срок учетной записи не истек
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //Аккаунт не заблокирован
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //Срок действия не истек
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //Аккаунт включен
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + firstName + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                '}';
    }
}
