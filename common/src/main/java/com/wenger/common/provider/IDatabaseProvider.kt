package com.wenger.common.provider

import com.google.firebase.database.DatabaseReference

interface IDatabaseProvider {

    fun provideUsersPath(): DatabaseReference

    fun provideLocationsPath(): DatabaseReference

}