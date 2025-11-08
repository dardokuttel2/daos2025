package tuti.daos.presentacion.productos;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.hateoas.Links;
import org.springframework.hateoas.RepresentationModel;

import io.swagger.v3.oas.annotations.media.Schema;
import tuti.daos.entidades.Producto;
import tuti.daos.presentacion.ciudades.CiudadResponseDTO;

public class ProductoResponseDTO extends RepresentationModel<CiudadResponseDTO> {
	private Long id;
	
	private String nombre;
	
	private Long calorias;
	
	private Date fechaVencimiento;
	
	private Long stock;
	
	private Double precio;

	public ProductoResponseDTO(Producto pojo) {
		super();
		this.nombre=pojo.getNombre();
		this.id=pojo.getId();
		this.calorias=pojo.getCalorias();
		this.fechaVencimiento=pojo.getFechaVencimiento();
		this.stock=pojo.getStock();
		this.precio=pojo.getPrecio();		
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Long getCalorias() {
		return calorias;
	}

	public void setCalorias(Long calorias) {
		this.calorias = calorias;
	}

	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public Long getStock() {
		return stock;
	}

	public void setStock(Long stock) {
		this.stock = stock;
	}

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}
	
	//Esto oculta los links en los ejemplos de respuestas que muestra swagger (para dejar mas limpio el ejemplo). 
	//Pero sí se devuelven los links en la respuesta cuando se ejecuta el servicio realmente
	@Schema(hidden = true)
    @Override
    public Links getLinks() {
        return super.getLinks();
    }
		
	
}
