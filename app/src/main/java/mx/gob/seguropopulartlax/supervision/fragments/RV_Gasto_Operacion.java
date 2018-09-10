package mx.gob.seguropopulartlax.supervision.fragments;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import mx.gob.seguropopulartlax.supervision.adaptadores.GastoOperacionAdaptador;
import mx.gob.seguropopulartlax.supervision.menu_principal;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class RV_Gasto_Operacion extends android.support.v4.app.Fragment {

    ConnectionClass conn = new ConnectionClass();
    Connection conexion = null;
    ArrayList<VariablesPOJO> variables = new ArrayList<>();
    ArrayList<String> variablesList;
    private RecyclerView listaVariables;
    public GastoOperacionAdaptador adaptador;
    ResultSet rst;
    VariablesPOJO variable = null;
    private ProgressDialog proceso;
    private cargarDatosTask mAuthTask;
    Button button;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static final String PREF_FILE_NAME = "preferencia";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        sharedPreferences = this.getActivity().getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();

        this.getActivity().setTitle(getResources().getString(R.string.rubro4));

        proceso = new ProgressDialog(getContext());

        View v = inflater.inflate(R.layout.fragment_rv__gasto__operacion, container, false);
        listaVariables = v.findViewById(R.id.rvVariablesGastoOperacion);
        button = v.findViewById(R.id.btn_siguente_fragment);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        listaVariables.setLayoutManager(llm);
        listaVariables.setItemAnimator(new DefaultItemAnimator());
        cargarDatos();

        button.setOnClickListener(new View.OnClickListener() {
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
                        editor.putBoolean("fase3", true);
                        editor.commit();
                        cuadroFinzalizar();
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

    private void cuadroFinzalizar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        final LayoutInflater inflater = this.getActivity().getLayoutInflater();
        final View dialogLayout = inflater.inflate(R.layout.cuadrodialogo_first, null);
        builder.setView(dialogLayout).setCancelable(false);
        final AlertDialog show = builder.show();

        Button btn_MenuPrincipal = dialogLayout.findViewById(R.id.btn_regresar_al_menu);
        Button btn_HacerVisita = dialogLayout.findViewById(R.id.btn_hacer_Visita);

        btn_MenuPrincipal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show.dismiss();
                Fragment fragment = new menu_principal();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main,fragment).commit();
            }
        });

        btn_HacerVisita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show.dismiss();
                Fragment fragment = new RV_VDomiciliaria();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main,fragment).commit();
            }
        });
    }

    private void cargarDatos() {
        mAuthTask = new cargarDatosTask();
        mAuthTask.execute((Void) null);
    }

    public class cargarDatosTask extends AsyncTask<Void, Void, Boolean> {

        public cargarDatosTask() {
            super();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                conexion = conn.conexionBD();
                if (conexion != null) {
                    String query = "SELECT Id_rubro, Rubro, Variables FROM Desarrollo.dbo.cedula WHERE Id_rubro=4";
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
    }

    private void inicializarAdaptador() {
        adaptador = new GastoOperacionAdaptador(variables, getActivity());
        listaVariables.setAdapter(adaptador);
    }

}
