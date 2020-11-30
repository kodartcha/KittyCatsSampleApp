package com.gabriel_codarcea.features.login.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class ValidatorsTest {

    @Test
    fun `empty string is not validated as email`(){
        val emptyString = ""

        assertEquals(isValidEmail(emptyString), false)
    }

    @Test
    fun `no email string is not validated as email`(){
        val noEmailString = "email@email@email"

        assertEquals(isValidEmail(noEmailString), false)
    }

    @Test
    fun `email string is validated as email`(){
        val emailString = "email@email.com"

        assertEquals(isValidEmail(emailString), true)
    }

    @Test
    fun `empty password string is not validated`(){
        val emptyPassword = ""

        assertEquals(isValidPassword(emptyPassword), false)
    }

    @Test
    fun `short password string is not validated`(){
        val shortPassword = "abc"

        assertEquals(isValidPassword(shortPassword), false)
    }

    @Test
    fun `correct password string is validated`(){
        val correctPassword = "abcabc"

        assertEquals(isValidPassword(correctPassword), true)
    }

}