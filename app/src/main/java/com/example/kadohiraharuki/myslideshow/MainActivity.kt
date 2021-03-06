package com.example.kadohiraharuki.myslideshow

import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.BounceInterpolator
import android.widget.ImageSwitcher
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {

    //イメージ画像をリスト化
    private val resources = listOf(
            R.drawable.slide00, R.drawable.slide01,
            R.drawable.slide02, R.drawable.slide03,
            /*R.drawable.slide04,*/ R.drawable.slide05,
            R.drawable.slide06, R.drawable.slide07,
            R.drawable.slide08  /*,R.drawable.slide09*/
    )

    //10枚の画像のうち、どの画像を表示しているかを把握するためのプロパティ
    private var position = 0

    private var isSlideshow = false
    //Handlerクラスのインスタンスを生成、Androidでスレッドを使用する場合Handlerクラスを利用する
    private val handler = Handler()

    //メディアプレイヤーのプロパティ用意
    private lateinit var player: MediaPlayer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //imageswitcherは内部に複数のビューを持ち、これを切り替える
        //setFactoryメソッドは、切り替えるビューを生成する方法を指定します、ViwSwitcherはimageSwitcherの親クラス
        imageSwitcher.setFactory {
            //イメージビューのインスタンス生成
            ImageView(this)
        }
        //最初の画像をimageSwitcherに表示
        imageSwitcher.setImageResource(resources[0])

        //前後のボタンで次のスライドに移行するように設定
        prevButton.setOnClickListener{
            //アニメーション機能追加
            imageSwitcher.setInAnimation(this, android.R.anim.fade_in)
            imageSwitcher.setOutAnimation(this, android.R.anim.fade_out)
            movePosition(-1)
        }

        nextButton.setOnClickListener{
            imageSwitcher.setInAnimation(this, android.R.anim.slide_in_left)
            imageSwitcher.setOutAnimation(this, android.R.anim.slide_out_right)
            movePosition(1)
        }

        //ドロイド君のイメージビューをタップした時にアニメーションするようにプログラム
        imageView.setOnClickListener {
            /*
            //引き数はit,ViewPropertyAnimatorのインスタンスを生成し、スコープ関数applyにより続けて処理
            it.animate().apply{
                //3秒かけてアニメーションするように指定
                duration = 3000L
                //rotationメソッドで回転する角度を指定。今回は5回転
                rotation(360.0f * 5.0f)
                }
                */

            it.animate().apply {
                duration = 1000L
                //ポールが飛ぶように移動するアニメーション
                interpolator = BounceInterpolator()
                //Viewのyプロパティはビューが配置されている位置のy座標を取得する。ここに100ピクセルを足す
                y(it.y + 100.0f)

            }
        }

        timer(period = 5000){
            handler.post{
                //isSlideshowがtrueの時スライド移動
                if(isSlideshow) movePosition(1)
            }
        }

        slideshowButton.setOnClickListener{
            isSlideshow = !isSlideshow
            //isSlideshowを起動時
            when(isSlideshow){
                true -> player.start()
                false -> player.apply{
                    //中止
                    pause()
                    //初めから再生
                    seekTo(0)
                }
            }
        }

        //createメソッドでMediaPlayerのインスタンスを生成
        player = MediaPlayer.create(this, R.raw.getdown)
        player.isLooping = true

        /*
        //お試し：x軸 ,y軸方向に2.5倍拡大
        imageView.setOnClickListener{
            it.animate().apply{
                duration = 3000L
                scaleX(2.5f)
                scaleY(2.5f)
            }
        }

        //お試し2：座標移動、両軸200ピクセル移動
        imageView.setOnClickListener{
            it.animate().apply{
                duration = 3000L
                x(200.0f)
                y(200.0f)
            }
        }
        */
    }

    //ImageSwitcherに画像を表示するmovePositionメソッド作成
    private fun movePosition(move: Int){
        //表示する画像の位置であるposition変数を引数moveの値だけ変化させる
        position += move
        if(position >= resources.size) {
            position = 0
        }
        //リストの要素数はsizeプロパティで取得する
        else if (position < 0){
            position = resources.size - 1
        }
        imageSwitcher.setImageResource(resources[position])

    }
}
