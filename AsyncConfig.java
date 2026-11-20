import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.Executor;

@Configuration
public class AsyncConfig {

    @Bean("demoExecutor")
    public Executor demoExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);      // 3 workers always ready
        executor.setMaxPoolSize(5);       // Up to 5 workers if very busy
        executor.setQueueCapacity(10);    // 10 tasks can wait in line
        executor.setThreadNamePrefix("worker-");
        executor.initialize();
        return executor;
    }
}