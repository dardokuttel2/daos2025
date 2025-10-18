package tuti.daos.presentacion.ciudades;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import tuti.daos.entidades.Ciudad;

/**
 * Objeto necesario para insertar o eliminar una persona. 
 * Nótese que en lugar de referenciar al objeto Ciudad, reemplaza ese atributo por el idCiudad, lo cual resulta mas sencillo de representar en JSON
 *
 */
public class CiudadDTO {


	private Long id;
	@NotNull
	@Size(min=2, max=30, message = "El nombre debe tener entre 2 y 30 caracteres")
	private String nombre;
	@NotNull
	private Long idProvincia;
	
	
	
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



	public Long getIdProvincia() {
		return idProvincia;
	}



	public void setIdProvincia(Long idProvincia) {
		this.idProvincia = idProvincia;
	}



	public Ciudad toPojo()
	{
		Ciudad p = new Ciudad();
		p.setNombre(this.getNombre());
		return p;
	}
	
}
