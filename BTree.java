import java.util.ArrayList;

public class BTree<E extends Comparable<E>> {
    BNode<E> root;
    int orden;
    boolean up;
    BNode<E> nDes;
    E mediana;

    public BTree(int orden) {
        this.orden = orden;
        this.root = null;
    }

    public boolean isEmpty() {
        return root == null;
    }

    // Inserción pública
    public void insert(E key) {
        up = false;
        mediana = push(root, key);
        if (up) {
            BNode<E> pnew = new BNode<>(orden);
            pnew.count = 1;
            pnew.keys.set(0, mediana);
            pnew.childs.set(0, root);
            pnew.childs.set(1, nDes);
            if (root != null) root.parent = pnew;
            if (nDes != null) nDes.parent = pnew;
            root = pnew;
        }
    }

    private E push(BNode<E> current, E key) {
        int[] pos = new int[1];
        if (current == null) {
            up = true;
            nDes = null;
            return key;
        }
        boolean found = current.searchNode(key, pos);
        if (found) {
            up = false; // clave ya existe, no inserta duplicados
            return null;
        }
        E med = push(current.childs.get(pos[0]), key);
        if (!up) return null;
        if (current.count < orden - 1) {
            putNode(current, med, nDes, pos[0]);
            up = false;
            return null;
        } else {
            return dividedNode(current, med, nDes, pos[0]);
        }
    }

    private void putNode(BNode<E> current, E key, BNode<E> child, int pos) {
        for (int i = current.count; i > pos; i--) {
            current.keys.set(i, current.keys.get(i - 1));
            current.childs.set(i + 1, current.childs.get(i));
        }
        current.keys.set(pos, key);
        current.childs.set(pos + 1, child);
        if (child != null) child.parent = current;
        current.count++;
    }

    private E dividedNode(BNode<E> current, E key, BNode<E> child, int pos) {
        int mid = orden / 2;
        BNode<E> right = new BNode<>(orden);

        // Claves y hijos temp para dividir
        ArrayList<E> tempKeys = new ArrayList<>();
        ArrayList<BNode<E>> tempChilds = new ArrayList<>();
        for (int i = 0; i < orden - 1; i++) tempKeys.add(current.keys.get(i));
        for (int i = 0; i < orden; i++) tempChilds.add(current.childs.get(i));

        // Insertar nuevo key y child temporalmente
        tempKeys.add(null);
        tempChilds.add(null);
        for (int i = tempKeys.size() - 2; i >= pos; i--) {
            tempKeys.set(i + 1, tempKeys.get(i));
            tempChilds.set(i + 2, tempChilds.get(i + 1));
        }
        tempKeys.set(pos, key);
        tempChilds.set(pos + 1, child);

        // Ajustar hijos padre nuevo
        for (int i = 0; i < orden; i++) {
            if (tempChilds.get(i) != null) tempChilds.get(i).parent = null;
        }

        // Copiar izquierda
        current.count = mid;
        for (int i = 0; i < mid; i++) {
            current.keys.set(i, tempKeys.get(i));
            current.childs.set(i, tempChilds.get(i));
            if (current.childs.get(i) != null) current.childs.get(i).parent = current;
        }
        current.childs.set(mid, tempChilds.get(mid));
        if (current.childs.get(mid) != null) current.childs.get(mid).parent = current;

        // Copiar derecha
        right.count = orden - 1 - mid;
        for (int i = 0; i < right.count; i++) {
            right.keys.set(i, tempKeys.get(i + mid + 1));
            right.childs.set(i, tempChilds.get(i + mid + 1));
            if (right.childs.get(i) != null) right.childs.get(i).parent = right;
        }
        right.childs.set(right.count, tempChilds.get(orden));
        if (right.childs.get(right.count) != null) right.childs.get(right.count).parent = right;

        // Mediana que sube
        mediana = tempKeys.get(mid);
        nDes = right;
        return mediana;
    }

    // Búsqueda
    public boolean search(E key) {
        return searchNode(root, key);
    }

    private boolean searchNode(BNode<E> current, E key) {
        if (current == null) return false;
        int[] pos = new int[1];
        boolean found = current.searchNode(key, pos);
        if (found) return true;
        else return searchNode(current.childs.get(pos[0]), key);
    }

    // Buscar nombre por código (para RegistroEstudiante)
    public String buscarNombre(int codigo) {
        return buscarNombreNodo(root, codigo);
    }

    private String buscarNombreNodo(BNode<E> current, int codigo) {
        if (current == null) return "No encontrado";

        int[] pos = new int[1];
        RegistroEstudiante buscado = new RegistroEstudiante(codigo, "");
        boolean found = current.searchNode((E) buscado, pos);
        if (found) {
            RegistroEstudiante real = (RegistroEstudiante) current.keys.get(pos[0]);
            return real.nombre;
        } else {
            return buscarNombreNodo(current.childs.get(pos[0]), codigo);
        }
    }

    // Eliminación pública
    public void remove(E key) throws Exception {
        if (root == null) throw new Exception("Árbol vacío");
        remove(root, key);
        if (root.count == 0 && root.childs.get(0) != null) {
            root = root.childs.get(0);
            root.parent = null;
        }
        if (root.count == 0 && root.childs.get(0) == null) root = null;
    }

