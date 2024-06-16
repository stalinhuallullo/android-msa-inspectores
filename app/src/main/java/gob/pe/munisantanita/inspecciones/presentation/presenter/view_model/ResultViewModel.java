package gob.pe.munisantanita.inspecciones.presentation.presenter.view_model;
import java.io.Serializable;

@SuppressWarnings("serial")
public class ResultViewModel  implements Serializable {

    private String idRegistro;
    private String numero;
    private String riesgo;
    private String rSocial;
    private String direccion;
    private String representante;
    private String maxNum;
    private String maxLetra;
    private String actividad;
    private String area;
    private String informe;
    private String expediente;
    private String resolucion;
    private String fExpedicion;
    private String fRenovacion;
    private String fCaducidad;
    private String estado;
    private String msgEstadoConsulta;


    public String getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(String idRegistro) {
        this.idRegistro = idRegistro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getRiesgo() {
        return riesgo;
    }

    public void setRiesgo(String riesgo) {
        this.riesgo = riesgo;
    }

    public String getrSocial() {
        return rSocial;
    }

    public void setrSocial(String rSocial) {
        this.rSocial = rSocial;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getRepresentante() {
        return representante;
    }

    public void setRepresentante(String representante) {
        this.representante = representante;
    }

    public String getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(String maxNum) {
        this.maxNum = maxNum;
    }

    public String getMaxLetra() {
        return maxLetra;
    }

    public void setMaxLetra(String maxLetra) {
        this.maxLetra = maxLetra;
    }

    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getInforme() {
        return informe;
    }

    public void setInforme(String informe) {
        this.informe = informe;
    }

    public String getExpediente() {
        return expediente;
    }

    public void setExpediente(String expediente) {
        this.expediente = expediente;
    }

    public String getResolucion() {
        return resolucion;
    }

    public void setResolucion(String resolucion) {
        this.resolucion = resolucion;
    }

    public String getfExpedicion() {
        return fExpedicion;
    }

    public void setfExpedicion(String fExpedicion) {
        this.fExpedicion = fExpedicion;
    }

    public String getfRenovacion() {
        return fRenovacion;
    }

    public void setfRenovacion(String fRenovacion) {
        this.fRenovacion = fRenovacion;
    }

    public String getfCaducidad() {
        return fCaducidad;
    }

    public void setfCaducidad(String fCaducidad) {
        this.fCaducidad = fCaducidad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMsgEstadoConsulta() {
        return msgEstadoConsulta;
    }

    public void setMsgEstadoConsulta(String msgEstadoConsulta) {
        this.msgEstadoConsulta = msgEstadoConsulta;
    }
}
