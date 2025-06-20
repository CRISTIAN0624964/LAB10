public class Main {
    public static void main(String[] args) {
        System.out.println("=== Enteros ===");
        BTree<Integer> t = new BTree<>(4);
        int[] vals = {100,50,20,70,10,30,80,90,200,25,15,5,65,35,60,18,93,94};
        for (int v : vals) t.insert(v);
        System.out.println(t);

        t.search(25);
        t.search(999);

        System.out.println("--- Eliminar 25 y 20 ---");
        t.remove(25);
        t.remove(20);
        System.out.println(t);

        System.out.println("\n=== Estudiantes ===");
        BTree<RegistroEstudiante> est = new BTree<>(4);
        int[] codes = {103,110,101,120,115,125,140,108,132,128,145,122,108};
        String[] names = {"Ana","Luis","Carlos","Lucía","David","Jorge","Camila","Rosa","Ernesto","Denis","Enrique","Karina","Juan"};
        for (int i = 0; i < codes.length; i++)
            est.insert(new RegistroEstudiante(codes[i], names[i]));

        est.search(new RegistroEstudiante(115, ""));
        est.search(new RegistroEstudiante(132, ""));
        est.search(new RegistroEstudiante(999, ""));

        est.remove(new RegistroEstudiante(101, ""));
        est.insert(new RegistroEstudiante(106, "Sara"));
        est.search(new RegistroEstudiante(106, ""));
        System.out.println(est);
    }
}
