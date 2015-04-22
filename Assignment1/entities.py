import point
import actions

RATE_MULTIPLIER = 30

PROPERTY_KEY = 0

MINER_NUM_PROPERTIES = 7
MINER_NAME = 1
MINER_LIMIT = 4
MINER_COL = 2
MINER_ROW = 3
MINER_RATE = 5
MINER_ANIMATION_RATE = 6

OBSTACLE_NUM_PROPERTIES = 4
OBSTACLE_NAME = 1
OBSTACLE_COL = 2
OBSTACLE_ROW = 3

ORE_NUM_PROPERTIES = 5
ORE_NAME = 1
ORE_COL = 2
ORE_ROW = 3
ORE_RATE = 4

SMITH_NUM_PROPERTIES = 7
SMITH_NAME = 1
SMITH_COL = 2
SMITH_ROW = 3
SMITH_LIMIT = 4
SMITH_RATE = 5
SMITH_REACH = 6

VEIN_NUM_PROPERTIES = 6
VEIN_NAME = 1
VEIN_RATE = 4
VEIN_COL = 2
VEIN_ROW = 3
VEIN_REACH = 5

PROPERTY_KEY = 0
MINER_KEY = 'miner'
OBSTACLE_KEY = 'obstacle'
ORE_KEY = 'ore'
SMITH_KEY = 'blacksmith'
VEIN_KEY = 'vein'

class Entity(object):

    def __init__(self, name, imgs):
        self.name = name
        self.imgs = imgs
        self.current_img = 0

    def get_images(self):
        return self.imgs

    def get_image(self):
        return self.imgs[self.current_img]

    def get_name(self):
        return self.name

    def next_image(self):
        self.current_img = (self.current_img + 1) % len(self.imgs)

    def entity_string(self):
        return 'unknown'


class Background(Entity):
      pass


class Positionable(Entity):

    def __init__(self, name, position, imgs):
        self.position = position
        super(Positionable, self).__init__(name, imgs)

    def set_position(self, point):
        self.position = point

    def get_position(self):
        return self.position


class Obstacle(Positionable):

    def entity_string(self):
        return ' '.join(['obstacle', self.name, str(self.position.x),
                         str(self.position.y)])
    @staticmethod
    def create_from_properties(properties, i_store):
        if len(properties) == OBSTACLE_NUM_PROPERTIES:
            return Obstacle(properties[OBSTACLE_NAME],
                    point.Point(int(properties[OBSTACLE_COL]), int(properties[OBSTACLE_ROW])),
                    i_store.get_images(properties[PROPERTY_KEY]))

        else:
            return None


class Actor(Positionable):

    def __init__(self, name, position, imgs):
        self.pending_actions = []
        super(Actor, self).__init__(name, position, imgs)

    def remove_pending_action(self, action):
        self.pending_actions.remove(action)

    def add_pending_action(self, action):
        self.pending_actions.append(action)

    def get_pending_actions(self):
        return self.pending_actions

    def clear_pending_actions(self):
        self.pending_actions = []

    def schedule_action(self, world, ticks, i_store, schedule_rate):
        world.schedule_action(self,
                        self.create_action(world, i_store),
                        ticks + schedule_rate)


