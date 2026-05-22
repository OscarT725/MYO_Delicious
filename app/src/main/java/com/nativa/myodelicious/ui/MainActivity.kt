package com.nativa.myodelicious.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.nativa.myodelicious.R
import com.nativa.myodelicious.R.id
import com.nativa.myodelicious.ui.auth.LoginActivity
import com.nativa.myodelicious.ui.main.carrito.CarritoFragment
import com.nativa.myodelicious.ui.main.productos.CatalogoFragment
import com.nativa.myodelicious.ui.main.productos.FavoritosFragment
import com.nativa.myodelicious.ui.main.productos.HomeFragment
import com.nativa.myodelicious.ui.main.admin.AdminFragment
import com.nativa.myodelicious.ui.main.usuario.CuponDescuentoActivity
import com.nativa.myodelicious.ui.main.usuario.CuponesDescuentoFragment
import com.nativa.myodelicious.ui.main.usuario.PerfilFragment


class MainActivity : AppCompatActivity() {



    private lateinit var drawerLayout: DrawerLayout
    private var esAdmin = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        drawerLayout = findViewById(id.drawer_layout)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        val navView = findViewById<NavigationView>(R.id.nav_view)

        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        toggle.drawerArrowDrawable.color = resources.getColor(R.color.white)

        val target = intent.getStringExtra("TARGET_FRAGMENT")
            esAdmin = target == "ADMIN"


        bottomNav.visibility = if (esAdmin) View.GONE else View.VISIBLE

        navView.menu.apply {
            findItem(R.id.nav_carrito)?.isVisible = !esAdmin
            findItem(R.id.nav_cupon_descuento)?.isVisible = !esAdmin
        }


        if (esAdmin) {
            cargarFragment(AdminFragment())
        } else {
            cargarFragment(HomeFragment())
            bottomNav.selectedItemId = R.id.nav_home
        }


        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> cargarFragment(HomeFragment())
                R.id.nav_catalogo -> cargarFragment(CatalogoFragment())
                R.id.nav_favoritos -> cargarFragment(FavoritosFragment())
            }
            true
        }


        navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_carrito -> cargarFragment(CarritoFragment())
                R.id.nav_perfil -> cargarFragment(PerfilFragment.newInstance(esAdmin))
                R.id.nav_cupon_descuento -> cargarFragment(CuponesDescuentoFragment())
                R.id.nav_cerrar_sesion -> finish()
            }
            drawerLayout.closeDrawers()
            true
        }
    }

    private fun cargarFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}