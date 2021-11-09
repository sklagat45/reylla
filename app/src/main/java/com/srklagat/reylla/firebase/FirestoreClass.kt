package com.srklagat.reylla.firebase

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import android.widget.Toast.makeText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.srklagat.reylla.activities.SignInActivity
import com.srklagat.reylla.activities.SignUpActivity
import com.srklagat.reylla.activities.agentclients.clientActivities.ClieantHomeActivity
import com.srklagat.reylla.activities.agentclients.RegisterCustomer
import com.srklagat.reylla.activities.agentclients.clientActivities.GalleryActivity
import com.srklagat.reylla.activities.agentclients.clientActivities.SelectedSalon
import com.srklagat.reylla.activities.serviceproviders.*
import com.srklagat.reylla.activities.serviceproviders.addingNewService.*
import com.srklagat.reylla.activities.serviceproviders.ui.CompleteProfile
import com.srklagat.reylla.activities.serviceproviders.ui.gallery.AddGalleryImagesActivity
import com.srklagat.reylla.activities.serviceproviders.ui.gallery.GalleryFragment
import com.srklagat.reylla.activities.serviceproviders.ui.home.HomeFragment
import com.srklagat.reylla.model.*
import com.srklagat.reylla.utils.Constants


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

    // A function to upload the image to the cloud storage.
    fun uploadImageToCloudStorage(activity: Activity, imageFileURI: Uri?, imageType: String) {

        //getting the storage reference
        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
            imageType + System.currentTimeMillis() + "."
                    + Constants.getFileExtension(
                activity,
                imageFileURI
            )
        )

        //adding the file to reference
        sRef.putFile(imageFileURI!!)
            .addOnSuccessListener { taskSnapshot ->
                // The image upload is success
                Log.e(
                    "Firebase Image URL",
                    taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
                )

                // Get the downloadable url from the task snapshot
                taskSnapshot.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener { uri ->
                        Log.e("Downloadable Image URL", uri.toString())
                        // Here call a function of base activity for transferring the result to it.
                        when (activity) {
                            is CompanyRegister -> {
                                activity.imageUploadSuccess(uri.toString())
                            }
                        }
                    }
            }
            .addOnFailureListener { exception ->

                // Hide the progress dialog if there is any error. And print the error in log.
                when (activity) {
                    is CompanyRegister -> {
                        activity.hideProgressDialog()
                    }

                }

                Log.e(
                    activity.javaClass.simpleName,
                    exception.message,
                    exception
                )
            }
    }
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
//            .document(
//            getCurrentUserID() + "." + System.currentTimeMillis()
//            )
//            .whereEqualTo("styleName", serviceInfor.styleName)
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge
            .set(serviceInfor, SetOptions.merge())
            .addOnSuccessListener {
                // Here call a function of base activity for transferring the result to it.
                activity.serviceUploadSuccess()
            }
            .addOnFailureListener{ e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while uploading the service details.",
                    e
                ) }
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


    fun uploadImageDetails(
        activity: AddGalleryImagesActivity,
        galleryItemInfor: GalleryItem
    ) {

        mFireStore.collection(Constants.GALLERY_ITEMS)
            .document()
//            .whereEqualTo("styleName", serviceInfor.styleName)
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge
            .set(galleryItemInfor, SetOptions.merge())
            .addOnSuccessListener {
//                val imageInfor = it.toObject(GalleryItem::class.java)
                galleryItemInfor.id = mFireStore.collection(Constants.GALLERY_ITEMS)
                    .document().toString()

                // Here call a function of base activity for transferring the result to it.
                activity.serviceUploadSuccess(galleryItemInfor.id)
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
                    makeText(context, "Error $e", Toast.LENGTH_SHORT).show()
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


    fun getSalonServiceList(context: Context, serviceName: String,salonId: String) {
        // The collection name for PRODUCTS
        mFireStore.collection(serviceName)
            .whereEqualTo(Constants.PROVIDER_IDD, salonId)
            .addSnapshotListener(EventListener { snapshots, e ->
                if (e != null) {
                    makeText(context, "Error $e", Toast.LENGTH_SHORT).show()
                    when (context) {
                        is SelectedSalon -> {
                            context.hideProgressDialog()
                            Log.d("service_1", "servicesss$salonId")

                        }
                    }
                    return@EventListener
                }
                when (context) {
                    is SelectedSalon -> {
                        displaySalonServicesData(context, snapshots)
                    }
                }

            })

    }

    fun getHairSalonServiceList(context: Context, serviceName: String,salonId: String) {
        // The collection name for PRODUCTS
        mFireStore.collection(serviceName)
            .whereEqualTo(Constants.PROVIDER_ID, salonId)
            .addSnapshotListener(EventListener { snapshots, e ->
                if (e != null) {
                    makeText(context, "Error $e", Toast.LENGTH_SHORT).show()
                    when (context) {
                        is SelectedSalon -> {
                            context.hideProgressDialog()
                            Log.d("service_1", "servicesss$salonId")

                        }
                    }
                    return@EventListener
                }
                when (context) {
                    is SelectedSalon -> {
                        displaySalonServicesData(context, snapshots)
                    }
                }

            })

    }


    private fun displaySalonServicesData(context: SelectedSalon, snapshots: QuerySnapshot?) {

        Log.d("service_1", "servicesss$snapshots")
        val serviceList: ArrayList<SalonService> = ArrayList()
        if (snapshots != null) {
            for (doc in snapshots) {
                val service = doc.toObject(SalonService::class.java)
                service!!.service_id = doc.id
                serviceList.add(service)
            }
        }
        context.hideProgressDialog()
        context.successServiceListFromFireStore(serviceList)

    }

    fun getServiceList(context: Context, serviceName: String) {
        // The collection name for PRODUCTS
        mFireStore.collection(serviceName)
            .whereEqualTo(Constants.PROVIDER_IDD, getCurrentUserID())
            .addSnapshotListener(EventListener { snapshots, e ->
                if (e != null) {
                    makeText(context, "Error $e", Toast.LENGTH_SHORT).show()
                    when (context) {
                        is BridalActivity -> {
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
        Log.d("service_1", "haiiiiirrrrrr")
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


    private fun displayBridalData(
        context: BridalActivity,
        snapshots: QuerySnapshot?, ) {
        Log.d("service_2", "bridieeeee")
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
        Log.d("service_3", "mmmmakeup")
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


    /**
     * A function to update the user profile data into the database.
     *
     * @param activity The activity is used for identifying the Base activity to which the result is passed.
     * @param userHashMap HashMap of fields which are to be updated.
     */
    fun updateUserProfileData(activity: Activity, userHashMap: HashMap<String, Any>) {
        // Collection Name
        mFireStore.collection(Constants.USERS)
            // Document ID against which the data to be updated. Here the document id is the current logged in user id.
            .document(getCurrentUserID())
            // A HashMap of fields which are to be updated.
            .update(userHashMap)
            .addOnSuccessListener {

                // Notify the success result.
                when (activity) {
                    is CompleteProfile -> {
                        // Call a function of base activity for transferring the result to it.
                        activity.userProfileUpdateSuccess()
                    }
                }
            }
            .addOnFailureListener { e ->

                when (activity) {
                    is CompleteProfile -> {
                        // Hide the progress dialog if there is any error. And print the error in log.
                        activity.hideProgressDialog()
                    }
                }

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updating the user details.",
                    e
                )
            }
    }


    fun getSalonItemsList(homeFragment: HomeFragment) {
        // The collection name for PRODUCTS
        mFireStore.collection(Constants.COMPANIES)
            .addSnapshotListener(EventListener { snapshots, e ->
                if (e != null) {
//                    Toast.makeText(homeFragment, "Error $e", Toast.LENGTH_SHORT).show()

                    when (homeFragment) {
                        else -> {
//                            homeFragment.hideProgressDialog()
                        }
                    }
                    return@EventListener
                }
                when (homeFragment) {
                    else -> {
                        displaySalonItemsList(homeFragment, snapshots)
                    }
                }

            })

    }

    private fun displaySalonItemsList(
        context: HomeFragment,
        snapshots: QuerySnapshot?,
    ) {
        Log.d("service_5", "")

        val saloonList: ArrayList<Company> = ArrayList()
        if (snapshots != null) {
            for (doc in snapshots) {
                val service = doc.toObject(Company::class.java)
                service!!.id = doc.id
                saloonList.add(service)
            }
        }
//        context.hideProgressDialog()
        context.successSaloonItemsList(saloonList)
    }

    fun getSalonsGalleryImages(activity: GalleryActivity, mSalonID: String){
        // The collection name for PRODUCTS
        mFireStore.collection(Constants.GALLERY_ITEMS)
            .whereEqualTo(Constants.USER_ID, mSalonID)
            .addSnapshotListener(EventListener { snapshots, e ->
                if (e != null) {
//                    Toast.makeText(homeFragment, "Error $e", Toast.LENGTH_SHORT).show()

                    when (activity) {
                        is GalleryActivity -> {
//                            homeFragment.hideProgressDialog()
                        }
                    }
                    return@EventListener
                }
                when (activity) {
                    is GalleryActivity -> {
                        displaySalonsGalleryItemsList(activity, snapshots)
                    }
                }

            })

    }

    private fun displaySalonsGalleryItemsList(
        activity: GalleryActivity,
        snapshots: QuerySnapshot?
    ) {
        Log.d("service_5", "")

        val galleryList: ArrayList<GalleryItem> = ArrayList()
        if (snapshots != null) {
            for (doc in snapshots) {
                val items = doc.toObject(GalleryItem::class.java)
                items!!.id = doc.id
                galleryList.add(items)
            }
        }
//        context.hideProgressDialog()
        activity.successGalleryItemsList(galleryList)

    }

    fun getGalleryImages(galleryFragment: GalleryFragment) {
        // The collection name for PRODUCTS
        mFireStore.collection(Constants.GALLERY_ITEMS)
            .addSnapshotListener(EventListener { snapshots, e ->
                if (e != null) {
//                    Toast.makeText(homeFragment, "Error $e", Toast.LENGTH_SHORT).show()

                    when (galleryFragment) {
                        is GalleryFragment -> {
//                            homeFragment.hideProgressDialog()
                        }
                    }
                    return@EventListener
                }
                when (galleryFragment) {
                    is GalleryFragment -> {
                        displayGalleryItemsList(galleryFragment, snapshots)
                    }
                }

            })

    }

    private fun displayGalleryItemsList(
        context: GalleryFragment,
        snapshots: QuerySnapshot?,
    ) {
        Log.d("service_5", "")

        val galleryList: ArrayList<GalleryItem> = ArrayList()
        if (snapshots != null) {
            for (doc in snapshots) {
                val items = doc.toObject(GalleryItem::class.java)
                items!!.id = doc.id
                galleryList.add(items)
            }
        }
//        context.hideProgressDialog()
        context.successGalleryItemsList(galleryList)
    }

    fun getSalonList(clieantHomeActivity: ClieantHomeActivity, salons: String) {
        // The collection name for PRODUCTS
        mFireStore.collection(salons)
//            .whereNotEqualTo(Constants.PROVIDER_ID, getCurrentUserID())
            .addSnapshotListener(EventListener { snapshots, e ->
                if (e != null) {
//                    Toast.makeText(homeFragment, "Error $e", Toast.LENGTH_SHORT).show()

                    when (clieantHomeActivity) {
                        else -> {
                            clieantHomeActivity.hideProgressDialog()
                        }
                    }
                    return@EventListener
                }
                when (clieantHomeActivity) {
                    else -> {
                        displaySalonsList(clieantHomeActivity, snapshots)
                    }
                }

            })

    }

    private fun displaySalonsList(
        context: ClieantHomeActivity, snapshots: QuerySnapshot?) {

        Log.d("service_5", "")

        val salonList: ArrayList<Company> = ArrayList()
        if (snapshots != null) {
            for (doc in snapshots) {
                val items = doc.toObject(Company::class.java)
                items!!.id = doc.id
                salonList.add(items)
            }
        }
        context.hideProgressDialog()
        context.successSalonsListFromFireStore(salonList)
    }


}