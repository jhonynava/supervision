package mx.gob.seguropopulartlax.supervision.vista.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import mx.gob.seguropopulartlax.supervision.ConnectionClass;
import mx.gob.seguropopulartlax.supervision.R;
import mx.gob.seguropopulartlax.supervision.adaptadores.VisitaDomicialiariaAdaptador;
import mx.gob.seguropopulartlax.supervision.informe_hallazgos;
import mx.gob.seguropopulartlax.supervision.POJOs.variablesUsuarioPOJO;

/**
 * A simple {@link Fragment} subclass.
 */
public class RV_VDomiciliaria extends android.support.v4.app.Fragment {

    ConnectionClass conn = new ConnectionClass();
    Connection conexion = null;
    ArrayList<variablesUsuarioPOJO> variables = new ArrayList<>();
    ArrayList<String> variablesList;
    private RecyclerView listaVariables;
    ResultSet rst;
    variablesUsuarioPOJO variable = null;
    private ProgressDialog proceso;
    public VisitaDomicialiariaAdaptador adaptador;
    private cargarDatosTask mAuthTask;
    Button button;
    private String nombre_usuario = "";
    private String aPaterno_usuario = "";
    private String aMaterno_usuario = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceStte) {

        proceso = new ProgressDialog(getContext());

        View v = inflater.inflate(R.layout.fragment_rv__vdomiciliaria, container, false);
        listaVariables = v.findViewById(R.id.rvVariablesVDomiciliaria);
        button = v.findViewById(R.id.btn_siguente_fragment);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        listaVariables.setLayoutManager(llm);
        listaVariables.setItemAnimator(new DefaultItemAnimator());

        cargarDatos();

        buscarPoliza();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cuadroDialogo();
            }
        });
        return v;
    }

    private void buscarPoliza() {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this.getActivity());
        final LayoutInflater inflater = this.getActivity().getLayoutInflater();
        final View dialogLayout = inflater.inflate(R.layout.poliza, null);
        builder.setView(dialogLayout).setCancelable(true);
        final android.app.AlertDialog show = builder.show();

        final EditText observacion_vaiable = dialogLayout.findViewById(R.id.edt_folio);
        Button btn_buscar_poliza = dialogLayout.findViewById(R.id.buscar_folio);

        btn_buscar_poliza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                final String folio_poliza = observacion_vaiable.getText().toString();
                View focusView = null;

                if (TextUtils.isEmpty(folio_poliza)) {
                    observacion_vaiable.setError("Ingrese el folio");
                    focusView = observacion_vaiable;
                } else {
                    try {
                        conexion = conn.conexionBDPoliza();
                        if (conexion != null) {
                            String query = "SELECT A.FOLIO_FAMILIA, B.APELLIDO_PATERNO, B.APELLIDO_MATERNO, B.NOMBRE \n" +
                                    "FROM FAMILIAS A JOIN INTEGRANTES B ON B.ID_ESTADO = A.ID_ESTADO AND B.ANIO_INSCRIPCION = A.ANIO_INSCRIPCION AND B.ID_FAMILIA = A.ID_FAMILIA \n" +
                                    "WHERE B.ESTATUS= 1 AND B.ID_PARENTESCO = 1 AND A.FOLIO_FAMILIA = ? ";
                            PreparedStatement pst = conexion.prepareStatement(query);
                            pst.setString(1, folio_poliza.trim());
                            rst = pst.executeQuery();

                            if (rst.next()) {
                                nombre_usuario = rst.getString("NOMBRE");
                                aPaterno_usuario = rst.getString("APELLIDO_PATERNO");
                                aMaterno_usuario = rst.getString("APELLIDO_MATERNO");
                                Toast.makeText(getActivity(), nombre_usuario + " " + aPaterno_usuario + " " + aMaterno_usuario, Toast.LENGTH_SHORT).show();

                                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(getActivity());

                                alertDialogBuilder.setTitle(nombre_usuario + " " + aPaterno_usuario + " " + aMaterno_usuario);
                                alertDialogBuilder.setMessage("¿El nombre del titular es correcto?")
                                        .setCancelable(false)
                                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                variables.get(1).setPoliza(folio_poliza);
                                                final String poliza = variable.getPoliza();

                                                final variablesUsuarioPOJO variabl = variables.get(0);
                                                variabl.setNombre(nombre_usuario + " " + aPaterno_usuario + " " + aMaterno_usuario);
                                                final String nombre_completo = variabl.getNombre();
                                                dialog.cancel();
                                                show.dismiss();

                                            }
                                        })
                                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                                observacion_vaiable.setText(null);
                                            }
                                        });
                                android.app.AlertDialog alert = alertDialogBuilder.create();
                                alert.show();
                            } else {
                                Toast.makeText(getActivity(), "El número de poliza es incorrecto", Toast.LENGTH_SHORT).show();
                            }

                        }
                        else {
                            Toast.makeText(getActivity(), "Error en la conexion", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {

                    }
                }

            }
        });
    }

    private void cuadroDialogo() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getActivity());

        alertDialogBuilder.setMessage("Revisa que haz contestado todas las preguntas.")
                .setCancelable(false)
                .setPositiveButton("Listo", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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
        final View dialogLayout = inflater.inflate(R.layout.cuadrodialogo_second, null);
        builder.setView(dialogLayout).setCancelable(false);
        final AlertDialog show = builder.show();

        Button btn_HacerInforme = dialogLayout.findViewById(R.id.btn_realizar_informe);

        btn_HacerInforme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show.dismiss();
                Fragment fragment = new informe_hallazgos();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main,fragment).commit();
            }
        });
    }

    private void cargarDatos() {
        mAuthTask = new cargarDatosTask();
        mAuthTask.execute((Void) null);
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
                    String query = "SELECT Id_rubro, Rubro, Variables FROM Desarrollo.dbo.cedula WHERE Id_rubro=3";
                    PreparedStatement pst = conexion.prepareStatement(query);
                    rst = pst.executeQuery();

                    while (rst.next()) {
                        variable = new variablesUsuarioPOJO();
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
                conexion.close();
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
        adaptador = new VisitaDomicialiariaAdaptador(variables, getActivity());
        listaVariables.setAdapter(adaptador);
    }

}
