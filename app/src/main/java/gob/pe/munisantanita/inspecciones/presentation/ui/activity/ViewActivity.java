package gob.pe.munisantanita.inspecciones.presentation.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import gob.pe.munisantanita.inspecciones.R;
import gob.pe.munisantanita.inspecciones.presentation.presenter.view_model.ResultViewModel;
import gob.pe.munisantanita.inspecciones.presentation.utils.Tools;

public class ViewActivity extends AppCompatActivity {

    private TextView tvRiesgo;
    private TextView tvRazonSocial;
    private TextView tvDireccion;
    private TextView tvRepresentante;
    private TextView tvMaxNumero;
    private TextView tvActividad;
    private TextView tvArea;
    private TextView tvInforme;
    private TextView tvExpediente;
    private TextView tvResolucion;
    private TextView tvFechaExpedicion;
    private TextView tvFechaRenovacion;
    private TextView tvFechaCaducidad;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        initToolbar();

        tvRiesgo = findViewById(R.id.tvRiesgo);
        tvRazonSocial = findViewById(R.id.tvRazonSocial);
        tvDireccion = findViewById(R.id.tvDireccion);
        tvRepresentante = findViewById(R.id.tvRepresentante);
        tvMaxNumero = findViewById(R.id.tvMaxNumero);
        tvActividad = findViewById(R.id.tvActividad);
        tvArea = findViewById(R.id.tvArea);
        tvInforme = findViewById(R.id.tvInforme);
        tvExpediente = findViewById(R.id.tvExpediente);
        tvResolucion = findViewById(R.id.tvResolucion);
        tvFechaExpedicion = findViewById(R.id.tvFechaExpedicion);
        tvFechaRenovacion = findViewById(R.id.tvFechaRenovacion);
        tvFechaCaducidad = findViewById(R.id.tvFechaCaducidad);

        getData();
    }



    private void getData(){
        Bundle parametros = this.getIntent().getExtras();
        if(parametros != null){
            ResultViewModel resultViewModel = (ResultViewModel)parametros.getSerializable("obj");


            tvRiesgo.setText("Riesgo " + resultViewModel.getRiesgo());
            tvRazonSocial.setText(resultViewModel.getrSocial());
            tvDireccion.setText(resultViewModel.getDireccion());
            tvRepresentante.setText(resultViewModel.getRepresentante());
            tvMaxNumero.setText(resultViewModel.getMaxNum());
            tvActividad.setText(resultViewModel.getActividad());
            tvArea.setText(resultViewModel.getArea());
            tvInforme.setText(resultViewModel.getInforme());
            tvExpediente.setText(resultViewModel.getExpediente());
            tvResolucion.setText(resultViewModel.getResolucion());
            tvFechaExpedicion.setText(resultViewModel.getfExpedicion());
            tvFechaRenovacion.setText(resultViewModel.getfRenovacion());
            tvFechaCaducidad.setText(resultViewModel.getfCaducidad());

        }
        else{
            Toast.makeText(this, "No se encontro resultados", Toast.LENGTH_SHORT).show();
        }
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Resultado de busqueda");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.cyan_900);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            //finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
