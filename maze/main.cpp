#include <iostream>
#include <random>
#include <unordered_set>
#include <unordered_map>
#include "headers/maze.h"
#include "headers/dsu.h"

maze generate() {
    maze result;
    std::random_device rd;

    dsu<MAZE_WIDTH + 2> ds;
    std::unordered_map<size_t, size_t> first;

    for (size_t k = 1; k <= MAZE_HEIGHT; k++) {
        std::unordered_set<size_t> sets[MAZE_WIDTH + 2];
        // STEP 1 : get union-set for new row

        dsu<MAZE_WIDTH + 2> nds;
        for (size_t i = 1; i <= MAZE_WIDTH; i++) {
            if (!result.get_wall(k, i, up)) {
                if (first.count(ds[i]) == 0) {
                    first[ds[i]] = i;
                    sets[i].insert(i);
                } else {
                    nds.join(first[ds[i]], i);
                    sets[first[ds[i]]].insert(i);
                }
            } else {
                sets[i].insert(i);
            }
        }
        first.clear();
        ds = nds;

        // STEP 2 : set vertical walls
        for (size_t i = 1; i < MAZE_WIDTH; i++) {
            if (ds[i] == ds[i + 1] || (rd() & 1u)) {
                result.set_wall(k, i, right);
            } else {
                ds.join(i, i + 1);
            }
        }

        // STEP 3 : set horizontal walls
        for (size_t i = 1; i <= MAZE_WIDTH; i++) {
            if (sets[ds[i]].size() > 1 && (rd() & 1u)) {
                result.set_wall(k, i, down);
                sets[ds[i]].erase(i);
            }
        }
    }
    // TODO: not forget about the last row
    for (size_t i = 1; i < MAZE_WIDTH; i++) {
        if (ds[i] != ds[i + 1]) {
            result.reset_wall(MAZE_HEIGHT, i, right);
            ds.join(i, i + 1);
        }
    }

    return result;
}

double measure(maze const & m) {
    std::unordered_map<uint32_t, size_t> all = {};
    for (size_t k = 1; k < MAZE_HEIGHT; k++) {
        for (size_t i = 1; i < MAZE_WIDTH; i++) {
            uint32_t value = m.get_data(k, i) |
                    (m.get_data(k, i + 1) << 4u) |
                    (m.get_data(k + 1, i) << 8u) |
                    (m.get_data(k + 1, i + 1) << 16u);
            all[value]++;
        }
    }
    double result = 1;
    for (auto const & it : all) {
        result += 1. / (abs(20 - static_cast<int>(it.second)) + 1);
    }
    return result;
}

maze generate_best(size_t tries) {
    maze result;
    double best_value = -1;

    for (size_t k = 0; k < tries; k++) {
        maze current = generate();
        double value = measure(current);

        if (value > best_value) {
            best_value = value;
            result = current;

            FILE * file = freopen("output.txt", "w", stdout);
            std::cout << current << '\n' << log(best_value) + 1 << '\n';
            fclose(file);
        }
    }

    return result;
}

int main() {
    generate_best(6 * 60 * 15000);
    return 0;
}
