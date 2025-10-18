package tuti.daos.presentacion.ciudades;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import tuti.daos.entidades.Ciudad;
import tuti.daos.entidades.Provincia;
import tuti.daos.error.MensajeError;
import tuti.daos.excepciones.Excepcion;
import tuti.daos.presentacion.provincias.ProvinciaRestController;
import tuti.daos.servicios.CiudadService;
import tuti.daos.servicios.ProvinciaService;




@RestController
@RequestMapping("/ciudades")
/**
 *  Recurso ciudades
 *  @author dardo
 *
 */
//@Api(tags = { SwaggerConfig.CIUDADES })
@Tag(name = "Ciudades", description = "Ciudades")
public class CiudadRestController {


	@Autowired
	private CiudadService service;
	
	@Autowired
	private ProvinciaService provinciaService;
	
	/**
	 * Obtiene todas las ciudades registradas en el sistema.
	 *  curl --location --request GET 'http://localhost:8081/ciudades'
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<List<Ciudad>> getCuidades() throws Exception
	{
		
		List<Ciudad> ciudades=service.getAll();
		if(ciudades.size()>0)
		{
			return new ResponseEntity<List<Ciudad>>(ciudades, HttpStatus.OK);
		}
		else
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	/**
	 * Obtiene una ciudad a través de su id.
	 *  curl --location --request GET 'http://localhost:8081/ciudades/3'
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Ciudad> getById(@PathVariable Long id) throws Exception
	{
		Optional<Ciudad> rta=service.getById(id);
		if(rta.isPresent())
		{
			Ciudad pojo=rta.get();
			return new ResponseEntity<Ciudad>(pojo, HttpStatus.OK);
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
	@PostMapping
	public ResponseEntity<Object> insertar( @Valid @RequestBody CiudadDTO dto, BindingResult result) throws Exception
	{
		
		if(result.hasErrors())
		{
			//Dos alternativas:
			//throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.formatearError(result));
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body( this.formatearError(result));
		}
		
		Ciudad c = dto.toPojo();
		Optional<Provincia> p = provinciaService.getById(dto.getIdProvincia());
		if(p.isPresent())
			c.setProvincia(p.get());
		else
		{
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getError("02", "Provincia Requerida", "La provincia indicada no se encuentra en la base de datos."));
//				return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La ciudad indicada no se encuentra en la base de datos.");
		}
		
		//ahora inserto la nueva ciudad
		service.insert(c);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(c.getId()).toUri(); //Por convención en REST, se devuelve la  url del recurso recién creado

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
	@PutMapping("/{id}")
	public ResponseEntity<Object>  actualizar(@RequestBody CiudadDTO dto, @PathVariable long id) throws Exception
	{
		Optional<Ciudad> rta = service.getById(id);
		if(!rta.isPresent())
			return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encuentra la persona que desea modificar.");
			
		else
		{
			Ciudad c = dto.toPojo();
			Optional<Provincia> p = provinciaService.getById(dto.getIdProvincia());
			if(p.isPresent())
				c.setProvincia(p.get());
			else
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getError("02", "Provincia Requerida", "La provincia indicada no se encuentra en la base de datos."));
//				return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("La ciudad indicada no se encuentra en la base de datos.");
			
			if(!c.getId().equals(id))//El dni es el identificador, con lo cual es el único dato que no permito modificar
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getError("03", "Dato no editable", "Noi puede modificar el Id."));
			service.update(c);
			return ResponseEntity.ok(buildResponse(c));
		}
		
	}
	
	
	
	
	
	
	
	
	
	/**
	 * Borra la ciudad con el id indicado
	 * 	  curl --location --request DELETE 'http://localhost:8081/ciudades/27837176'
	 * @param id di de la ciudad a borrar
	 * @return ok en caso de borrar exitosamente, error en otro caso
	 * @throws Excepcion 
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<String> eliminar(@PathVariable Long id) throws Excepcion
	{
		if(!service.getById(id).isPresent())
			return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe una ciudad con ese id");
		service.delete(id);
		
		return ResponseEntity.ok().build();
	}
	
	
	
	
	
	
	
	
	
	
	

	/**
	 * Métdo auxiliar que toma los datos del pojo y construye el objeto a devolver en la response, con su hipervinculos
	 * @param pojo
	 * @return
	 * @throws Excepcion 
	 */
	private CiudadResponseDTO buildResponse(Ciudad pojo) throws Excepcion {
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
			throw new Excepcion(e.getMessage(), 500);
		}
	}
	
	
	private String formatearError(BindingResult result) throws JsonProcessingException
	{
//		primero transformamos la lista de errores devuelta por Java Bean Validation
		List<Map<String, String>> errores= result.getFieldErrors().stream().map(err -> {
															Map<String, String> error= new HashMap<>();
															error.put(err.getField(),err.getDefaultMessage() );
															return error;
														}).collect(Collectors.toList());
		MensajeError e1=new MensajeError();
		e1.setCodigo("01");
		e1.setMensajes(errores);
		
		//ahora usamos la librería Jackson para pasar el objeto a json
		ObjectMapper maper = new ObjectMapper();
		String json = maper.writeValueAsString(e1);
		return json;
	}

	private String getError(String code, String err, String descr) throws JsonProcessingException
	{
		MensajeError e1=new MensajeError();
		e1.setCodigo(code);
		ArrayList<Map<String,String>> errores=new ArrayList<>();
		Map<String, String> error=new HashMap<String, String>();
		error.put(err, descr);
		errores.add(error);
		e1.setMensajes(errores);
		
		//ahora usamos la librería Jackson para pasar el objeto a json
				ObjectMapper maper = new ObjectMapper();
				String json = maper.writeValueAsString(e1);
				return json;
	}
	
	
	
}
