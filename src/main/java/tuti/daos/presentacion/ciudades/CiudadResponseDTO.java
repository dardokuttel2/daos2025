package tuti.daos.presentacion.ciudades;

import org.springframework.hateoas.Links;
import org.springframework.hateoas.RepresentationModel;

import io.swagger.v3.oas.annotations.media.Schema;
import tuti.daos.entidades.Ciudad;


/**
 * Objeto utilizado para construir la respuesta de los servicios. 
 * Incluye datos basicos y links HATEOAS para ampliar info (esto es heredado de la clase RepresentationModel).
 * 
 *
 */
public class CiudadResponseDTO extends RepresentationModel<CiudadResponseDTO> {

	private Long id;
	private String nombre;
	
	/**
	 * Construye el DTO correspondiente a partir del pojo pasado como argumento
	 * @param pojo
	 */
	public CiudadResponseDTO(Ciudad pojo) {
		super();
		this.nombre=pojo.getNombre();
		this.id=pojo.getId();
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
	
	
	
	@Override
	public String toString() {
		return id+" - "+ nombre ;
	}
	
	
	//Esto oculta los links en los ejemplos de respuestas que muestra swagger (para dejar mas limpio el ejemplo). 
	//Pero sí se devuelven los links en la respuesta cuando se ejecuta el servicio realmente
	@Schema(hidden = true)
    @Override
    public Links getLinks() {
        return super.getLinks();
    }
	
	
}
