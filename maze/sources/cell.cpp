#include "../headers/cell.h"

cell::cell() {}

bool cell::get(direction dir) const {
    return (_walls >> dir) & 1u;
}

void cell::set(direction dir) {
    _walls |= (1u << dir);
}

void cell::reset(direction dir) {
    _walls &= ~(1u << dir);
}

uint32_t cell::data() const {
    return _walls;
}
