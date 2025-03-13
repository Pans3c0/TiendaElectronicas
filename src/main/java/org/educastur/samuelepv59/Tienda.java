package org.educastur.samuelepv59;

import javax.swing.text.html.parser.Parser;
import java.io.*;
import java.sql.SQLOutput;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


class Tienda {
    private ArrayList<Pedido> pedidos;
    private HashMap<String,Articulo> articulos;
    private  HashMap<String, Cliente> clientes;
    private ArrayList<String> categoriasArt;


    public HashMap<String, Articulo> getArticulos() {
        return articulos;
    }
    public HashMap<String, Cliente> getCliente() {
        return clientes;
    }
    public Tienda(){
        pedidos = new ArrayList<>();
        articulos = new HashMap<>();
        clientes = new HashMap<>();
        categoriasArt = new ArrayList<>();

    }
    public static void main(String[] args) {
        Tienda t = new Tienda();
        t.leerArchivos();
        //t.cargaDatos();
        t.cargaCategorias();
        t.imprimePedidos();
        t.menu();
        t.backup();

        /**
        t.clientesTxtBackupMas1000();
        t.clientesTxtBackup();
        t.clientesTxtBackupSin();
        t.clientesTxtBackupCon();
        t.clientesTxtLeerCon();
        t.clientesTxtLeerSin();
        t.clientesTxtLeerMas1000();
        */

    }

    //region marzoEjercicios
    public int artEnPedido(String idArt, Pedido p){
        return p.getCestaCompra().stream().filter(lP -> lP.getIdArticulo().equals(idArt))
                .mapToInt(LineaPedido::getUnidades).sum();
    }

    private void artClientes(){
        Articulo articuloEncontrado = Entrada.articulo(articulos);
        if (articuloEncontrado == null) return;
        pedidos.stream()
                .sorted(
                        Comparator.comparingInt(p->
                                        artEnPedido(articuloEncontrado.getIdArticulo(),(Pedido) p))
                                .reversed())
                .forEach(p->
                        System.out.println("Pedido" + p.toString()+" con " + artEnPedido(articuloEncontrado.getIdArticulo(),(Pedido) p)+" unidades.\n"));
    }
    public void mostrarUsuariosPorArticulo() {
        String idArticulo = Entrada.articulo(articulos).getIdArticulo();
        if (idArticulo == null) return;
        // Filtrar los pedidos donde el artículo aparece en la cesta de compra
        Map<Cliente, Integer> usuariosConUnidades = pedidos.stream()
                .flatMap(pedido -> pedido.getCestaCompra().stream()
                        .filter(lp -> lp.getIdArticulo().equals(idArticulo))
                        .map(lp -> new AbstractMap.SimpleEntry<>(pedido.getClientePedido(), lp.getUnidades()))
                )
                .collect(Collectors.toMap(
                        Map.Entry::getKey, // Usuario
                        Map.Entry::getValue, // Unidades compradas
                        Integer::sum // Sumar si el usuario ya ha comprado más unidades
                ));

        // Mostrar los resultados
        if (usuariosConUnidades.isEmpty()) {
            System.out.println("Ningún usuario ha comprado este artículo.");
        } else {
            System.out.println("Usuarios que compraron el artículo " + idArticulo + ":");
            usuariosConUnidades.forEach((usuario, unidades) ->
                    System.out.println(usuario.getNombre() + " ha comprado " + unidades + " unidades."));
        }
    }

    // El ejercicio 2 esta en la region de examenes

    //Ejercicio 3
    public void listaSecciones2(){
        Scanner sc = new Scanner(System.in);
        listadoSecciones("SECCIONES DE ARTICULOS",categoriasArt);
        System.out.println("Introduce la opción: ");
        String seccionString;
        System.out.println();
        do {
            seccionString = sc.nextLine();
        }while (!Utilidades.esInt(seccionString));
        if (Integer.parseInt(seccionString)>6 | Integer.parseInt(seccionString)<1){
            System.out.println("La categoria introducida no existe.");
            return;
        }
        int seccion = Integer.parseInt(seccionString);
        if (seccion == 6){
            imprimeArticulo();
            return;
        }
        System.out.println("Articulos de la categoria "+categoriasArt.get(seccion)+" son:");
        articulos.values().stream()
                .filter(a -> a.getSeccion() == seccion)
                .forEach(a -> System.out.println('\n'+a.getIdArticulo()+" | "+a));

    }

//endregion

