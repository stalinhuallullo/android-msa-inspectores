package gob.pe.munisantanita.inspecciones.presentation.presenter.view_model;

import android.arch.lifecycle.*;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import gob.pe.munisantanita.inspecciones.BuildConfig;
import gob.pe.munisantanita.inspecciones.data.datasource.DataSourceCallback;
import io.reactivex.disposables.CompositeDisposable;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchLicensesViewModel extends ViewModel {

    public String postUrlLicenses = BuildConfig.base_url + BuildConfig._api;

    private Context context;
    ResultViewModel result = new ResultViewModel();

    private Object compositeDisposable = new CompositeDisposable();

    public void initViewModel(Context context){

        this.context = context;
    }

    DataSourceCallback dataSourceCallback;


    public ResultViewModel searchLicence(String codigo){
        String url = postUrlLicenses + codigo;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                parse(response.body().string());

            }
        });
        return result;
    }

    private void parse(String response) {
        JSONObject obj = null;

        try {
            obj = new JSONObject(response);

            if(obj != null) {
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

                } else {
                    result.setEstado(obj.getString("estado"));
                    result.setMsgEstadoConsulta(obj.getString("mensaje"));
                }

            } else {
                result.setEstado("ERR");
                result.setMsgEstadoConsulta("No se encontro la licensia");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
