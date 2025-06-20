public class Main {
    public static void main(String[] args) {
        BTree<Integer> tree = new BTree<>(5); // Orden del BTree

        int[] values = {100, 50, 20, 70, 10, 30, 80, 90, 200, 25, 15, 5, 65, 35, 60, 18, 93, 94};

        for (int value : values) {
            tree.insert(value);
        }

        System.out.println("ID Nodo | Claves Nodo | ID Padre | IDs Hijos");
        System.out.println("--------------------------------------------------");
        System.out.println(tree);
    }
}