    private void remove(BNode<E> current, E key) throws Exception {
        int[] pos = new int[1];
        boolean found = current.searchNode(key, pos);
        if (found) {
            if (current.childs.get(pos[0]) == null) { // hoja
                for (int i = pos[0]; i < current.count - 1; i++) {
                    current.keys.set(i, current.keys.get(i + 1));
                }
                current.keys.set(current.count - 1, null);
                current.count--;
            } else { // nodo interno
                BNode<E> pred = current.childs.get(pos[0]);
                while (pred.childs.get(pred.count) != null)
                    pred = pred.childs.get(pred.count);
                E predKey = pred.keys.get(pred.count - 1);
                current.keys.set(pos[0], predKey);
                remove(pred, predKey);
            }
        } else {
            if (current.childs.get(pos[0]) == null)
                throw new Exception("Clave no encontrada");
            remove(current.childs.get(pos[0]), key);
        }

        // Verificar si hace falta balancear
        if (current != null && current != root && current.count < (orden - 1) / 2) {
            fixNode(current);
        }
    }

    private void fixNode(BNode<E> node) throws Exception {
        BNode<E> parent = node.parent;
        if (parent == null) return;

        int idx = parent.childs.indexOf(node);

        BNode<E> leftSibling = idx > 0 ? parent.childs.get(idx - 1) : null;
        BNode<E> rightSibling = idx < parent.count ? parent.childs.get(idx + 1) : null;

        if (leftSibling != null && leftSibling.count > (orden - 1) / 2) {
            // Rotación derecha
            for (int i = node.count; i > 0; i--) {
                node.keys.set(i, node.keys.get(i - 1));
            }
            node.keys.set(0, parent.keys.get(idx - 1));
            parent.keys.set(idx - 1, leftSibling.keys.get(leftSibling.count - 1));

            if (leftSibling.childs.get(leftSibling.count) != null) {
                node.childs.set(0, leftSibling.childs.get(leftSibling.count));
                node.childs.get(0).parent = node;
            }
            node.count++;
            leftSibling.keys.set(leftSibling.count - 1, null);
            leftSibling.childs.set(leftSibling.count, null);
            leftSibling.count--;
        } else if (rightSibling != null && rightSibling.count > (orden - 1) / 2) {
            // Rotación izquierda
            node.keys.set(node.count, parent.keys.get(idx));
            parent.keys.set(idx, rightSibling.keys.get(0));
            for (int i = 0; i < rightSibling.count - 1; i++) {
                rightSibling.keys.set(i, rightSibling.keys.get(i + 1));
            }
            if (rightSibling.childs.get(0) != null) {
                node.childs.set(node.count + 1, rightSibling.childs.get(0));
                node.childs.get(node.count + 1).parent = node;
            }
            for (int i = 0; i < rightSibling.count; i++) {
                rightSibling.childs.set(i, rightSibling.childs.get(i + 1));
            }
            rightSibling.keys.set(rightSibling.count - 1, null);
            rightSibling.childs.set(rightSibling.count, null);
            node.count++;
            rightSibling.count--;
        } else {
            // Fusión
            if (leftSibling != null) {
                // Fusionar con hermano izquierdo
                leftSibling.keys.set(leftSibling.count, parent.keys.get(idx - 1));
                for (int i = 0; i < node.count; i++) {
                    leftSibling.keys.set(leftSibling.count + 1 + i, node.keys.get(i));
                }
                for (int i = 0; i <= node.count; i++) {
                    leftSibling.childs.set(leftSibling.count + 1 + i, node.childs.get(i));
                    if (node.childs.get(i) != null) node.childs.get(i).parent = leftSibling;
                }
                leftSibling.count += 1 + node.count;

                // Remover key y child del padre
                for (int i = idx - 1; i < parent.count - 1; i++) {
                    parent.keys.set(i, parent.keys.get(i + 1));
                    parent.childs.set(i + 1, parent.childs.get(i + 2));
                }
                parent.keys.set(parent.count - 1, null);
                parent.childs.set(parent.count, null);
                parent.count--;

                if (parent == root && parent.count == 0) {
                    root = leftSibling;
                    root.parent = null;
                } else if (parent.count < (orden - 1) / 2) {
                    fixNode(parent);
                }
            } else if (rightSibling != null) {
                // Fusionar con hermano derecho
                node.keys.set(node.count, parent.keys.get(idx));
                for (int i = 0; i < rightSibling.count; i++) {
                    node.keys.set(node.count + 1 + i, rightSibling.keys.get(i));
                }
                for (int i = 0; i <= rightSibling.count; i++) {
                    node.childs.set(node.count + 1 + i, rightSibling.childs.get(i));
                    if (rightSibling.childs.get(i) != null) rightSibling.childs.get(i).parent = node;
                }
                node.count += 1 + rightSibling.count;

                for (int i = idx; i < parent.count - 1; i++) {
                    parent.keys.set(i, parent.keys.get(i + 1));
                    parent.childs.set(i + 1, parent.childs.get(i + 2));
                }
                parent.keys.set(parent.count - 1, null);
                parent.childs.set(parent.count, null);
                parent.count--;

                if (parent == root && parent.count == 0) {
                    root = node;
                    root.parent = null;
                } else if (parent.count < (orden - 1) / 2) {
                    fixNode(parent);
                }
            }
        }
    }

    // toString para imprimir árbol simple (inorden)
    public String toString() {
        StringBuilder sb = new StringBuilder();
        toStringRec(root, sb, 0);
        return sb.toString();
    }

    private void toStringRec(BNode<E> node, StringBuilder sb, int level) {
        if (node == null) return;
        for (int i = 0; i < node.count; i++) {
            toStringRec(node.childs.get(i), sb, level + 1);
            sb.append(" ".repeat(level * 2)).append(node.keys.get(i)).append("\n");
        }
        toStringRec(node.childs.get(node.count), sb, level + 1);
    }
}
