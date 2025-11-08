/**
 * @author kuttel
 *
 */
package tuti.daos.servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tuti.daos.accesoDatos.IProvinciaRepo;
import tuti.daos.entidades.Provincia;

@Service
public class ProvinciaServiceImpl implements ProvinciaService {
//	Logger LOG = LoggerFactory.getLogger(CiudadService.class);
//	
	@Autowired
	IProvinciaRepo repo;

	@Override
	public List<Provincia> getAll() {
		return repo.findAll();
	}



	@Override
	public Optional<Provincia>  getById(Long idProvincia) {
		return repo.findById(idProvincia);
	}
	
	

}
