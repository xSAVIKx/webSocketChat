package chat.coder;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
    CommandPojoDecoderTest.class, CommandPojoEncoderTest.class, CommandSerializerTest.class,
    ListCommandPojoEncoderTest.class
})
public class AllTests {

}
