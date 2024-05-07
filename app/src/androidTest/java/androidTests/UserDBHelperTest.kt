package androidTests

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.example.diabout.database.Targets
import com.example.diabout.database.UserDBHelper
import com.example.diabout.database.Users
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class UserDBHelperTest {

    //unit tests for various functions in the UserDBHelper class

    lateinit var context : Context
    lateinit var dbHelper: UserDBHelper
    var userID: Int = 0

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        dbHelper = UserDBHelper(context)

        //makes a user
        val user = Users()
        user.email = "kevin@gmail"
        user.name = "kevin"
        user.password = "password"

        dbHelper.addUser(user)
        userID = dbHelper.getIdFromEmail("kevin@gmail")

        val targets = Targets(userID, 50, 60, 70,80)
        dbHelper.addUserTargets(targets)
    }

    @After
    fun tearDown() {
        //deletes the user created
        dbHelper.deleteUser(userID.toString())
    }

    @Test
    fun checkUserExists() {
        val userExists = dbHelper.findUser("kevin@gmail","password")
        Assert.assertTrue(userExists)
    }

    @Test
    fun checkUserDoesntExists() {
        val userExists = dbHelper.findUser("maris@gmail","password")
        Assert.assertFalse(userExists)
    }

    @Test
    fun getName() {
        val name = dbHelper.getNameFromID(userID.toString())
        Assert.assertEquals("kevin", name)
    }

    @Test
    fun getEmail() {
        val email = dbHelper.getEmailFromID(userID.toString())
        Assert.assertEquals("kevin@gmail", email)
    }

    @Test
    fun getTargetSteps() {
        val target = dbHelper.getStepsTargets(userID.toString())
        Assert.assertEquals(50, target)
    }

    @Test
    fun getTargetCarbs() {
        val target = dbHelper.getCarbsTargets(userID.toString())
        Assert.assertEquals(60, target)
    }

    @Test
    fun getTargetMinGlucose() {
        val target = dbHelper.getMinGlucoseTargets(userID.toString())
        Assert.assertEquals(70, target)
    }

    @Test
    fun getTargetMaxGlucose() {
        val target = dbHelper.getMaxGlucoseTargets(userID.toString())
        Assert.assertEquals(80, target)
    }

    @Test
    fun checkNameIsUpdated(){
        dbHelper.updateName(userID.toString(), "Clive")
        val nameRetrieved = dbHelper.getNameFromID(userID.toString())
        Assert.assertEquals("Clive", nameRetrieved)
    }
}