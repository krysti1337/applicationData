package com.example.reports.applicationdata.service;

import com.example.reports.applicationdata.dao.impl.GenericDao;
import com.example.reports.applicationdata.model.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private GenericDao<Product, String> productDao;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    public void testSaveProduct() {

        Product product = new Product();
        product.setStockCode("200");
        product.setDescription("Product Description 1");

        productService.save(product);

        verify(productDao).save(product);
    }

    @Test
    public void testFindByStockId() {

        Product product = new Product();
        product.setStockCode("201");
        product.setDescription("Product Description 2");

        when(productDao.findById(("201"))).thenReturn(product);

        Product result = productService.findById("201");

        assertNotNull(result);
        assertEquals("Product Description 2", result.getDescription());
        verify(productDao).findById(("201"));
    }

    @Test
    public void testFindAllProducts() {
        List<Product> products = List.of(
                new Product() {{
                    setStockCode("200");
                    setDescription("Product Description 1");
                }},
                new Product() {{
                    setStockCode("201");
                    setDescription("Product Description 2");
                }},
                new Product() {{
                    setStockCode("300");
                    setDescription("Product Description 3");
                }}
        );

        when(productDao.findAll()).thenReturn(products);

        List<Product> result = productService.findAll();

        assertEquals(3, result.size());
        verify(productDao).findAll();
    }

    @Test
    public void testUpdateProduct() {
        Product product = new Product();
        product.setStockCode("200");
        product.setDescription("Product Description 1");

        productService.update(product);

        verify(productDao).update(product);
    }

    @Test
    public void testDeleteProduct() {
        Product product = new Product();
        product.setStockCode("200");
        product.setDescription("Product Description 1");

        productService.delete(product);

        verify(productDao).delete(product);
    }
}
