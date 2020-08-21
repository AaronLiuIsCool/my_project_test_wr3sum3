package com.kuaidaoresume.job.config;

import com.kuaidaoresume.common.matching.InMemoryKeywordMatcher;
import com.kuaidaoresume.common.matching.KeywordMatcher;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.plugin.core.SimplePluginRegistry;
import org.springframework.plugin.core.support.PluginRegistryFactoryBean;

import org.springframework.hateoas.client.JsonPathLinkDiscoverer;
import org.springframework.hateoas.client.LinkDiscoverers;
import org.springframework.hateoas.mediatype.hal.HalLinkDiscoverer;
import org.springframework.hateoas.server.LinkRelationProvider;
import org.springframework.hateoas.server.core.DelegatingLinkRelationProvider;
import org.springframework.hateoas.server.core.EvoInflectorLinkRelationProvider;

import com.kuaidaoresume.common.async.ContextCopyingDecorator;
import com.kuaidaoresume.common.config.KuaidaoresumeRestConfig;

import java.io.FileInputStream;
import java.util.*;
import java.util.concurrent.Executor;


@Configuration
@EnableAsync
@Import(value = {KuaidaoresumeRestConfig.class})
@SuppressWarnings(value = "Duplicates")
public class AppConfig {

    public static final String ASYNC_EXECUTOR_NAME = "asyncExecutor";

    @Bean(name=ASYNC_EXECUTOR_NAME)
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // for passing in request scope context
        executor.setTaskDecorator(new ContextCopyingDecorator());
        executor.setCorePoolSize(3); // TODO Aaron add to properties
        executor.setMaxPoolSize(5); // TODO Aaron add to properties
        executor.setQueueCapacity(100); // TODO Aaron add to properties
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setThreadNamePrefix("AsyncThread-");
        executor.initialize();
        return executor;
    }

    /**
     * @see <a href="https://stackoverflow.com/questions/58431876/spring-boot-2-2-0-spring-hateoas-startup-issue">https://stackoverflow.com/questions/58431876/spring-boot-2-2-0-spring-hateoas-startup-issue</a>
     * @see <a href="https://dev.to/otaviotarelhodb/how-to-use-springfox-2-9-2-with-spring-hateoas-2-on-gradle-project-6mn">https://dev.to/otaviotarelhodb/how-to-use-springfox-2-9-2-with-spring-hateoas-2-on-gradle-project-6mn</a>
     *
     */
    @Bean
    public LinkDiscoverers discoverers() {
        List<JsonPathLinkDiscoverer> plugins = new ArrayList<>();
        plugins.add(new HalLinkDiscoverer());
        return new LinkDiscoverers(SimplePluginRegistry.create(plugins));
    }

    @Bean
    public KeywordMatcher keywordMatcher() {
        FileInputStream fis;
        XSSFWorkbook keywordsWorkBook;
        try {
            fis = new FileInputStream(Thread.currentThread().getContextClassLoader()
                    .getResource("matching/keywords.xlsx").getFile());
            keywordsWorkBook = new XSSFWorkbook(fis);
        } catch (Exception e1) {
            try {
                fis = new FileInputStream("matching/keywords.xlsx");
                keywordsWorkBook = new XSSFWorkbook(fis);
            }
            catch (Exception e2){
                return null;
            }
        }

        XSSFSheet keywordsSheet = keywordsWorkBook.getSheetAt(0);
        Iterator<Row> rowIterator = keywordsSheet.iterator();
        Set<String> keywordsSet = new HashSet<>();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                keywordsSet.add(cell.getStringCellValue());
            }
        }
        return new InMemoryKeywordMatcher(keywordsSet);
    }
}
