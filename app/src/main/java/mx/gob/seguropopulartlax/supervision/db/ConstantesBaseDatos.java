package mx.gob.seguropopulartlax.supervision.db;

public final class ConstantesBaseDatos {

    public static final String DATABASE_NAME = "supervision";
    public static final int DATABASE_VERSION = 1;

    public static final String NAME_TABLE_USUARIO = "Usuario";
    public static final String NAME_TABLE_AREA_SUPERVISION = "Area_Supervision";
    public static final String NAME_TABLE_RUBRO = "Rubro";
    public static final String NAME_TABLE_VARIABLE = "Variable";
    public static final String NAME_TABLE_OBSERVACION = "Observacion";
    public static final String NAME_TABLE_LIKES = "Likes";

    // Tabla Usuario
    public static final String TABLE_ID_USUARIO = "id_usuario";
    public static final String TABLE_CURP = "curp";
    public static final String TABLE_PASSWORD = "pass";
    public static final String TABLE_NOMBRE = "nombre";
    public static final String TABLE_APATERNO = "apellido_paterno";
    public static final String TABLE_AMATERNO = "apellido_materno";

    //Tabla Area Supervision
    public static final String TABLE_ID_AREA_SUPERVISION = "id_area_supervision";
    public static final String TABLE_MODULO = "modulo";
    public static final String TABLE_ENCARGADO = "encargado";
    public static final String TABLE_FECHA = "fecha";

    //Tabla Rubro
    public static final String TABLE_ID_RUBRO = "id_rubro";
    public static final String TABLE_RUBRO = "rubro";

    //Tabla Variable
    public static final String TABLE_ID_VARIABLE = "id_variable";
    public static final String TABLE_VARIABLE = "variable";

    //Tabla Observacion
    public static final String TABLE_ID_OBSERVACION = "id_observacion";
    public static final String TABLE_OBSERVACION = "observacion";

    //Tabla Like y Dislike
    public static final String TABLE_ID_LIKES = "id_likes";
    public static final String TABLE_VALOR = "valor";
    public static final String TABLE_POSICION =  "posicion";

    // ----- Datos internos de la aplicación ------//
    public static final String NAME_TABLE_MAO = "Mao";
    public static final String NAME_TABLE_USUARIOS = "Usarios";
    public static final String NAME_TABLE_CEDULA = "Cedula";


    //Tabla MAO
    public static final String TABLE_ID_MAO  = "id_mao";
    public static final String TABLE_MAO  = "mao";
    public static final String TABLE_ALIAS = "alias";
    public static final String TABLE_ACTIVO = "activo";

    //Tabla Usuarios
    public static final String TABLE_SUPERVISOR = "supervisor";
    public static final String TABLE_ID_SUPER = "id_super";

    //Tabla Cedula
}
