package karate.chargestation;

import com.intuit.karate.junit5.Karate;

public class ChargeStationRunner {
    @Karate.Test
    Karate testAll() {
        return Karate.run().relativeTo(getClass());
    }

}
