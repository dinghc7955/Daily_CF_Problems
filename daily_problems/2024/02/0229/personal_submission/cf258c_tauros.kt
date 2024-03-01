class MOD {
    companion object {
        private val INV_MAP = mutableMapOf<Int, MutableMap<Int, Int>>()
        private val INV_LONG_MAP = mutableMapOf<Int, MutableMap<Long, Int>>()
        var globalMod: Int = 0

        fun inv(num: Int): Int = INV_MAP.computeIfAbsent(globalMod) { mutableMapOf() }.computeIfAbsent(num) { inv(num, globalMod) }

        fun inv(num: Long): Int = INV_LONG_MAP.computeIfAbsent(globalMod) { mutableMapOf() }.computeIfAbsent(num) { inv(num, globalMod.toLong()).toInt() }

        fun pow(num: Long, p: Long): Int {
            var pow = p
            var a = num
            if (a >= globalMod) {
                a %= globalMod.toLong()
            }
            var res: Long = 1
            while (pow > 0) {
                if (pow and 1 == 1L) {
                    res = res * a % globalMod
                }
                a = a * a % globalMod
                pow = pow shr 1
            }
            return res.toInt()
        }

        inline fun regular(num: Int): Int = if (num in 0 until globalMod) num else {
            val r = num % globalMod
            r + (globalMod and (((r xor globalMod) and (r or -r)) shr 31))
        }

        inline fun regular(num: Long): Int = if (num in 0 until globalMod) num.toInt() else {
            val r = (num % globalMod).toInt()
            r + (globalMod and (((r xor globalMod) and (r or -r)) shr 31))
        }

        inline fun add(a: Int, b: Int): Int {
            val res = regular(a) + regular(b)
            return regular(res)
        }

        inline fun sub(a: Int, b: Int): Int {
            val res = regular(a) - regular(b)
            return regular(res)
        }

        inline fun mul(a: Int, b: Int): Int {
            val res = regular(a).toLong() * regular(b)
            return regular(res)
        }

        inline fun div(a: Int, b: Int): Int {
            val res = regular(a).toLong() * inv(regular(b))
            return regular(res)
        }

        inline fun <T> decorate(mod: Int, process: Companion.() -> T): T {
            globalMod = mod
            val res = process(Companion)
            globalMod = 0
            return res
        }
    }
}
fun exgcd(a: Int, b: Int): IntArray {
    // calc ax + by = gcd(a, b)
    // x = x0 + k * (b/gcd(a,b)); y = y0 - k * (a/gcd(a,b))
    // calc ax + by = o * gcd(a, b) -> calc a * o * x' + b * o * y' = o * gcd(a, b)
    // x = x0 * o + k * (b/gcd(a,b)); y = y0 * o - k * (a/gcd(a,b))
    if (b == 0) {
        // gcd, x, y
        return intArrayOf(a, 1, 0)
    }
    // x = y0; y = x0 - (a / b) * y0;
    val (gcd, x0, y0) = exgcd(b, a % b)
    val y = x0 - a / b * y0
    return intArrayOf(gcd, y0, y)
}
fun exgcd(a: Long, b: Long): LongArray {
    // calc ax + by = gcd(a, b)
    // x = x0 + k * (b/gcd(a,b)); y = y0 - k * (a/gcd(a,b))
    // calc ax + by = o * gcd(a, b) -> calc a * o * x' + b * o * y' = o * gcd(a, b)
    // x = x0 * o + k * (b/gcd(a,b)); y = y0 * o - k * (a/gcd(a,b))
    if (b == 0L) {
        // gcd, x, y
        return longArrayOf(a, 1, 0)
    }
    // x = y0; y = x0 - (a / b) * y0;
    val (gcd, x0, y0) = exgcd(b, a % b)
    val y = x0 - a / b * y0
    return longArrayOf(gcd, y0, y)
}
typealias int = Int
fun inv(a: Long, p: Long): Long {
    val (gcd, inv, _) = exgcd(a, p)
    if (gcd != 1L) return -1
    return (inv + p) % p
}
fun inv(a: Int, p: Int): Int {
    val (gcd, inv, _) = exgcd(a, p)
    if (gcd != 1) return -1
    return (inv + p) % p
}
infix fun Int.ma(other: Int): Int = MOD.add(this, other)
infix fun Int.mm(other: Int): Int = MOD.mul(this, other)
infix fun Int.ms(other: Int): Int = MOD.sub(this, other)
typealias string = String
inline fun <T> withMod(mod: Int, process: MOD.Companion.() -> T): T {
    return MOD.decorate(mod, process)
}
inline fun iao(vararg nums: Int) = intArrayOf(*nums)
inline fun iar(size: Int, init: (Int) -> Int = { 0 }) = IntArray(size) { init(it) }
inline fun <reified T> mlo(vararg elements: T) = mutableListOf(*elements)
inline fun <reified K, reified V> mmo(vararg pairs: Pair<K, V>) = mutableMapOf(*pairs)

/**
 * generated by kotlincputil@tauros
 */
fun main(args: Array<String>) {
    // https://codeforces.com/contest/258/problem/C
    // 一开始想容斥，想错了，推了一年，可以根据约数直接算
    // - 考虑枚举每个位置 lcm=max 的这个值，设为num，那么这个数组将由num的约数构成
    // - 统计可以取到某个值的位置个数，可以差分统计下
    // - 然后约数从大到小枚举，当前约数为div，能选div就一定能选比div小的约数
    // - 比如24的约数有 24 12 8 6 4 3 2 1，当前约数为24就有8个选项，当前约数为8就有6个选项，...
    // - 统计答案应该为 约数x选项个数^约数x位置数 * 约数y选项个数^约数y位置数 * ...
    // - 统计约数位置个数：前面已经选了sum个，那么当前能选min(n - sum, 能选当前约数位置个数 - sum)，从大到小执行...
    // - 然后考虑必须要有至少一个最大的约数，也就是其自身，把最大的约数单独拿出来逻辑处理下即可
    // - 瓶颈是筛约数，约数总个数规模为AlogA，快速幂还有个logA，所以是AlogAlogA
    val n = readln().toInt()
    val nums = readln().split(" ").map(string::toInt).toIntArray()

    val cap = 1e5.toInt()
    val cnt = iar(cap + 2)
    for (num in nums) {
        cnt[1] += 1
        cnt[num + 1] -= 1
    }
    for (num in 1..cap) cnt[num] += cnt[num - 1]

    withMod(1e9.toInt() + 7) {
        val ept = mlo<int>()
        val divs = mmo<int, MutableList<int>>()
        var ans = 0
        for (i in 1..cap) if (cnt[i] > 0) {
            var (sum, less) = iao(cnt[i], 1)
            for (j in divs.getOrDefault(i, ept).indices.reversed()) {
                if (sum >= n) break
                val c = divs[i]!![j]
                val use = minOf(n - sum, c - sum)
                less = pow(j + 1L, use.toLong()) mm less
                sum += use;
            }
            if (sum == n) {
                val divSize = divs.getOrDefault(i, ept).size.toLong()
                val topTotal = pow(divSize + 1, cnt[i].toLong())
                val topRemove = pow(divSize, cnt[i].toLong())
                val res = less mm (topTotal ms topRemove)
                ans = ans ma res
            }

            for (j in i..cap step i)
                divs.computeIfAbsent(j) { mlo() }.add(cnt[i])
        }
        println(ans)
    }
}