// RegistroEstudiante.java
public class RegistroEstudiante implements Comparable<RegistroEstudiante> {
    int codigo;
    String nombre;

    public RegistroEstudiante(int codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
    }

    @Override
    public int compareTo(RegistroEstudiante o) {
        return Integer.compare(this.codigo, o.codigo);
    }

    @Override
    public String toString() {
        return "(" + codigo + ", \"" + nombre + "\")";
    }
}
