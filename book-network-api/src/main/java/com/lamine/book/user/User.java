package com.lamine.book.user;

import com.lamine.book.role.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "_user")
@EntityListeners(AuditingEntityListener.class) //EntityListener specifies the listener of an Entity. it can automatically save when the user has been created, modified, .... it ensures traceability and data management
/* So Again if we have this annotation @EntityListeners(AuditingEntityListener.class), always think about going to our
  application configuration (main class) and add @EnableJpaAuditing*/
public class User implements UserDetails, Principal {

    @Id
    @GeneratedValue
    private Integer id;
    private String firstname;
    private String lastname;
    private LocalDate dateOfBirth;
    @Column(unique = true)
    private String email;
    private String password;
    private Boolean accountLocked;
    private Boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER) // The roles associated with this entity will be loaded automatically and immediately when the main entity is retrieved from the database
    private List<Role> roles;

    @CreatedDate // it automatically audited the entity
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;
    @LastModifiedDate
    @Column(insertable  = false)
    private LocalDateTime modifiedDate;





    @Override
    public String getName() {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles
                .stream()
                .map(r->new SimpleGrantedAuthority(r.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public String fullName(){
        return firstname + " " + lastname;
    }
}
