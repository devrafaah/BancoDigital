package com.example.bancodigital.data.repository.profile

import android.net.Uri
import com.example.bancodigital.data.model.User
import com.example.bancodigital.util.FirebaseHelper
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class ProfileDataSourceImp @Inject constructor(
    database: FirebaseDatabase,
    storage: FirebaseStorage
) : ProfileDataSource {

    private val dataBaseReference = database.reference
        .child("profile")

    private val storageReference = storage.reference
        .child("Images")
        .child("Profiles")
        .child("${FirebaseHelper.getUserId()}.jpeg")
    override suspend fun saveprofile(user: User) {
        return suspendCoroutine {  continuation ->
            dataBaseReference
                .child(FirebaseHelper.getUserId())
                .setValue(user).addOnCompleteListener {
                if(it.isSuccessful) {
                    continuation.resumeWith(Result.success(Unit))
                }else {
                    it.exception?.let {
                        continuation.resumeWith(Result.failure(it))
                    }
                }
            }
        }
    }

    override suspend fun getprofile(id : String): User {
        return suspendCoroutine { continuation ->
            dataBaseReference
                .child(id)
                .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)
                    user?.let { continuation.resumeWith(Result.success(it))}

                }

                override fun onCancelled(error: DatabaseError) {
                    continuation.resumeWith(Result.failure(error.toException()))
                }

            })
        }
    }

    override suspend fun getProfileList(): List<User> {
        return suspendCoroutine { continuation ->
            dataBaseReference
                .addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val userList: MutableList<User> = mutableListOf()
                        for (ds in snapshot.children) {
                            val user = ds.getValue(User::class.java)
                            user?.let {
                                if(it.id != FirebaseHelper.getUserId()) {
                                    userList.add(user)
                                }
                            }
                        }
                        continuation.resumeWith(Result.success(userList))

                    }

                    override fun onCancelled(error: DatabaseError) {
                        continuation.resumeWith(Result.failure(error.toException()))
                    }

                })
        }
    }

    override suspend fun saveImage(imageProfile: String): String {
        return suspendCoroutine {  continuation ->
            val uploadTask = storageReference.putFile(Uri.parse(imageProfile))
            uploadTask.addOnSuccessListener {
                storageReference.downloadUrl.addOnCompleteListener { task ->
                    continuation.resumeWith(Result.success(task.result.toString()))
                }
            }.addOnFailureListener{
                continuation.resumeWith(Result.failure(it))
            }
        }
    }


}