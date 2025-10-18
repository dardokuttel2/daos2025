/**
 * @author kuttel
 *
 */
package tuti.daos.servicios;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import tuti.daos.accesoDatos.ICiudadRepo;
import tuti.daos.entidades.Ciudad;
import tuti.daos.excepciones.Excepcion;
import tuti.daos.presentacion.ciudades.CiudadesBuscarForm;

@Service
public class CiudadServiceImpl implements CiudadService {
//	Logger LOG = LoggerFactory.getLogger(CiudadService.class);
//	
	
	@Autowired
	private  Validator validator;
	
	@Autowired
	ICiudadRepo repo;

	@Override
	public List<Ciudad> getAll() {
		
		return repo.findAll();
	}



	@Override
	public Optional<Ciudad> getById(Long idCiudad) throws Excepcion {

		return repo.findById(idCiudad);
	}
	
	
	@Override
	public List<Ciudad> filter(CiudadesBuscarForm filter) throws Excepcion
	{
		//ver https://docs.spring.io/spring-data/jpa/docs/1.5.0.RELEASE/reference/html/jpa.repositories.html
		if(filter.getNombre()==null && filter.getProvinciaSeleccionada()==null)
			//return repo.findAll();
			throw new Excepcion("Es necesario al menos un filtro",400);
		else
			return repo.findByNombreOrIdProvincia(filter.getNombre(),filter.getProvinciaSeleccionada());
				
	}



	@Override
	public void deleteByid(Long id) {
		repo.deleteById(id);
		
	}



	@Override
	public void save(Ciudad c) throws Excepcion {
		if(c.getId()==null && !repo.findByNombreAndIdProvincia(c.getNombre(), c.getProvincia().getId()).isEmpty()) //estoy dando de alta una nueva ciudad y ya existe una igual?
			throw new Excepcion("Ya existe una ciudad con el mismo nombre, para la misma provincia",400);  
		else
		{
			if(!repo.findByNombreAndIdProvinciaAndIdNot(c.getNombre(), c.getProvincia().getId(),c.getId()).isEmpty()) //si edito el nombre, valido que no exista otra con el mismo nombre?
				throw new Excepcion("Existe otra ciudad con el mismo nombre para la misma provincia",400);
			else
				repo.save(c);
		}
		
	}



	@Override
	public void delete(Long id) {
		repo.deleteById(id);		
	}
	
	@Override
	public void insert(Ciudad c) throws Exception {
		
		Set<ConstraintViolation<Ciudad>> cv = validator.validate(c);
		if(cv.size()>0)
		{
			String err="";
			for (ConstraintViolation<Ciudad> constraintViolation : cv) {
				err+=constraintViolation.getPropertyPath()+": "+constraintViolation.getMessage()+"\n";
			}
			throw new Excepcion(err,400);
		}
		else if(c.getId()!=null)
		{
			throw new Excepcion("El id es generado automáticamente por el sistema, no puede ser establecido por el usuario.",400); 
		}
//		else if(getById(c.getId()).isPresent())
//		{
//			throw new Excepcion("Ya existe una ciudad con ese Id.",400); --- esto es si el id fuera el cp por ejemplo, en este caso es autoincremental así que no espero que venga seteado
//		}
		else
			repo.save(c);
	}



	@Override
	public void update(Ciudad c) {
		repo.save(c);
		
	}

}
