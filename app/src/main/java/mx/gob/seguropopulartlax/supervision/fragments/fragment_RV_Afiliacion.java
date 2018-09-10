package mx.gob.seguropopulartlax.supervision.fragments;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import mx.gob.seguropopulartlax.supervision.ConnectionClass;
import mx.gob.seguropopulartlax.supervision.R;
import mx.gob.seguropopulartlax.supervision.POJOs.VariablesPOJO;
import mx.gob.seguropopulartlax.supervision.adaptadores.AfiliciacionReafiliacionAdaptador;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_RV_Afiliacion extends android.support.v4.app.Fragment {

    ConnectionClass conn = new ConnectionClass();
    Connection conexion = null;
    ArrayList<VariablesPOJO> variables = new ArrayList<>();
    ArrayList<String> variablesList;
    private RecyclerView listaVariables;
    public AfiliciacionReafiliacionAdaptador adaptador;
    ResultSet rst;
    VariablesPOJO variable = null;
    private ProgressDialog proceso;
    private cargarDatosTask mAuthTask;
    Button btn_siguiente;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.getActivity().setTitle(getResources().getString(R.string.rubro1));

        proceso = new ProgressDialog(getContext());

        View v = inflater.inflate(R.layout.fragment_rv_afilicacion, container, false);
        listaVariables = v.findViewById(R.id.rvVariablesAfiliacion);
        btn_siguiente = v.findViewById(R.id.btn_siguente_fragment);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        listaVariables.setLayoutManager(llm);
        listaVariables.setHasFixedSize(true);
        listaVariables.setItemAnimator(new DefaultItemAnimator());
        cargarDatos();

        btn_siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cuadroDialogo();
            }
        });

        return v;
    }

    private void cuadroDialogo() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getActivity());

        alertDialogBuilder.setMessage("Revisa que haz contestado todas las preguntas.")
                .setCancelable(false)
                .setPositiveButton("Listo", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Fragment fragment = new RV_Expedientes();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main,fragment).commit();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void inicializarAdaptador() {
        adaptador = new AfiliciacionReafiliacionAdaptador(variables, getActivity());
        listaVariables.setAdapter(adaptador);
    }

    public class cargarDatosTask extends AsyncTask<Void, Void, Boolean>{

        public cargarDatosTask() {
            super();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
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

                    variablesList = new ArrayList<>();

                    for (int i = 0; i < variables.size(); i++) {
                        variablesList.add(variables.get(i).getVariable());
                    }
                }
            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            proceso.setMessage("Cargando...");
            proceso.setTitle("Obteniendo información");
            proceso.show();
            proceso.setCancelable(false);
            proceso.setCanceledOnTouchOutside(false);
        }
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            inicializarAdaptador();
            proceso.dismiss();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    private void cargarDatos() {
        mAuthTask = new cargarDatosTask();
        mAuthTask.execute((Void) null);
    }
}
