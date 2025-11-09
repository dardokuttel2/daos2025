package tuti.daos.servicios;


import java.util.List;
import java.util.Optional;

import tuti.daos.entidades.Ciudad;
import tuti.daos.excepciones.Excepcion;
import tuti.daos.presentacion.ciudades.CiudadDTO;
/**
 * Clase que permite gestionar la entidad Ciudad en el sistema.
 * @author kuttel
 * @version 1.0
 */

public interface CiudadService {

	

	/**
	 * Obtiene la lista completa de ciudades
	 * @return Todas las ciudades
	 */
	List<Ciudad> getAll();
	
	/**
	 * Obtiene una ciudad determinada
	 * @param idCiudad Identificador de la ciudad buscada
	 * @return Ciudad encontrada
	 */
	Optional<Ciudad> getById(Long idCiudad) throws Excepcion;


	void save(Ciudad c) throws Excepcion;


	/**
	 * Elimina una ciudad del sistema
	 * @param id id de la ciudad a eliminar
	 */
	void delete(Long id);
	/**
	 * Crea una nueva ciudad
	 * @param p
	 */
	Long insert(CiudadDTO dto) throws Exception ;
	
	/**
	 * Actualiza datos de una ciudad
	 * @param p
	 */
	Ciudad update(CiudadDTO c, Long id) throws Exception;

	
	
}
