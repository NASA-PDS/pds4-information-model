package cucumber;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty", "html:target/cucumber.html"},
    features = "features/", glue = "cucumber")
public class CucumberTest {

  public CucumberTest() {}

}
