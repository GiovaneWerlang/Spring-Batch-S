package br.gw.bat.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.parameters.InvalidJobParametersException;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.launch.JobInstanceAlreadyCompleteException;

import org.springframework.batch.core.launch.JobLauncher;

import org.springframework.batch.core.launch.JobRestartException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/product-jobs")
public class ProductJobController {

    private static final Logger log = LoggerFactory.getLogger(ProductJobController.class);
    private final JobLauncher jobLauncher;

    private final Job productImportJob;

    public ProductJobController(
            @Qualifier("productImportJob") Job productImportJob,
            JobLauncher jobLauncher) {
        this.productImportJob = productImportJob;
        this.jobLauncher = jobLauncher;
    }

    @PostMapping("/import")
    public ResponseEntity<Void> importProducts() throws JobInstanceAlreadyCompleteException, InvalidJobParametersException, JobExecutionAlreadyRunningException, JobRestartException {
        log.info("REST request to import products");
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(productImportJob, jobParameters);
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().build();
    }
}
