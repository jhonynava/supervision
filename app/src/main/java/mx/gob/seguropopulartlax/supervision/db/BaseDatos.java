package mx.gob.seguropopulartlax.supervision.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import mx.gob.seguropopulartlax.supervision.POJOs.VariablesPOJO;

public class BaseDatos extends SQLiteOpenHelper {

    private Context context;

    public BaseDatos(Context context) {
        super(context, ConstantesBaseDatos.DATABASE_NAME, null, ConstantesBaseDatos.DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String queryCrearTablaListaSupervisiones = "CREATE TABLE " + ConstantesBaseDatos.NAME_TABLE_LISTA_SUPERVISIONES + "(" +
                ConstantesBaseDatos.TABLE_ID_LISTA_SUPERVISION +  " INTEGER, " +
                ConstantesBaseDatos.TABLE_ID_AREA_SUPERVISION +  
                ConstantesBaseDatos.TABLE_ID_USUARIO +
                ConstantesBaseDatos.TABLE_SUPERVISOR +
                ConstantesBaseDatos.TABLE_NO_POLIZA +
                ConstantesBaseDatos.TABLE_TITULAR_POLIZA +
                ConstantesBaseDatos.TABLE_REALIZA_POLIZA +
                ")";

        String queryCrearTablaAreaSupervision = "CREATE TABLE " + ConstantesBaseDatos.NAME_TABLE_AREA_SUPERVISION + "(" +
                ConstantesBaseDatos.TABLE_ID_AREA_SUPERVISION + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ConstantesBaseDatos.TABLE_ID_USUARIO + " INTEGER, " +
                ConstantesBaseDatos.TABLE_MODULO + " VARCHAR(50), " +
                ConstantesBaseDatos.TABLE_ENCARGADO + " VARCHAR(150), " +
                ConstantesBaseDatos.TABLE_FECHA + " DATE, " +
                "FOREIGN KEY (" + ConstantesBaseDatos.TABLE_ID_USUARIO + ") " +
                "REFERENCES " + ConstantesBaseDatos.NAME_TABLE_USUARIO + "(" + ConstantesBaseDatos.TABLE_ID_USUARIO + ")" +
                ")";
        db.execSQL(queryCrearTablaAreaSupervision);

        String queryCrearTablaObservacion = "CREATE TABLE " + ConstantesBaseDatos.NAME_TABLE_OBSERVACION + "(" +
                ConstantesBaseDatos.TABLE_ID_OBSERVACION + " INTEGER PRIMARY KEY, " +
                ConstantesBaseDatos.TABLE_ID_USUARIO + " INTEGER, " +
                ConstantesBaseDatos.TABLE_OBSERVACION + " VARCHAR(250), " +
                ConstantesBaseDatos.TABLE_ID_AREA_SUPERVISION + " INTEGER, " +
                ConstantesBaseDatos.TABLE_ID_RUBRO + " INTEGER, " +
                ConstantesBaseDatos.TABLE_ID_VARIABLE + " INTEGER, " +
                "FOREIGN KEY (" + ConstantesBaseDatos.TABLE_ID_USUARIO + ") " +
                "REFERENCES " + ConstantesBaseDatos.NAME_TABLE_USUARIO + "(" + ConstantesBaseDatos.TABLE_ID_USUARIO + ")" +
                ")";
        db.execSQL(queryCrearTablaObservacion);

        String queryCrearTablaLikes = "CREATE TABLE " + ConstantesBaseDatos.NAME_TABLE_LIKES + "(" +
                ConstantesBaseDatos.TABLE_ID_LIKES + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ConstantesBaseDatos.TABLE_ID_USUARIO + " INTEGER, " +
                ConstantesBaseDatos.TABLE_VALOR + " BOOLEAN, " +
                ConstantesBaseDatos.TABLE_ID_AREA_SUPERVISION + " INTEGER, " +
                ConstantesBaseDatos.TABLE_ID_VARIABLE + " INTEGER, " +
                ConstantesBaseDatos.TABLE_ID_RUBRO + " INTEGER, " +
                "FOREIGN KEY (" + ConstantesBaseDatos.TABLE_ID_USUARIO + ") " +
                "REFERENCES " + ConstantesBaseDatos.NAME_TABLE_USUARIO + "(" + ConstantesBaseDatos.TABLE_ID_USUARIO + ")" +
                ")";
        db.execSQL(queryCrearTablaLikes);

        String queryCrearTablaCAT_Usuarios = "CREATE TABLE " + ConstantesBaseDatos.NAME_TABLE_USUARIO + "(" +
                ConstantesBaseDatos.TABLE_ID_USUARIO + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ConstantesBaseDatos.TABLE_CURP + " VARCHAR(18), " +
                ConstantesBaseDatos.TABLE_PASSWORD + " VARCHAR(20), " +
                ConstantesBaseDatos.TABLE_NOMBRE + " VARCHAR(50), " +
                ConstantesBaseDatos.TABLE_APATERNO + " VARCHAR(50), " +
                ConstantesBaseDatos.TABLE_AMATERNO + " VARCHAR(50), " +
                ConstantesBaseDatos.TABLE_SUPERVISOR + " BOOLEAN, " +
                ConstantesBaseDatos.TABLE_ID_SUPERVISOR + " SMALLINT" +
        ")";
        db.execSQL(queryCrearTablaCAT_Usuarios);

        String queryCrearTablaCAT_MAO = "CREATE TABLE " + ConstantesBaseDatos.NAME_TABLE_MAO + "(" +
                ConstantesBaseDatos.TABLE_ID_MAO + " INTEGER PRIMARY KEY, " +
                ConstantesBaseDatos.TABLE_MAO + " VARCHAR(100), " +
                ConstantesBaseDatos.TABLE_ALIAS + " VARCHAR(30), " +
                ConstantesBaseDatos.TABLE_ACTIVO + " BOOLEAN" +
                ")";
        db.execSQL(queryCrearTablaCAT_MAO);


        String queryCrearTablaCAT_Rubro = "CREATE TABLE " + ConstantesBaseDatos.NAME_TABLE_RUBRO + "(" +
                ConstantesBaseDatos.TABLE_ID_RUBRO + " INTEGER PRIMARY KEY, " +
                ConstantesBaseDatos.TABLE_RUBRO + " VARCHAR(300)" +
                ")";
        db.execSQL(queryCrearTablaCAT_Rubro);

        String queryCrearTablaCAT_Variable = "CREATE TABLE " + ConstantesBaseDatos.NAME_TABLE_VARIABLE + "(" +
                ConstantesBaseDatos.TABLE_ID_VARIABLE + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ConstantesBaseDatos.TABLE_ID_RUBRO + " INTEGER, " +
                ConstantesBaseDatos.TABLE_VARIABLE + " VARCHAR(290), " +
                "FOREIGN KEY (" + ConstantesBaseDatos.TABLE_ID_RUBRO + ") " +
                "REFERENCES " + ConstantesBaseDatos.NAME_TABLE_RUBRO + "(" + ConstantesBaseDatos.TABLE_ID_RUBRO + ")" +
                ")";
        db.execSQL(queryCrearTablaCAT_Variable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST " + ConstantesBaseDatos.NAME_TABLE_USUARIO);
        db.execSQL("DROP TABLE IF EXIST " + ConstantesBaseDatos.NAME_TABLE_AREA_SUPERVISION);
        db.execSQL("DROP TABLE IF EXIST " + ConstantesBaseDatos.NAME_TABLE_RUBRO);
        db.execSQL("DROP TABLE IF EXIST " + ConstantesBaseDatos.NAME_TABLE_VARIABLE);
        db.execSQL("DROP TABLE IF EXIST " + ConstantesBaseDatos.NAME_TABLE_OBSERVACION);
        db.execSQL("DROP TABLE IF EXIST " + ConstantesBaseDatos.NAME_TABLE_LIKES);
    }

    public void insertarValorUsuario(ContentValues contentValues) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(ConstantesBaseDatos.NAME_TABLE_USUARIO, null, contentValues);
        db.close();
    }

    public void insertarValorAreaSupervision(ContentValues contentValues) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(ConstantesBaseDatos.NAME_TABLE_AREA_SUPERVISION, null, contentValues);
        db.close();
    }

