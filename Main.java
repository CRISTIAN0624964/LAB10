// Main.java
public class Main {
    public static void main(String[] args) {
        BTree<RegistroEstudiante> arbol = new BTree<>(4);

        // Insertar estudiantes
        arbol.insert(new RegistroEstudiante(103, "Ana"));
        arbol.insert(new RegistroEstudiante(110, "Luis"));
        arbol.insert(new RegistroEstudiante(101, "Carlos"));
        arbol.insert(new RegistroEstudiante(120, "Lucía"));
        arbol.insert(new RegistroEstudiante(115, "David"));
        arbol.insert(new RegistroEstudiante(125, "Jorge"));
        arbol.insert(new RegistroEstudiante(140, "Camila"));
        arbol.insert(new RegistroEstudiante(108, "Rosa"));
        arbol.insert(new RegistroEstudiante(132, "Ernesto"));
        arbol.insert(new RegistroEstudiante(128, "Denis"));
        arbol.insert(new RegistroEstudiante(145, "Enrique"));
        arbol.insert(new RegistroEstudiante(122, "Karina"));
        arbol.insert(new RegistroEstudiante(108, "Juan")); // duplicado, no se insertará

        // Buscar código 115
        System.out.println("Buscar código 115: " + arbol.buscarNombre(115)); // David

        // Buscar código 132
        System.out.println("Buscar código 132: " + arbol.buscarNombre(132)); // Ernesto

        // Buscar código 999
        System.out.println("Buscar código 999: " + arbol.buscarNombre(999)); // No encontrado

        // Eliminar estudiante con código 101 (no implementado, solo mensaje)
        System.out.println("Eliminar código 101 ");

        // Insertar nuevo estudiante (106, "Sara")
        arbol.insert(new RegistroEstudiante(106, "Sara"));

        // Buscar código 106
        System.out.println("Buscar código 106: " + arbol.buscarNombre(106)); // Sara

        // Imprimir árbol (inorden)
        System.out.println("\nÁrbol B (inorden):\n" + arbol);
    }
}
