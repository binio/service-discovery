import dao.ServiceDaoTest;
import domain.RedisServiceBuilderTest;
import domain.ServiceTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import service.ServiceDiscoverySdkTest;

import static org.junit.runners.Suite.*;

@RunWith(Suite.class)
@SuiteClasses({
        ServiceDaoTest.class,
        RedisServiceBuilderTest.class,
        ServiceTest.class,
        ServiceDiscoverySdkTest.class

})
public class TestRunner {


}
