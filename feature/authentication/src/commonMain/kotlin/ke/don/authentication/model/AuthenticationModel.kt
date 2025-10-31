package ke.don.authentication.model

import cafe.adriel.voyager.core.model.ScreenModel
import ke.don.domain.repo.AuthClient

class AuthenticationModel(
    private val authClient: AuthClient
): ScreenModel {
    
}