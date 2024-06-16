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
import android.util.SparseArray;
import android.view.MenuItem;
import android.widget.TextView;

import gob.pe.munisantanita.inspecciones.BuildConfig;
import gob.pe.munisantanita.inspecciones.R;
import gob.pe.munisantanita.inspecciones.presentation.presenter.view_model.ResultViewModel;
import gob.pe.munisantanita.inspecciones.presentation.utils.CameraSourcePreview;
import gob.pe.munisantanita.inspecciones.presentation.utils.Tools;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class CamaraQRActivity extends AppCompatActivity {

    public String url = BuildConfig.base_url + BuildConfig._api;
    private TextView barcodeInfo;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private CameraSourcePreview preview;
    private TextRecognizer textRecognizer;
    private Boolean stoped = false;
    AlertDialog.Builder builder;
    ProgressDialog pd;
    public Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camara_qr);

        initToolbar();

        ctx = this;
        barcodeInfo = findViewById(R.id.code_info);
        preview = findViewById(R.id.cameraSourcePreview);


        setupBarcodeDetector();
        setupCameraSource();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        hiddenProgress();
        stoped = false;
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Escanear el código");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.cyan_900);
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

    @Override
    protected void onResume() {
        super.onResume();
        startCameraSource();
    }

    private void startCameraSource() {
        if (cameraSource != null) {
            try {
                preview.start(cameraSource);
            } catch (IOException e) {
                cameraSource.release();
                cameraSource = null;
            }
        }
    }

    private void setupBarcodeDetector() {
        barcodeDetector = new BarcodeDetector.Builder(this)
                //.setBarcodeFormats(Barcode.QR_CODE)
                .build();

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                if (barcodes.size() != 0) {
                    if(stoped == false){
                        stoped = true;
                        barcodeInfo.post(new Runnable() {    // Use the post method of the TextView
                            public void run() {
                                barcodeInfo.setText(    // Update the TextView
                                        barcodes.valueAt(0).displayValue
                                );
                                showCodeQR(barcodes.valueAt(0).displayValue);
                            }
                        });
                    }
                }
            }
        });

        if (!barcodeDetector.isOperational()) {
            Log.w("TAG_QR", "Detector dependencies are not yet available.");
        }

        textRecognizer = new TextRecognizer.Builder(this).build();

        if (!textRecognizer.isOperational()) {
            Log.w("MainActivity", "Detector Dependencies are not yet available.");
        }
        textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<TextBlock> detections)
            {
                final SparseArray<TextBlock> items = detections.getDetectedItems();
                if (items.size() != 0)
                {
                    Log.d("ReceiveDetections", items.size()+"");
                    /*barcodeInfo.post(new Runnable() {
                        @Override
                        public void run() {
                            StringBuilder stringBuilder = new StringBuilder();
                            for (int i=0 ; i < items.size(); i++)
                            {
                                TextBlock item = items.valueAt(i);
                                stringBuilder.append(item.getValue());
                                stringBuilder.append("\n");
                            }
                            barcodeInfo.setText(stringBuilder.toString());
                        }
                    });*/
                }
            }
        });
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
                            stoped = false;
                            dialog.cancel();
                        }
                    });
                    builder.setCancelable(false);
                    builder.show();
                }
            }
        });
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

    private void showCodeQR(String qr)
    {


        showProgress();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url + getUri(qr))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                showDialog("onFailure", false);
                hiddenProgress();
                stoped = false;
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                parse(response.body().string());
            }
        });
    }

    private String getUri(String url){
        String url_get  = "";
        URL miUrl = null;
        try {
            miUrl = new URL(url);
            url_get = miUrl.getQuery();
        } catch (MalformedURLException e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return url_get;
    }

    private void parse(String response) {
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

                stoped = true;
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

    private void setupCameraSource() {
        //DisplayMetrics metrics = new DisplayMetrics();
        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedFps(30.0f)
                .setRequestedPreviewSize(1024, 1024)
                //.setRequestedPreviewSize(metrics.heightPixels, metrics.widthPixels)
                .build();
    }

    @Override
    protected void onPause() {
        super.onPause();
        preview.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraSource != null) {
            cameraSource.release();
        }
    }
}
