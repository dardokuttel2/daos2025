package tuti.daos.servicios;


import java.util.List;
import java.util.Optional;

import tuti.daos.entidades.Provincia;
/**
 * Clase que permite gestionar la entidad Provincia en el sistema.
 * @author kuttel
 * @version 1.0
 */

public interface ProvinciaService {

	

	/**
	 * Obtiene la lista completa de Provincias
	 * @return Todas las Provincias
	 */
	List<Provincia> getAll();
	
	/**
	 * Obtiene una Provincia determinada
	 * @param idCiudad Identificador de la Provincia buscada
	 * @return Provincia encontrada
	 */
	Optional<Provincia> getById(Long idProv) ;

}
