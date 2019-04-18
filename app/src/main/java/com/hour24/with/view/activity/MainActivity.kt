package com.hour24.with.view.activity

import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.hour24.with.R
import com.hour24.with.model.MarvelModel
import com.hour24.with.utils.Logger
import com.hour24.with.utils.ObjectUtils
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements


class MainActivity : AppCompatActivity() {

    private val TAG: String = MainActivity::class.java.name
    private var mMarvelList: List<Element> = ArrayList<Element>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        Description().execute()

    }

    inner class ViewModel {


    }

    inner class Description : AsyncTask<Void, Void, ArrayList<MarvelModel>>() {


        override fun onPreExecute() {
            super.onPreExecute()

        }

        override fun doInBackground(vararg void: Void): ArrayList<MarvelModel>? {

            val list = ArrayList<MarvelModel>()

            try {

                // 한번 불러온 데이터는 다시 불러오지 않는다.
                if (ObjectUtils.isEmpty(mMarvelList)) {

                    val doc = Jsoup.connect("https://www.thewrap.com/marvel-movies-ranked-worst-best-avengers-infinity-war-ant-man-venom-stan-lee-spider-man-into-the-spider-verse/").get()

                    // articleContents 영역 가져옴
                    val articleContents: Elements = doc
                            .select("div[class=gallery-wrap photos]")
                            .select("div[class=item-wrap]")

                    // 역순으로 변환
                    mMarvelList = articleContents.reversed()

                }

                for (element in mMarvelList) {

                    // 이미지 영역
                    val imageElement = element.select("div[class=image-wrap]")

                    // 캡션 영역
                    val cationElement = element.select("div[class=caption]")
//                    Logger.e(TAG, cationElement.text())
                    Logger.e(TAG, cationElement.select("strong").text())

                }
//
//                for (elem in mElementDataSize) { //이렇게 요긴한 기능이
//                    //영화목록 <li> 에서 다시 원하는 데이터를 추출해 낸다.
//                    val my_title = elem.select("li dt[class=tit] a").text()
//                    val my_link = elem.select("li div[class=thumb] a").attr("href")
//                    val my_imgUrl = elem.select("li div[class=thumb] a img").attr("src")
//                    //특정하기 힘들다... 저 앞에 있는집의 오른쪽으로 두번째집의 건너집이 바로 우리집이야 하는 식이다.
//                    val rElem = elem.select("dl[class=info_txt1] dt").next().first()
//                    val my_release = rElem.select("dd").text()
//                    val dElem = elem.select("dt[class=tit_t2]").next().first()
//                    val my_director = "감독: " + dElem.select("a").text()
//                    //Log.d("test", "test" + mTitle);
//                    //ArrayList에 계속 추가한다.
//                    list.add(ItemObject(my_title, my_imgUrl, my_link, my_release, my_director))
//                }

//                //추출한 전체 <li> 출력해 보자.
//                Logger.d("debug :", "List $mElementDataSize")
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return list
        }

        override fun onPostExecute(result: ArrayList<MarvelModel>) {


        }
    }
}
