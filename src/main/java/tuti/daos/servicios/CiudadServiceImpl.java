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
import tuti.daos.entidades.Provincia;
import tuti.daos.excepciones.Excepcion;
import tuti.daos.presentacion.ciudades.CiudadDTO;

@Service
public class CiudadServiceImpl implements CiudadService {
//	Logger LOG = LoggerFactory.getLogger(CiudadService.class);
//	
	
	@Autowired
	private  Validator validator;
	
	@Autowired
	ICiudadRepo repo;

	@Autowired
	private ProvinciaService provinciaService;

	@Override
	public List<Ciudad> getAll() {
		
		return repo.findAll();
	}



	@Override
	public Optional<Ciudad> getById(Long idCiudad) throws Excepcion {

		return repo.findById(idCiudad);
	}
	
	

	@Override
	public void deleteByid(Long id) {
		repo.deleteById(id);
		
	}



	@Override
	public void save(Ciudad c) throws Excepcion {
		if(c.getId()==null && !repo.findByNombreAndIdProvincia(c.getNombre(), c.getProvincia().getId()).isEmpty()) //estoy dando de alta una nueva ciudad y ya existe una igual?
			throw new Excepcion("Nombre","Existe otra ciudad con el mismo nombre para la misma provincia",400);
		else
		{
			if(!repo.findByNombreAndIdProvinciaAndIdNot(c.getNombre(), c.getProvincia().getId(),c.getId()).isEmpty()) //si edito el nombre, valido que no exista otra con el mismo nombre?
				throw new Excepcion("Nombre","Existe otra ciudad con el mismo nombre para la misma provincia",400);
			else
				repo.save(c);
		}
		
	}



	@Override
	public void delete(Long id) {
		repo.deleteById(id);		
	}
	
	@Override
	public Long insert(CiudadDTO dto) throws Exception {
		
		Ciudad c = dto.toPojo();
		Optional<Provincia> p = provinciaService.getById(dto.getIdProvincia());
		if(p.isPresent())
			c.setProvincia(p.get());
		else
		{
			throw new Excepcion("IdProvincia","La provincia asociada no se encuentra en la base de datos.",404); 
		}
		
		//Esta parte podrìa obviarse si ya se validó en el controller la ciudad a insertar
		Set<ConstraintViolation<Ciudad>> cv = validator.validate(c);
		if(cv.size()>0)
		{
			String err="";
			for (ConstraintViolation<Ciudad> constraintViolation : cv) {
				err+=constraintViolation.getPropertyPath()+": "+constraintViolation.getMessage()+"\n";
			}
			throw new Excepcion("",err,400);
		}
		else
		{
			Ciudad ciudadCreada=repo.save(c);
			return ciudadCreada.getId();
		}
	}



	@Override
	public Ciudad update(CiudadDTO dto, Long id) throws Excepcion {
		
				
		Optional<Ciudad> c = repo.findById(id);
		if(c.isPresent())
		{
			
			Optional<Provincia> p = provinciaService.getById(dto.getIdProvincia());
			if(p.isPresent())
			{
				c.get().setProvincia(p.get());
				return repo.save(c.get());
			}
			else
				throw new Excepcion("idProvincia","La provincia asociada no se encuentra en la base de datos.",404);
			
		}
		else
			throw new Excepcion("","No se encuentra la ciudad que desea modificar.",404); //404: not found
		
		
		
	}

}
