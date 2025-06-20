public class Main {
    public static void main(String[] args) {
        try {
            BTree<Integer> t = BTree.building_Btree("arbolB.txt");
            System.out.println("✔ Árbol leído y validado:\n" + t);
        } catch (ItemNoFoundException infe) {
            System.err.println("✘ Violación B‑Tree: " + infe.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
