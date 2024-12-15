package moe.emi.wanikani

fun interface AuthProvider {
	suspend fun getAuthToken(): String?
	
	companion object {
		operator fun invoke(token: String) = AuthProvider { token }
		
		val None = AuthProvider { null }
	}
}