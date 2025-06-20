import java.util.ArrayList;

public class BNode<E extends Comparable<E>> {
    public ArrayList<E> keys;
    public ArrayList<BNode<E>> childs;
    public int count;
    public int idNode;
    public BNode<E> parent;
    private static int counter = 0;

    public BNode(int orden) {
        this.idNode = counter++;
        this.count = 0;
        this.parent = null;
        this.keys = new ArrayList<>();
        this.childs = new ArrayList<>();
        for (int i = 0; i < orden - 1; i++) keys.add(null);
        for (int i = 0; i < orden; i++) childs.add(null);
    }

    public boolean nodeFull(int maxKeys) {
        return count == maxKeys;
    }

    public boolean searchNode(E key, int[] pos) {
        pos[0] = 0;
        while (pos[0] < count && keys.get(pos[0]) != null && key.compareTo(keys.get(pos[0])) > 0)
            pos[0]++;
        return pos[0] < count && key.compareTo(keys.get(pos[0])) == 0;
    }

    public String describeNode() {
        StringBuilder sb = new StringBuilder();
        sb.append("[ID ").append(idNode).append(": ");
        for (int i = 0; i < count; i++) sb.append(keys.get(i).toString()).append(" ");
        sb.append("] parent=").append(parent != null ? parent.idNode : "null").append(" childs=");
        for (int i = 0; i <= count; i++) sb.append(childs.get(i) != null ? childs.get(i).idNode + " " : "null ");
        return sb.toString();
    }
}
