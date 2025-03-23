import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class Carrito {
}

fun main() {
    val archivo = Archivo()
    val carretilla = Carretilla()

    var opcion = 0
    do {
        println("\nMenú:")
        println("----------------------------------")
        println("1. Mostrar la lista de productos.")
        println("2. Agregar productos.")
        println("3. Editar productos.")
        println("4. Gestionar carrito de compras.")
        println("5. Salir.")

        print("Seleccione una opción: ")
        opcion = readLine()!!.toInt()

        when(opcion){
            1-> archivo.mostrarArchivo()
            2-> archivo.agregarProducto()
            3-> archivo.editarProducto()
            4-> gestionarCarretilla(carretilla, archivo)
            5-> println("Saliendo.....")
        }
    } while (opcion !=5)
}

fun gestionarCarretilla(carretilla: Carretilla, archivo: Archivo) {
    var opcionCarretilla = 0

    do {
        println("\nMenú Carrito de Compras:")
        println("----------------------------------")
        println("1. Mostrar productos en el carrito.")
        println("2. Agregar producto al carrito.")
        println("3. Quitar producto de la carretilla.")
        println("4. Borrar carretilla.")
        println("5. Pagar Carrito")
        println("6. Volver al menú principal.")

        print("Elija una opción: ")
        opcionCarretilla = readLine()!!.toInt()

        when(opcionCarretilla){
            1-> carretilla.mostrarCarretilla()
            2-> {
                print("\nIngrese el id del producto a agregar: ")
                val id = readLine()!!
                val productoInfo = archivo.datos.find { it.split(",")[0] == id }
                if (productoInfo != null){
                    val (_, producto, existencia, precioUnitario) = productoInfo.split(",")
                    print("Ingrese la cantidad de $producto que desea agregar (Disponible $existencia): ")
                    val cantidad = readLine()!!.toInt()
                    if (cantidad <= existencia.toInt()){
                        carretilla.agregarProducto(producto, cantidad, precioUnitario.toDouble())
                    } else {
                        println("No hay suficiente producto en existencia.")
                    }
                } else {
                    println("No se encontró el producto con el id $id")
                }
            }
            3->{
                println("Ingrese el id del producto a quitar de la carretilla:")
                val id = readLine()!!
                val producto = archivo.datos.find { it.split(",")[0] == id }
                if (producto != null){
                    carretilla.quitarProducto(producto)
                } else{
                    println("No se encontro un producto con el ID $id")
                }
            }
            4-> carretilla.borrarCarretilla()

            5-> {
                carretilla.generarFactura(carretilla, archivo)
            }

            6-> println("Volviendo al menu principal...")
        }
    } while (opcionCarretilla != 6)
}

class Archivo(){
    private val archivo = File("C:\\DSM\\inventario.txt")
    private val lector = BufferedReader(FileReader(archivo))

    val datos = ArrayList<String>()

    init {
        while (true){
            val linea = lector.readLine()
            if (linea == null){
                break
            }
            datos.add(linea)
        }
        lector.close()
    }
    //Mtodo para agregar producto
    fun agregarProducto(){
        print("\nIngrese el id del producto: ")
        val id = readLine()!!

        print("Ingrese el nombre del producto: ")
        val producto = readLine()!!

        print("Ingrese la cantidad de producto: ")
        val existencia = readLine()!!

        print("Ingrese el precio unitario del producto: ")
        val precioUnitario = readLine()!!

        datos.add("${id},${producto},${existencia},${precioUnitario}")

        guardarArchivo(1)
    }

    fun editarProducto(){
        print("\nIngrese el id del producto para actualizar: ")
        val id = readLine()!!

        val posicion = datos.indexOfFirst { it.split(",")[0] == id }
        if (posicion == -1){
            println("No se encontró el producto con el id $id")
            return
        }

        print("Ingrese el nuevo nombre del producto: ")
        val producto = readLine()!!

        print("Ingrese la nueva existencia del producto: ")
        val existencia = readLine()!!.toInt()

        print("Ingrese el nuevo precio unitario del producto: ")
        val precioUnitario = readLine()!!

        datos[posicion] = "${id},${producto},${existencia},${precioUnitario}"

        guardarArchivo(2)
    }

    //metodo para guardar el archivo
    private fun guardarArchivo(aux: Number) {
        val escritor = FileWriter(archivo)
        for (linea in datos){
            escritor.write(linea + "\n")
        }
        escritor.close()
        if (aux == 1){
            println("Producto agregado correctamente.")
        } else if (aux == 2){
            println("Producto editado correctamente.")
        }

    }

