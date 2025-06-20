public class BTree<E extends Comparable<E>> {
    private BNode<E> root;
    private int orden;
    private boolean up;
    private BNode<E> nDes;

    public BTree(int orden) {
        this.orden = orden;
        this.root = null;
    }

    public boolean isEmpty() {
        return this.root == null;
    }

    public void insert(E cl) {
        up = false;
        E mediana;
        BNode<E> pnew;
        mediana = push(this.root, cl);
        if (up) {
            pnew = new BNode<>(this.orden);
            pnew.count = 1;
            pnew.keys.set(0, mediana);
            pnew.childs.set(0, this.root);
            pnew.childs.set(1, nDes);
            if (this.root != null) this.root.parent = pnew;
            if (nDes != null) nDes.parent = pnew;
            this.root = pnew;
        }
    }

    private E push(BNode<E> current, E cl) {
        int[] pos = new int[1];
        E mediana;

        if (current == null) {
            up = true;
            nDes = null;
            return cl;
        } else {
            boolean found = current.searchNode(cl, pos);
            if (found) {
                System.out.println("Item duplicado");
                up = false;
                return null;
            }

            mediana = push(current.childs.get(pos[0]), cl);

            if (up) {
                if (current.nodeFull(this.orden - 1)) {
                    mediana = dividedNode(current, mediana, pos[0]);
                } else {
                    putNode(current, mediana, nDes, pos[0]);
                    up = false;
                }
            }

            return mediana;
        }
    }

    private void putNode(BNode<E> current, E cl, BNode<E> rd, int k) {
        for (int i = current.count - 1; i >= k; i--) {
            current.keys.set(i + 1, current.keys.get(i));
            current.childs.set(i + 2, current.childs.get(i + 1));
        }
        current.keys.set(k, cl);
        current.childs.set(k + 1, rd);
        if (rd != null) rd.parent = current;
        current.count++;
    }

    private E dividedNode(BNode<E> current, E cl, int k) {
        BNode<E> rd = nDes;
        int posMdna = (this.orden - 1) / 2;
        BNode<E> temp = new BNode<>(this.orden);
        temp.parent = current.parent;

        for (int i = posMdna + 1; i < this.orden - 1; i++) {
            temp.keys.set(i - posMdna - 1, current.keys.get(i));
            temp.childs.set(i - posMdna, current.childs.get(i + 1));
            if (current.childs.get(i + 1) != null) {
                current.childs.get(i + 1).parent = temp;
            }
        }

        temp.count = (this.orden - 1) - posMdna - 1;

        if (k <= posMdna) {
            putNode(current, cl, rd, k);
        } else {
            putNode(temp, cl, rd, k - posMdna - 1);
        }

        E median = current.keys.get(posMdna);
        temp.childs.set(0, current.childs.get(posMdna + 1));
        if (temp.childs.get(0) != null) temp.childs.get(0).parent = temp;

        current.count = posMdna;
        nDes = temp;
        up = true;
        return median;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "BTree is empty...";
        } else {
            return writeTree(this.root, 0);
        }
    }

    private String writeTree(BNode<E> current, int level) {
        StringBuilder sb = new StringBuilder();
        if (current != null) {
            sb.append(current.describeNode());
            for (int i = 0; i <= current.count; i++) {
                sb.append(writeTree(current.childs.get(i), level + 1));
            }
        }
        return sb.toString();
    }
}
