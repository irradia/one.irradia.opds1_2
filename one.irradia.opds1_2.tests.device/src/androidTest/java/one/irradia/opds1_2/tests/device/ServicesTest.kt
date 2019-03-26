package one.irradia.opds1_2.tests.device

import android.support.test.filters.MediumTest
import android.support.test.runner.AndroidJUnit4
import one.irradia.opds1_2.tests.ServicesContract
import org.junit.runner.RunWith
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@RunWith(AndroidJUnit4::class)
@MediumTest
class ServicesTest : ServicesContract() {

  override fun logger(): Logger {
    return LoggerFactory.getLogger(ServicesTest::class.java)
  }

}