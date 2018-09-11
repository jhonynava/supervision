package mx.gob.seguropopulartlax.supervision.presentador;

import android.content.Context;

import java.util.ArrayList;

import mx.gob.seguropopulartlax.supervision.POJOs.VariablesPOJO;
import mx.gob.seguropopulartlax.supervision.db.ConstructorVariables;
import mx.gob.seguropopulartlax.supervision.vista.fragments.IRV_Vista_fragmentAfiliacionReafiliacion;

public class RV_Presentador_AfiliacionReafiliacion implements IRV_Presentador_AfiliacionReafiliacion{

    private IRV_Vista_fragmentAfiliacionReafiliacion irv_vista_fragmentAfiliacionReafiliacion;
    private Context context;
    private ConstructorVariables constructorVariables;
    private ArrayList<VariablesPOJO> variables;

    public RV_Presentador_AfiliacionReafiliacion(IRV_Vista_fragmentAfiliacionReafiliacion irv_vista_fragmentAfiliacionReafiliacion, Context context) {
        this.irv_vista_fragmentAfiliacionReafiliacion = irv_vista_fragmentAfiliacionReafiliacion;
        this.context = context;
        obtenerVariablesBD();
    }

    @Override
    public void obtenerVariablesBD() {
        constructorVariables = new ConstructorVariables(context);
        variables = constructorVariables.obtenerDatos();
        mostrarVariablesRV();
    }

    @Override
    public void mostrarVariablesRV() {
        irv_vista_fragmentAfiliacionReafiliacion.inicializarAdaptadorRV(irv_vista_fragmentAfiliacionReafiliacion.crearAdaptador(variables));
        irv_vista_fragmentAfiliacionReafiliacion.generarLinearLayoutVertical();
    }
}
