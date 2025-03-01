package org.educastur.samuelepv59;


import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Pedido implements Comparable<Pedido>, Serializable {
    private String idPedido;
    private Cliente clientePedido;
    private LocalDate fechaPedido;
    private ArrayList<LineaPedido> cestaCompra;

    public Pedido( Cliente clientePedido, LocalDate fechaPedido, ArrayList<LineaPedido> cestaCompra, ArrayList<Pedido> pedidos) {
        this.clientePedido = clientePedido;
        this.fechaPedido = fechaPedido;
        this.cestaCompra = cestaCompra;
        this.idPedido = generaId(pedidos);
    }

    public String getIdPedido() {
        return idPedido;
    }

    @Override
    public String toString() {
        return "\n Pedidos ID: " + idPedido +
                "\n Cliente:" + clientePedido.getNombre() +
                "\n Fecha: " + fechaPedido +
                "\n Cesta: \n" + cestaCompra.stream()
                .map(LineaPedido :: toString)
                .collect(Collectors.joining("\n - "," - "," "));
    }

    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }

    public String generaId(ArrayList<Pedido>pedidos) {
        int count = 0;
        for (Pedido p : pedidos) {
            if (p.getClientePedido().getDni().equals(this.clientePedido.getDni())) {
                count++;
            }
        }
        count++; //Añadimos la compra actual que se va a hacer
        return this.clientePedido.getDni() + "-" + String.format("%03d", count) + "/" + LocalDate.now().getYear();
    }

    public Cliente getClientePedido() {
        return clientePedido;
    }

    public void setClientePedido(Cliente clientePedido) {
        this.clientePedido = clientePedido;
    }

    public LocalDate getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(LocalDate fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public ArrayList<LineaPedido> getCestaCompra() {
        return cestaCompra;
    }

    public void setCestaCompra(ArrayList<LineaPedido> cestaCompra) {
        this.cestaCompra = cestaCompra;
    }


    @Override
    public int compareTo(Pedido p) {
         //  return this.idPedido.compareTo(p.getIdPedido());
        return this.fechaPedido.compareTo(p.getFechaPedido());
    }
}
