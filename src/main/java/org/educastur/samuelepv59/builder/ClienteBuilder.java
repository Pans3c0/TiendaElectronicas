package org.educastur.samuelepv59.builder;

import org.educastur.samuelepv59.Cliente;

public class ClienteBuilder {
    Cliente cliente;
    private ClienteBuilder(){}
    public ClienteBuilder (String dni){
        this.cliente = new Cliente(dni);
        this.cliente.dni = dni;
    }

    public ClienteBuilder setNombre(String nombre){
        this.cliente.nombre = nombre;
        return this;
    }

    public ClienteBuilder setTelefono (String telefono){
        this.cliente.telefono = telefono;
        return this;
    }

    public ClienteBuilder setEmail (String email){
        this.cliente.email = email;
        return this;
    }

    public Cliente build(){
        return this.cliente;
    }
}
