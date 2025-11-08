package tuti.daos.servicios;

import java.util.List;
import java.util.Optional;

import tuti.daos.entidades.Producto;

public interface ProductoService {

	List<Producto> getAll();

	/**
	 * permite obtener una persona determinada 
	 * @param idPersona identificador de la persona buscada
	 * @return persona encontrada o null si no encontr{o la persona
	 * @throws Exception ante un error
	 */
	Optional<Producto> getProductoById(Long id) throws Exception;

	List<Producto> getProductosByNombre(String nombre);
}