    // region examen
    public void menuExamen() {
        Scanner sc=new Scanner(System.in);
        String opcion;
        do{
            System.out.println("\n\n\n\n\n\t\t\t\tMENU EXAMEN\n");
            System.out.println("\t\t\t\t1 - LISTADO ARTICULOS POR SECCION");
            System.out.println("\t\t\t\t2 - TOTAL PEDIDO");
            System.out.println("\t\t\t\t3 - LISTADO CLIENTES POR GASTO");
            System.out.println("\t\t\t\t4 - LISTADO ARTICULOS POR STOCK");
            System.out.println("\t\t\t\t5 - SALIR");
            opcion=sc.next();
        } while (!Utilidades.esInt(opcion) || !opcion.matches("[1-4]"));
        switch (Integer.parseInt(opcion)){
            case 1:{
                listaSecciones();
                break;
            }
            case 2:{
                imprimeArticulos();
                break;
            }
            case 3:{
                listadoClientesGasto();
                break;
            }
            case 4:{
                listadoArticulosStock();
                break;
            }
        }
    }






    private void total() {
        Scanner sc=new Scanner(System.in);
        System.out.println("Ingrese el ID del pedido.");
        pedidos.stream()
                .forEach(p-> System.out.println('\n'+p.getIdPedido())); //paso la lista de ids que se genera automaticamente y no se encuentra en el carga datos (63921307Y-001/2025)
        String idString = sc.nextLine();
        pedidos.stream()
                .filter(p->p.getIdPedido()
                        .equals(idString))
                .forEach(p->System.out.println("\n"+p + "\n Importe Total del pedido "+p.getIdPedido()+" es :"+totalPedido(p)));
    }

    private void listadoClientesGasto() {
        System.out.println("Clientes ordenados de mayor a menor basado en todos sus gastos:\n");
        clientes.values().stream().sorted(Comparator.comparing(this::totalPedidos).reversed())
                .forEach(c-> System.out.println(
                        '\n'+c.getDni()+" : "+c + "\nImporte Total:\n"+totalPedidos(c)+"\n"
                ));
    }
    private void listadoArticulosStock() {
        Scanner sc=new Scanner(System.in);
        System.out.println("Indica el limite de unidades:");
        String uds;
        do{
            uds = sc.nextLine();
        }while(!Utilidades.esInt(uds));
        int ud = Integer.parseInt(uds);
        System.out.println("Los siguientes articulos tienen menos de "+ud);
        articulos.values().stream()
                .filter(a->a.getExistencias()<ud)
                .forEach(System.out::println);
    }

    // endregion

// region menu


    public void menu(){
        Scanner sc=new Scanner(System.in);
        int opcion;
        do{
            System.out.println("\n\t\t\t\t *TIENDA 2025*");
            System.out.println("\t\t\t\t1 - Crear Usuario");
            System.out.println("\t\t\t\t2 - Crear Articulo");
            System.out.println("\t\t\t\t3 - Gestion Prestamos/Devoluciones");
            System.out.println("\t\t\t\t4 - Prueba Examen");
            System.out.println("\t\t\t\t5 - Persistencias");
            System.out.println("\t\t\t\t6 - Tester");
            System.out.println("\n\n\t\t\t\t9 - SALIR\n\n");
            opcion=sc.nextInt();
            switch (opcion){
                case 0:
                    System.out.println("Saliendo del programa...");
                    break;
                case 1:{
                    backupSeccion();
                    break;
                }
                case 2:{
                    imprimePedidos();
                    break;
                }
                case 3:{
                    imprimeArticulo();
                    break;
                }
                case 4:{
                    nuevoPedido();
                    break;
                }
                case 5:{
                    menuPersistencias();
                    break;
                }
                case 6:{
                    listaSecciones2();
                    break;
                }
            }

        }while (opcion !=9);
    }

    private void menuPersistencias() {
        Scanner sc=new Scanner(System.in);
        int opcion;
        do{
            System.out.println("\n\t\t\t\t *PERSISTENCIAS*");
            System.out.println("\t\t\t\t1 - seccion articulos");
            System.out.println("\t\t\t\t2 - Crear Articulo");
            System.out.println("\t\t\t\t3 - Gestion Prestamos/Devoluciones");
            System.out.println("\t\t\t\t4 - Prueba Examen");
            System.out.println("\t\t\t\t5 - Persistencias");
            System.out.println("\n\n\t\t\t\t9 - SALIR\n\n");
            opcion=sc.nextInt();
            switch (opcion){
                case 0:
                    System.out.println("Saliendo del programa...");
                    break;
                case 1:{
                    cargaArticulos();
                    break;
                }
                case 2:{
                    nuevoArticulo();
                    break;
                }
                case 3:{
                    imprimeArticulos();
                    break;
                }
                case 4:{
                    nuevoPedido();
                    break;
                }
                case 5:{
                    menuPersistencias();
                    break;
                }
            }

        }while (opcion !=9);
    }

