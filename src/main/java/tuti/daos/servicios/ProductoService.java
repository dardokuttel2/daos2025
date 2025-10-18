package tuti.daos.servicios;

import java.util.List;

import tuti.daos.entidades.Producto;
import tuti.daos.excepciones.Excepcion;
import tuti.daos.presentacion.productos.ProductosBuscarForm;

public interface ProductoService {



	List<Producto> getAll();

	List<Producto> filter(ProductosBuscarForm filter) throws Excepcion;

	/**
	 * Si la persona existe la actualizará, sino la creará en BD
	 * @param persona
	 * @throws Exception 
	 */
	void save(Producto entity) throws Excepcion;

	/**
	 * permite obtener una persona determinada 
	 * @param idPersona identificador de la persona buscada
	 * @return persona encontrada o null si no encontr{o la persona
	 * @throws Exception ante un error
	 */
	Producto getProductoById(Long id) throws Exception;

	void deleteProductoByid(Long id);

	
}
