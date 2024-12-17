package com.note.compose.dagger.base

import com.note.compose.dagger.utils.VideoModel
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

object RxBus {

    private val publisher = PublishSubject.create<Any>()

    fun publish(event: Any) {
        publisher.onNext(event)
    }

    // Listen should return an Observable and not the publisher
    // Using ofType we filter only events that match that class type
    fun <T> listen(eventType: Class<T>): Observable<T> = publisher.ofType(eventType)
}

class RxEvent {
    data class HideMuteUnMuteIcon(val isHide:Boolean)
    data class ReelMuteUnMuteClick(val isMute:Boolean)
    data class LikeClick(val reel : VideoModel)
}