    private void cargaArticulos() {
        Scanner sc = new Scanner(System.in);
        ArrayList<Articulo>articulosAuxs = new ArrayList<>();
        Articulo a = null;
        listadoSecciones("SECCIONES DE ARTICULOS",categoriasArt);
        System.out.println("Introduce la opción: ");
        String seccionString;
        System.out.println();
        do {
            seccionString = sc.nextLine();
        }while (!Utilidades.esInt(seccionString));

        try (ObjectInputStream oisArticulosSeccion = new ObjectInputStream(new FileInputStream("articulos.dat"))){
            while ( (a=(Articulo)oisArticulosSeccion.readObject())!=null){
                if (a.getIdArticulo().startsWith(seccionString)){
                    articulosAuxs.add(a);
                }
            }
            System.out.println("Exito");
        } catch (FileNotFoundException e){
            System.out.println(e.toString());
        }
        catch (EOFException e) {
        } catch (ClassNotFoundException | IOException e) {
            System.out.println(e.toString());
        }
        articulosAuxs.forEach(System.out::println);
    }
    // endregion

// region imprime
    private void imprimeClientes() {
        for (Map.Entry<String,Cliente> c : clientes.entrySet()){
            System.out.println("Dni:"+c.toString());
        }
    }

    /**
     * Nos imprime un menu con unas categorias
     * @param headMenu Introducimos el texto que encabeza el menu
     * @param bodyMenu Introducimas lista de categoria o palabra predefinida
     */
    public void listadoSecciones(String headMenu, ArrayList<String>bodyMenu){
        System.out.println("\n\t\t\t\t* "+headMenu);
        for (int i = 0; i<bodyMenu.size();i++){
            System.out.println("\t\t\t\t"+(i+1)+" : "+bodyMenu.get(i).toUpperCase(Locale.ROOT));
        }
        System.out.println("\n\n\t\t\t\t9 - SALIR\n\n");
    }

    /**
     * Este metodo nos imprime menu automatizado
     * @param headMenu Introducimos el texto que encabeza el menu
     * @param bodyMenuPre Introducimos los prefijos para dar sentido a la opcion
     * @param bodyMenuCat Introducimas lista de categoria o palabra predefinida
     */
    public void listadoSecciones(String headMenu, ArrayList<String>bodyMenuPre, ArrayList<String>bodyMenuCat){
        System.out.println("\n\t\t\t\t* "+headMenu);
        for (int i = 0; i<bodyMenuCat.size();i++){
            System.out.println("\t\t\t\t"+(i+1)+" : "+bodyMenuCat.get(i).toUpperCase(Locale.ROOT));
        }
        System.out.println("\n\n\t\t\t\t9 - SALIR\n\n");
    }

    public void listaSecciones(){
        Scanner sc = new Scanner(System.in);
        listadoSecciones("SECCIONES DE ARTICULOS",categoriasArt);
        System.out.println("Introduce la opción: ");
        String seccionString;
        System.out.println();
        do {
            seccionString = sc.nextLine();
        }while (!Utilidades.esInt(seccionString));
        if (Integer.parseInt(seccionString)>5 | Integer.parseInt(seccionString)<1){
            System.out.println("La categoria introducida no existe.");
            return;
        }
        int seccion = Integer.parseInt(seccionString);
        System.out.println("Articulos de la categoria "+categoriasArt.get(seccion)+" son:");
        articulos.values().stream()
                .filter(a -> a.getSeccion() == seccion)
                .forEach(a -> System.out.println('\n'+a.getIdArticulo()+" | "+a));

    }
    private void imprimeArticulo(){
        for (Articulo a : articulos.values()){
            System.out.println("\nID: "+a.getIdArticulo()+"\n"+a);
        }
    }
    private void imprimeArticulos() {
        ArrayList<Articulo>articulosAux = new ArrayList<>(articulos.values()); // Convertimos el hash en un arraylist para poder usar comparadores
        Collections.sort(articulosAux); // invocamos al comparador que hemos creado previamente en la clase Articulo implementando comparable()
        for (Articulo a : articulosAux){ // imprimimos la lista despues de haberle aplicado el ordenador.
            System.out.println("Clave: "+a.toString());
        }
        System.out.println();
        Collections.sort(articulosAux,new ComparaArtPorExistencias()); // repetimos el proceso usando en el sort la lista y el comparador que hemos creado en otra clase.
        for (Articulo a : articulosAux){
            System.out.println("Clave: "+a.toString());
        }
        System.out.println();
        Collections.sort(articulosAux,new ComparaArtPorPrecio());
        for (Articulo a : articulosAux){
            System.out.println("Clave: "+a.toString());
        }
        System.out.println();
        articulos.values().stream()
                .sorted(new ComparaArtPorExistencias())
                .forEach(System.out::println);
    }
    private void imprimePedidos() {
        pedidos.stream().sorted(Comparator.comparing(this::totalPedido))
                .forEach(p-> System.out.println(
                        p + "\n Importe Total:\n "+totalPedido(p)+"\n"
                ));
        System.out.println("-----");
        pedidos.stream().sorted(Comparator.comparing(this::totalPedido).reversed())
                .filter(p->p.getClientePedido().getNombre().equals("ANA "))
                .forEach(p-> System.out.println(
                        p + "\n Importe Total:\n "+totalPedido(p)+"\n"
                ));
    }
    private void imprimePedidosPorFecha(){
        pedidos.stream().sorted().forEach(p -> System.out.println(p));
    }
    private void nuevoArticulo() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Nuevo articulo:");
        System.out.println("Id:");
        String id=sc.next();
        System.out.println("Descripcion:");
        String descripcion = sc.nextLine();
        System.out.println("Existencias:");
        String exs;
        do {
                exs = sc.nextLine();
        }while(!Utilidades.esInt(exs));
        int existencias = Integer.parseInt(exs);
        System.out.println("Pvp:");
        double pvp = sc.nextDouble();
        try {
            Articulo articulo = new Articulo(id, descripcion, existencias, pvp);
            crearArticulo(articulo);
            System.out.println("✅ Articulo Registrado Con Exito");
        } catch (Excepciones.ArticuloRegistrado e) {
            System.out.println("Error al registrar\n"+e.getMessage());
        }
    }
    // endregion

