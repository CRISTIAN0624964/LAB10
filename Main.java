public class Main {
    public static void main(String[] args) {
        BTree<Integer> tree = new BTree<>(5);
        int[] values = {100, 50, 20, 70, 10, 30, 80, 90, 200, 25, 15, 5, 65, 35, 60, 18, 93, 94};

        for (int v : values) tree.insert(v);

        System.out.println(tree);

        System.out.println("Buscar 999:");
        boolean found70 = tree.search(999); // false
        System.out.println("Resultado: " + found70);

        System.out.println("Buscar 555:");
        boolean found999 = tree.search(555); // false
        System.out.println("Resultado: " + found999);
    }
}
