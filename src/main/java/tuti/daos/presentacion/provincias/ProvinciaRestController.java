package tuti.daos.presentacion.provincias;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import tuti.daos.entidades.Provincia;
import tuti.daos.excepciones.Excepcion;
import tuti.daos.servicios.ProvinciaService;




@RestController
@RequestMapping("/provincias")
/**
 *  Recurso Provincias
 *  @author dardo
 *
 */
@Tag(name = "Provincias", description = "Provincias")
public class ProvinciaRestController {
	@Autowired
	private ProvinciaService service;
	
	/**
	 * Obtiene todas las provincias registradas en el sistema.
	 *  curl --location --request GET 'http://localhost:8081/provincias'
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE})
	//Abajo Documentación Swagger de la operación (descripcion basica y formato de respuesta)
	@Operation(summary = "Obtiene todas las provincias registradas")  
	@ApiResponse(responseCode = "200", 
				description = "Lista de provincias", 
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProvinciaResponseDTO.class)))
	public ResponseEntity<List<ProvinciaResponseDTO>> getProvincias() throws Exception
	{
		List<Provincia> provincias=service.getAll();
		List<ProvinciaResponseDTO> dtos=new ArrayList<ProvinciaResponseDTO>();
		
		for (Provincia provincia : provincias) {
			dtos.add(buildResponseBody(provincia)); //para cada entidad, construyo un DTO con informacion bàsica más links HATEOAS
		}
		
		return new ResponseEntity<List<ProvinciaResponseDTO>>(dtos, HttpStatus.OK);
	}
	
	/**
	 * Obtiene una provincia a través de su id.
	 *  curl --location --request GET 'http://localhost:8081/provincias/3'
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
	//Abajo Documentación Swagger de la operación (descripcion basica y formato de respuesta)
	@Operation(summary = "Obtiene una provincias a partir de su Id ")
	@ApiResponses(value = { 
		    @ApiResponse(responseCode = "200", description = "Provincia encontrada",content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ProvinciaResponseDTO.class)) }),
		    @ApiResponse(responseCode = "404", description = "Provincia no encontrada",content = { @Content() })
		})
	public ResponseEntity<ProvinciaResponseDTO> getById(@PathVariable Long id) throws Exception
	{
		Optional<Provincia> pojo=service.getById(id);
		if(pojo.isPresent())
		{
			ProvinciaResponseDTO rta=buildResponseBody(pojo.get());
			return new ResponseEntity<ProvinciaResponseDTO>(rta, HttpStatus.OK);
		}
		else
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	
	// --------------------METODOS AUXILIARES--------------------
	
	
	/**
	 * Métdo auxiliar que toma los datos del pojo y construye el objeto a devolver en la response, con su hipervinculos HATEOAS
	 * @param pojo
	 * @return
	 * @throws Excepcion 
	 */
	private ProvinciaResponseDTO buildResponseBody(Provincia pojo) throws Excepcion {
		try {
			ProvinciaResponseDTO dto = new ProvinciaResponseDTO(pojo);
			 //Self link
			Link selfLink = WebMvcLinkBuilder.linkTo(ProvinciaRestController.class)
										.slash(pojo.getId())                
										.withSelfRel();
			
			dto.add(selfLink);
			return dto;
		} catch (Exception e) {
			throw new Excepcion("",e.getMessage(), 500);
		}
	}
}
