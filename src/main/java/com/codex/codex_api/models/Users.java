package com.codex.codex_api.models;

import com.codex.codex_api.models.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tb_users")
public class Users extends RepresentationModel<Users> implements Serializable, UserDetails {

    private static  final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idAccess;
    private String login;
    private String password;
    private UserRole role;
    private String name;

    //atributes for unity only
    private String zipCode;
    private String addres;

    //atribute for student only
    private String nameUnity;

    @OneToOne(mappedBy = "user")
    private MyAvatar myAvatar;

    //Constructor for register user Admin
    public Users(String login, String password, UserRole role){
        this.login = login;
        this.password = password;
        this.role = role;
    }

    //Constructor for register user Unity
    public Users(String login, String password, UserRole role, String name, String zipCode, String address){
        this.login = login;
        this.password = password;
        this.role = role;
        this.name = name;
        this.zipCode = zipCode;
        this.addres = address;
    }

    //SpringSecurity
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.role == UserRole.ADMIN) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        else return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
