package tuti.daos.servicios;

import java.util.List;
import java.util.Optional;

import tuti.daos.entidades.Producto;

public interface ProductoService {

	List<Producto> getAll();

	/**
	 * permite obtener un producto determinado 
	 * @param idPersona identificador del producto buscado
	 * @return producto encontrad o null si no encontró
	 * @throws Exception ante un error
	 */
	Optional<Producto> getProductoById(Long id) throws Exception;

	List<Producto> getProductosByNombre(String nombre);
}
