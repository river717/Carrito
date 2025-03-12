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

}

class Carretilla(){

}
