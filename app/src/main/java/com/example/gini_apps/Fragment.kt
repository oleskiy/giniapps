package com.example.gini_apps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_layout.view.*
import java.util.*

class Fragment : Fragment() {

    lateinit var adapter:Adapter
    private lateinit var mainViewModel: ViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainViewModel = ViewModel(requireActivity().application)
        return inflater.inflate(R.layout.fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var rvData = view.recyclerView

        var linearLayoutManager = GridLayoutManager(activity, 3)
        rvData.layoutManager = linearLayoutManager

        adapter = Adapter(view.context ,TreeSet<Int>())
        rvData.adapter = adapter

            subscribeData()
    }

    private fun subscribeData() {

        mainViewModel.getData()?.observe(this.viewLifecycleOwner, Observer<SortedSet<Int>> {

            if (it != null) {
                adapter.updateList(mainViewModel.dataList)
                adapter.notifyDataSetChanged()
            }

        })

    }
}