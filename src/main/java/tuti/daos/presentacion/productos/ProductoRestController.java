package tuti.daos.presentacion.productos;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import tuti.daos.entidades.Producto;
import tuti.daos.excepciones.Excepcion;
import tuti.daos.servicios.ProductoService;




@RestController
@RequestMapping("/productos")
@Tag(name = "Productos", description = "Productos")
public class ProductoRestController {
	@Autowired
	private ProductoService service;
	
	/**
	 * Obtiene una lista de Productos que cumplan con el criterio de filtrado indicado (o todos, en caso de no indicar filtro).
	 *  curl --location --request GET 'http://localhost:8080/productos'
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@GetMapping( produces = { MediaType.APPLICATION_JSON_VALUE})
	//Abajo Documentación Swagger de la operación (descripcion basica y formato de respuesta)
	@Operation(summary = " Obtiene una lista de Productos que cumplan con el criterio de filtrado indicado (o todos, en caso de no indicar filtro).")  
	@ApiResponse(responseCode = "200", 
				description = "Lista de productos", 
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductoResponseDTO.class)))
	public ResponseEntity<List<ProductoResponseDTO>> getProductoByNombre(@RequestParam(name = "nombre",required = false) String nombre ) throws Exception
	{
		List<Producto> prods=service.getProductosByNombre(nombre);
		List<ProductoResponseDTO> dtos=new ArrayList<ProductoResponseDTO>();
		
		for (Producto prod : prods) {
			dtos.add(buildResponseBody(prod)); //para cada entidad, construyo un DTO con informacion bàsica más links HATEOAS
		}
		
		return new ResponseEntity<List<ProductoResponseDTO>>(dtos, HttpStatus.OK);
	}
	
	
	/**
	 * Obtiene un Producto a través de su id.
	 *  curl --location --request GET 'http://localhost:8081/productos/3'
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
	//Abajo Documentación Swagger de la operación (descripcion basica y formato de respuesta)
	@Operation(summary = "Obtiene un producto a partir de su Id ")
	@ApiResponses(value = { 
		    @ApiResponse(responseCode = "200", description = "Producto encontrado",content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ProductoResponseDTO.class)) }),
		    @ApiResponse(responseCode = "404", description = "Producto no encontrado",content = { @Content() })
		})
	public ResponseEntity<ProductoResponseDTO> getProductoById(@PathVariable Long id) throws Exception
	{
		Optional<Producto> rta= service.getProductoById(id);
		if(rta.isPresent())
		{
			Producto pojo=rta.get();
			return new ResponseEntity<ProductoResponseDTO>(buildResponseBody(pojo), HttpStatus.OK);
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
		private ProductoResponseDTO buildResponseBody(Producto pojo) throws Excepcion {
			try {
				ProductoResponseDTO dto = new ProductoResponseDTO(pojo);
				 //Self link
				Link selfLink = WebMvcLinkBuilder.linkTo(ProductoRestController.class)
											.slash(pojo.getId())                
											.withSelfRel();
				dto.add(selfLink);
				return dto;
			} catch (Exception e) {
			    throw new Excepcion("Producto", "Error al construir los enlaces HATEOAS: " + e.getMessage(), 500);
			}
		}
}
