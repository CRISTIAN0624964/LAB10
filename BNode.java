// BNode.java
import java.util.ArrayList;

public class BNode<E extends Comparable<E>> {
    int count; // número de claves en el nodo
    ArrayList<E> keys;
    ArrayList<BNode<E>> childs;
    BNode<E> parent;
    int orden;
    int idNode;  // opcional, para identificar nodos (no necesario aquí)

    public BNode(int orden) {
        this.orden = orden;
        this.count = 0;
        this.keys = new ArrayList<>();
        this.childs = new ArrayList<>();
        for (int i = 0; i < orden; i++) { // máximo hijos = orden
            childs.add(null);
        }
        for (int i = 0; i < orden - 1; i++) { // máximo claves = orden -1
            keys.add(null);
        }
        parent = null;
    }

    /**
     * Busca la posición para la clave y retorna si la encontró.
     * pos[0] = posición donde está o donde debe ir la clave.
     */
    public boolean searchNode(E key, int[] pos) {
        int i = 0;
        while (i < count && key.compareTo(keys.get(i)) > 0) i++;
        pos[0] = i;
        if (i < count && key.compareTo(keys.get(i)) == 0) return true;
        return false;
    }
}
