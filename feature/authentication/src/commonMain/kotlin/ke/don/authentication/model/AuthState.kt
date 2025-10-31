package ke.don.authentication.model

import ke.don.domain.result.ResultStatus

data class AuthState(
    val initiallyAuthenticated: Boolean = true,
    val authStatus: ResultStatus<Unit> = ResultStatus.Idle,
)