#include <bits/stdc++.h>
#pragma GCC optimize("O3,Ofast,unroll-loops")
// #include "atcoder/convolution"
// #include "atcoder/dsu"
// #include "atcoder/fenwicktree"
// #include "atcoder/lazysegtree"
// #include "atcoder/math"
// #include "atcoder/maxflow"
// #include "atcoder/mincostflow"
// #include "atcoder/modint"
// #include "atcoder/scc"
// #include "atcoder/segtree"
// #include "atcoder/string"
// #include "atcoder/twosat"
// using namespace __gnu_cxx;
// using namespace __gnu_pbds;
using namespace std;
auto rng = mt19937(random_device()());
auto rngl = mt19937_64(random_device()());

// Let's set a bit and flow!
// I came, I divided, I conquered!

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(0);
    cout.tie(0);

    int n, s = 1e6;
    vector<bool> vis(s + 1);

    cin >> n;
    for (int i = 0; i < n; i ++) {
        int x;
        cin >> x;
        vis[x] = 1;
    }

    cout << n << '\n';

    for (int i = 1; i <= s; i ++) {
        if (!vis[i] && vis[s + 1 - i]) {
            n --;
            vis[i] = 1;
            cout << i << ' ';
        }
    }

    for (int i = 1; i <= s; i ++) {
        if (!vis[i] && !vis[s + 1 - i] && n) {
            n -= 2;
            vis[i] = 1;
            vis[s + 1 - i] = 1;
            cout << i << ' ';
            cout << s + 1 - i << ' ';
        }
    }

    return 0;
}