package com.nativa.myodelicious

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest



object SupabaseClient {
    val client = createSupabaseClient(
        supabaseUrl = "https://thlnuidcrtwsxaocrzvj.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InRobG51aWRjcnR3c3hhb2NyenZqIiwicm9sZSI6ImFub24iLCJpYXQiOjE3Nzg5NjY1MzAsImV4cCI6MjA5NDU0MjUzMH0.XMkYlZKyNm8ww-51QeMGnQnW-kpXSyvaYvdUibpE0Oc"
    ){
     install(plugin = Postgrest)
     install(plugin = Auth)
    }
}