package test_util.annotation;

import com.example.repository.MessageRepository;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraRepositoriesAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@EnableAutoConfiguration(exclude = {CassandraAutoConfiguration.class, CassandraDataAutoConfiguration.class, CassandraRepositoriesAutoConfiguration.class})
@MockBean(MessageRepository.class)
public @interface DisableDatabaseAutoConfiguration {
}
