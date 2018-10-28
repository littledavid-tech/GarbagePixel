package cn.shycoder.garbagepixel.lib

/**
 * Created by ShyCoder on 10/28/2018.
 */
class Request {
    class RequestBuilder {
        private var mLoadFrom: LoadFrom = LoadFrom.ANY

        fun loadFrom(loadFrom: LoadFrom) {
            mLoadFrom = loadFrom
        }
    }
}