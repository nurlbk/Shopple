package com.example.shopple.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.text.set
import androidx.fragment.app.Fragment
import com.example.shopple.MainActivity
import com.example.shopple.R
import com.example.shopple.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var _profileBinding: FragmentProfileBinding? = null
    private val profileBinding get() = _profileBinding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { }
        auth = Firebase.auth
        setHasOptionsMenu(true)

        _profileBinding?.profileSave?.setOnClickListener {
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _profileBinding = FragmentProfileBinding.inflate(inflater, container, false)
        profileBinding.profileEmail.text = auth.currentUser?.email
        profileBinding.profileName.setText(auth.currentUser?.displayName)

//        Тут Добавить фото профиля

        profileBinding.profileSave.setOnClickListener{
            val profileName = profileBinding.profileName.text.toString()
            val profilePhone = profileBinding.profilePhone.text.toString()

            if(isValidUsername(profileName) && isValidPhoneNumber(profilePhone)){
                profileBinding.profileName.isEnabled = false
                profileBinding.profilePhone.isEnabled = false
                profileBinding.profileSave.visibility = View.GONE
                Toast.makeText(this.context, "$profileName $profilePhone", Toast.LENGTH_SHORT).show()
            }
            else Toast.makeText(context, R.string.userDataReq, Toast.LENGTH_SHORT).show()
        }

        return profileBinding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.findItem(R.id.edit)?.isVisible = true
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.refresh -> {
//                Обновление страницы
            }
            R.id.edit -> {
                profileBinding.profileName.isEnabled = true
                profileBinding.profilePhone.isEnabled = true
                profileBinding.profileSave.visibility = View.VISIBLE
            }
        }
        return true
    }

    private fun isValidUsername(userName: String): Boolean {
        val userNameRegex = "^[A-Za-z][A-Za-z0-9_]{1,24}$"
        return userNameRegex.toRegex().matches(userName)
    }

    private fun isValidPhoneNumber(phoneNumber: String): Boolean {
        val phoneNumberRegex = "^[+7][0-9]{11}$"
        return phoneNumberRegex.toRegex().matches(phoneNumber)
    }
}