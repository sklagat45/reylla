package com.sklagat46.reylla.firebase

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import com.sklagat46.reylla.activities.SignInActivity
import com.sklagat46.reylla.activities.SignUpActivity
import com.sklagat46.reylla.activities.agentclients.RegisterCustomer
import com.sklagat46.reylla.activities.serviceproviders.*
import com.sklagat46.reylla.activities.serviceproviders.addingNewService.*
import com.sklagat46.reylla.model.*
import com.sklagat46.reylla.utils.Constants


/**
 * A custom class where we will add the operation performed for the firestore database.
 */
class FirestoreClass {

    @JvmField
    // Create a instance of Firebase Firestore
    val mFireStore = FirebaseFirestore.getInstance()

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
    fun registerIndividualProvider(
        activity: IndividualRegister,
        individualInfo: IndividualProviders
    ) {
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
     * A function to get the product details based on the product id.
     */
    fun getServiceListFromFirestoreDB(activity: HairCareActivity, service: Service) {

        // The collection name for PRODUCTS
        mFireStore.collection(Constants.HAIR_SERVICES)
            .document(getCurrentUserID())
            .get() // Will get the document snapshots.
            .addOnSuccessListener { document ->

                // Here we get the product details in the form of document.
                Log.e(activity.javaClass.simpleName, document.toString())

                // Convert the snapshot to the object of Product data model class.
                val service = document.toObject(Service::class.java)!!

//                activity.productDetailsSuccess(service)
            }
            .addOnFailureListener { e ->

                // Hide the progress dialog if there is an error.
//                activity.hideProgressDialog()

                Log.e(activity.javaClass.simpleName, "Error while getting the product details.", e)
            }
    }


    /**
     * A function to make an entry of the user's product in the cloud firestore database.
     */
    fun uploadHairDetails(activity: AddHairServiceActivity, serviceInfor: Service) {
        mFireStore.collection(Constants.HAIR_SERVICES)
            .document()
//            .document(getCurrentUserID() + "." + System.currentTimeMillis())
//            .whereEqualTo("styleName", serviceInfor.styleName)
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
     * A function to make an entry of the user's product in the cloud firestore database.
     */
    fun uploadBridalDetails(activity: AddBridalServiceActivity, bridalInfor: BridalService) {

        mFireStore.collection(Constants.BRIDAL_SERVICES)
            .document()
//            .whereEqualTo("styleName", serviceInfor.styleName)
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge
            .set(bridalInfor, SetOptions.merge())
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
     * A function to make an entry of the user's product in the cloud firestore database.
     */
    fun uploadNailDetails(activity: AddNailCareServiceActivity, nailInfor: NailService) {

        mFireStore.collection(Constants.NAIL_SERVICES)
            .document()
//            .whereEqualTo("styleName", serviceInfor.styleName)
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge
            .set(nailInfor, SetOptions.merge())
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
     * A function to make an entry of the user's product in the cloud firestore database.
     */
    fun uploadMakeupDetails(activity: AddMakeUpServiceActivity, makeupInfor: MakeupService) {

        mFireStore.collection(Constants.MAKEUP_SERVICES)
            .document()
//            .whereEqualTo("styleName", serviceInfor.styleName)
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge
            .set(makeupInfor, SetOptions.merge())
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
     * A function to make an entry of the user's product in the cloud firestore database.
     */
    fun uploadMassageDetails(activity: AddMassageServiceActivity, massageInfor: MassageService) {

        mFireStore.collection(Constants.MASSAGE_SERVICES)
            .document()
//            .whereEqualTo("styleName", serviceInfor.styleName)
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge
            .set(massageInfor, SetOptions.merge())
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
     * A function to make an entry of the user's product in the cloud firestore database.
     */
    fun uploadTatColourDetails(
        activity: AddTatooAndColourServiceActivity,
        tatColourInfor: TatColorService
    ) {

        mFireStore.collection(Constants.TATCOLOUR_SERVICES)
            .document()
//            .whereEqualTo("styleName", serviceInfor.styleName)
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge
            .set(tatColourInfor, SetOptions.merge())
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


    fun deleteService(activity: Activity, serviceID: String) {
        TODO("Not yet implemented")
    }

    fun getHairServiceList(context: Context, serviceName: String) {
        // The collection name for PRODUCTS
        mFireStore.collection(serviceName)
            .whereEqualTo(Constants.PROVIDER_ID, getCurrentUserID())
            .addSnapshotListener(EventListener { snapshots, e ->
                if (e != null) {
                    Toast.makeText(context, "Error $e", Toast.LENGTH_SHORT).show()
                    when (context) {
                        is HairCareActivity -> {
                            context.hideProgressDialog()
                        }
                    }
                    return@EventListener
                }

                when (context) {
                    is HairCareActivity -> {
                        displayHairCareData(context, snapshots)
                    }
                }

            })

    }

    fun getServiceList(context: Context, serviceName: String) {
        // The collection name for PRODUCTS
        mFireStore.collection(serviceName)
            .whereEqualTo(Constants.PROVIDER_IDD, getCurrentUserID())
            .addSnapshotListener(EventListener { snapshots, e ->
                if (e != null) {
                    Toast.makeText(context, "Error $e", Toast.LENGTH_SHORT).show()
                    when (context) {
                        is HairCareActivity -> {
                            context.hideProgressDialog()
                        }
                    }
                    return@EventListener
                }

                when (context) {
                    is BridalActivity -> {
                        displayBridalData(context, snapshots)
                    }
                    is NailCareActivity -> {
                        displayNailCareData(context, snapshots)
                    }
                    is MassageActivity -> {
                        displayMassageCareData(context, snapshots)
                    }
                    is MakeUPActivity -> {
                        displayMakeupCareData(context, snapshots)
                    }
                    is TatooAndColorActivity -> {
                        displayTatColorData(context, snapshots)
                    }

                }

            })

    }

    private fun displayHairCareData(
        context: HairCareActivity,
        snapshots: QuerySnapshot?, ) {
        Log.d("service_1", "")
        val serviceList: ArrayList<Service> = ArrayList()
        if (snapshots != null) {
            for (doc in snapshots) {
                val service = doc.toObject(Service::class.java)
                service!!.service_id = doc.id
                serviceList.add(service)
            }
        }
        context.hideProgressDialog()
        context.successServiceListFromFireStore(serviceList)
    }

    private fun displayBridalData(
        context: BridalActivity,
        snapshots: QuerySnapshot?, ) {

        Log.d("service_2", "")
        val serviceList: ArrayList<BridalService> = ArrayList()
        if (snapshots != null) {
            for (doc in snapshots) {
                val service = doc.toObject(BridalService::class.java)
                service.service_id = doc.id
                serviceList.add(service)
            }
        }
        context.hideProgressDialog()
        context.successBridalServiceListFromFireStore(serviceList)
    }

    private fun displayMakeupCareData(
        context: MakeUPActivity,
        snapshots: QuerySnapshot?,
    ) {
        Log.d("service_3", "")
        val makeupServiceList: ArrayList<MakeupService> = ArrayList()
        if (snapshots != null) {
            for (doc in snapshots) {
                val service = doc.toObject(MakeupService::class.java)
                service!!.service_id = doc.id
                makeupServiceList.add(service)
            }
        }
        context.hideProgressDialog()
        context.successMakeupServiceListFromFireStore(makeupServiceList)
    }

    private fun displayMassageCareData(
        context: MassageActivity,
        snapshots: QuerySnapshot?,
    ) {
        Log.d("service_4", "")
        val massageServiceList: ArrayList<MassageService> = ArrayList()
        if (snapshots != null) {
            for (doc in snapshots) {
                val service = doc.toObject(MassageService::class.java)
                service!!.service_id = doc.id
                massageServiceList.add(service)
            }
        }
        context.hideProgressDialog()
        context.successMassageServiceListFromFireStore(massageServiceList)
    }

    private fun displayNailCareData(
        context: NailCareActivity,
        snapshots: QuerySnapshot?,
    ) {
        Log.d("service_5", "")
        val nailServiceList: ArrayList<NailService> = ArrayList()
        if (snapshots != null) {
            for (doc in snapshots) {
                val service = doc.toObject(NailService::class.java)
                service!!.service_id = doc.id
                nailServiceList.add(service)
            }
        }
        context.hideProgressDialog()
        context.successNailCareServiceListFromFireStore(nailServiceList)
    }

    private fun displayTatColorData(
        context: TatooAndColorActivity,
        snapshots: QuerySnapshot?,
    ) {
        Log.d("service_5", "")
        val tatServiceList: ArrayList<TatColorService> = ArrayList()
        if (snapshots != null) {
            for (doc in snapshots) {
                val service = doc.toObject(TatColorService::class.java)
                service!!.service_id = doc.id
                tatServiceList.add(service)
            }
        }
        context.hideProgressDialog()
        context.successTatColorServiceListFromFireStore(tatServiceList)
    }


}