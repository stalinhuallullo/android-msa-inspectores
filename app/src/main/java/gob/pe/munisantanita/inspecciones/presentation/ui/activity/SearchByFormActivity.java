package gob.pe.munisantanita.inspecciones.presentation.ui.activity;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import gob.pe.munisantanita.inspecciones.BuildConfig;
import gob.pe.munisantanita.inspecciones.R;
import gob.pe.munisantanita.inspecciones.presentation.presenter.view_model.ResultViewModel;
import gob.pe.munisantanita.inspecciones.presentation.utils.Tools;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchByFormActivity extends AppCompatActivity implements OnClickListener {
    public String url = BuildConfig.base_url + BuildConfig._api;

    private EditText etCodigo;
    private Button btn_search_license;

    ProgressDialog pd;
    AlertDialog.Builder builder;
    public Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_form);
        initToolbar();

        ctx = this;
        etCodigo = findViewById(R.id.etCodigo);
        btn_search_license = findViewById(R.id.btn_search_license);

        btn_search_license.setOnClickListener(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        etCodigo.setText("");
        hiddenProgress();
    }

    private void showProgress(){
        pd = new ProgressDialog(this);
        pd.setMessage("Cargando...");
        pd.setCancelable(false);
        pd.show();
    }
    public void hiddenProgress(){
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
            pd = null;
        }
    }

    public void showDialog(final String msj, final boolean type) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                    builder.setTitle("¡Mensaje!");
                    builder.setMessage(msj);
                    if (type){
                        builder.setPositiveButton(
                                "Aceptar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        finish();
                                    }
                                });
                    }
                    else builder.setNegativeButton("Aceptar", new DialogInterface.OnClickListener() { // define the 'Cancel' button
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.setCancelable(false);
                    builder.show();
                }
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Buscar");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.cyan_900);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search_license:
                validateData();
                break;
        }
    }

    private void validateData(){
        String codigo = etCodigo.getText().toString();

        if(codigo.length() <= 0) showDialog("Ingresa el codigo de la licencia", false);
        else showResults(codigo);
    }

    private void showResults(String code){
        showProgress();
        Log.e("url", url + code);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url + code)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("ERROR", "ERROOOOOOOOOOOOOOOOOOOR  " +  e.getMessage());
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                parse(response.body().string());
            }
        });
    }

    private void parse(String response) {
        Log.e("AAAA", response);
        String jsonData = response;
        JSONObject obj = null;

        try {
            obj = new JSONObject(jsonData);

            ResultViewModel result = new ResultViewModel();

            if(obj.getString("estado").equals("OK")){
                result.setIdRegistro(obj.getString("id_registro"));
                result.setNumero(obj.getString("numero"));
                result.setRiesgo(obj.getString("riesgo"));
                result.setrSocial(obj.getString("r_social"));
                result.setDireccion(obj.getString("direccion"));
                result.setRepresentante(obj.getString("representante"));
                result.setMaxNum(obj.getString("max_num"));
                result.setMaxLetra(obj.getString("max_letra"));
                result.setActividad(obj.getString("actividad"));
                result.setArea(obj.getString("area"));
                result.setInforme(obj.getString("informe"));
                result.setExpediente(obj.getString("expediente"));
                result.setResolucion(obj.getString("resolucion"));
                result.setfExpedicion(obj.getString("f_expedicion"));
                result.setfRenovacion(obj.getString("f_renovacion"));
                result.setfCaducidad(obj.getString("f_caducidad"));
                result.setEstado(obj.getString("estado"));

                result.setMsgEstadoConsulta("");

                Intent intentView = new Intent(getApplicationContext(), ViewActivity.class);
                intentView.putExtra("obj", result);
                startActivity(intentView);

            }
            else {
                showDialog("No se encontro resultado", false);
                hiddenProgress();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            showDialog("Ocurrio un erro, por favor intente nuevamente.", false);
            hiddenProgress();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
