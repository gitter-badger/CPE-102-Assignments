import pygame

import entities
import ordered_list
import actions
import occ_grid
import point
import image_store

PROPERTY_KEY = 0

BGND_KEY = 'background'
BGND_NUM_PROPERTIES = 4
BGND_NAME = 1
BGND_COL = 2
BGND_ROW = 3

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
                self.occupancy.get_cell(pt) != None)

    def add_entity(self, entity):
        pt = entity.get_position()
        if self.within_bounds( pt):
            old_entity = self.occupancy.get_cell(pt)
            if old_entity != None:
                old_entity.clear_pending_actions()
            self.occupancy.set_cell(pt, entity)
            self.entities.append(entity)

    def remove_entity_at(self, pt):
        if (self.within_bounds(pt) and
                    self.occupancy.get_cell(pt) != None):
            entity = self.occupancy.get_cell(pt)
            entity.set_position(point.Point(-1, -1))
            self.entities.remove(entity)
            self.occupancy.set_cell(pt, None)

    def move_entity(self, entity, pt):
        tiles = []
        if self.within_bounds(pt):
            old_pt = entity.get_position()
            self.occupancy.set_cell(old_pt, None)
            tiles.append(old_pt)
            self.occupancy.set_cell(pt, entity)
            tiles.append(pt)
            entity.set_position(pt)

        return tiles
    
    def remove_entity(self, entity):
        for action in entity.get_pending_actions():
            self.unschedule_action(action)
        entity.clear_pending_actions()
        self.remove_entity_at(entity.get_position())

    def unschedule_action(self, action):
        self.action_queue.remove(action)

    def schedule_action(self, entity, action, time):
        entity.add_pending_action(action)
        #self.schedule_action(action, time)
        self.action_queue.insert(action, time)

    def schedule_animation(self, entity, repeat_count=0):
        self.schedule_action(entity,
                        entity.create_animation_action(self, repeat_count),
                        entity.get_animation_rate())

    def clear_pending_actions(self, entity):
        for action in entity.get_pending_actions():
            self.unschedule_action(action)
        entity.clear_pending_actions()

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
            return self.background.get_cell(pt).get_image();

    def get_background(self, pt):
        if self.within_bounds(pt):
            return self.background.get_cell(pt)

    def set_background(self, pt, bgnd):
        if self.within_bounds(pt):
            self.background.set_cell(pt, bgnd)

    def get_tile_occupant(self, pt):
        if self.within_bounds(pt):
            return self.occupancy.get_cell(pt)

    def get_entities(self):
        return self.entities

    def find_nearest(self, pt, type):
        oftype = [(e, distance_sq(pt, e.get_position()))
                  for e in self.entities if isinstance(e, type)]

        return nearest_entity(oftype)

    def save_world(self, file):
        self.save_entities(file)
        self.save_background(file)

    def save_entities(self, file):
        for entity in self.get_entities():
            file.write(entity.entity_string() + '\n')

    def save_background(self, file):
        for row in range(0, self.num_rows):
            for col in range(0, self.num_cols):
                file.write('background' +
                           self.get_background(point.Point(col, row)).get_name() +
                           ' ' + str(col) + ' ' + str(row) + '\n')

    def load_world(self, images, file, run=False):
        for line in file:
            properties = line.split()
            if properties:
                if properties[PROPERTY_KEY] == BGND_KEY:
                    self.add_background(properties, images)
                else:
                    self.create_add_entity(properties, images, run)

    def add_background(self, properties, i_store):
        if len(properties) >= BGND_NUM_PROPERTIES:
            pt = point.Point(int(properties[BGND_COL]), int(properties[BGND_ROW]))
            name = properties[BGND_NAME]
            self.set_background(pt, entities.Background(name,
                                  i_store.get_images(name)))

    def create_add_entity(self, properties, i_store, run):
        new_entity = entities.create_from_properties(properties, i_store)
        if new_entity:
            self.add_entity(new_entity)
            if run and hasattr(new_entity, 'schedule'):
                new_entity.schedule(self, 0, i_store)

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

