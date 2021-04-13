package com.sklagat46.reylla.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.sklagat46.reylla.activities.SignInActivity
import com.sklagat46.reylla.activities.SignUpActivity
import com.sklagat46.reylla.activities.agentclients.RegisterCustomer
import com.sklagat46.reylla.activities.serviceproviders.CompanyRegister
import com.sklagat46.reylla.activities.serviceproviders.IndividualRegister
import com.sklagat46.reylla.activities.serviceproviders.addingNewService.AddHairServiceActivity
import com.sklagat46.reylla.activities.serviceproviders.ui.gallery.AddGalleryImagesActivity
import com.sklagat46.reylla.model.*
import com.sklagat46.reylla.utils.Constants

/**
 * A custom class where we will add the operation performed for the firestore database.
 */
class FirestoreClass {

    // Create a instance of Firebase Firestore
    private val mFireStore = FirebaseFirestore.getInstance()

    /**
     * A function to make an entry of the registered user in the firestore database.
     */
    fun registerUser(activity: SignUpActivity, userInfo: User) {

        mFireStore.collection(Constants.USERS)
            // Document ID for users fields. Here the document it is the User ID.
            .document(getCurrentUserID())
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {

                // Here call a function of base activity for transferring the result to it.
                activity.userRegisteredSuccess()
            }
            .addOnFailureListener { e ->
                Log.e(
                    activity.javaClass.simpleName,
                    "Error writing document",
                    e
                )
            }
    }

    /**
     * A function to make an entry of the registered Individual service provider in the firestore database.
     */
    fun registerIndividualProvider(activity: IndividualRegister, individualInfo: IndividualProviders) {
        mFireStore.collection(Constants.INDIVIDUALPROVIDERS)
            // Document ID for users fields. Here the document it is the User ID.
            .document(getCurrentUserID())
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge
            .set(individualInfo, SetOptions.merge())
            .addOnSuccessListener {

                // Here call a function of base activity for transferring the result to it.
                activity.individualRegisteredSuccess()
            }
            .addOnFailureListener { e ->
                Log.e(
                    activity.javaClass.simpleName,
                    "Error writing document",
                    e
                )
            }
    }

    /**
     * A function to make an entry of the registered company in the firestore database.
     */
    fun registerCustomer(activity: RegisterCustomer, customerInfor: Customer) {

        mFireStore.collection(Constants.CUSTOMERS)
            // Document ID for users fields. Here the document it is the User ID.
            .document(getCurrentUserID())
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge
            .set(customerInfor, SetOptions.merge())
            .addOnSuccessListener {

                // Here call a function of base activity for transferring the result to it.
                activity.customerRegisteredSuccess()
            }
            .addOnFailureListener { e ->
                Log.e(
                    activity.javaClass.simpleName,
                    "Error writing document",
                    e
                )
            }
    }

    /**
     * A function to make an entry of the registered customer in the firestore database.
     */
    fun registerCompany(activity: CompanyRegister, companyInfor: Company) {

        mFireStore.collection(Constants.COMPANIES)
            // Document ID for users fields. Here the document it is the User ID.
            .document(getCurrentUserID())
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge
            .set(companyInfor, SetOptions.merge())
            .addOnSuccessListener {

                // Here call a function of base activity for transferring the result to it.
                activity.companyRegisteredSuccess()
            }
            .addOnFailureListener { e ->
                Log.e(
                    activity.javaClass.simpleName,
                    "Error writing document",
                    e
                )
            }
    }

    /**
     * A function to make an entry of the user's product in the cloud firestore database.
     */
    fun uploadProductDetails(activity: AddHairServiceActivity, serviceInfor: Service) {

        mFireStore.collection(Constants.HAIR_SERVICES)
            .document()
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge
            .set(serviceInfor, SetOptions.merge())
            .addOnSuccessListener {
                // Here call a function of base activity for transferring the result to it.
                activity.serviceUploadSuccess()
            }
            .addOnFailureListener { e ->

                activity.hideProgressDialog()

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while uploading the service details.",
                    e
                )
            }
    }

    /**
     * A function to SignIn using firebase and get the user details from Firestore Database.
     */
    fun signInUser(activity: SignInActivity) {

        // Here we pass the collection name from which we wants the data.
        mFireStore.collection(Constants.INDIVIDUALPROVIDERS)
            // The document id to get the Fields of user.
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.e(
                    activity.javaClass.simpleName, document.toString()
                )

                // Here we have received the document snapshot which is converted into the User Data model object.
                val loggedInUser = document.toObject(User::class.java)!!

                // Here call a function of base activity for transferring the result to it.
//                activity.signInSuccess(loggedInUser)
            }
            .addOnFailureListener { e ->
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while getting loggedIn user details",
                    e
                )
            }
    }

//    // A function to upload the image to the cloud storage.
//    fun uploadImageToCloudStorage(activity: Activity, imageFileURI: Uri?, imageType: String) {
//
//        //getting the storage reference
//        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
//            imageType + System.currentTimeMillis() + "."
//                    + Constants.getFileExtension(
//                activity,
//                imageFileURI
//            )
//        )
//
//        //adding the file to reference
//        sRef.putFile(imageFileURI!!)
//            .addOnSuccessListener { taskSnapshot ->
//                // The image upload is success
//                Log.e(
//                    "Firebase Image URL",
//                    taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
//                )
//
//                // Get the downloadable url from the task snapshot
//                taskSnapshot.metadata!!.reference!!.downloadUrl
//                    .addOnSuccessListener { uri ->
//                        Log.e("Downloadable Image URL", uri.toString())
//
//                        // Here call a function of base activity for transferring the result to it.
//                        when (activity) {
//                            is UserProfileActivity -> {
//                                activity.imageUploadSuccess(uri.toString())
//                            }
//
//                            is AddProductActivity -> {
//                                activity.imageUploadSuccess(uri.toString())
//                            }
//                        }
//                    }
//            }
//            .addOnFailureListener { exception ->
//
//                // Hide the progress dialog if there is any error. And print the error in log.
//                when (activity) {
//                    is UserProfileActivity -> {
//                        activity.hideProgressDialog()
//                    }
//
//                    is AddProductActivity -> {
//                        activity.hideProgressDialog()
//                    }
//                }
//
//                Log.e(
//                    activity.javaClass.simpleName,
//                    exception.message,
//                    exception
//                )
//            }
//    }

//    /**
//     * A function to make an entry of the user's product in the cloud firestore database.
//     */
//    fun uploadProductDetails(activity: AddProductActivity, productInfo: Service) {
//
//        mFireStore.collection(Constants.PRODUCTS)
//            .document()
//            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge
//            .set(productInfo, SetOptions.merge())
//            .addOnSuccessListener {
//
//                // Here call a function of base activity for transferring the result to it.
//                activity.productUploadSuccess()
//            }
//            .addOnFailureListener { e ->
//
//                activity.hideProgressDialog()
//
//                Log.e(
//                    activity.javaClass.simpleName,
//                    "Error while uploading the product details.",
//                    e
//                )
//            }
//    }

    /**
     * A function for getting the user id of current logged user.
     */
    fun getCurrentUserID(): String {
        // TODO (Step 1: Return the user id if he is already logged in before or else it will be blank.)
        // START
        // An Instance of currentUser using FirebaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser

        // A variable to assign the currentUserId if it is not null or else it will be blank.
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }

        return currentUserID
        // END
    }

    fun loadGalleryData(addGalleryImagesActivity: AddGalleryImagesActivity) {
        TODO("Not yet implemented")
    }
}