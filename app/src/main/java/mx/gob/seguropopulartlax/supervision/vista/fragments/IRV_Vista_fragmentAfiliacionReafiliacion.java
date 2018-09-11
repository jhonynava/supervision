package mx.gob.seguropopulartlax.supervision.vista.fragments;

import java.util.ArrayList;

import mx.gob.seguropopulartlax.supervision.POJOs.VariablesPOJO;
import mx.gob.seguropopulartlax.supervision.adaptadores.AfiliciacionReafiliacionAdaptador;

public interface IRV_Vista_fragmentAfiliacionReafiliacion {

    public void generarLinearLayoutVertical();

    public AfiliciacionReafiliacionAdaptador crearAdaptador(ArrayList<VariablesPOJO> variables);

    public void inicializarAdaptadorRV(AfiliciacionReafiliacionAdaptador adaptador);
}
