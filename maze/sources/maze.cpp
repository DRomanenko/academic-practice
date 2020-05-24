#include <random>
#include <unordered_set>
#include <unordered_map>
#include "../headers/maze.h"
#include "../headers/dsu.h"

maze::maze() {
    for (size_t k = 1; k <= MAZE_WIDTH; k++) {
        set_wall(1, k, up);
        set_wall(MAZE_HEIGHT, k, down);
    }
    for (size_t k = 1; k <= MAZE_HEIGHT; k++) {
        set_wall(k, 1, left);
        set_wall(k, MAZE_WIDTH, right);
    }
}

bool maze::get_wall(size_t x, size_t y, direction d) const {
    return _table[x][y].get(d);
}

void maze::set_wall(size_t x, size_t y, direction d) {
    _table[x][y].set(d);
    _table[x + dx[d]][y + dy[d]].set(opposite[d]);
}

void maze::reset_wall(size_t x, size_t y, direction d) {
    _table[x][y].reset(d);
    _table[x + dx[d]][y + dy[d]].reset(opposite[d]);
}

uint32_t maze::get_data(size_t x, size_t y) const {
    return _table[x][y].data();
}

std::ostream & operator<<(std::ostream & out, maze const & m) {
    bool x, y;
    for (size_t k = 2; k <= 2 * MAZE_HEIGHT + 2; k++) {
        for (size_t i = 2; i <= 2 * MAZE_WIDTH + 2; i++) {
            x = k & 1u;
            y = i & 1u;
            if (x && y) {
                out << '.';
            } else if (!x && !y) {
                out << '+';
            } else if (x) {
                out << ((m.get_wall(k >> 1u, i >> 1u, left)) ? '|' : '.');
            } else {
                out << ((m.get_wall(k >> 1u, i >> 1u, up)) ? '-' : '.');
            }
        }
        out << '\n';
    }
    return out;
}
