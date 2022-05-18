package karate.auth;

import com.intuit.karate.junit5.Karate;

public class AuthRunner {
    @Karate.Test
    Karate testAll() {
        return Karate.run().relativeTo(getClass());
    }

}
