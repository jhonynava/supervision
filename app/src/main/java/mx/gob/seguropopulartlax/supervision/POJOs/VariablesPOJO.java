package mx.gob.seguropopulartlax.supervision.POJOs;

public class VariablesPOJO {

    int id_rubro;
    String nombre_rubro;
    String variable;
    String respuesta_like;
    String respuesta_dislike;
    String comentario;
    int Foto;
    int img_like;

    public VariablesPOJO(int id_rubro, String nombre_rubro, String variable, String respuesta_like, String respuesta_dislike, String comentario, int foto, int img_like, int img_dislike, Boolean valor_like) {
        this.id_rubro = id_rubro;
        this.nombre_rubro = nombre_rubro;
        this.variable = variable;
        this.respuesta_like = respuesta_like;
        this.respuesta_dislike = respuesta_dislike;
        this.comentario = comentario;
        Foto = foto;
        this.img_like = img_like;
        this.img_dislike = img_dislike;
        this.valor_like = valor_like;
    }

    public Boolean getValor_like() {
        return valor_like;
    }

    public void setValor_like(Boolean valor_like) {
        this.valor_like = valor_like;
    }

    int img_dislike;
    Boolean valor_like;

    public int getFoto() {
        return Foto;
    }

    public int getImg_like() {
        return img_like;
    }

    public void setImg_like(int img_like) {
        this.img_like = img_like;
    }

    public int getImg_dislike() {
        return img_dislike;
    }

    public void setImg_dislike(int img_dislike) {
        this.img_dislike = img_dislike;
    }

    public void setFoto(int foto) {
        Foto = foto;
    }

    public VariablesPOJO() {

    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
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