import pygame

import entities
import ordered_list
import actions
import occ_grid
import point


class WorldModel:
    def __init__(self, num_rows, num_cols, background):
        self.background = occ_grid.Grid(num_cols, num_rows, background)
        self.num_rows = num_rows
        self.num_cols = num_cols
        self.occupancy = occ_grid.Grid(num_cols, num_rows, None)
        self.entities = []
        self.action_queue = ordered_list.OrderedList()

    def find_open_around(self, pt, distance):
        for dy in range(-distance, distance + 1):
            for dx in range(-distance, distance + 1):
                new_pt = point.Point(pt.x + dx, pt.y + dy)

                if (self.within_bounds(new_pt) and
                        (not self.is_occupied(new_pt))):
                    return new_pt

        return None

    def within_bounds(self, pt):
        return (pt.x >= 0 and pt.x < self.num_cols and
                pt.y >= 0 and pt.y < self.num_rows)

    def is_occupied(self, pt):
        return (self.within_bounds(pt) and
                occ_grid.get_cell(self.occupancy, pt) != None)

    def add_entity(self, entity):
        pt = entities.get_position(entity)
        if self.within_bounds( pt):
            old_entity = occ_grid.get_cell(self.occupancy, pt)
            if old_entity != None:
                entities.clear_pending_actions(old_entity)
            occ_grid.set_cell(self.occupancy, pt, entity)
            self.entities.append(entity)

    def remove_entity_at(self, pt):
        if (self.within_bounds(pt) and
                    occ_grid.get_cell(self.occupancy, pt) != None):
            entity = occ_grid.get_cell(self.occupancy, pt)
            entity.set_position(point.Point(-1, -1))
            self.entities.remove(entity)
            occ_grid.set_cell(self.occupancy, pt, None)

    def move_entity(self, entity, pt):
        tiles = []
        if self.within_bounds(pt):
            old_pt = entities.get_position(entity)
            occ_grid.set_cell(self.occupancy, old_pt, None)
            tiles.append(old_pt)
            occ_grid.set_cell(self.occupancy, pt, entity)
            tiles.append(pt)
            entity.set_position(pt)

        return tiles
    
    def remove_entity(self, entity):
        self.remove_entity_at(entities.get_position(entity))

    def schedule_action(self, action, time):
        self.action_queue.insert(action, time)

    def unschedule_action(self, action):
        self.action_queue.remove(action)

    def update_on_time(self, ticks):
        tiles = []

        next = self.action_queue.head()
        while next and next.ord < ticks:
            self.action_queue.pop()
            tiles.extend(next.item(ticks))  # invoke action function
            next = self.action_queue.head()

        return tiles

    def get_background_image(self, pt):
       if self.within_bounds(pt):
            return entities.get_image(occ_grid.get_cell(self.background, pt))

    def get_background(self, pt):
        if self.within_bounds(pt):
            return occ_grid.get_cell(self.background, pt)

    def set_background(self, pt, bgnd):
        if self.within_bounds(pt):
            occ_grid.set_cell(self.background, pt, bgnd)

    def get_tile_occupant(self, pt):
        if self.within_bounds(pt):
            return occ_grid.get_cell(self.occupancy, pt)

    def get_entities(self):
        return self.entities

    def find_nearest(world, pt, type):
        oftype = [(e, distance_sq(pt, entities.get_position(e)))
                  for e in world.entities if isinstance(e, type)]

        return nearest_entity(oftype)

def nearest_entity(entity_dists):
    if len(entity_dists) > 0:
        pair = entity_dists[0]
        for other in entity_dists:
            if other[1] < pair[1]:
                pair = other
        nearest = pair[0]
    else:
        nearest = None

    return nearest

def distance_sq(p1, p2):
    return (p1.x - p2.x) ** 2 + (p1.y - p2.y) ** 2