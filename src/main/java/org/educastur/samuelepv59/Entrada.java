package org.educastur.samuelepv59;

import java.util.HashMap;
import java.util.Scanner;

public class Entrada {
    static Tienda t = new Tienda();

    public static Articulo articulo(HashMap<String, Articulo>as) {
        Scanner sc = new Scanner(System.in);
        String idArt = idArt();
        if (idArt == null) return null;
        String s = "";
        Articulo a = null;
        do{
        try {
            eArticulo(idArt, as);
            a = as.get(idArt);
            System.out.println("El Articulo: "+a.getDescripcion()+" tiene "+a.getExistencias());
            s = "finalizar";
        } catch (Excepciones.NoEncontrado e) {
            System.out.println("Error:\n"+e.getMessage());
            System.out.println("Pulsa Enter para volver a intentarlo | Escribe algo para finalizar.\n");
            s = sc.nextLine();
        }
        }while(s.isBlank());
        return a;
    }
    public static void eArticulo(String idArt, HashMap<String, Articulo>as) throws Excepciones.NoEncontrado {
        if (!as.containsKey(idArt)){
            throw new Excepciones.NoEncontrado("❌El articulo "+idArt+ " no se encuentra en la Base De Datos...");
        }
    }

    public static String idArt() {
        Scanner sc = new Scanner (System.in);
        System.out.println("Introduce el ID del Articulo:");
        String idArt = sc.nextLine();
        try {
            eidArt(idArt);
        } catch (Excepciones.Blanco e) {
            System.out.println("Error: "+e.getMessage()+"\n");
            return null;
        }
        return idArt;
    }
    public static void eidArt(String idArt) throws Excepciones.Blanco {
        if (idArt.isBlank()){
            throw new Excepciones.Blanco("❌El articulo introducido esta en blanco");
        }
    }


    public static Cliente cliente(HashMap<String, Cliente> cs){
        Cliente c = null;
        String dni = dni();
        if (dni == null) return null;
        try{
            eCliente(dni,cs);
            return cs.get(dni);
        } catch (Excepciones.NoEncontrado e) {
            System.out.println("Error: "+e.getMessage()+"\n");
            return null;
        }
    }
    public static void eCliente(String dni, HashMap<String, Cliente> cs) throws Excepciones.NoEncontrado {
        if (!cs.containsKey(dni)){
            throw new Excepciones.NoEncontrado("❌El usuario"+dni+ "no se encuentra en la Base De Datos...");
        }
    }
    public static String email(){
        Scanner sc = new Scanner (System.in);
        System.out.println("Introduce el Email:");
        String email = sc.nextLine();
        try {
            eEmail(email);
        } catch (Excepciones.Blanco e) {
            System.out.println("Error:\n");
            return null;
        }
        return email;
    }
    public static void eEmail (String email) throws Excepciones.Blanco {
        if(email.isBlank()){
            throw new Excepciones.Blanco("❌El Email Introducido esta en blanco");
        }
    }
    public static String nombre(){
        Scanner sc = new Scanner (System.in);
        System.out.println("Introduce el Nombre:");
        String nombre = sc.nextLine();
        return nombre;
    }

    public static String telefono(){
        Scanner sc = new Scanner (System.in);
        System.out.println("Introduce el Teléfono:");
        String telefono = sc.nextLine();
        try {
            eNumero(telefono);
        } catch (Excepciones.Blanco e) {
            System.out.println("❌El Telefono esta en Blanco.");
            return null;
        } catch (Excepciones.NoInt e) {
            System.out.println("❌El Telefono no es válido.");
            return null;
        }
        return telefono;
    }
    public static void eNumero (String n) throws Excepciones.NoInt, Excepciones.Blanco {
        if(n.isBlank()){
            throw new Excepciones.Blanco("❌El numero introducido esta en blanco");
        }
        else if (Utilidades.esInt(n)==false){
            throw new Excepciones.NoInt("❌El numero introducido no es válido.");
        }
    }

    public static String dni () {
        Scanner sc = new Scanner(System.in);
        System.out.println("Introduce el DNI:");
        String dni = sc.nextLine();
        try {
            eDni(dni);
            return dni;
        } catch (Excepciones.Blanco | Excepciones.DniNoValido e) {
            System.out.println("Error: "+e.getMessage()+"\n");
            return null;
        }

    }
    public static void eDni (String dni) throws Excepciones.DniNoValido, Excepciones.Blanco {
        if(dni.isBlank()){
            throw new Excepciones.Blanco("❌El Dni Introducido esta en blanco");
        }
        else if (Utilidades.validarDNI(dni)==false){
            throw new Excepciones.DniNoValido("❌El Dni Introducido no es válido.");
        }
    }
    public static void stockPedidos(int udPedidos, Articulo a) throws Excepciones.StockAgotado, Excepciones.StockInsuficiente {
        if (a.getExistencias()== 0){
            throw new Excepciones.StockAgotado("❌ El Stock se encuentra agotado, intente con otro produto.");
        }
        else if (a.getExistencias() < udPedidos){
            throw new Excepciones.StockInsuficiente("❌ El Stock no cubre con la cantidad deseada.\nSolo quedan "+a.getExistencias()+" Unidades.");

        }
    }
    public static LineaPedido lineaPedido(Articulo a) {
        Scanner sc = new Scanner(System.in);
        boolean st = false;
        String ud;
        do {
            System.out.println("Introduce la cantidad:");
            ud = sc.nextLine();
            try {
                eNumero(ud);
                st = true;
            } catch (Excepciones.Blanco | Excepciones.NoInt e) {
                System.out.println("Error: "+e.getMessage());
                System.out.println("Pulsa Enter para reintentarlo | Escribe algo para finalizar.");
                String opc = sc.nextLine();
                if (!opc.isBlank()) {
                    return null;
                }

            }
        }while (!st) ;

        try {
            Entrada.stockPedidos(Integer.parseInt(ud), a);
            return new LineaPedido(a.getIdArticulo(), Integer.parseInt(ud));
        } catch (Excepciones.StockAgotado e) {
            System.out.println("Error: ");
            return null;
        } catch (Excepciones.StockInsuficiente e) {
            System.out.println("Error: \nQuieres las disponibles?");
            String opc = sc.nextLine();
            if (opc.equalsIgnoreCase("S")) return new LineaPedido(a.getIdArticulo(), a.getExistencias());
            return null;
        }

    }
}
