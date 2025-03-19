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
        println("Menu:")
        println("----------------------------------")
        println("1. Mostrar la lista de productos.")
        println("2. Agregar productos.")
        println("3. Editar productos.")
        println("4. Gestionar carrito")
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
        println("Menu Carretilla:")
        println("----------------------------------")
        println("1. Mostrar productos en la carretilla.")
        println("2. Agregar producto a la carretilla.")
        println("3. Volver al menu principal.")

        opcionCarretilla = readLine()!!.toInt()

        when(opcionCarretilla){
            1-> carretilla.mostrarCarretilla()
            2-> {
                println("Ingrese el id del producto a agregar: ")
                val id = readLine()!!
                val productoInfo = archivo.datos.find { it.split(",")[0] == id }
                if (productoInfo != null){
                    val (_, producto, existencia, precioUnitario) = productoInfo.split(",")
                    println("Ingrese la cantidad de $producto que desea agregar (Disponible $existencia): ")
                    val cantidad = readLine()!!.toInt()
                    if (cantidad <= existencia.toInt()){
                        carretilla.agregarProducto(producto, cantidad, precioUnitario.toDouble())
                    } else {
                        println("No hay suficiente producto en existencia.")
                    }
                } else {
                    println("No se encontro el producto con el id $id")
                }
            }
        }
    } while (opcionCarretilla != 3)
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
        println("Ingrese el id del producto: ")
        val id = readLine()!!

        println("Ingrese el nombre del producto:")
        val producto = readLine()!!

        println("Ingrese la cantidad de producto:")
        val existencia = readLine()!!

        println("Ingrese el precio unitario del producto:")
        val precioUnitario = readLine()!!

        datos.add("${id},${producto},${existencia},${precioUnitario}")

        guardarArchivo()
    }

    fun editarProducto(){
        println("Ingrese el id del producto para actualizar:")
        val id = readLine()!!

        val posicion = datos.indexOfFirst { it.split(",")[0] == id }
        if (posicion == -1){
            println("No se encontro el producto con el id $id")
            return
        }

        println("Ingrese el nuevo nombre del producto:")
        val producto = readLine()!!

        println("Ingrese la nueva existencia del producto:")
        val existencia = readLine()!!.toInt()

        println("Ingrese el nuevo precio unitario del producto:")
        val precioUnitario = readLine()!!

        datos[posicion] = "${id},${producto},${existencia},${precioUnitario}"

        guardarArchivo()
    }

    //metodo para guardar el archivo
    private fun guardarArchivo() {
        val escritor = FileWriter(archivo)
        for (linea in datos){
            escritor.write(linea + "\n")
            println(linea)
        }
        escritor.close()
    }

    //Este metodo muestra la lista, aun con cuando se agregar filas nuevas
    fun mostrarArchivo(){
        for (linea in datos){
            println(linea)
        }
    }
}

class Carretilla(){
    private val productos = mutableListOf<Pair<String, Int>>() //Intente almacenar  producto y cantidad
    private val precios = mutableMapOf<String, Double>() // Almacenar producto y precio  unitario

    // Mostrar los productos en la carretilla

    fun mostrarCarretilla(){
        if (productos.isEmpty()) {
            println("La carretilla está vacia.")
        } else {
            println("Productos en la carretilla:")
            productos.forEach { (producto, cantidad) ->
                val precioUnitario = precios[producto] ?: 0.0
                val precioTotal = cantidad * precioUnitario
                println("$producto - Cantidad: $cantidad - Precio unitario: $precioUnitario - Precio total: $precioTotal")
            }
            println("Precio total de la carretilla: ${calcularPrecioTotal()}")
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
        println("$cantidad unidades del producto $producto agregados a la carretilla")
    }
}
