package br.gw.bat.mapper;

import br.gw.bat.entities.product.ProductDTO;
import org.springframework.batch.infrastructure.item.file.mapping.FieldSetMapper;
import org.springframework.batch.infrastructure.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.FormatStyle;
import java.util.Objects;

public class ProductFieldSetMapper implements FieldSetMapper<ProductDTO> {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Override
    public ProductDTO mapFieldSet(FieldSet fieldSet) throws BindException {
        return new ProductDTO(
                fieldSet.readString("description"),
                LocalDate.parse(
                        Objects.requireNonNull(fieldSet.readString("bestby")), formatter
                ),
                fieldSet.readFloat("price")
        );
    }
}
