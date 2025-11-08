package tuti.daos.presentacion.ciudades;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import tuti.daos.entidades.Ciudad;
import tuti.daos.error.ErrorAtributo;
import tuti.daos.excepciones.Excepcion;
import tuti.daos.presentacion.provincias.ProvinciaRestController;
import tuti.daos.servicios.CiudadService;



/**
 *  Recurso ciudades
 *  @author dardo
 *
 */
@RestController
@RequestMapping("/ciudades") // URL base del recurso
@Tag(name = "Ciudades", description = "Operaciones CRUD sobre las ciudades") //Documentación Swagger del Recurso
public class CiudadRestController {

	@Autowired
	private CiudadService service;
	
	/**
	 * Obtiene todas las ciudades registradas en el sistema.
	 *  curl --location --request GET 'http://localhost:8081/ciudades'
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE}) // Operacion GET sobre el recurso Ciudades
	//Abajo Documentación Swagger de la operación (descripcion basica y formato de respuesta)
	@Operation(summary = "Obtiene todas las ciudades registradas")  
	@ApiResponse(responseCode = "200", 
				description = "Lista de ciudades", 
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = CiudadResponseDTO.class)))
	public ResponseEntity<List<CiudadResponseDTO>> getCiudades() throws Exception
	{
		List<Ciudad> ciudades=service.getAll(); 
		List<CiudadResponseDTO> ciudadesDto=new ArrayList<CiudadResponseDTO>();
		
		for (Ciudad ciudad : ciudades) {
			ciudadesDto.add(buildResponseBody(ciudad)); //para cada entidad, construyo un DTO con informacion bàsica más links HATEOAS
		}
		
		return new ResponseEntity<List<CiudadResponseDTO>>(ciudadesDto, HttpStatus.OK); //respuesta exitosa (codigo 200)
	}
	
	/**
	 * Obtiene una ciudad a través de su id.
	 *  curl --location --request GET 'http://localhost:8081/ciudades/3'
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE}) // Operacion GET by Id sobre el recurso Ciudades
	//Abajo Documentación Swagger de la operación (descripcion basica y formato de respuesta)
	@Operation(summary = "Obtiene una ciudad a partir de su Id ")
	@ApiResponses(value = { 
		    @ApiResponse(responseCode = "200", description = "Ciudad encontrada",content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CiudadResponseDTO.class)) }),
		    @ApiResponse(responseCode = "404", description = "Ciudad no encontrada",content = { @Content() })
		})
	public ResponseEntity<CiudadResponseDTO> getById(@PathVariable Long id) throws Exception
	{
		Optional<Ciudad> rta=service.getById(id);
		if(rta.isPresent())
		{
			Ciudad pojo=rta.get();
			return new ResponseEntity<CiudadResponseDTO>(buildResponseBody(pojo), HttpStatus.OK);
		}
		else
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	/**
	 * Inserta una nueva ciudad en la base de datos
	 * 			curl --location --request POST 'http://localhost:8081/ciudades' 
	 *			--header 'Accept: application/json' 
	 * 			--header 'Content-Type: application/json' 
	 *			--data-raw '{
	 *			    "nombre": "Santa Fe"
	 *			    "idProvincia": 1
	 *			}'
	 * @param p ciudad  a insertar
	 * @return ciudad insertada o error en otro caso
	 * @throws Exception 
	 */
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE) // Operacion POST sobre el recurso Ciudades (alta de una ciudad nueva)
	//Abajo Documentación Swagger de la operación (descripcion basica y formato de respuesta)
	@Operation(summary = "Inserta una nueva ciudad en la base de datos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ciudad creada correctamente",content = @Content),
            @ApiResponse(responseCode = "400", description = "Error de validación (Ej, la provincia indicada no existe)",
        		content = @Content(mediaType = "application/json",schema = @Schema(implementation = ErrorAtributo.class)))
    })
    public ResponseEntity<Object> insertar( @Valid @RequestBody CiudadDTO dto, BindingResult result) throws Exception
	{
		if(result.hasErrors())
		{
			//Dos alternativas:
			//throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.formatearError(result));
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body( this.formatearErrores(result));
		}
		
		//ahora inserto la nueva ciudad
		Long idNuevaCiudad = service.insert(dto);
		
		//Por convención en una api REST, se devuelve la  url del recurso recién creado
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(idNuevaCiudad).toUri(); 

		return ResponseEntity.created(location).build();//201 (Recurso creado correctamente)
	}
	
	/**
	 * Modifica una ciudad existente en la base de datos:
	 * 			curl --location --request PUT 'http://localhost:8081/ciudades/1' 
	 *			--header 'Accept: application/json' 
	 * 			--header 'Content-Type: application/json' 
	 *			--data-raw '{
	 *			    "nombre": "Santa Fe"
	 *			    "idProvincia": 1
	 *			}'
	 * @param p ciudad a modificar
	 * @return ciudad editada o error en otro caso
	 * @throws Excepcion 
	 */
	@PutMapping("/{id}") // Operacion PUT sobre el recurso Ciudades (modificación de una ciudad existente)
	//Abajo Documentación Swagger de la operación (descripcion basica y formato de respuesta)
	@Operation(summary = "Actualiza los datos de una ciudad existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ciudad actualizada correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CiudadResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Error de validación o provincia inexistente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorAtributo.class))),
            @ApiResponse(responseCode = "404", description = "Ciudad no encontrada", content = @Content)
    })
	public ResponseEntity<Object>  actualizar( @Valid @RequestBody CiudadDTO dto, @PathVariable long id, BindingResult result) throws Exception
	{
		if(result.hasErrors())
		{
			//Dos alternativas:
			//throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.formatearError(result));
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body( this.formatearErrores(result));
		}
		
		Ciudad rta=service.update(dto,id);
		return ResponseEntity.ok(buildResponseBody(rta));
	}
	
	/**
	 * Borra la ciudad con el id indicado
	 * 	  curl --location --request DELETE 'http://localhost:8081/ciudades/27837176'
	 * @param id di de la ciudad a borrar
	 * @return ok en caso de borrar exitosamente, error en otro caso
	 * @throws Excepcion 
	 */
	@DeleteMapping("/{id}") // Operacion DELETE sobre el recurso Ciudades 
	//Abajo Documentación Swagger de la operación (descripcion basica y formato de respuesta)
	@Operation(summary = "Elimina una ciudad existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ciudad eliminada correctamente", content = @Content),
            @ApiResponse(responseCode = "404", description = "Ciudad no encontrada", content = @Content)
    })
	public ResponseEntity<String> eliminar(@PathVariable Long id) throws Excepcion
	{
		if(!service.getById(id).isPresent())
			return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe una ciudad con ese id");
		service.delete(id);
		
		return ResponseEntity.noContent().build(); // HTTP 204 - el recurso fue eliminado exitosamente y no hay contenido para devolver
	}
	
	
	
	
	
	// --------------------METODOS AUXILIARES--------------------
	
	/**
	 * Métdo auxiliar que toma los datos del pojo y construye el objeto a devolver en la response, con su hipervinculos HATEOAS
	 * @param pojo
	 * @return
	 * @throws Excepcion 
	 */
	private CiudadResponseDTO buildResponseBody(Ciudad pojo) throws Excepcion {
		try {
			CiudadResponseDTO dto = new CiudadResponseDTO(pojo);
			 //Self link
			Link selfLink = WebMvcLinkBuilder.linkTo(CiudadRestController.class)
										.slash(pojo.getId())                
										.withSelfRel();
			//Method link: Link al servicio que permitirá navegar hacia la ciudad relacionada a la persona
			Link provLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProvinciaRestController.class)
			       													.getById(pojo.getProvincia().getId()))
			       													.withRel("provincia");
			dto.add(selfLink);
			dto.add(provLink);
			return dto;
		} catch (Exception e) {
			throw new Excepcion("",e.getMessage(), 500);
		}
	}
	
	/**
	 * Toma los errores resultantes de la validacion mediante Java Bean Validation y los transforma 
		en una lista de objetos ErrorAtributo, propios del proyecto (objetos mas pequelos y simples, mas adecuados para ser devueltos en el body)
	 */
	private List<ErrorAtributo> formatearErrores(BindingResult result)
	{
//		primero transformamos la lista de errores devuelta por Java Bean Validation en nuestra lista de objetos (objetos mas simples para retornar)
		List<FieldError> errors = result.getFieldErrors();
		List<ErrorAtributo> respuesta =new ArrayList<ErrorAtributo>();
		
		for (FieldError error : errors) {
			respuesta.add(new ErrorAtributo(error.getField(),error.getDefaultMessage()));
		}
		
		return respuesta;
	}
}
