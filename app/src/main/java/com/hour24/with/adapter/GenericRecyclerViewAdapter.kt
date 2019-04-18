package com.hour24.tb.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.hour24.with.utils.ObjectUtils


import java.util.ArrayList
import java.util.Random

@Suppress("UNCHECKED_CAST")
abstract class GenericRecyclerViewAdapter<T : Any, in D : ViewDataBinding>(
        private val mContext: Context,
        private val mLayoutId: Int,
        private var mList: ArrayList<T>?)
    : RecyclerView.Adapter<GenericRecyclerViewAdapter.ViewHolder>() {

    private val mRandom = Random()


    abstract fun onBindData(position: Int, model: T?, dataBinding: D?)

    init {

        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val dataBinding = DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(parent.context), mLayoutId, parent, false)
        return ViewHolder(dataBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        try {
            onBindData(position, mList!![position], holder.mDataBinding as D)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun getItemCount(): Int {
        return if (!ObjectUtils.isEmpty(mList)) mList!!.size else 0
    }

    fun addItems(arrayList: ArrayList<T>) {
        mList = arrayList
        this.notifyDataSetChanged()
    }

    fun getItem(position: Int): T {
        return mList!![position]
    }

    override fun getItemId(position: Int): Long {
        return try {
            mList!![position].hashCode().toLong()
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }

    }

    class ViewHolder(binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        var mDataBinding: ViewDataBinding = binding
    }

//    mRecentAdapter = object : GenericRecyclerViewAdapter<Recent, MainRecentItemBinding>(this@MainActivity, R.layout.main_recent_item, mRecentList) {
//
//        override fun onBindData(position: Int, model: Recent?, dataBinding: MainRecentItemBinding?) {
//
//            try {
//
//                val viewModel = ViewModel()
//                viewModel.mModel = model
//                dataBinding!!.viewModel = viewModel
//
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//
//        }
//    }
}
