package com.software.jpetstore.controller;

import com.software.jpetstore.domain.Cart;
import com.software.jpetstore.domain.Category;
import com.software.jpetstore.domain.Item;
import com.software.jpetstore.domain.Product;
import com.software.jpetstore.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
//@RestController
//@CrossOrigin


@Controller
public class CatalogController {
  /* @Autowired
    private CatalogService catalogService;

    @GetMapping("/catalog/category/{categoryId}")
    public Category category(@PathVariable("categoryId") String categoryId){
        return catalogService.getCategory(categoryId);
    }

    @GetMapping("/catalog/category/{categoryId}/products")
    public List<Product> products(@PathVariable("categoryId") String categoryId){
        return catalogService.getProductListByCategory(categoryId);
    }

    @GetMapping("/catalog/products/{productId}")
    public List<Item> items(@PathVariable("productId") String productId ){        return catalogService.getItemListByProduct(productId);
    }
    @GetMapping("/catalog/product/{productId}")
    public Product product(@PathVariable("productId") String productId){
        return catalogService.getProduct(productId);
    }
    @GetMapping("/catalog/item/{itemId}")
    public Item item(@PathVariable("itemId") String itemId){
        return catalogService.getItem(itemId);
    }
*/

    private Cart cart = new Cart();
    @Autowired
    private CatalogService catalogService;

    @GetMapping("/catalog/main")
    public String view(HttpSession session, Model model) {
        boolean authenticated = false;
        session.setAttribute("authenticated", authenticated);
        session.setAttribute("cart", cart);
        return "/catalog/main";
    }
    @GetMapping("/catalog/category")
    public String viewCategory(@RequestParam("categoryId") String categoryid, Model model){
        Category category=catalogService.getCategory(categoryid);
        List<Product> productList=catalogService.getProductListByCategory(categoryid);
        model.addAttribute(category);
        model.addAttribute(productList);
        return "/catalog/category";
    }
    @GetMapping("/catalog/product")
    public String viewProduct(@RequestParam("productId") String productid, Model model){
        //System.out.println(productid);
        Product product=catalogService.getProduct(productid);
        List<Item> itemList =catalogService.getItemListByProduct(productid);
        for(Item item:itemList){
            if(item.getAttribute1()==null){
                item.setAttribute1(" ");
            }
            if(item.getAttribute2()==null){
                item.setAttribute2(" ");
            }
            if(item.getAttribute3()==null){
                item.setAttribute3(" ");
            }
            if(item.getAttribute4()==null){
                item.setAttribute4(" ");
            }
            if(item.getAttribute5()==null){
                item.setAttribute5(" ");
            }
        }
        model.addAttribute(product);
        model.addAttribute(itemList);
        return "/catalog/product";
    }
    @GetMapping("/catalog/item")
    public String viewItem(@RequestParam("itemId") String itemid,@RequestParam("productId") String productid, Model model){
        Item item=catalogService.getItem(itemid);

        if(item.getAttribute1()==null){
            item.setAttribute1(" ");
        }
        if(item.getAttribute2()==null){
            item.setAttribute2(" ");
        }
        if(item.getAttribute3()==null){
            item.setAttribute3(" ");
        }
        if(item.getAttribute4()==null){
            item.setAttribute4(" ");
        }
        if(item.getAttribute5()==null){
            item.setAttribute5(" ");
        }
      //  System.out.println("procreate");
        Product product=catalogService.getProduct(productid);





        model.addAttribute(item);
        model.addAttribute(product);

        return "/catalog/item";
    }

    // @GetMapping("/catalog/SearchProducts")
    @RequestMapping(value = "/catalog/SearchProducts",method = RequestMethod.POST)
    public String search(@RequestParam("keyword") String keyword,Model model){
        List<Product> productList=catalogService.searchProductList(keyword);
        model.addAttribute(productList);
        return "/catalog/SearchProducts";
    }

}