// region articulos
    public void crearArticulo (Articulo a) throws Excepciones.ArticuloRegistrado{
        if (articulos.containsKey(a.getIdArticulo())){
            throw new Excepciones.ArticuloRegistrado("❌El org.educastur.samuelepv59.Articulo:\n"+ articulos.get(a.getIdArticulo()).toString() +"\nya esta registrado en nuestra base de datos...\n y esta tratando de registrar\n "+a.toString());
        }
        articulos.put(a.getIdArticulo(),a);
        System.out.println("✅ El Articulo se ha registrado correctamente.");

    }
    public void reponerArticulos(){
        Scanner sc = new Scanner(System.in);
        for (Articulo a : articulos.values()){
            if (a.getExistencias()==0){
                System.out.println(a.getIdArticulo()+a);
            }
        }
        Articulo aEncontrado = Entrada.articulo(articulos);
        String udString;
        do{
            System.out.println("Introduce la cantidad que deseas añadir");
            udString = sc.nextLine();
        } while (!Utilidades.esInt(udString));
        System.out.println("El articulo "+aEncontrado.getDescripcion()+" tendrá ahora "+(aEncontrado.getExistencias()+Integer.parseInt(udString))+"\nPulsa Enter para continuar | Escribe algo para cancelarlo.");
        String st = sc.nextLine();
        if (!st.isBlank()){
            System.out.println("El articulo no se ha podido registrar");
            return;

        }
        articulos.get(aEncontrado.getIdArticulo()).setExistencias(aEncontrado.getExistencias()+Integer.parseInt(udString));
        System.out.println("Articulo modificado con exito\n"+aEncontrado);

    }

// endregion

// region persistencias
public void backup() {
    try (ObjectOutputStream oosArticulos = new ObjectOutputStream(new FileOutputStream("articulos.dat"));
         ObjectOutputStream oosClientes = new ObjectOutputStream(new FileOutputStream("clientes.dat"));
         ObjectOutputStream oosPedidos = new ObjectOutputStream (new FileOutputStream("pedidos.dat"))) {

        //LOS PEDIDOS SE GUARDAN OBJETO A OBJETO
        for (Articulo a:articulos.values()){
            oosArticulos.writeObject(a);
        }

        for (Cliente c: clientes.values()){
            oosClientes.writeObject(c);
        }

        for (Pedido p:pedidos){
            oosPedidos.writeObject(p);
        }

        System.out.println("Copia de seguridad realizada con éxito.");

    } catch (FileNotFoundException e) {
        System.out.println(e.toString());
    } catch (IOException e) {
        System.out.println(e.toString());
    }
}

    public void leerArchivos() {
        try (ObjectInputStream oisArticulos = new ObjectInputStream(new FileInputStream("articulos.dat"))){
            Articulo a;
            while ( (a=(Articulo)oisArticulos.readObject()) != null){
                articulos.put(a.getIdArticulo(), a);
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.toString());
        } catch (EOFException e){

        } catch (ClassNotFoundException | IOException e) {
            System.out.println(e.toString());
        }

        try (ObjectInputStream oisClientes = new ObjectInputStream(new FileInputStream("clientes.dat"))){
            Cliente c;
            while ( (c=(Cliente)oisClientes.readObject()) != null){
                clientes.put(c.getDni(), c); // parte variable
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.toString());
        } catch (EOFException e){

        } catch (ClassNotFoundException | IOException e) {
            System.out.println(e.toString());
        }


        try (ObjectInputStream oisPedidos = new ObjectInputStream(new FileInputStream("pedidos.dat"))){
            Pedido p;
            while ( (p=(Pedido)oisPedidos.readObject()) != null){
                pedidos.add(p);
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.toString());
        } catch (EOFException e){

        } catch (ClassNotFoundException | IOException e) {
            System.out.println(e.toString());
        }

    }

