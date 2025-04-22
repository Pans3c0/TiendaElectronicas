package org.educastur.samuelepv59;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
class EntradaTest {
    private ArrayList<Pedido> pedidos;
    private HashMap<String,Articulo> articulos;
    private  HashMap<String, Cliente> clientes;
    private ArrayList<String> categoriasArt;

    public static void main(String[] args) {


    }

    @Test
    public void eArticulo() throws Excepciones.NoEncontrado {
        Tienda t = new Tienda();
        t.leerArchivos();

        NoEncontrado excepcion = assertThrows()
        assertTrue(Entrada.eArticulo("1-11",articulos),"Se ha encontrado");


    }

    @Test
    void eCliente() {
    }

    @Test
    void eDni() {
    }
}