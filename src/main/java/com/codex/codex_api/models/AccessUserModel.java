package com.codex.codex_api.models;

import com.codex.codex_api.models.enums.UserRole;
import jakarta.persistence.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_access_users")
public class AccessUserModel extends RepresentationModel<AccessUserModel> implements Serializable, UserDetails {

    private static  final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    private UUID idMyAvatar;

    public AccessUserModel() {
        // Construtor vazio
    }
    public AccessUserModel(String login, String password, UserRole role){
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public AccessUserModel(String login, String password, UserRole role, String name, String zipCode, String address){
        this.login = login;
        this.password = password;
        this.role = role;
        this.name = name;
        this.zipCode = zipCode;
        this.addres = address;
    }

    public UUID getIdAccess() {
        return idAccess;
    }

    public void setIdAccess(UUID idAccess) {
        this.idAccess = idAccess;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public UUID getIdMyAvatar() {
        return idMyAvatar;
    }

    public void setIdMyAvatar(UUID idMyAvatar) {
        this.idMyAvatar = idMyAvatar;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    //SpringSecurity

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.role == UserRole.ADMIN) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        else return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    public String getPassword() {
        return password;
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

    public void setPassword(String password) {
        this.password = password;
    }

}
