public class BTree<E extends Comparable<E>> {
    private BNode<E> root;
    private int orden;
    private boolean up;
    private BNode<E> nDes;

    public BTree(int orden) {
        this.orden = orden;
        this.root = null;
    }

    public void insert(E cl) {
        up = false;
        E med = push(root, cl);
        if (up) {
            BNode<E> newR = new BNode<>(orden);
            newR.count = 1;
            newR.keys.set(0, med);
            newR.childs.set(0, root);
            newR.childs.set(1, nDes);
            if (root != null) root.parent = newR;
            if (nDes != null) nDes.parent = newR;
            root = newR;
        }
    }

    private E push(BNode<E> cur, E cl) {
        if (cur == null) {
            up = true; nDes = null;
            return cl;
        }
        int[] pos = new int[1];
        if (cur.searchNode(cl, pos)) {
            System.out.println("Item duplicado: " + cl);
            up = false;
            return null;
        }
        E med = push(cur.childs.get(pos[0]), cl);
        if (!up) return null;
        if (cur.nodeFull(orden - 1)) med = dividedNode(cur, med, pos[0]);
        else { putNode(cur, med, nDes, pos[0]); up = false; }
        return med;
    }

    private void putNode(BNode<E> cur, E cl, BNode<E> rd, int k) {
        cur.keys.add(null); cur.childs.add(null);
        for (int i = cur.count - 1; i >= k; i--) {
            cur.keys.set(i + 1, cur.keys.get(i));
            cur.childs.set(i + 2, cur.childs.get(i + 1));
        }
        cur.keys.set(k, cl);
        cur.childs.set(k + 1, rd);
        if (rd != null) rd.parent = cur;
        cur.count++;
    }

    private E dividedNode(BNode<E> cur, E cl, int k) {
        int mid = (orden - 1) / 2;
        BNode<E> sib = new BNode<>(orden);
        sib.parent = cur.parent;
        for (int i = mid + 1; i < orden - 1; i++) {
            sib.keys.set(i - mid - 1, cur.keys.get(i));
            sib.childs.set(i - mid, cur.childs.get(i + 1));
            if (sib.childs.get(i - mid) != null) sib.childs.get(i - mid).parent = sib;
        }
        sib.count = cur.count - mid - 1;
        cur.count = mid;

        E median = cur.keys.get(mid);
        cur.keys.set(mid, null);

        if (k <= mid) putNode(cur, cl, nDes, k);
        else putNode(sib, cl, nDes, k - mid - 1);

        sib.childs.set(0, cur.childs.get(mid + 1));
        if (sib.childs.get(0) != null) sib.childs.get(0).parent = sib;
        nDes = sib; up = true;
        return median;
    }

    public boolean search(E cl) {
        return searchNode(root, cl);
    }

    private boolean searchNode(BNode<E> cur, E cl) {
        if (cur == null) return false;
        int[] pos = new int[1];
        if (cur.searchNode(cl, pos)) {
            System.out.println("Encontrado " + cl + " en nodo " + cur.idNode + " pos " + pos[0]);
            return true;
        }
        return searchNode(cur.childs.get(pos[0]), cl);
    }

    // ------------------ Métodos Remove ------------------

    public void remove(E cl) {
        if (root == null) { System.out.println("Árbol vacío"); return; }
        if (!removeNode(root, cl)) System.out.println("Clave no encontrada: " + cl);
        if (root.count == 0 && root.childs.get(0) != null) {
            root = root.childs.get(0); root.parent = null;
        }
    }

    private boolean removeNode(BNode<E> cur, E cl) {
        if (cur == null) return false;
        int[] pos = new int[1];
        if (cur.searchNode(cl, pos)) {
            if (cur.childs.get(0) == null) {
                deleteKey(cur, pos[0]);
                fix(cur);
                return true;
            } else {
                BNode<E> p = cur.childs.get(pos[0]);
                while (p.childs.get(p.count) != null) p = p.childs.get(p.count);
                E pk = p.keys.get(p.count - 1);
                cur.keys.set(pos[0], pk);
                return removeNode(p, pk);
            }
        }
        return removeNode(cur.childs.get(pos[0]), cl);
    }

    private void deleteKey(BNode<E> node, int p) {
        for (int i = p; i < node.count - 1; i++) {
            node.keys.set(i, node.keys.get(i + 1));
            node.childs.set(i + 1, node.childs.get(i + 2));
        }
        node.keys.set(node.count - 1, null);
        node.childs.set(node.count, null);
        node.count--;
    }

    private void fix(BNode<E> node) {
        if (node == root || node.count >= min()) return;
        BNode<E> p = node.parent;
        int idx;
        for (idx = 0; idx <= p.count && p.childs.get(idx) != node; idx++);
        BNode<E> left = (idx > 0 ? p.childs.get(idx - 1) : null);
        BNode<E> right = (idx < p.count ? p.childs.get(idx + 1) : null);

        if (left != null && left.count > min()) {
            deleteKey(left, left.count - 1);
            putNode(node, p.keys.get(idx - 1), left.childs.get(left.count), 0);
            p.keys.set(idx - 1, left.keys.get(left.count - 1));
        } else if (right != null && right.count > min()) {
            deleteKey(right, 0);
            putNode(node, p.keys.get(idx), right.childs.get(0), node.count);
            p.keys.set(idx, right.keys.get(0));
        } else if (left != null) merge(left, node, idx - 1);
        else if (right != null) merge(node, right, idx);
    }

    private void merge(BNode<E> l, BNode<E> r, int pIdx) {
        BNode<E> p = l.parent;
        putNode(l, p.keys.get(pIdx), r, l.count);
        deleteKey(l, l.count);  // remove first key from r
        deleteKey(p, pIdx);
        fix(p);
    }

    private int min() { return (orden - 1) / 2; }

    @Override
    public String toString() {
        return root == null ? "Árbol vacío" : traverse(root);
    }

    private String traverse(BNode<E> cur) {
        if (cur == null) return "";
        StringBuilder sb = new StringBuilder(cur.describeNode()).append("\n");
        for (int i = 0; i <= cur.count; i++) sb.append(traverse(cur.childs.get(i)));
        return sb.toString();
    }
}
