package org.educastur.samuelepv59;

import java.io.Serializable;

public class Cliente implements Serializable {
    public String dni;
    public String nombre;
    public String telefono;
    public String email;

    public Cliente(String dni)  {
        this.dni = dni;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return nombre + '\'' + telefono + '\'' + email + '\'';
    }
}
