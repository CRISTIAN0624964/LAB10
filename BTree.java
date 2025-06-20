import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class BTree<E extends Comparable<E>> {
    public BNode<E> root;
    private int orden;

    public BTree(int orden) {
        this.orden = orden;
        this.root = null;
    }

    public static BTree<Integer> building_Btree(String path) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(path));
        if (lines.isEmpty()) throw new ItemNoFoundException("Archivo vacío");

        int ord = Integer.parseInt(lines.get(0).trim());
        BTree<Integer> tree = new BTree<>(ord);
        Map<Integer, List<BNode<Integer>>> levelMap = new HashMap<>();

        for (int i = 1; i < lines.size(); i++) {
            String[] parts = lines.get(i).split(",");
            int level = Integer.parseInt(parts[0]);
            int id = Integer.parseInt(parts[1]);
            BNode<Integer> node = new BNode<>(ord);
            node.idNode = id;  // conservar el id dado

            for (int j = 2; j < parts.length; j++) {
                int key = Integer.parseInt(parts[j].trim());
                node.keys.set(node.count++, key);
            }

            levelMap.computeIfAbsent(level, k -> new ArrayList<>()).add(node);
            if (level == 0) tree.root = node;
        }

        for (Map.Entry<Integer, List<BNode<Integer>>> entry : levelMap.entrySet()) {
            int level = entry.getKey();
            List<BNode<Integer>> parents = entry.getValue();
            List<BNode<Integer>> children = levelMap.get(level + 1);
            if (children == null) continue;
            for (BNode<Integer> p : parents) {
                int childIndex = 0;
                for (BNode<Integer> c : children) {
                    if (childIndex <= p.count) {
                        p.childs.set(childIndex++, c);
                        c.parent = p;
                    }
                }
            }
        }

        validar(tree.root, ord, 0, levelMap.size() - 1);
        return tree;
    }

    private static void validar(BNode<Integer> node, int orden, int nivel, int nivelMax) throws ItemNoFoundException {
        if (node == null) return;
        int minKeys = (node.parent == null ? 1 : (orden - 1) / 2);
        if (node.count < minKeys || node.count > orden - 1) {
            throw new ItemNoFoundException("Nodo " + node.idNode + " tiene " + node.count + " claves (orden=" + orden + ")");
        }
        boolean isLeaf = node.childs.stream().allMatch(Objects::isNull);
        if (isLeaf && nivel != nivelMax)
            throw new ItemNoFoundException("Hoja " + node.idNode + " en nivel incorrecto");
        if (!isLeaf && nivel == nivelMax)
            throw new ItemNoFoundException("Nodo interno " + node.idNode + " al nivel de hojas");

        for (BNode<Integer> c : node.childs) {
            if (c != null) validar(c, orden, nivel + 1, nivelMax);
        }
    }

    @Override
    public String toString() {
        return traverse(root);
    }

    private String traverse(BNode<E> cur) {
        if (cur == null) return "";
        StringBuilder sb = new StringBuilder(cur.describeNode());
        for (BNode<E> c : cur.childs) {
            sb.append(traverse(c));
        }
        return sb.toString();
    }
}
