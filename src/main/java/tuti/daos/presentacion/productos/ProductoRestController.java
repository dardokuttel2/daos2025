package tuti.daos.presentacion.productos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import tuti.daos.entidades.Ciudad;
import tuti.daos.entidades.Producto;
import tuti.daos.servicios.ProductoService;




@RestController
@RequestMapping("/productos")
@Tag(name = "Productos", description = "Productos")
public class ProductoRestController {


	@Autowired
	private ProductoService service;
	
//	/**
//	 * Obtiene todas las productos registradas en el sistema.
//	 *  curl --location --request GET 'http://localhost:8080/productos'
//	 * @param id
//	 * @return
//	 * @throws Exception 
//	 */
//	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE})
//	public ResponseEntity<List<Producto>> getProductos() throws Exception
//	{
//		
//		List<Producto> prods=service.getAll();
//		return new ResponseEntity<List<Producto>>(prods, HttpStatus.OK);
//		
//	}
	
	
	/**
	 * Obtiene un Producto a través de su id.
	 *  curl --location --request GET 'http://localhost:8081/productos/3'
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Producto> getProductoById(@PathVariable Long id) throws Exception
	{
		
		Producto prod=service.getProductoById(id);
		return new ResponseEntity<Producto>(prod, HttpStatus.OK);
		
	}
	
	/**
	 * Obtiene una lista de Productos a través de su nombre.
	 *  curl --location --request GET 'http://localhost:8080/productos'
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@GetMapping( produces = { MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<List<Producto>> getProductoByNombre(@RequestParam(name = "nombre",required = false) String nombre ) throws Exception
	{
		
		List<Producto> prod=service.getProductosByNombre(nombre);
		return new ResponseEntity<List<Producto>>(prod, HttpStatus.OK);
		
	}
	
	
}
