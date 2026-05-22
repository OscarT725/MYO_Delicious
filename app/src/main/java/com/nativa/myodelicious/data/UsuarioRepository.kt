package com.nativa.myodelicious.data

import com.nativa.myodelicious.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.serialization.Serializable

@OptIn(kotlinx.serialization.InternalSerializationApi::class)
@Serializable
data class UsuarioData(
    val id: String,
    val nombres: String,
    val apellidos: String,
    val correo: String? = null,
    val rol: String = "cliente",
    val foto_url: String? = null,
    val telefono: String? = null
)

object UsuarioRepository {

    suspend fun existeUsuario(userid: String): Boolean {
        return try {
            val resultado = SupabaseClient.client
                .postgrest["usuarios"]
                .select {
                    filter { eq("id", userid) }
                }
                .decodeList<UsuarioData>()
            resultado.isNotEmpty()
        } catch (e: Exception) {
            false
        }
    }

    suspend fun obtenerUsuarioActual(): UsuarioData? {
        val userId = SupabaseClient.client.auth.currentUserOrNull()?.id ?: return null
        return try {
            val resultado = SupabaseClient.client
                .postgrest["usuarios"]
                .select { filter { eq("id", userId) } }
                .decodeList<UsuarioData>()
            resultado.firstOrNull()
        } catch (e: Exception) {
            android.util.Log.e("UsuarioRepository", "Error al obtener usuario", e)
            null
        }
    }

    suspend fun insertarUsuario(id: String, nombres: String, apellidos: String, correo: String?) {
        try {
            SupabaseClient.client
                .postgrest["usuarios"]
                .insert(UsuarioData(id, nombres, apellidos, correo))
        } catch (e: Exception) {
            android.util.Log.e("UsuarioRepository", "Error al insertar usuario: ${e.message}", e)
        }
    }

    suspend fun actualizarCorreo(id: String, correo: String) {
        try {
            SupabaseClient.client
                .postgrest["usuarios"]
                .update({
                    set("correo", correo)
                }) {
                    filter { eq("id", id) }
                }
        } catch (e: Exception) {
            android.util.Log.e("UsuarioRepository", "Error al actualizar correo: ${e.message}", e)
        }
    }

    suspend fun obtenerRolActual(): String {
        return try {
            val userid = SupabaseClient.client.auth.currentUserOrNull()?.id ?: return "cliente"
            val resultado = SupabaseClient.client
                .postgrest["usuarios"]
                .select { filter { eq("id", userid) } }
                .decodeList<UsuarioData>()

            val rolRaw = resultado.firstOrNull()?.rol ?: "cliente"

            // Limpiar el valor que trae de rol en Supabase
            val rol = rolRaw
                .replace("'", "")       // quita comillas simples
                .replace("::text", "")  // quita ::text
                .trim()
                .lowercase()

            android.util.Log.d("DEBUG_ROL", "rol limpio: '$rol'")
            rol
        } catch (e: Exception) {
            "cliente"
        }
    }
    suspend fun actualizarPerfil(id: String, nombres: String, apellidos: String, telefono: String) {
        try {
            SupabaseClient.client
                .postgrest["usuarios"]
                .update({
                    set("nombres", nombres)
                    set("apellidos", apellidos)
                    set("telefono", telefono)
                }) {
                    filter { eq("id", id) }
                }
        } catch (e: Exception) {
            android.util.Log.e("UsuarioRepository", "Error al actualizar perfil: ${e.message}", e)
        }
    }
}