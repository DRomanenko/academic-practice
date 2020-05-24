#ifndef INC_MAZE_CELL_H
#define INC_MAZE_CELL_H

#include <cstdint>
#include "direction.h"

class cell {
 public:
    cell();
    cell(cell const & other) = default;
    cell & operator=(cell const & other) = default;

    bool get(direction dir) const;
    void set(direction dir);
    void reset(direction dir);

    uint32_t data() const;

 private:
    uint8_t _walls = 0;
};


#endif //INC_MAZE_CELL_H
