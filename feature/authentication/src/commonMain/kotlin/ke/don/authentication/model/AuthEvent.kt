package ke.don.authentication.model

interface AuthEvent {
    object SwitchSignIn: AuthEvent
    object SwitchMain: AuthEvent
    object SwitchProfile: AuthEvent
}