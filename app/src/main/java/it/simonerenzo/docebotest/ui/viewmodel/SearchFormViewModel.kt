package it.simonerenzo.docebotest.ui.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchFormViewModel : ViewModel() {

    val itemName = MutableLiveData<String>("")

    val valid = MediatorLiveData<Boolean>()
        .apply {
            addSource(itemName) {
                value = itemName.value?.isNotBlank() ?: false
            }
        }

}