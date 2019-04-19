package com.hour24.with.view.activity

import android.databinding.DataBindingUtil
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.hour24.tb.adapter.GenericRecyclerViewAdapter
import com.hour24.with.R
import com.hour24.with.databinding.MainActivityBinding
import com.hour24.with.databinding.MainItemBinding
import com.hour24.with.model.MarvelModel
import com.hour24.with.utils.ObjectUtils
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements


class MainActivity : AppCompatActivity() {

    private val TAG: String = MainActivity::class.java.name

    // DataBinding
    private lateinit var mBinding: MainActivityBinding
    private val mViewModel = ViewModel()

    // List
    private lateinit var mAdapter: GenericRecyclerViewAdapter<MarvelModel, MainItemBinding>
    private val mList = ArrayList<MarvelModel>()

    private var mElementList: List<Element> = ArrayList<Element>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.main_activity)

        initDataBinding()
        initLayout()
        getData()

    }

    /**
     * Init DataBinding
     */
    private fun initDataBinding() {

        mBinding.viewModel = mViewModel

    }

    /**
     * Init Layout
     */
    private fun initLayout() {

        // Adapter
        mAdapter = object : GenericRecyclerViewAdapter<MarvelModel, MainItemBinding>(this@MainActivity, R.layout.main_item, mList) {

            override fun onBindData(position: Int, model: MarvelModel, dataBinding: MainItemBinding) {

                try {

                    val viewModel = ViewModel()
                    viewModel.mModel = model

                    dataBinding.viewModel = viewModel

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
        mBinding.rvMain.adapter = mAdapter

    }

    private fun getData() {

        mViewModel.getData()

    }


    inner class ViewModel {

        var mModel: MarvelModel? = null

        /**
         * 마블 데이터 가져옴
         */
        fun getData() {
            MarvelCrawling().execute()
        }

    }

    /**
     * 웹사이트 크롤링
     */
    inner class MarvelCrawling : AsyncTask<Void, Void, ArrayList<MarvelModel>>() {


        override fun onPreExecute() {
            super.onPreExecute()

        }

        override fun doInBackground(vararg void: Void): ArrayList<MarvelModel>? {

            val list = ArrayList<MarvelModel>()

            try {

                // 한번 불러온 데이터는 다시 불러오지 않는다.
                if (ObjectUtils.isEmpty(mElementList)) {

                    val doc = Jsoup.connect("https://www.thewrap.com/marvel-movies-ranked-worst-best-avengers-infinity-war-ant-man-venom-stan-lee-spider-man-into-the-spider-verse/").get()

                    // articleContents 영역 가져옴
                    val articleContents: Elements = doc
                            .select("div[class=gallery-wrap photos]")
                            .select("div[class=item-wrap]")

                    // 역순으로 변환
                    mElementList = articleContents.reversed()

                }

                for (element in mElementList) {

                    // 이미지 영역
                    val imageElement = element.select("div[class=image-wrap]")
                    val imageUrl = imageElement.select("a img").attr("data-src")

                    // 캡션 영역
                    val cationElement = element.select("div[class=caption]")

                    // 제목 처리
                    val cation = cationElement.text()
                    var title = cationElement.select("strong").text()

                    // 디스크립션 추출
                    val description = cation.replace(title, "").trim()

                    val index = title.indexOf(".")
                    if (index != -1) {
                        val rank = title.substring(0, index).toInt()
                        title = title.substring(index + 1, title.length).trim()

                        list.add(MarvelModel(rank, title, description, imageUrl))
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

            return list
        }

        override fun onPostExecute(result: ArrayList<MarvelModel>) {
            super.onPostExecute(result)
            mList.addAll(result)
            mAdapter.notifyDataSetChanged()
        }
    }
}
