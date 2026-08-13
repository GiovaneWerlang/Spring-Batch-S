package br.gw.bat.writer;

import br.gw.bat.entities.product.Product;
import br.gw.bat.entities.product.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

public class ProductItemWriter implements ItemWriter<Product> {

    private static final Logger log = LoggerFactory.getLogger(ProductItemWriter.class);

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void write(Chunk<? extends Product> items) throws Exception {
        log.info("Writing data for Product: {}", items.getItems().size());
        productRepository.saveAll(items.getItems());
    }

}
