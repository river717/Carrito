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

        /* when(opcion){
            1-> Función para mostrar lista de productos
            2-> Función para agregar fila de productos
            3-> Función para editar fila de productos
            4-> Función para gestionar el carrito
            5-> println("Saliendo.....")
        }*/
    } while (opcion !=5)
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

}
