/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package org.educastur.samuelepv59;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author alu19d
 */
public class UtilidadesTest {
    
    public UtilidadesTest() {
    }

    @org.junit.jupiter.api.BeforeAll
    public static void setUpClass() throws Exception {
    }

    @org.junit.jupiter.api.AfterAll
    public static void tearDownClass() throws Exception {
    }

    @org.junit.jupiter.api.BeforeEach
    public void setUp() throws Exception {
    }

    @org.junit.jupiter.api.AfterEach
    public void tearDown() throws Exception {
    }

    /**
     * Test of esInt method, of class Utilidades.
     */
    @org.junit.jupiter.api.Test
    public void testEsInt() {
        System.out.println("Test para el método es Int");
        assertTrue(Utilidades.esInt("-5"),"El -5 es int");
        assertTrue(Utilidades.esInt("5.5"),"El 5 es int");
        assertFalse(Utilidades.esInt("5.5"),"El 5.5 es int");
        assertFalse(Utilidades.esInt("xty"),"El xty no es int");
    }

    /**
     * Test of esDouble method, of class Utilidades.
     */
    @org.junit.jupiter.api.Test
    public void testEsDouble() {
        System.out.println("Test para el método es Double");
    }

    /**
     * Test of validarDNI method, of class Utilidades.
     */
    @org.junit.jupiter.api.Test
    public void testValidarDNI() {
        System.out.println("Validar DNI");
    }

    /**
     * Test of calcularLetraDNI method, of class Utilidades.
     */
    @org.junit.jupiter.api.Test
    public void testCalcularLetraDNI() {
        System.out.println("Validar letra");
    }
    
}
