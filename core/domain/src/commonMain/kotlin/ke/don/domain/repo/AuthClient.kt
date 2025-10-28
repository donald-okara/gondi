package ke.don.domain.repo

interface AuthClient {
    suspend fun signInWithGoogle()
}