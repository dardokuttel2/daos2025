package tuti.daos.servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tuti.daos.accesoDatos.IProductoRepo;
import tuti.daos.entidades.Producto;

@Service
public class ProductoServiceImpl implements ProductoService {
	@Autowired
	IProductoRepo repo;
	
	@Override
	public List<Producto> getAll() {
		
		return repo.findAll();
	}

	@Override
	public Optional<Producto> getProductoById(Long id) throws Exception {
		return repo.findById(id);
	}

	@Override
	public List<Producto> getProductosByNombre(String nombre) {
		if(nombre!= null)
			return repo.findByNombreContaining(nombre);
		else
			return repo.findAll();
	}
}