    //Este metodo muestra la lista, aun con cuando se agregar filas nuevas
    fun mostrarArchivo(){
        println("\n--- Inventario de Productos ---")
        println("ID   | Producto    | Existencia | Precio Unitario")
        println("---------------------------------------------")

        for (linea in datos) {
            val (id, producto, existencia, precioUnitario) = linea.split(",")

            // Usamos String.format() para alinear los valores en columnas
            println(String.format("%-4s | %-10s | %-8s | %-13s", id, producto, existencia, "$$precioUnitario"))
        }
    }

    fun actualizarInventario(productosComprados: Map<String, Int>) {
        productosComprados.forEach { (producto, cantidadComprada) ->
            val posicion = datos.indexOfFirst { it.split(",")[1] == producto }
            if (posicion != -1) {
                val (id, nombre, existencia, precioUnitario) = datos[posicion].split(",")
                val nuevaExistencia = existencia.toInt() - cantidadComprada
                datos[posicion] = "$id,$nombre,$nuevaExistencia,$precioUnitario"
            }
        }
        guardarArchivo(3)
    }
}

class Carretilla(){
    private val productos = mutableListOf<Pair<String, Int>>() //Intente almacenar  producto y cantidad
    private val precios = mutableMapOf<String, Double>() // Almacenar producto y precio  unitario

    // Mostrar los productos en la carretilla

    fun mostrarCarretilla(){
        if (productos.isEmpty()) {
            println("\nEl carrito está vacío.")
        } else {
            println("\nProductos en el carrito:")
            productos.forEach { (producto, cantidad) ->
                val precioUnitario = precios[producto] ?: 0.0
                val precioTotal = cantidad * precioUnitario
                println("$producto - Cantidad: $cantidad - Precio unitario: $precioUnitario - Precio total: $precioTotal")
            }
            println("Precio total del carrito (sin iva): ${calcularPrecioTotal()}")
        }
    }

    // Calcular el precio total de la carretilla
    private fun calcularPrecioTotal(): Double {
        return productos.sumOf { (producto, cantidad) ->
            val precioUnitario = precios[producto] ?: 0.0
            cantidad * precioUnitario
        }
    }

    fun agregarProducto(producto: String, cantidad: Int, precioUnitario: Double) {
        productos.add(Pair(producto,cantidad))
        precios[producto] = precioUnitario
        println("\n$cantidad unidades del producto $producto agregados al carrito")
    }

    // Quitar un producto de la carretilla
    fun quitarProducto(producto: String) {
        if (productos.isEmpty()) {
            println("El carrito está vacio, no hay productos que quitar.")
        } else {
            val productoEnCarretilla = productos.find { it.first == producto }
            if (productoEnCarretilla != null) {
                productos.remove(productoEnCarretilla)
                precios.remove(producto)
                println("Producto '$producto' retirado de la carretilla.")
            } else {
                println("El producto '$producto' no esta en la carretilla.")
            }
        }
    }

    fun borrarCarretilla(){
        if (productos.isEmpty()){
            println("El carrito esta vacio")
        } else{
            println("Esta seguro que desea borrar la carretilla? Y/N")
            var res = readLine()!!.trim().uppercase()
            if (res == "Y"){
                productos.clear()
                println("La carretilla ha sido borrada!")
            } else {
                println("Operacion cancellada. La carretila no se ha borrado")
            }
        }
    }

    fun generarFactura(carretilla: Carretilla, archivo: Archivo) {
        if (productos.isEmpty()) {
            println("\nNo hay productos en el carrito para facturar.")
            return
        }

        println("\n--- FACTURA ---")
        println("Producto     | Cantidad | Precio unidad | Precio total")
        println("-------------------------------------------------------")
        var totalSinImpuesto = 0.0

        productos.forEach { (producto, cantidad) ->
            val precioUnitario = precios[producto] ?: 0.0
            val precioTotal = cantidad * precioUnitario
            totalSinImpuesto += precioTotal
            println(String.format("%-12s | %-8s | %-13s | %-10s", producto, cantidad, "$$precioUnitario", "$$precioTotal"))
        }

        val impuesto = totalSinImpuesto * 0.13
        val totalConImpuesto = totalSinImpuesto + impuesto

        println("------------------------")
        println("Subtotal: $${"%.2f".format(totalSinImpuesto)}")
        println("Impuesto (13%): $${"%.2f".format(impuesto)}")
        println("Total a pagar: $${"%.2f".format(totalConImpuesto)}")

        archivo.actualizarInventario(carretilla.obtenerProductosComprados())
    }

    fun obtenerProductosComprados(): Map<String, Int> {
        return productos.associate { it.first to it.second }
    }
}
