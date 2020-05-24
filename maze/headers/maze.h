#ifndef INC_MAZE_MAZE_H
#define INC_MAZE_MAZE_H

#include "cell.h"
#include "constants.h"
#include <iostream>

class maze {
 public:
    maze();

    bool get_wall(size_t x, size_t y, direction d) const;
    void set_wall(size_t x, size_t y, direction d);
    void reset_wall(size_t x, size_t y, direction d);

    uint32_t get_data(size_t x, size_t y) const;

 private:
    cell _table[MAZE_HEIGHT + 2][MAZE_WIDTH + 2] = {};
};

std::ostream & operator<<(std::ostream &, maze const &);

#endif //INC_MAZE_MAZE_H
