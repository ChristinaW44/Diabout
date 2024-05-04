package tests

import com.example.diabout.helpers.DetailChecker
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class DetailCheckerTest {

    private lateinit var detailChecker: DetailChecker

    @Before
    fun setUp() {
        detailChecker = DetailChecker()
    }

    @Test
    fun checkPasswordLengthTooShort(){
        val password = "test"
        val condition = detailChecker.checkPasswordLength(password)
        Assert.assertFalse(condition)
    }

    @Test
    fun checkPasswordLengthOk() {
        val password = "testtest"
        val condition = detailChecker.checkPasswordLength(password)
        Assert.assertTrue(condition)
    }

    @Test
    fun checkConfirmPasswordMatch() {
        val password = "test"
        val confirmPassword = "test"
        val condition = detailChecker.checkConfimPassword(password, confirmPassword)
        Assert.assertTrue(condition)
    }

    @Test
    fun checkConfirmPasswordDontMatch() {
        val password = "test"
        val confirmPassword = "test1"
        val condition = detailChecker.checkConfimPassword(password, confirmPassword)
        Assert.assertFalse(condition)
    }

    @Test
    fun checkEmailFalse() {
        val email = "test"
        val condition = detailChecker.checkEmail(email)
        Assert.assertFalse(condition)
    }

    @Test
    fun checkEmailTrue() {
        val email = "test@test"
        val condition = detailChecker.checkEmail(email)
        Assert.assertTrue(condition)
    }

}