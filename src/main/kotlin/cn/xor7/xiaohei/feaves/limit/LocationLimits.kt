package cn.xor7.xiaohei.feaves.limit

import cn.xor7.xiaohei.feaves.data.LocationData

typealias LocationLimit = LocationData
typealias LocationLimits = MutableSet<LocationLimit>

fun LocationLimits.allIncludes(real: LocationData) = filter { it.includes(real) }

fun LocationLimits.removeAllIncludes(real: LocationData) = removeAll { it.includes(real) }