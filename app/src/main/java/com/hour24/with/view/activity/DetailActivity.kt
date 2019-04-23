package com.hour24.with.view.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.hour24.with.R
import com.hour24.with.databinding.DetailActivityBinding
import com.hour24.with.model.MarvelModel
import com.hour24.with.view.viewmodel.DetailViewModel


class DetailActivity : AppCompatActivity() {

    private val TAG: String = DetailActivity::class.java.name

    // DataBinding
    private lateinit var mBinding: DetailActivityBinding
    private val mViewModel = DetailViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.detail_activity)

        initDataBinding()
        initIntent()

    }

    /**
     * Init DataBinding
     */
    private fun initDataBinding() {

        mBinding.viewModel = mViewModel

    }

    /**
     * Intent 데이터
     */
    private fun initIntent() {

        try {

            val intent = intent
            val model: MarvelModel = intent.getSerializableExtra(MarvelModel::class.java.name) as MarvelModel
            mViewModel.mModel = model

            setScale()

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     *이미지 확대
     */
    private fun setScale() {
        Handler().postDelayed({
            try {
                mBinding.pvDetail.setScale(mBinding.pvDetail.maximumScale, true)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, 1000)
    }

}