public void backupSeccion(){
    try(ObjectOutputStream oisImpresora=new ObjectOutputStream(new FileOutputStream("impresoras.dat"));
        ObjectOutputStream oisMonitores=new ObjectOutputStream(new FileOutputStream("monitores.dat"));
        ObjectOutputStream oisAlmacenamiento=new ObjectOutputStream(new FileOutputStream("almacenamiento.dat"));
        ObjectOutputStream oisPerifericos=new ObjectOutputStream(new FileOutputStream("perifericos.dat"));
        ObjectOutputStream oisComponentes=new ObjectOutputStream(new FileOutputStream("componentes.dat"))){
        for (Articulo a : articulos.values()){
            if (a.getSeccion()==3){
                oisImpresora.writeObject(a);
            }
        }
        for (Articulo a : articulos.values()){
            if (a.getSeccion()==1){
                oisPerifericos.writeObject(a);
            }
        }
        for (Articulo a : articulos.values()){
            if (a.getSeccion()==2){
                oisAlmacenamiento.writeObject(a);
            }
        }
        for (Articulo a : articulos.values()){
            if (a.getSeccion()==4){
                oisMonitores.writeObject(a);
            }
        }
        for (Articulo a : articulos.values()){
            if (a.getSeccion()==5){
                oisComponentes.writeObject(a);
            }
        }
        System.out.println("Copia de las seccinoes realizada con exito.");
    } catch (FileNotFoundException e) {
        throw new RuntimeException(e);
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}
    public void leerArchivosSeccion(String seccion){
        ArrayList<Articulo>articulosAuxs = new ArrayList<>();
        Articulo a = null;
        try (ObjectInputStream oisArticulosSeccion = new ObjectInputStream(new FileInputStream("articulos.dat"))){
            while ( (a=(Articulo)oisArticulosSeccion.readObject())!=null){
                if (a.getIdArticulo().startsWith(seccion)){
                    articulosAuxs.add(a);
                }
            }
            System.out.println("Exito");
        } catch (FileNotFoundException e){
            System.out.println(e.toString());
        }
        catch (EOFException e) {
        } catch (ClassNotFoundException | IOException e) {
            System.out.println(e.toString());
        }
        articulosAuxs.forEach(System.out::println);
    }


    public void clientesTxtBackup() {
        try(BufferedWriter bfwClientes=new BufferedWriter(new FileWriter("clientes.csv"))){
            for (Cliente c : clientes.values()) {
                bfwClientes.write(c.getDni() + "," + c.getNombre() + "," + c.getTelefono() + "," + c.getEmail() + "\n");
            }
        }catch (FileNotFoundException e) {
            System.out.println(e.toString());
        }catch(IOException e){
            System.out.println(e.toString());
        }
    }
    public void clientesTxtBackupCon() {
        try(BufferedWriter bfwClientes1000=new BufferedWriter(new FileWriter("clientesCon.csv"))) {
            clientes.values().stream().filter(c->totalPedidos((Cliente) c)>0).forEach(c-> {
                try {
                    bfwClientes1000.write(c.getDni() + "," + c.getNombre() + "," + c.getTelefono() + "," + c.getEmail() + "\n");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            System.out.println("Guardando clientes con mayor gasto de 1000\n");
        }catch (FileNotFoundException e) {
            System.out.println(e.toString());
        }catch(IOException e){
            System.out.println(e.toString());
        }
    }
    public void clientesTxtBackupSin() {
        try(BufferedWriter bfwClientes1000=new BufferedWriter(new FileWriter("clientesSin.csv"))) {
            clientes.values().stream().filter(c->totalPedidos((Cliente) c)<1)
                    .forEach(c-> {
                try {
                    bfwClientes1000.write(c.getDni() + "," + c.getNombre() + "," + c.getTelefono() + "," + c.getEmail() + "\n");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            System.out.println("Guardando clientes con 0 gasto\n");
        }catch (FileNotFoundException e) {
            System.out.println(e.toString());
        }catch(IOException e){
            System.out.println(e.toString());
        }
    }
    public void clientesTxtBackupMas1000() {
        try(BufferedWriter bfwClientes1000=new BufferedWriter(new FileWriter("clientesConMasDe1000.csv"))) {
            clientes.values().stream().filter(c->totalPedidos((Cliente) c)>1000).forEach(c-> {
                try {
                    bfwClientes1000.write(c.getDni() + "," + c.getNombre() + "," + c.getTelefono() + "," + c.getEmail() + "\n");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            System.out.println("Guardando clientes con mayor gasto de 1000\n");
        }catch (FileNotFoundException e) {
            System.out.println(e.toString());
        }catch(IOException e){
            System.out.println(e.toString());
        }
    }
    public void clientesTxtLeerCon() {
        ArrayList<Cliente> clientesAux = new ArrayList<>();
        try(Scanner scClientes=new Scanner(new File("clientesCon.csv"))){
            while (scClientes.hasNextLine()){
                String [] atributos = scClientes.nextLine().split("[,]");
                Cliente c=new Cliente(atributos[0],atributos[1],atributos[2],atributos[3]);
                clientesAux.add(c);
            }
            clientesAux.stream().forEach(System.out :: println);
        }catch(IOException e){
            System.out.println(e.toString());
        }


    }
    public void clientesTxtLeerSin() {
        ArrayList<Cliente> clientesAux = new ArrayList<>();
        try(Scanner scClientes=new Scanner(new File("clientesSin.csv"))){
            while (scClientes.hasNextLine()){
                String [] atributos = scClientes.nextLine().split("[,]");
                Cliente c=new Cliente(atributos[0],atributos[1],atributos[2],atributos[3]);
                clientesAux.add(c);
            }
            clientesAux.stream().forEach(System.out :: println);
        }catch(IOException e){
            System.out.println(e.toString());
        }
    }
    public void clientesTxtLeerMas1000() {
        ArrayList<Cliente> clientesAux = new ArrayList<>();
        try(Scanner scClientes=new Scanner(new File("clientesConMasDe1000.csv"))){
            while (scClientes.hasNextLine()){
                String [] atributos = scClientes.nextLine().split("[,]");
                Cliente c=new Cliente(atributos[0],atributos[1],atributos[2],atributos[3]);
                clientesAux.add(c);
            }
            clientesAux.stream().forEach(System.out :: println);
        }catch(IOException e){
            System.out.println(e.toString());
        }
    }


    public void clientesTxtLeer() {
        // LEEMOS LOS CLIENTES DESDE EL ARCHIVO .csv A UNA COLECCION HASHMAP AUXILIAR Y LA IMPRIMIMOS
        HashMap <String,Cliente> clientesAux = new HashMap();
        try(Scanner scClientes=new Scanner(new File("clientes.csv"))){
            while (scClientes.hasNextLine()){
                String [] atributos = scClientes.nextLine().split("[,]");
                Cliente c=new Cliente(atributos[0],atributos[1],atributos[2],atributos[3]);
                clientesAux.put(atributos[0], c);
            }
        }catch(IOException e){
            System.out.println(e.toString());
        }
        clientesAux.values().forEach(System.out::println);
    }

    /**
    public void persistirTienda() throws IOException {
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(new FileOutputStream("Tienda.dat"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        oos.writeObject(Tienda);
    }

    */
// endregion


//region pedidos
    public double totalPedido (Pedido p){
        double precio = 0;
        for (LineaPedido l:p.getCestaCompra()){
            precio+=(articulos.get(l.getIdArticulo()).getPvp()+l.getUnidades());
        }
        return precio;
    }
    public double totalPedidos (Cliente c){
        double precio = 0;
            for (Pedido p: pedidos){
                if (p.getClientePedido().equals(c)){
                    for (LineaPedido l : p.getCestaCompra()){
                        precio += l.getUnidades()*articulos.get(l.getIdArticulo()).getPvp();
                    }
                }
            }
        return precio;
    }
    public void nuevoPedido(){
        ArrayList<LineaPedido> cestaCompraAux = new ArrayList<>();
        Cliente c = Entrada.cliente(clientes);
        if (c == null) return;
        ArrayList<LineaPedido>cesta= cesta(c);
        if (cesta == null) return;
        pedidos.add(new Pedido(c,LocalDate.now(),cesta,pedidos));
        //revisar
        for (LineaPedido l :pedidos.getLast().getCestaCompra()){
            articulos.get(l.getIdArticulo()).setExistencias(articulos.get(l.getIdArticulo()).getExistencias()-l.getUnidades());
        }
        System.out.println("El pedido se ha grabado correctamente.");

    }
    private ArrayList<LineaPedido> cesta(Cliente c) {
        ArrayList<LineaPedido> cestaAux=new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        System.out.println("Introduzca los articulos uno a uno\n");
        String st = "";
        do{
        Articulo a = Entrada.articulo(articulos);
        if (a==null) {
            break;
        }

        LineaPedido linea = Entrada.lineaPedido(a);
        if (linea==null) {
            System.out.println("Pulsa Enter para continuar comprando | Escribe algo para finalizar.");
            st = sc.nextLine();
            if (!st.isBlank()) {
                break;
            } else{
                st = null;
                continue;
            }
        }
        if(!(linea == null)){
            cestaAux.add(linea);
            System.out.println("Pulsa Enter para continuar comprando | Escribe algo para finalizar.");
            st = sc.nextLine();
        }
        }while(st.isBlank());

        System.out.println("El pedido que vas a realizar es el siguiente:");
        for (LineaPedido l : cestaAux){
            System.out.println(l.getIdArticulo() + " "+ l.getUnidades()+"\n");
        }
        System.out.println("Pulsa Enter para continuar | Escribe algo para cancelarlo.");
        st = "";
        st = sc.nextLine();
        if (!st.isBlank()){
            System.out.println("Se va a cancelar la cesta actual...");
            return null;
        }
        return cestaAux;
    }

    // endregion

// region clientes


    public void nuevoUsuario(){
        System.out.println("Creacion De Usuario:");
        String dni = Entrada.dni();
        if (dni==null) return;
        String nombre = Entrada.nombre();
        if (nombre==null) return;
        String telefono = Entrada.telefono();
        if (telefono==null) return;
        String email = Entrada.email();
        if (email == null) return;

        try {
            Cliente cliente = new Cliente(dni, nombre, telefono, email);
            crearUsuario(cliente);
            System.out.println("✅ Cliente Registrado Con Exito");
        } catch (Excepciones.Registrado e) {
            System.out.println("Error al registrar\n"+e.getMessage());
        }
    }

    public void crearUsuario (Cliente c) throws Excepciones.Registrado {
        if (clientes.containsKey(c.getDni())){
            throw new Excepciones.Registrado("❌El usuario:\n"+ clientes.get(c.getDni()).toString() +"\nya esta registrado en nuestra base de datos...\n y esta tratando de registrar\n "+c.toString());
        }
        clientes.put(c.getDni(),c);
        System.out.println("✅ El usuario se ha registrado correctamente.");

    }


    public Cliente buscarCliente(String dni) throws Excepciones.NoEncontrado {
        if (!clientes.containsKey(dni)){
            throw new Excepciones.NoEncontrado("El usuario"+dni+ "no se encuentra en la Base De Datos...");
        }

        return clientes.get(dni);
    }
    // endregion

    public void cargaCategorias(){
        categoriasArt.add("periféricos");
        categoriasArt.add("almacenamiento");
        categoriasArt.add("impresoras");
        categoriasArt.add("monitores");
        categoriasArt.add("componentes");
        categoriasArt.add("todos");
    }
    public void cargaDatos(){



        clientes.put("80580845T",new Cliente("80580845T","ANA","658111111","ana@gmail.com"));
        clientes.put("36347775R",new Cliente("36347775R","LOLA","649222222","lola@gmail.com"));
        clientes.put("63921307Y",new Cliente("63921307Y","JUAN","652333333","juan@gmail.com"));
        clientes.put("02337565Y",new Cliente("02337565Y","EDU","634567890","edu@gmail.com"));
        clientes.put("14665825J",new Cliente("14665825J","REYSHEN","678170433","reyshen35@gmail.com"));
        clientes.put("65318802K",new Cliente("65318802K","ORCREMA","688870433","orcrema@gmail.com"));
        clientes.put("76648739C",new Cliente("76648739C","JOSEIRO","634999890","negritogay@gmail.com"));
        clientes.put("51395126Q",new Cliente("51395126Q","ROCES","633330433","autism@gmail.com"));
        clientes.put("88558514G",new Cliente("88558514G","MORO","611178833","españa@gmail.com"));
        clientes.put("47540602Q",new Cliente("47540602Q","PACHECO","686666710","samupachebq@gmail.com"));


        articulos.put("1-11",new Articulo("1-11","RATON LOGITECH ST ",14,15));
        articulos.put("1-22",new Articulo("1-22","TECLADO STANDARD  ",9,18));
        articulos.put("1-33", new Articulo("1-33", "AURICULARES STEELSERIES 9", 20, 50));
        articulos.put("1-44", new Articulo("1-44", "MICRÓFONO USB RAZER", 10, 35));
        articulos.put("2-11",new Articulo("2-11","HDD SEAGATE 1 TB  ",16,80));
        articulos.put("2-22",new Articulo("2-22","SSD KINGSTOM 256GB",9,70));
        articulos.put("2-33",new Articulo("2-33","SSD KINGSTOM 512GB",0,200));
        articulos.put("2-44", new Articulo("2-44", "NVME 1TB SAMSUNG", 8, 120));
        articulos.put("3-11", new Articulo("3-11", "HP OFFICEJET 3830", 6, 90));
        articulos.put("3-22",new Articulo("3-22","EPSON PRINT XP300 ",5,80));
        articulos.put("4-11",new Articulo("4-11","ASUS  MONITOR  22 ",5,100));
        articulos.put("4-22",new Articulo("4-22","HP MONITOR LED 28 ",5,180));
        articulos.put("4-33",new Articulo("4-33","SAMSUNG ODISSEY G5",12,580));
        articulos.put("4-44", new Articulo("4-44", "LG MONITOR 24''", 10, 140));
        articulos.put("5-11", new Articulo("5-11", "PLACA BASE MSI B450", 7, 110));

        LocalDate hoy = LocalDate.now();
        pedidos.add(new Pedido(clientes.get("80580845T"),hoy.minusDays(1), new ArrayList<>
                (List.of(new LineaPedido("1-11",3),new LineaPedido("4-22",3))),pedidos));
        pedidos.add(new Pedido(clientes.get("80580845T"),hoy.minusDays(2), new ArrayList<>
                (List.of(new LineaPedido("4-11",3),new LineaPedido("4-22",2),new LineaPedido("4-33",4))),pedidos));
        pedidos.add(new Pedido(clientes.get("36347775R"),hoy.minusDays(3), new ArrayList<>
                (List.of(new LineaPedido("4-22",1),new LineaPedido("2-22",3))),pedidos));
        pedidos.add(new Pedido(clientes.get("36347775R"),hoy.minusDays(5), new ArrayList<>
                (List.of(new LineaPedido("4-33",3),new LineaPedido("2-11",3))),pedidos));
        pedidos.add(new Pedido(clientes.get("63921307Y"),hoy.minusDays(4), new ArrayList<>
                (List.of(new LineaPedido("2-11",5),new LineaPedido("2-33",3),new LineaPedido("4-33",2))),pedidos));
        pedidos.add(new Pedido(clientes.get("65318802K"), hoy.minusDays(2),
                new ArrayList<>(List.of(new LineaPedido("1-33", 2), new LineaPedido("4-11", 1))), pedidos));
        pedidos.add(new Pedido(clientes.get("14665825J"), hoy.minusDays(3),
                new ArrayList<>(List.of(new LineaPedido("2-11", 1), new LineaPedido("2-22", 2))), pedidos));
        pedidos.add(new Pedido(clientes.get("02337565Y"), hoy.minusDays(1),
                new ArrayList<>(List.of(new LineaPedido("5-11", 1), new LineaPedido("3-22", 1))), pedidos));
        pedidos.add(new Pedido(clientes.get("80580845T"), hoy.minusDays(4),
                new ArrayList<>(List.of(new LineaPedido("1-44", 1), new LineaPedido("2-44", 2))), pedidos));
        pedidos.add(new Pedido(clientes.get("88558514G"), hoy.minusDays(6),
                new ArrayList<>(List.of(new LineaPedido("4-22", 1), new LineaPedido("3-11", 2))), pedidos));
        pedidos.add(new Pedido(clientes.get("47540602Q"), hoy.minusDays(5),
                new ArrayList<>(List.of(new LineaPedido("4-44", 1), new LineaPedido("5-11", 1))), pedidos));
        pedidos.add(new Pedido(clientes.get("36347775R"), hoy.minusDays(7),
                new ArrayList<>(List.of(new LineaPedido("1-22", 2), new LineaPedido("4-33", 1))), pedidos));
        pedidos.add(new Pedido(clientes.get("51395126Q"), hoy.minusDays(8),
                new ArrayList<>(List.of(new LineaPedido("2-33", 1), new LineaPedido("3-22", 3))), pedidos));
        pedidos.add(new Pedido(clientes.get("76648739C"), hoy.minusDays(9),
                new ArrayList<>(List.of(new LineaPedido("4-22", 2), new LineaPedido("1-44", 1))), pedidos));
        pedidos.add(new Pedido(clientes.get("63921307Y"), hoy.minusDays(10),
                new ArrayList<>(List.of(new LineaPedido("2-44", 1), new LineaPedido("3-11", 1))), pedidos));
        pedidos.add(new Pedido(clientes.get("65318802K"), hoy.minusDays(11),
                new ArrayList<>(List.of(new LineaPedido("5-11", 1), new LineaPedido("1-22", 1))), pedidos));
        pedidos.add(new Pedido(clientes.get("14665825J"), hoy.minusDays(12),
                new ArrayList<>(List.of(new LineaPedido("1-33", 1), new LineaPedido("2-11", 2))), pedidos));
        pedidos.add(new Pedido(clientes.get("02337565Y"), hoy.minusDays(13),
                new ArrayList<>(List.of(new LineaPedido("4-44", 1), new LineaPedido("2-22", 1))), pedidos));
        pedidos.add(new Pedido(clientes.get("88558514G"), hoy.minusDays(14),
                new ArrayList<>(List.of(new LineaPedido("3-22", 2), new LineaPedido("1-11", 3))), pedidos));
        pedidos.add(new Pedido(clientes.get("47540602Q"), hoy.minusDays(15),
                new ArrayList<>(List.of(new LineaPedido("2-33", 2), new LineaPedido("4-22", 1))), pedidos));



    }


}