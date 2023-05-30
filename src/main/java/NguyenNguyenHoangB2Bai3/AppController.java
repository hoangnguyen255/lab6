package NguyenNguyenHoangB2Bai3;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import jakarta.validation.Valid;


@Controller
public class AppController {
	@Autowired
	private ProductService service;

	@RequestMapping("/")
	public String viewHomePage(Model model) {
		List<Product> listProducts = service.listAll();
		model.addAttribute("listProducts", listProducts);
		return "index";
	}

	@RequestMapping("/new")
	public String showNewProductForm(Model model) {
		Product product = new Product();
		model.addAttribute("product", product);
		return "new_product";
	}
	@PostMapping("/new")
	public String create(@Valid Product newProduct, @RequestParam MultipartFile imageProduct, BindingResult result,
			Model model) {
		if (result.hasErrors()) {
			model.addAttribute("product", newProduct);
			return "products/create";
		}

		if (imageProduct != null && imageProduct.getSize() > 0) {
			try {
				File saveFile = new ClassPathResource("static/images").getFile();
				String newImageFile = UUID.randomUUID() + ".png";
				java.nio.file.Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + newImageFile);
				Files.copy(imageProduct.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				newProduct.setImage(newImageFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		service.add(newProduct);;
		return "redirect:/products";
	}
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String saveProduct(@ModelAttribute("product") Product product) {
		service.save(product);
		return "redirect:/";
	}

	@RequestMapping("/edit/{id}")
	public ModelAndView showEditProductForm(@PathVariable(name = "id") Long id) {
		ModelAndView mav = new ModelAndView("edit_product");
		Product product = service.get(id);
		mav.addObject("product", product);
		return mav;
	}
	@PostMapping("/edit")
	public String edit(@Valid Product editProduct, @RequestParam MultipartFile imageProduct, BindingResult result,
			Model model) {
		if (result.hasErrors()) {
			model.addAttribute("product", editProduct);
			return "products/edit";
		}

		if (imageProduct != null && imageProduct.getSize() > 0) {
			try {
				byte[] bytes = imageProduct.getBytes();
				File saveFile = new ClassPathResource("static/images").getFile();
				java.nio.file.Path path = Paths
						.get(saveFile.getAbsolutePath() + File.separator + editProduct.getImage());
				Files.write(path, bytes);
				Files.copy(imageProduct.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//service.edit(editProduct);
		return "redirect:/products";
	}

	@RequestMapping("/delete/{id}")
	public String deleteProduct (@PathVariable(name="id")Long id) {
		service.delete(id);
		return "redirect:/";  
	}
}