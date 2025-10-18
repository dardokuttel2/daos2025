package tuti.daos.presentacion.provincias;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import tuti.daos.entidades.Provincia;
import tuti.daos.servicios.ProvinciaService;




@RestController
@RequestMapping("/provincias")
/**
 *  Recurso Provincias
 *  @author dardo
 *
 */
//@Api(tags = { SwaggerConfig.PROVINCIAS })
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
	public ResponseEntity<List<Provincia>> getProvincias() throws Exception
	{
		
		List<Provincia> provincias=service.getAll();
		if(provincias.size()>0)
		{
			return new ResponseEntity<List<Provincia>>(provincias, HttpStatus.OK);
		}
		else
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	/**
	 * Obtiene una provincia a través de su id.
	 *  curl --location --request GET 'http://localhost:8081/provincias/3'
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Provincia> getById(@PathVariable Long id) throws Exception
	{
		Optional<Provincia> rta=service.getById(id);
		if(rta.isPresent())
		{
			Provincia pojo=rta.get();
			return new ResponseEntity<Provincia>(pojo, HttpStatus.OK);
		}
		else
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	
	
	
}
