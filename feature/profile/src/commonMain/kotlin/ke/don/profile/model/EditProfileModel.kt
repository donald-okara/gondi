package ke.don.profile.model

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import ke.don.components.helpers.Matcha
import ke.don.domain.repo.ProfileRepository
import ke.don.domain.table.Avatar
import ke.don.domain.table.AvatarBackground
import ke.don.koffee.domain.Koffee
import ke.don.koffee.model.ToastDuration
import ke.don.local.datastore.ProfileStore
import ke.don.remote.api.errorTitle
import ke.don.utils.result.ResultStatus
import ke.don.utils.result.onFailure
import ke.don.utils.result.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditProfileModel(
    private val repository: ProfileRepository,
    private val profileStore: ProfileStore
): ScreenModel {
    private val _uiState = MutableStateFlow(EditProfileState())
    val uiState = _uiState.asStateFlow()

    init {
        getProfile()
    }

    fun onEvent(event: EditProfileEvent) {
        when (event) {
            is EditProfileEvent.OnAvatarChanged -> updateAvatar(event.avatar)
            is EditProfileEvent.OnBackgroundChanged -> updateBackground(event.background)
            is EditProfileEvent.OnUsernameChanged -> updateUsername(event.username)
            is EditProfileEvent.OnSaveProfile -> saveProfile {
                event.onSaved()
            }
        }
    }

    fun getProfile(){
        screenModelScope.launch {
            val profile = profileStore.profileFlow.first()
            profile?.let { editProfile ->
                _uiState.update {
                    it.copy(
                        fetchedProfile = editProfile,
                        editProfile = editProfile
                    )
                }
            }
        }
    }

    fun updateAvatar(avatar: Avatar){
        _uiState.update {
            it.copy(
                editProfile = it.editProfile.copy(
                    avatar = avatar
                )
            )
        }
    }

    fun updateUsername(username: String) {
        _uiState.update {
            it.copy(
                editProfile = it.editProfile.copy(
                    username = username
                )
            )
        }
    }

    fun updateBackground(background: AvatarBackground) {
        _uiState.update {
            it.copy(
                editProfile = it.editProfile.copy(
                    background = background
                )
            )
        }
    }

    fun saveProfile(onSaved: () -> Unit) {
        screenModelScope.launch {
            _uiState.update {
                it.copy(
                    saveStatus = ResultStatus.Loading
                )
            }

            repository.updateProfile(uiState.value.editProfile).onSuccess{ result ->
                _uiState.update {
                    it.copy(
                        saveStatus = ResultStatus.Success(result),
                        editProfile = result,
                        fetchedProfile = result
                    )
                }
                onSaved()
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        saveStatus = ResultStatus.Error(error.message ?: "Unknown error")
                    )
                }

                Matcha.showErrorToast(
                    title = error.category.errorTitle,
                    message = error.message ?: "Unknown error",
                    retryAction = { saveProfile(onSaved) },
                    duration = ToastDuration.Indefinite
                )
            }
        }
    }

    override fun onDispose() {
        Koffee.dismissAll()
        super.onDispose()
    }
}