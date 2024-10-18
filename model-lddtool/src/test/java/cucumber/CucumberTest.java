package cucumber;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty", "html:target/cucumber-reports/cucumber.html", "usage"},
    features = "src/test/resources/features/", glue = "cucumber", monochrome = true)
public class CucumberTest {

  public CucumberTest() {}

}
