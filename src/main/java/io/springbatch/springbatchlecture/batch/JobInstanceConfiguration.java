package io.springbatch.springbatchlecture.batch;

import io.springbatch.springbatchlecture.step.tasklet.OrderBookApi;
import io.springbatch.springbatchlecture.jobparmeter.CustomJobParameterIncrementer;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class JobInstanceConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final OrderBookApi orderBookApi;
    @Bean
    public Job job(){
        return jobBuilderFactory.get("upbit")
                .start(step2())
                .next(step1())
                .incrementer(new
                        CustomJobParameterIncrementer())
                .build();
    }

    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .tasklet(orderBookApi)
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("step1 has executed");
//                        throw  new RuntimeException("step2 has failed");
                        System.out.println("stepContribution.getStepExecution().getJobExecution().getJobInstance().getJobName() = " + stepContribution.getStepExecution().getJobExecution().getJobInstance().getJobName());
//
                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }
}
