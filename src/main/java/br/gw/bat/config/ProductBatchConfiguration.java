package br.gw.bat.config;

import br.gw.bat.entities.product.Product;
import br.gw.bat.entities.product.ProductDTO;
import br.gw.bat.listener.ProductImportJobCompletionListener;
import br.gw.bat.mapper.ProductFieldSetMapper;
import br.gw.bat.processor.ProductItemProcessor;
import br.gw.bat.writer.ProductItemWriter;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.LineMapper;
import org.springframework.batch.infrastructure.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.infrastructure.item.file.separator.DefaultRecordSeparatorPolicy;
import org.springframework.batch.infrastructure.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ProductBatchConfiguration {

    private static final String FIRST_STEP = "step-1";
    private static final String JOB_NAME = "productImportJob";

    @Bean
    public ItemReader<ProductDTO> productItemReader() {
        LineMapper<ProductDTO> mapper = new DefaultLineMapper<ProductDTO>(){{
            setLineTokenizer(
                    new DelimitedLineTokenizer() {{
                        setNames("id", "description", "bestby", "price");
                        setDelimiter(",");
                        setQuoteCharacter('\"');
                        setStrict(false);
                    }}
            );
            setFieldSetMapper(new ProductFieldSetMapper());
        }};
        FlatFileItemReader<ProductDTO> reader = new FlatFileItemReader<ProductDTO>(mapper);
        reader.setResource(new ClassPathResource("data/products.csv"));
        reader.setLinesToSkip(1);
        reader.setRecordSeparatorPolicy(new DefaultRecordSeparatorPolicy());

        return reader;
    }

    @Bean
    public ItemProcessor<ProductDTO, Product> productItemProcessor() {
        return new ProductItemProcessor();
    }

    @Bean
    public ProductItemWriter productItemWriter() {
        return new ProductItemWriter();
    }

    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                      ItemReader<ProductDTO> productItemReader, ItemProcessor<ProductDTO, Product> productItemProcessor,
                      ProductItemWriter productItemWriter) {
        return new StepBuilder(FIRST_STEP, jobRepository).<ProductDTO, Product>chunk(10)
                .transactionManager(transactionManager)
                .reader(productItemReader)
                .processor(productItemProcessor)
                .writer(productItemWriter)
                .build();
    }

    @Bean("productImportJob")
    public Job productImportJob(JobRepository jobRepository, Step step1, ProductImportJobCompletionListener listener) {
        return new JobBuilder(JOB_NAME, jobRepository)
                .listener(listener)
                .start(step1)
                .build();
    }

}
