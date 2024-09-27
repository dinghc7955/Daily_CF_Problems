/*
一个组里的人不能有一个人比其他人都强，求一个组使得总能力值最大
如果恰好是两个人，则必须算法一模一样
如何快速判断一个人是否比另一个人强？
假设一个人的算法是 a，另一个人是 b，则 a ^ b 就是他们差异的算法
(a & (a ^ b)) > 0，则 a 比 b 厉害
假设现在有一个组满足不存在任何一个人比其他任何人都强
现在要新加入一个人，这个人算法能力是 a，则要满足什么条件？
需要组里有一个人算法能力是 b，满足 a 是 b 的子集，即存在 b 使得 (a & b) = a
倘若有两个人会的算法一样，则这两个人一定不会导致不行

羊解：
假如新加入的人 a 是某个组内已有的人 b 的子集，则可以分两种情况：
- a = b，则可以把所有 a 的子集表示的人加入到组中
- a 是 b 的真子集，则由于原来的组是合法的，所以组里必须有一个人 c，使得 b 是 c 的子集
  这样又可以分两种情况来讨论...由于算法种类有限，所以一直分类下去肯定组里有两个人 d 和 e 算法相同，
  否则这就不是合法的组。然后我们可以把所有是 d 的子集的人加入进来（事实上早就加进来了）
因此，我们发现一个合法的组必须有两个人算法一模一样，然后把他们会的算法的子集的人都加进来
我们每遇到两个一样的人，就把没他们强的都统计进来计算即可
*/

int n;
LL a[M], b[M];
bool vis[M];
unordered_map<LL, int> cnt;
unordered_map<LL, int> id;

void solve1() {
    cin >> n;
    for (int i = 1; i <= n; i++) {
        cin >> a[i];
        cnt[a[i]]++;
        id[a[i]] = i;
    }
    for (int i = 1; i <= n; i++) {
        cin >> b[i];
    }
    LL res = 0;
    for (auto &it : cnt) {
        LL mask = it.x, c = it.y;
        if (c >= 2) {
            int pos = id[mask];
            for (int i = 1; i <= n; i++) {
                if ( (a[pos] & a[i]) == a[i] && !vis[i] ) {
                    res += b[i];
                    vis[i] = true;
                }
            }
        }
    }
    cout << res << "\n";
}    