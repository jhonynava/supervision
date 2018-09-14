package mx.gob.seguropopulartlax.supervision.db;

import android.content.ContentValues;
import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import mx.gob.seguropopulartlax.supervision.ConnectionClass;
import mx.gob.seguropopulartlax.supervision.POJOs.VariablesPOJO;

public class ConstructorVariables {

    private Context context;
    private ConnectionClass conn = new ConnectionClass();
    private Connection conexion = null;
    private ResultSet rst;
    ArrayList<VariablesPOJO> variables = new ArrayList<>();
    ArrayList<String> variablesList;
    private RecyclerView listaVariables;
    VariablesPOJO variable = null;

    public ConstructorVariables(Context context) {
        this.context = context;
    }

    public ArrayList<VariablesPOJO> obtenerDatos(){
        BaseDatos db = new BaseDatos(context);
        return db.obtenerVariables(1);
        /*try {
            conexion = conn.conexionBD();
            if (conexion != null) {
                String query = "SELECT Id_rubro, Rubro, Variables FROM Desarrollo.dbo.cedula WHERE Id_rubro=1";
                PreparedStatement pst = conexion.prepareStatement(query);
                rst = pst.executeQuery();

                while (rst.next()) {
                    variable = new VariablesPOJO();
                    variable.setId_rubro(rst.getInt("Id_rubro"));
                    variable.setNombre_rubro(rst.getString("Rubro"));
                    variable.setVariable(rst.getString("Variables"));
                    insertarVariables(db,rst.getInt("Id_rubro"),rst.getString("Variables"));
                    variables.add(variable);
                }
            }
        } catch (Exception e) {

        }
        return variables;*/
    }

    public void insertarVariables(BaseDatos db, int id_rubro, String variable){
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConstantesBaseDatos.TABLE_ID_RUBRO, id_rubro);
        contentValues.put(ConstantesBaseDatos.TABLE_VARIABLE, variable);
        db.insertarValorCAT_Variable(contentValues);
    }

    public void insertarLike(VariablesPOJO variablesPOJO, int id_usuario, Boolean valor_like, int id_area_supervision, int id_vairable, int id_rubro){
        BaseDatos db = new BaseDatos(context);
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConstantesBaseDatos.TABLE_ID_USUARIO, id_usuario);
        contentValues.put(ConstantesBaseDatos.TABLE_VALOR, valor_like);
        contentValues.put(ConstantesBaseDatos.TABLE_ID_AREA_SUPERVISION, id_area_supervision);
        contentValues.put(ConstantesBaseDatos.TABLE_ID_VARIABLE, id_vairable);
        contentValues.put(ConstantesBaseDatos.TABLE_ID_RUBRO, variablesPOJO.getId_rubro());
        db.insertarValorLikes(contentValues);
    }

}
