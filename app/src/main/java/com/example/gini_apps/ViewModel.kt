package com.example.gini_apps

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

class ViewModel(application: Application) : AndroidViewModel(application){

    var dataList: SortedSet<Int> = TreeSet()
    private val mutableList: MutableLiveData<SortedSet<Int>> = MutableLiveData()

    public fun getData(): LiveData<SortedSet<Int>> {

        ApiService.initApiService(getApplication()).getData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {  it.localizedMessage }
            .subscribe{it->
                for(item in  it.numbers){
                    dataList.add(item.number)
                }
                    mutableList.postValue(dataList)
            }
        return mutableList
    }

}