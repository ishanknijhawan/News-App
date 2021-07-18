package com.ishanknijhawan.newsapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ishanknijhawan.newsapp.ui.EveryThingNewsFragment
import com.ishanknijhawan.newsapp.ui.SourcesFragment
import com.ishanknijhawan.newsapp.ui.TopHeadlinesFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
//class MainActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation)
//        val navHostFragment =
//            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
//        val navController = navHostFragment.navController
//        //bottomNavigationView.setupWithNavController(navController)
//
//        NavigationUI.setupWithNavController(
//            bottomNavigationView,
//            navController
//        )
//    }
//}
class MainActivity : AppCompatActivity() {
    //hello this is a test line
    //hello this is a test line
    //hello this is a test line
    //hello this is a test line
    //hello this is a test line
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val firstFragment= TopHeadlinesFragment()
        val secondFragment= EveryThingNewsFragment()
        val thirdFragment= SourcesFragment()

        setCurrentFragment(firstFragment)

        bottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.topHeadlinesFragment->setCurrentFragment(firstFragment)
                R.id.everyThingNews->setCurrentFragment(secondFragment)
                R.id.sourcesFragment->setCurrentFragment(thirdFragment)

            }
            true
        }

    }

    private fun setCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)
            commit()
        }

}