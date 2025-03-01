package org.educastur.samuelepv59;

public class Excepciones {
    public static class Registrado extends Exception{
        public Registrado(String s){
            super(s);
        }
    }
    public static class NoEncontrado extends Exception{
        public NoEncontrado(String s){
            super(s);
        }
    }
    public static class StockAgotado extends Exception{
        public StockAgotado(String cadena){
            super(cadena); //Llama al constructor de Exception y le pasa el contenido de cadena
        }
    }

    public static class ArticuloRegistrado extends Exception{
        public ArticuloRegistrado(String s){
            super(s);
        }
    }

    public static class NoInt extends Exception{
        public NoInt(String s){
            super(s);
        }
    }
    public static class StockInsuficiente extends Exception{
        public StockInsuficiente(String s){
            super(s);
        }
    }
    public static class DniNoValido extends Exception{
        public DniNoValido(String s){
            super(s);
        }
    }
    public static class Blanco extends Exception{
        public Blanco(String s){
            super(s);
        }
    }

}
