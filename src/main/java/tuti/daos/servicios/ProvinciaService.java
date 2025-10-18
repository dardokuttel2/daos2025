package tuti.daos.servicios;


import java.util.List;
import java.util.Optional;

import tuti.daos.entidades.Provincia;
import tuti.daos.excepciones.Excepcion;
import tuti.daos.presentacion.provincias.ProvinciasBuscarForm;
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

	List<Provincia> filter(ProvinciasBuscarForm filter) throws Excepcion;
	
	void deleteByid(Long id);

	void save(Provincia c) throws Excepcion;
}
