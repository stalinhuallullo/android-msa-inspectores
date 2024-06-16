package gob.pe.munisantanita.inspecciones.data.datasource;

public interface DataSourceCallback {
    void onSuccess(Object data);
    void onError(String error);
}