    public void insertarValorObservacion(ContentValues contentValues) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(ConstantesBaseDatos.NAME_TABLE_OBSERVACION, null, contentValues);
        db.close();
    }

    public void insertarValorLikes(ContentValues contentValues) {
         SQLiteDatabase db = this.getWritableDatabase();
        db.insert(ConstantesBaseDatos.NAME_TABLE_LIKES, null, contentValues);
        db.close();
    }

    public void insertarValorCAT_Mao(ContentValues contentValues) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(ConstantesBaseDatos.NAME_TABLE_MAO, null, contentValues);
        db.close();
    }

    public void insertarValorCAT_Usuarios(ContentValues contentValues) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(ConstantesBaseDatos.NAME_TABLE_USUARIOS, null, contentValues);
        db.close();
    }

    public void insertarValorCAT_Rubro(ContentValues contentValues) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(ConstantesBaseDatos.NAME_TABLE_RUBRO, null, contentValues);
        db.close();
    }

    public void insertarValorCAT_Variable(ContentValues contentValues) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(ConstantesBaseDatos.NAME_TABLE_VARIABLE, null, contentValues);
        db.close();
    }

    public ArrayList<VariablesPOJO> obtenerVariables(int rubro) {
        ArrayList<VariablesPOJO> variablesPOJO = new ArrayList<>();

        String query;
        query = "SELECT " + ConstantesBaseDatos.TABLE_VARIABLE +
                " FROM " + ConstantesBaseDatos.NAME_TABLE_VARIABLE +
                " WHERE " + ConstantesBaseDatos.TABLE_ID_RUBRO + " = " + rubro;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor registros = db.rawQuery(query, null);

        while (registros.moveToNext()) {
            VariablesPOJO variableActual = new VariablesPOJO();
            variableActual.setVariable(registros.getString(0));

            variablesPOJO.add(variableActual);
        }
        db.close();

        return variablesPOJO;
    }
}