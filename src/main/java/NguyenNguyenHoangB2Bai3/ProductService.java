package NguyenNguyenHoangB2Bai3;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProductService {
	private List<Product> listProduct = new ArrayList<>();

    @Autowired
    private ProductRepository repo;
     
    public List<Product> listAll() {
        return repo.findAll();
    }
    public void add(Product newProduct) {
		listProduct.add(newProduct);
	}
    public void save(Product product) {
        repo.save(product);
    }
    public Product get(long id) {
        return repo.findById(id).get();
    }
     
//    public void edit(Product editProduct) {
//		Product find = listProduct.get(editProduct.getId());
//		if(find != null) {
//			find.setName(editProduct.getName());
//			find.setImage(editProduct.getImage());
//			find.setPrice(editProduct.getPrice());
//		}
//	} 
//  
    public void delete(long id) {
        repo.deleteById(id);
    }
}