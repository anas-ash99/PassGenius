package com.example.passgenius.common

import java.security.SecureRandom

object PasswordGenerator {
    fun  generatePassword():String {
       val length = 10

        val lowercase = "abcdefghijklmnopqrstuvwxyz".toCharArray()
        val uppercase = "ABCDEFGJKLMNPRSTUVWXYZ".toCharArray()
        val numbers = "0123456789".toCharArray()
        val symbols = "^$?!@#%&".toCharArray()
        val allAllowed = "abcdefghijklmnopqrstuvwxyzABCDEFGJKLMNPRSTUVWXYZ0123456789^$?!@#%&".toCharArray()
        //Use cryptographically secure random number generator
        val random = SecureRandom()

        val password = StringBuilder()


        for (i in (0..length)) {
            password.append(allAllowed[random.nextInt(allAllowed.size)])
        }

        //Ensure password policy is met by inserting required random chars in random positions
        password.insert(random.nextInt(password.length), lowercase[random.nextInt(lowercase.size)])
        password.insert(random.nextInt(password.length), uppercase[random.nextInt(uppercase.size)])
        password.insert(random.nextInt(password.length), numbers[random.nextInt(numbers.size)])
        password.insert(random.nextInt(password.length), symbols[random.nextInt(symbols.size)])

        return password.toString()

    }







}