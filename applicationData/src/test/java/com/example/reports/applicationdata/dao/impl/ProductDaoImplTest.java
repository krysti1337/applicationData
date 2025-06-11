package com.example.reports.applicationdata.dao.impl;


import com.example.reports.applicationdata.model.Product;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ProductDaoImplTest {

    @Autowired
    private ProductDaoImpl productDao;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void testSaveAndFindById(){
        Product product = new Product();
        product.setStockCode("9000");
        product.setDescription("Description");

        productDao.save(product);
        entityManager.flush();

        Product found = productDao.findById("9000");
        assertNotNull(found);
        assertEquals("Description", found.getDescription());
    }

    @Test
    public void testFindAllProducts(){
        Product product1 = new Product();
        product1.setStockCode("9000");
        product1.setDescription("Description1");

        Product product2 = new Product();
        product2.setStockCode("9001");
        product2.setDescription("Description2");

        productDao.save(product1);
        productDao.save(product2);
        entityManager.flush();

        List<Product> products = productDao.findAll();
        assertEquals(2, products.size());
    }

    @Test
    public void testUpdateProductDescription(){
        Product product = new Product();
        product.setStockCode("9000");
        product.setDescription("Description");

        productDao.save(product);
        entityManager.flush();

        product.setDescription("Description2");
        product.setStockCode("9000");
        productDao.update(product);
        entityManager.flush();

        Product update = productDao.findById(product.getStockCode());
        assertEquals("Description2", update.getDescription());
    }
}
