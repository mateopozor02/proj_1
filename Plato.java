import java.util.Calendar;
import java.util.List;

public class Plato implements Comparable<Plato>{
    private String descripcion; 
    private Calendar fechaInicio;
    private float precio; 
    private List<String> componentes; 

    public Plato(String descripcion, Calendar fechaInicio, float precio, List<String> componentes) {
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.precio = precio;
        this.componentes = componentes;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setFechaInicio(Calendar fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public void setComponentes(List<String> componentes) {
        this.componentes = componentes;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Calendar getFechaInicio() {
        return fechaInicio;
    }

    public float getPrecio() {
        return precio;
    }

    public List<String> getComponentes() {
        return componentes;
    }

    public int compareTo(Plato p){
        return this.fechaInicio.compareTo(p.getFechaInicio());
    }

    private String getDate(){
        return this.fechaInicio.get(Calendar.DAY_OF_MONTH) + "-" 
        + this.fechaInicio.get(Calendar.MONTH) + "-" + this.fechaInicio.get(Calendar.YEAR);
    }

    @Override 
    public String toString(){
        return String.format("%-15s %-10s %10.2f %s", this.descripcion, this.getDate(), this.precio, this.componentes);
    }

}