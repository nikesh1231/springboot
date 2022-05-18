package karate.chargesite;

import com.intuit.karate.junit5.Karate;

public class ChargeSiteRunner {
    @Karate.Test
    Karate testAll() {
        return Karate.run().relativeTo(getClass());
    }

}
