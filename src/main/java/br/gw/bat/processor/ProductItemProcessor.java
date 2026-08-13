package br.gw.bat.processor;

import br.gw.bat.entities.product.Product;
import br.gw.bat.entities.product.ProductDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.infrastructure.item.ItemProcessor;

import java.util.Objects;

public class ProductItemProcessor implements ItemProcessor<ProductDTO, Product> {

    private static final Logger log = LoggerFactory.getLogger(ProductItemProcessor.class);

    private static final String ACTIVE_STATUS = "ACTIVE";

    private static final String ADMIN_USER = "admin";

    /**
     * Process the provided ProductDTO and convert it to Product
     *
     * @param productDTO ProductDTO
     * @return Product
     * @throws Exception
     */
    @Override
    public Product process(ProductDTO productDTO) throws Exception {
        if (Objects.nonNull(productDTO)) {
            log.debug("Processing data for ProductDTO: {}", productDTO);
            return new Product(null, productDTO.description(), productDTO.bestby(), productDTO.price());
        } else {
            log.error("Error: While processing data: ProductDTO is null");
            return null;
        }
    }

}
