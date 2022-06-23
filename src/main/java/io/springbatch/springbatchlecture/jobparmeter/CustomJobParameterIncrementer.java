package io.springbatch.springbatchlecture.jobparmeter;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomJobParameterIncrementer implements JobParametersIncrementer {
    static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyyMMdd-hhmmss");

    @Override
    public JobParameters getNext(JobParameters jobParameters) {
        String id = FORMAT.format(new Date());
        return new JobParametersBuilder().addString("run.id",id).toJobParameters();
    }
}