class Ore(Actor):

    def __init__(self, name, position, imgs, rate=5000):
        self.rate = rate
        super(Ore, self).__init__(name, position, imgs)

    def entity_string(self):
        return ' '.join(['ore', self.name, str(self.position.x),
                         str(self.position.y), str(self.rate)])

    def schedule(self, world, ticks, i_store):
        self.schedule_action(world, ticks, i_store, self.rate)

    def create_action(self, world, i_store):
        def action(current_ticks):
            self.remove_pending_action(action)
            blob = actions.create_blob(world, self.name + " -- blob",
                               self.position,
                               self.rate // actions.BLOB_RATE_SCALE // RATE_MULTIPLIER,
                               current_ticks, i_store)

            world.remove_entity(self)
            world.add_entity(blob)

            return [blob.get_position()]

        return action

    @staticmethod
    def create_from_properties(properties, i_store):
        if len(properties) == ORE_NUM_PROPERTIES:
            ore = Ore(properties[ORE_NAME],
                   point.Point(int(properties[ORE_COL]), int(properties[ORE_ROW])),
                   i_store.get_images(properties[PROPERTY_KEY]),
                   int(properties[ORE_RATE])//RATE_MULTIPLIER)

            return ore

        else:
            return None


class Vein(Actor):

    def __init__(self, name, rate, position, imgs, resource_distance=1):
        self.rate = rate
        self.resource_distance = resource_distance
        super(Vein, self).__init__(name, position, imgs)


    def entity_string(self):
        return ' '.join(['vein', self.name, str(self.position.x),
                         str(self.position.y), str(self.rate),
                         str(self.resource_distance)])

    def create_action(self, world, i_store):
        def action(current_ticks):
            self.remove_pending_action(action)

            open_pt = world.find_open_around(self.position,
                                       self.resource_distance)
            if open_pt:
                ore = actions.create_ore(world,
                                 "ore - " + self.name + " - " + str(current_ticks),
                                 open_pt, current_ticks, i_store)
                world.add_entity(ore)
                tiles = [open_pt]
            else:
                tiles = []

            self.schedule_action(world, current_ticks, i_store, self.rate)

            return tiles

        return action

    def schedule(self, world, ticks, i_store):
        self.schedule_action(world, ticks, i_store, self.rate)

    @staticmethod
    def create_from_properties(properties, i_store):
        if len(properties) == VEIN_NUM_PROPERTIES:
            vein = Vein(properties[VEIN_NAME],
                    int(properties[VEIN_RATE])//RATE_MULTIPLIER,
                    point.Point(int(properties[VEIN_COL]), int(properties[VEIN_ROW])),
                    i_store.get_images(properties[PROPERTY_KEY]),
                    int(properties[VEIN_REACH]))

            return vein

        else:
            return None


class Blacksmith(Actor):
    def __init__(self, name, position, imgs, resource_limit, rate,
                 resource_distance=1):
        self.rate = rate
        self.resource_limit = resource_limit
        self.resource_count = 0
        self.resource_distance = resource_distance
        super(Blacksmith, self).__init__(name, position, imgs)

    def set_resource_count(self, n):
        self.resource_count = n

    def get_resource_count(self):
        return self.resource_count

    def get_resource_limit(self):
        return self.resource_limit

    def get_resource_distance(self):
         return self.resource_distance

    def entity_string(self):
        return ' '.join(['blacksmith', self.name, str(self.position.x),
                         str(self.position.y), str(self.resource_limit),
                         str(self.rate), str(self.resource_distance)])

    @staticmethod
    def create_from_properties(properties, i_store):
        if len(properties) == SMITH_NUM_PROPERTIES:
            return Blacksmith(properties[SMITH_NAME],
                    point.Point(int(properties[SMITH_COL]), int(properties[SMITH_ROW])),
                    i_store.get_images(properties[PROPERTY_KEY]),
                    int(properties[SMITH_LIMIT]),
                    int(properties[SMITH_RATE])//RATE_MULTIPLIER,
                    int(properties[SMITH_REACH]))

            return smith

        else:
            return None


class Quake(Actor):

    def __init__(self, name, position, imgs, animation_rate):
        self.animation_rate = animation_rate
        super(Quake, self).__init__(name, position, imgs)

    def get_animation_rate(self):
        return self.animation_rate

    def create_animation_action(self, world, repeat_count):
        def action(current_ticks):
            self.remove_pending_action(action)

            self.next_image()

            if repeat_count != 1:
                world.schedule_action(self,
                 self.create_animation_action(world, max(repeat_count - 1, 0)),
                 current_ticks + self.animation_rate)

            return [self.position]

        return action

    def create_action(self, world, i_store):
        def action(current_ticks):
            self.remove_pending_action(action)
            pt = self.get_position()
            world.remove_entity(self)
            return [pt]

        return action

    def schedule(self, world, ticks, i_store):
        world.schedule_animation(self, actions.QUAKE_STEPS)
        self.schedule_action(world, ticks, i_store, actions.QUAKE_DURATION)


class Mover(Quake):

    def __init__(self, name, position, rate, imgs, animation_rate):
        self.rate = rate
        super(Mover, self).__init__(name, position, imgs, animation_rate)

    def get_rate(self):
        return self.rate

    def next_position(self, world, dest_pt):
        horiz = sign(dest_pt.x - self.position.x)
        new_pt = point.Point(self.position.x + horiz, self.position.y)

        if horiz == 0 or not self.can_move(world, new_pt):

            vert = sign(dest_pt.y - self.position.y)
            new_pt = point.Point(self.position.x, self.position.y + vert)

            if vert == 0 or not self.can_move(world, new_pt):
                new_pt = point.Point(self.position.x, self.position.y)

        return new_pt

    def to_target(self, world,  target):
        if not target:
            return ([self.position], False)
        target_pt = target.get_position()
        if adjacent(self.position, target_pt):
            return ([target_pt], True)
        else:
            new_pt = self.next_position(world, target_pt)
            return (world.move_entity(self, new_pt), False)

    def schedule(self, world, ticks, i_store):
        self.schedule_action(world, ticks, i_store, self.rate)
        world.schedule_animation(self)


class OreBlob(Mover):

    def create_action(self, world, i_store):
        def action(current_ticks):
            self.remove_pending_action(action)

            entity_pt = self.position
            vein = world.find_nearest(entity_pt, Vein)
            (tiles, found) = self.to_vein(world, vein)

            delay_time = self.rate
            if found:
                world.remove_entity(vein)
                quake = actions.create_quake(world, tiles[0], current_ticks, i_store)
                world.add_entity(quake)
                delay_time = self.rate * 2

            self.schedule_action(world, current_ticks, i_store, delay_time)

            return tiles

        return action

    def to_vein(self, world, vein):
        (new_pt, adjacent) = self.to_target(world, vein)

        if new_pt and vein and not adjacent:
            old_entity = world.get_tile_occupant(new_pt[0])
            if isinstance(old_entity, Ore):
                world.remove_entity(old_entity)

        return (new_pt, adjacent)

    def can_move(self, world, pt):
        return not world.is_occupied(pt) or isinstance(world.get_tile_occupant(pt), Ore)


class Miner(Mover):
    def __init__(self, name, resource_limit, position, rate, imgs,
                 animation_rate):
        self.resource_limit = resource_limit
        self.resource_count = 0
        super(Miner, self).__init__(name, position, rate, imgs, animation_rate)

    def entity_string(self):
        return ' '.join(['miner', self.name, str(self.position.x),
                         str(self.position.y), str(self.resource_limit),
                         str(self.rate), str(self.animation_rate)])

    def create_action(self, world, i_store):
        def action(current_ticks):
            self.remove_pending_action(action)

            entity_pt = self.position

            target = None
            if self.resource_count < self.resource_limit:
                target = world.find_nearest(entity_pt, Ore)

            else:
                target = world.find_nearest(entity_pt, Blacksmith)

            (tiles, found) = self.to_target(world, target)
            if found:
                self.update_resource_count(world, target)

            self.schedule_action(world, current_ticks, i_store, self.rate)

            return tiles

        return action

    def update_resource_count(self, world, target):
        if self.resource_count < self.resource_limit:
            world.remove_entity(target)
            self.resource_count += 1

        else:
            target.set_resource_count(target.get_resource_count() +
                 self.resource_count)
            self.resource_count = 0

    def can_move(self, world, pt):
        return not world.is_occupied(pt)

    @staticmethod
    def create_from_properties(properties, i_store):
        if len(properties) == MINER_NUM_PROPERTIES:
            miner = Miner(properties[MINER_NAME],
                     int(properties[MINER_LIMIT]),
                     point.Point(int(properties[MINER_COL]), int(properties[MINER_ROW])),
                     int(properties[MINER_RATE]) // RATE_MULTIPLIER,
                     i_store.get_images(properties[PROPERTY_KEY]),
                     int(properties[MINER_ANIMATION_RATE]))

            return miner

        else:
            return None



def sign(x):
    if x < 0:
        return -1
    elif x > 0:
        return 1
    else:
        return 0

def adjacent(pt1, pt2):
    return ((pt1.x == pt2.x and abs(pt1.y - pt2.y) == 1) or
            (pt1.y == pt2.y and abs(pt1.x - pt2.x) == 1))

keys = {MINER_KEY: Miner,
    VEIN_KEY: Vein,
    ORE_KEY: Ore,
    SMITH_KEY: Blacksmith,
    OBSTACLE_KEY: Obstacle
}

def create_from_properties(properties, i_store):
    key = properties[PROPERTY_KEY]
    if properties:
            return keys[key].create_from_properties(properties, i_store)

    return None
