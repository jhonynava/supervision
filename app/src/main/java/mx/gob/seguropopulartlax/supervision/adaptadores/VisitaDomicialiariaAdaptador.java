package mx.gob.seguropopulartlax.supervision.adaptadores;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import mx.gob.seguropopulartlax.supervision.ConnectionClass;
import mx.gob.seguropopulartlax.supervision.R;
import mx.gob.seguropopulartlax.supervision.POJOs.variablesUsuarioPOJO;

public class VisitaDomicialiariaAdaptador extends RecyclerView.Adapter<VisitaDomicialiariaAdaptador.VariablesViewHolder> {

    ArrayList<variablesUsuarioPOJO> variables;
    public Activity activity;
    ConnectionClass conn = new ConnectionClass();
    Connection conexion = null;
    ResultSet rst;

    private String nombre_usuario = "";
    private String aPaterno_usuario = "";
    private String aMaterno_usuario = "";

    public VisitaDomicialiariaAdaptador(ArrayList<variablesUsuarioPOJO> variables, Activity activity) {
        this.variables = variables;
        this.activity = activity;
    }

    @NonNull
    @Override
    public VisitaDomicialiariaAdaptador.VariablesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_variables_poliza, parent, false);
        return new VariablesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final VisitaDomicialiariaAdaptador.VariablesViewHolder variablesViewHolder, final int position) {
        final variablesUsuarioPOJO variable = variables.get(position);
        final String respuesta = variable.getVariable();
        final String respuesraLike = variable.getRespuesta_like();
        final String respuetsaDislike = variable.getRespuesta_dislike();
        final String poliza = variable.getPoliza();
        final String nombre_completo = variable.getNombre();
        final String comentario = variable.getComentario();
        final int foto = variable.getFoto();
        final int[] valor_respuesta = {0};

        variablesViewHolder.texto_like.setText(respuesraLike);

        variablesViewHolder.texto_dislike.setText(respuetsaDislike);
        variablesViewHolder.texto_variables.setText(respuesta);
        variablesViewHolder.texto_poliza.setText(poliza);
        variablesViewHolder.texto_nombre.setText(nombre_completo);
        variablesViewHolder.img_comentario.setImageResource(R.drawable.icons8_speech_bubble_64);

        variablesViewHolder.texto_variables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                final LayoutInflater inflater = activity.getLayoutInflater();
                final View dialogLayout = inflater.inflate(R.layout.poliza, null);
                builder.setView(dialogLayout);
                final AlertDialog show = builder.show();

                final EditText observacion_vaiable = dialogLayout.findViewById(R.id.edt_folio);
                Button btn_guardar_observacion = dialogLayout.findViewById(R.id.buscar_folio);

                btn_guardar_observacion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
                                        Toast.makeText(activity, nombre_usuario + " " + aPaterno_usuario + " " + aMaterno_usuario, Toast.LENGTH_SHORT).show();

                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);

                                        alertDialogBuilder.setTitle(nombre_usuario + " " + aPaterno_usuario + " " + aMaterno_usuario);
                                        alertDialogBuilder.setMessage("¿El no" +
                                                "mbre del titular es correcto?")
                                                .setCancelable(false)
                                                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        variables.get(1).setPoliza(folio_poliza);
                                                        final String poliza = variable.getPoliza();
                                                        variablesViewHolder.texto_poliza.setText(poliza);
                                                        //variablesViewHolder.texto_poliza.setVisibility(View.VISIBLE);

                                                        final variablesUsuarioPOJO variabl = variables.get(0);
                                                        variabl.setNombre(nombre_usuario + " " + aPaterno_usuario + " " + aMaterno_usuario);
                                                        final String nombre_completo = variabl.getNombre();
                                                        variablesViewHolder.texto_nombre.setText(nombre_completo);
                                                        //variablesViewHolder.texto_nombre.setVisibility(View.VISIBLE);
                                                        variablesViewHolder.texto_variables.setEnabled(false);
                                                        notifyDataSetChanged();
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
                                        AlertDialog alert = alertDialogBuilder.create();
                                        alert.show();
                                    } else {
                                        Toast.makeText(activity, "El número de poliza es incorrecto", Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    Toast.makeText(activity, "Error en la conexion", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {

                            }
                        }

                    }
                });
            }
        });

        variablesViewHolder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String comentario = variable.getComentario();

                String valor1 = variablesViewHolder.texto_like.getText().toString();
                String valor2 = variablesViewHolder.texto_dislike.getText().toString();


                if (valor2.equals("1")) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);

                    alertDialogBuilder.setMessage("¿Deseas cambia la respuesta?")
                            .setCancelable(false)
                            .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    variable.setRespuesta_dislike(null);
                                    variablesViewHolder.texto_dislike.setText(null);
                                    variable.setRespuesta_like("1");
                                    final String respuestaLike = variable.getRespuesta_like();
                                    variablesViewHolder.texto_like.setText(respuestaLike);

                                    if (comentario == null) {
                                        Toast.makeText(activity, "Respuesta cambiada", Toast.LENGTH_SHORT).show();
                                    } else if (comentario.isEmpty()) {
                                        Toast.makeText(activity, "Respuesta cambiada", Toast.LENGTH_SHORT).show();
                                    } else {
                                        variable.setComentario("");
                                        Toast.makeText(activity, "La observación anterior ha sido eliminada", Toast.LENGTH_SHORT).show();
                                    }
                                    dialog.cancel();
                                    valor_respuesta[0] = 1;
                                }
                            })
                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = alertDialogBuilder.create();
                    alert.show();
                } else if (valor1.equals("1")) {
                    Toast.makeText(activity, "Ya haz seleccionado la respuesta", Toast.LENGTH_SHORT).show();
                } else if (valor1.isEmpty()) {
                    variable.setRespuesta_like("1");
                    final String respuestaLike = variable.getRespuesta_like();
                    variablesViewHolder.texto_like.setText(respuestaLike);
                    valor_respuesta[0] = 1;
                }
            }
        });

        variablesViewHolder.dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String comentario = variable.getComentario();
                String valor1 = variablesViewHolder.texto_like.getText().toString();
                String valor2 = variablesViewHolder.texto_dislike.getText().toString();
                if (valor1.equals("1")) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);

                    alertDialogBuilder.setMessage("¿Deseas cambia la respuesta?")
                            .setCancelable(false)
                            .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    variable.setRespuesta_like(null);
                                    variablesViewHolder.texto_like.setText(null);
                                    variable.setRespuesta_dislike("1");
                                    final String respuestaDislike = variable.getRespuesta_dislike();
                                    variablesViewHolder.texto_dislike.setText(respuestaDislike);

                                    if (comentario == null) {
                                        Toast.makeText(activity, "Respuesta cambiada", Toast.LENGTH_SHORT).show();
                                    } else if (comentario.isEmpty()) {
                                        Toast.makeText(activity, "Respuesta cambiada", Toast.LENGTH_SHORT).show();
                                    } else {
                                        variable.setComentario("");
                                        Toast.makeText(activity, "La observación anterior ha sido eliminada", Toast.LENGTH_SHORT).show();
                                    }
                                    dialog.cancel();
                                    valor_respuesta[0] = 1;
                                }
                            })
                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = alertDialogBuilder.create();
                    alert.show();
                } else if (valor2.equals("1")) {
                    Toast.makeText(activity, "Ya haz seleccionado la respuesta", Toast.LENGTH_SHORT).show();
                } else if (valor2.isEmpty()) {
                    variable.setRespuesta_dislike("1");
                    final String respuestaDislike = variable.getRespuesta_dislike();
                    variablesViewHolder.texto_dislike.setText(respuestaDislike);
                    valor_respuesta[0] = 1;
                }
            }
        });

        variablesViewHolder.img_comentario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comentario = variable.getComentario();

                String valor1 = variablesViewHolder.texto_like.getText().toString();
                String valor2 = variablesViewHolder.texto_dislike.getText().toString();
                int valor = valor_respuesta[0];

                if (valor == 0) {
                    Toast.makeText(activity, "Antes selecciona bien o mal", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(comentario)) {
                    cuadro_dialogo(variablesViewHolder, variable);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    final LayoutInflater inflater = activity.getLayoutInflater();
                    final View dialogLayout = inflater.inflate(R.layout.observacion, null);
                    builder.setView(dialogLayout);
                    final AlertDialog show = builder.show();

                    final EditText edt_observacion = dialogLayout.findViewById(R.id.edt_comentario);
                    final Button btn_editar_observacion = dialogLayout.findViewById(R.id.btn_editar_observacion);
                    Button btn_cancelar_observacion = dialogLayout.findViewById(R.id.btn_cancelar);
                    final Button btn_guardar_observacion = dialogLayout.findViewById(R.id.btn_guardar_observacion);

                    edt_observacion.setText(comentario);
                    edt_observacion.setEnabled(false);

                    btn_editar_observacion.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            edt_observacion.setEnabled(true);
                            btn_editar_observacion.setVisibility(View.INVISIBLE);
                            btn_guardar_observacion.setVisibility(View.VISIBLE);
                        }
                    });

                    btn_guardar_observacion.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String observacion = edt_observacion.getText().toString();
                            View focusView = null;

                            if (TextUtils.isEmpty(observacion)) {
                                edt_observacion.setError("Escriba la observación");
                                focusView = edt_observacion;
                            } else {
                                variable.setComentario(observacion);
                                final String respuesta = variable.getComentario();
                                variable.setFoto(R.drawable.icons8_speech_bubble_64);
                                variablesViewHolder.img_comentario.setImageResource(variable.getFoto());
                                Toast.makeText(activity, "Guardado con exito", Toast.LENGTH_SHORT).show();
                                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                                show.dismiss();
                            }
                        }
                    });
                    btn_cancelar_observacion.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            show.dismiss();
                        }
                    });
                }
            }
        });
    }

    private void cuadro_dialogo(final VariablesViewHolder variablesViewHolder,
                                final variablesUsuarioPOJO variable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final LayoutInflater inflater = activity.getLayoutInflater();
        final View dialogLayout = inflater.inflate(R.layout.cuadro_dialogo, null);
        builder.setView(dialogLayout).setCancelable(false);
        final AlertDialog show = builder.show();

        final EditText observacion_vaiable = dialogLayout.findViewById(R.id.obervación_vaiable);
        Button btn_guardar_observacion = dialogLayout.findViewById(R.id.btn_guardar_observacion);
        Button btn_cancelar_observacion = dialogLayout.findViewById(R.id.cancelar_observacion);


        btn_guardar_observacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String observacion = observacion_vaiable.getText().toString();
                View focusView = null;

                if (TextUtils.isEmpty(observacion)) {
                    observacion_vaiable.setError("Escriba la observación");
                    focusView = observacion_vaiable;
                } else {
                    variable.setComentario(observacion);
                    final String respuesta = variable.getComentario();
                    variable.setFoto(R.drawable.icons8_speech_bubble_64);
                    variablesViewHolder.img_comentario.setImageResource(variable.getFoto());
                    Toast.makeText(activity, "Guardado con exito", Toast.LENGTH_SHORT).show();
                    InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    show.dismiss();
                }

            }
        });
        btn_cancelar_observacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(dialogLayout.getWindowToken(), 0);
                show.dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return variables.size();
    }

    public class VariablesViewHolder extends RecyclerView.ViewHolder {

        private TextView texto_variables, texto_dislike, texto_like, texto_poliza, texto_nombre;
        private ImageView like, dislike;
        private ImageButton img_comentario;

        public VariablesViewHolder(View itemView) {
            super(itemView);
            texto_variables = itemView.findViewById(R.id.tv_variables);
            like = itemView.findViewById(R.id.img_like);
            dislike = itemView.findViewById(R.id.img_dislike);
            texto_dislike = itemView.findViewById(R.id.tv_dislike);
            texto_like = itemView.findViewById(R.id.tv_like);
            texto_poliza = itemView.findViewById(R.id.poliza);
            texto_nombre = itemView.findViewById(R.id.nombre_usuario);
            img_comentario = itemView.findViewById(R.id.img_comentario);

        }
    }
}