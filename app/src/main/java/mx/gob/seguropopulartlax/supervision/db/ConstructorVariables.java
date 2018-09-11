package mx.gob.seguropopulartlax.supervision.db;

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
        try {
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
                    variables.add(variable);
                }
            }
        } catch (Exception e) {

        }
        return variables;
    }
}
