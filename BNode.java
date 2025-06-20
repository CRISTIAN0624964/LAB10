import java.util.ArrayList;

public class BNode<E extends Comparable<E>> {
    protected ArrayList<E> keys;
    protected ArrayList<BNode<E>> childs;
    protected int count;
    protected int idNode;
    protected BNode<E> parent;
    private static int idCounter = 0;

    public BNode(int n) {
        this.keys = new ArrayList<>(n);
        this.childs = new ArrayList<>(n + 1);
        this.count = 0;
        this.idNode = idCounter++;
        this.parent = null;

        for (int i = 0; i < n; i++) {
            this.keys.add(null);
        }
        for (int i = 0; i <= n; i++) {
            this.childs.add(null);
        }
    }

    public boolean nodeFull(int maxKeys) {
        return this.count == maxKeys;
    }

    public boolean nodeEmpty() {
        return this.count == 0;
    }

    public boolean searchNode(E key, int[] pos) {
        pos[0] = 0;
        while (pos[0] < this.count && key.compareTo(this.keys.get(pos[0])) > 0) {
            pos[0]++;
        }
        return pos[0] < this.count && key.compareTo(this.keys.get(pos[0])) == 0;
    }

    public String describeNode() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-8s | ", "Nodo " + idNode));

        sb.append("Claves: ");
        for (int i = 0; i < count; i++) {
            sb.append(keys.get(i));
            if (i < count - 1) sb.append(", ");
        }

        sb.append(" | Padre: ").append((parent != null) ? parent.idNode : "null");

        sb.append(" | Hijos: ");
        boolean atLeastOne = false;
        for (int i = 0; i <= count; i++) {
            BNode<E> child = childs.get(i);
            if (child != null) {
                if (atLeastOne) sb.append(", ");
                sb.append(child.idNode);
                atLeastOne = true;
            }
        }

        sb.append("\n");
        return sb.toString();
    }
}
