package mx.gob.seguropopulartlax.supervision.POJOs;

public class UsuarioPOJO {

    private String nombre;
    private String aPaterno;
    private String aMaterno;

    public UsuarioPOJO() {
    }

    public UsuarioPOJO(String aPaterno, String aMaterno, String nombre) {
        this.nombre = nombre;
        this.aPaterno = aPaterno;
        this.aMaterno = aMaterno;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getaPaterno() {
        return aPaterno;
    }

    public void setaPaterno(String aPaterno) {
        this.aPaterno = aPaterno;
    }

    public String getaMaterno() {
        return aMaterno;
    }

    public void setaMaterno(String aMaterno) {
        this.aMaterno = aMaterno;
    }

}
