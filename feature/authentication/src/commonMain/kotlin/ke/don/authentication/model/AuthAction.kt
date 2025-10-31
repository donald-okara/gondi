package ke.don.authentication.model

sealed interface AuthAction {
    object SignIn: AuthAction
}