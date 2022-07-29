package com.example.shopple

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.shopple.databinding.ActivityMainBinding
import com.example.shopple.fragments.FavoriteFragment
import com.example.shopple.fragments.MainFragment
import com.example.shopple.fragments.ProfileFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private val mainFragment = MainFragment()
    private val favoriteFragment = FavoriteFragment()
    private val profileFragment = ProfileFragment()
    private var prevBottomItems = arrayOf(R.id.home, R.id.home)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        auth = Firebase.auth
        setUpActionBar()
        mainBinding.bottomNav.selectedItemId = R.id.home
        replaceFragment(mainFragment)

        mainBinding.bottomNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home ->{
                    replaceFragment(mainFragment)
                }
                R.id.favorites ->{
                    replaceFragment(favoriteFragment)
                }
                R.id.create ->{
                    replaceFragment(null)
                }
                R.id.profile ->{
                    if(auth.currentUser == null) {
                        createAccountDialog()
                    }
                    else replaceFragment(profileFragment)
                }
            }
            prevBottomItems[0] = prevBottomItems[1]
            prevBottomItems[1] = it.itemId

            true
        }

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_menu, menu)
        if(auth.currentUser == null) menu?.findItem(R.id.sign_out)?.isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        profileFragment.onOptionsItemSelected(item)
        when(item.itemId){
            android.R.id.home ->
                startActivity(Intent(this, SignInActivity::class.java))
            R.id.refresh -> {
//                Обновление страницы
            }
            R.id.sign_out -> {
                auth.signOut()
                this.finish()
            }
        }
        return true
    }

    private fun setUpActionBar(){
        supportActionBar?.displayOptions = androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.sample_action_bar)
        if(auth.currentUser == null) supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun replaceFragment(fragment: Fragment?){
        if(fragment != null) supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment).commit()
        else supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, Fragment()).commit()
    }

    private fun createAccountDialog(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.registerDialogTitle)
        builder.setMessage(R.string.registerDialogMessage)
        builder.setOnDismissListener{
            mainBinding.bottomNav.selectedItemId = prevBottomItems[0]
        }
        builder.setPositiveButton("Yes"){dialog, _ ->
            dialog.cancel()
            startActivity(Intent(this, SignInActivity::class.java))
        }
        builder.setNegativeButton("No") { dialog, _ -> dialog.cancel() }
        builder.show()
    }
}