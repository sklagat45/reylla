package com.srklagat.reylla.activities.serviceproviders.ui.settings

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.srklagat.reylla.R
import com.srklagat.reylla.model.Company

class SettingsFragment : Fragment() {

    private lateinit var settingsViewModel: SettingsViewModel

    // Instance of User data model class. We will initialize it later on.
    private lateinit var mUserDetails: Company

    // Add a global variable for URI of a selected image from phone storage.
    private var mSelectedImageFileUri: Uri? = null

    private var mUserProfileImageURL: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (activity as AppCompatActivity).supportActionBar!!.hide()
        return inflater.inflate(R.layout.settings_fragment, container, false)

    }
}