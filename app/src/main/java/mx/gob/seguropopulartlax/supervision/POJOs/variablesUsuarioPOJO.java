package mx.gob.seguropopulartlax.supervision.POJOs;

public class variablesUsuarioPOJO {
    int id_rubro;
    String nombre_rubro;
    String variable;
    String respuesta_like;
    String respuesta_dislike;
    String nombre;
    String poliza;
    int visible;
    String comentario;
    int foto;

    public variablesUsuarioPOJO(int id_rubro, String nombre_rubro, String variable, String respuesta_like, String respuesta_dislike, String nombre, String poliza, int visible, String comentario, int foto) {
        this.id_rubro = id_rubro;
        this.nombre_rubro = nombre_rubro;
        this.variable = variable;
        this.respuesta_like = respuesta_like;
        this.respuesta_dislike = respuesta_dislike;
        this.nombre = nombre;
        this.poliza = poliza;
        this.visible = visible;
        this.comentario = comentario;
        this.foto = foto;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public int getFoto() {
        return foto;
    }

    public void setFoto(int foto) {
        this.foto = foto;
    }

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }

    public String getPoliza() {
        return poliza;
    }

    public void setPoliza(String poliza) {
        this.poliza = poliza;
    }

    public variablesUsuarioPOJO() {

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRespuesta_like() {
        return respuesta_like;
    }

    public void setRespuesta_like(String respuesta_like) {
        this.respuesta_like = respuesta_like;
    }

    public String getRespuesta_dislike() {
        return respuesta_dislike;
    }

    public void setRespuesta_dislike(String respuesta_dislike) {
        this.respuesta_dislike = respuesta_dislike;
    }

    public int getId_rubro() {
        return id_rubro;
    }

    public void setId_rubro(int id_rubro) {
        this.id_rubro = id_rubro;
    }

    public String getNombre_rubro() {
        return nombre_rubro;
    }

    public void setNombre_rubro(String nombre_rubro) {
        this.nombre_rubro = nombre_rubro;
    }

    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }
}
