#ifndef INC_MAZE_DSU_H
#define INC_MAZE_DSU_H

#include <cstddef>
#include <algorithm>

template<size_t N>
class dsu {
 public:
    dsu();
    size_t operator[](size_t x) {
        return _mark[get(x)];
    }
    void join(size_t x, size_t y);
 private:
    size_t get(size_t x);

    size_t _p[N] = {};
    size_t _sz[N] = {};
    size_t _mark[N] = {};
};

template<size_t N>
dsu<N>::dsu() {
    for (size_t k = 0; k < N; k++) {
        _sz[k] = 1;
        _mark[k] = _p[k] = k;
    }
}

template<size_t N>
void dsu<N>::join(size_t x, size_t y) {
    x = get(x);
    y = get(y);
    if (x != y) {
        if (_sz[x] < _sz[y]) {
            std::swap(x, y);
        }
        _p[y] = x;
        _sz[x] += _sz[y];
        _mark[x] = std::min(_mark[x], _mark[y]);
    }
}

template<size_t N>
size_t dsu<N>::get(size_t x) {
    if (_p[x] != x) {
        _p[x] = get(_p[x]);
    }
    return _p[x];
}

#endif //INC_MAZE_DSU_H
