package mx.gob.seguropopulartlax.supervision.db;

public final class ConstantesBaseDatos {

    public static final String DATABASE_NAME = "supervision";
    public static final int DATABASE_VERSION = 1;

    public static final String NAME_TABLE_USUARIO = "Usuario";
    public static final String NAME_TABLE_AREA_SUPERVISION = "Area_Supervision";
    public static final String NAME_TABLE_OBSERVACION = "Observacion";
    public static final String NAME_TABLE_LIKES = "Likes";
    public static final String NAME_TABLE_LISTA_SUPERVISIONES = "Lista_supervisiones";

    // Tabla Lista Supervisiones
    public static final String TABLE_ID_LISTA_SUPERVISION = "id_lista_supervision";
    public static final String TABLE_NO_POLIZA = "no_poliza";
    public static final String TABLE_TITULAR_POLIZA = "titular_poliza";
    public static final String TABLE_REALIZA_POLIZA = "realiza_poliza";

    // Tabla Usuarios
    public static final String TABLE_ID_USUARIO = "id_usuario";
    public static final String TABLE_CURP = "curp";
    public static final String TABLE_PASSWORD = "pass";
    public static final String TABLE_NOMBRE = "nombre";
    public static final String TABLE_APATERNO = "apellido_paterno";
    public static final String TABLE_AMATERNO = "apellido_materno";
    public static final String TABLE_SUPERVISOR = "supervisor";
    public static final String TABLE_ID_SUPERVISOR = "id_supervisor";

    //Tabla Area Supervision
    public static final String TABLE_ID_AREA_SUPERVISION = "id_area_supervision";
    public static final String TABLE_MODULO = "modulo";
    public static final String TABLE_ENCARGADO = "encargado";
    public static final String TABLE_FECHA = "fecha";

    //Tabla Observacion
    public static final String TABLE_ID_OBSERVACION = "id_observacion";
    public static final String TABLE_OBSERVACION = "observacion";

    //Tabla Like y Dislike
    public static final String TABLE_ID_LIKES = "id_likes";
    public static final String TABLE_VALOR = "valor";

    // ----- Datos internos de la aplicación ------//
    public static final String NAME_TABLE_MAO = "CAT_Mao";
    public static final String NAME_TABLE_USUARIOS = "CAT_Usarios";
    public static final String NAME_TABLE_RUBRO = "CAT_Rubro";
    public static final String NAME_TABLE_VARIABLE = "CAT_Variable";

    //Tabla MAO
    public static final String TABLE_ID_MAO  = "id_mao";
    public static final String TABLE_MAO  = "mao";
    public static final String TABLE_ALIAS = "alias";
    public static final String TABLE_ACTIVO = "activo";

    //Tabla Rubro
    public static final String TABLE_ID_RUBRO = "id_rubro";
    public static final String TABLE_RUBRO = "rubro";

    //Tabla Variable
    public static final String TABLE_ID_VARIABLE = "id_variable";
    public static final String TABLE_VARIABLE = "variable";

}
