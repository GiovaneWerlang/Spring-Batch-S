package br.gw.bat.entities.product;

import java.time.LocalDate;

public record ProductDTO(String description, LocalDate bestby, Float price) {
}
