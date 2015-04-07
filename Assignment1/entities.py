import point
import actions

class Background:
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

    def remove_pending_action(self, action):
        pass

    def add_pending_action(self, action):
        pass

    def get_pending_actions(self):
        return []

    def clear_pending_actions(self):
        pass

    def entity_string(self):
        return 'unknown'

class MinerNotFull:
    def __init__(self, name, resource_limit, position, rate, imgs,
                 animation_rate):
        self.name = name
        self.position = position
        self.rate = rate
        self.imgs = imgs
        self.current_img = 0
        self.resource_limit = resource_limit
        self.resource_count = 0
        self.animation_rate = animation_rate
        self.pending_actions = []

    def set_position(self, point):
        self.position = point

    def get_position(self):
        return self.position

    def get_images(self):
        return self.imgs

    def get_image(self):
        return self.imgs[self.current_img]

    def get_rate(self):
        return self.rate

    def set_resource_count(self, n):
        self.resource_count = n

    def get_resource_count(self):
        return self.resource_count

    def get_resource_limit(self):
        return self.resource_limit

    def get_name(self):
        return self.name

    def get_animation_rate(self):
        return self.animation_rate

    def next_image(self):
        self.current_img = (self.current_img + 1) % len(self.imgs)

    def remove_pending_action(self, action):
        self.pending_actions.remove(action)

    def add_pending_action(self, action):
        self.pending_actions.append(action)

    def get_pending_actions(self):
        return self.pending_actions

    def clear_pending_actions(self):
        self.pending_actions = []

    def entity_string(self):
        return ' '.join(['miner', self.name, str(self.position.x),
                         str(self.position.y), str(self.resource_limit),
                         str(self.rate), str(self.animation_rate)])

    def create_action(self, world, i_store):
        def action(current_ticks):
            self.remove_pending_action(action)

            entity_pt = self.get_position()
            ore = world.find_nearest(entity_pt, Ore)
            (tiles, found) = actions.miner_to_ore(world, self, ore)

            new_entity = self
            if found:
                new_entity = actions.try_transform_miner(world, self,
                                                   self.try_transform)

            world.schedule_action(new_entity,
                            new_entity.create_action(world, i_store),
                            current_ticks + new_entity.get_rate())
            return tiles

        return action

    def try_transform(self):
        if self.resource_count < self.resource_limit:
            return self
        else:
            new_entity = MinerFull(
                self.get_name(), self.get_resource_limit(),
                self.get_position(), self.get_rate(),
                self.get_images(), self.get_animation_rate())
            return new_entity

    def create_animation_action(self, world, repeat_count):
        def action(current_ticks):
            self.remove_pending_action(action)

            self.next_image()

            if repeat_count != 1:
                world.schedule_action(self,
                                self.create_animation_action(world, max(repeat_count - 1, 0)),
                                current_ticks + self.get_animation_rate())

            return [self.get_position()]

        return action

class MinerFull:
    def __init__(self, name, resource_limit, position, rate, imgs,
                 animation_rate):
        self.name = name
        self.position = position
        self.rate = rate
        self.imgs = imgs
        self.current_img = 0
        self.resource_limit = resource_limit
        self.resource_count = resource_limit
        self.animation_rate = animation_rate
        self.pending_actions = []

    def set_position(self, point):
        self.position = point

    def get_position(self):
        return self.position

    def get_images(self):
        return self.imgs

    def get_image(self):
        return self.imgs[self.current_img]

    def get_rate(self):
        return self.rate

    def set_resource_count(self, n):
        self.resource_count = n

    def get_resource_count(self):
        return self.resource_count

    def get_resource_limit(self):
        return self.resource_limit

    def get_name(self):
        return self.name

    def get_animation_rate(self):
        return self.animation_rate

    def next_image(self):
        self.current_img = (self.current_img + 1) % len(self.imgs)

    def remove_pending_action(self, action):
        self.pending_actions.remove(action)

    def add_pending_action(self, action):
        self.pending_actions.append(action)

    def get_pending_actions(self):
        return self.pending_actions

    def clear_pending_actions(self):
        self.pending_actions = []

    def entity_string(self):
        return 'unknown'

    def create_action(self, world, i_store):
        def action(current_ticks):
            self.remove_pending_action(action)

            entity_pt = self.get_position()
            smith = world.find_nearest(entity_pt, Blacksmith)
            (tiles, found) = actions.miner_to_smith(world, self, smith)

            new_entity = self
            if found:
                new_entity = actions.try_transform_miner(world, self,
                                                 self.try_transform)

            world.schedule_action(new_entity,
                            new_entity.create_action(world, i_store),
                            current_ticks + new_entity.get_rate())
            return tiles

        return action

    def try_transform(self):
        new_entity = MinerNotFull(
            self.get_name(), self.get_resource_limit(),
            self.get_position(), self.get_rate(),
            self.get_images(), self.get_animation_rate())

        return new_entity

    def create_animation_action(self, world, repeat_count):
        def action(current_ticks):
            self.remove_pending_action(action)

            self.next_image()

            if repeat_count != 1:
                world.schedule_action(self,
                                self.create_animation_action(world, max(repeat_count - 1, 0)),
                                current_ticks + self.get_animation_rate())

            return [self.get_position()]

        return action

class Vein:
    def __init__(self, name, rate, position, imgs, resource_distance=1):
        self.name = name
        self.position = position
        self.rate = rate
        self.imgs = imgs
        self.current_img = 0
        self.resource_distance = resource_distance
        self.pending_actions = []

    def set_position(self, point):
        self.position = point

    def get_position(self):
        return self.position

    def get_images(self):
        return self.imgs

    def get_image(self):
        return self.imgs[self.current_img]

    def get_rate(self):
        return self.rate

    def get_resource_distance(self):
         return self.resource_distance

    def get_name(self):
        return self.name

    def next_image(self):
        self.current_img = (self.current_img + 1) % len(self.imgs)

    def remove_pending_action(self, action):
        self.pending_actions.remove(action)

    def add_pending_action(self, action):
        self.pending_actions.append(action)

    def get_pending_actions(self):
        return self.pending_actions

    def clear_pending_actions(self):
        self.pending_actions = []

    def entity_string(self):
        return ' '.join(['vein', self.name, str(self.position.x),
                         str(self.position.y), str(self.rate),
                         str(self.resource_distance)])

    def create_action(self, world, i_store):
        def action(current_ticks):
            self.remove_pending_action(action)

            open_pt = world.find_open_around(self.get_position(),
                                       self.get_resource_distance())
            if open_pt:
                ore = actions.create_ore(world,
                                 "ore - " + self.get_name() + " - " + str(current_ticks),
                                 open_pt, current_ticks, i_store)
                world.add_entity(ore)
                tiles = [open_pt]
            else:
                tiles = []

            world.schedule_action(self,
                            self.create_action(world, i_store),
                            current_ticks + self.get_rate())
            return tiles

        return action

class Ore:
    def __init__(self, name, position, imgs, rate=5000):
        self.name = name
        self.position = position
        self.imgs = imgs
        self.current_img = 0
        self.rate = rate
        self.pending_actions = []

    def set_position(self, point):
        self.position = point

    def get_position(self):
        return self.position

    def get_images(self):
        return self.imgs

    def get_image(self):
        return self.imgs[self.current_img]

    def get_rate(self):
        return self.rate

    def get_name(self):
        return self.name

    def next_image(self):
        self.current_img = (self.current_img + 1) % len(self.imgs)

    def remove_pending_action(self, action):
        self.pending_actions.remove(action)

    def add_pending_action(self, action):
        self.pending_actions.append(action)

    def get_pending_actions(self):
        return self.pending_actions

    def clear_pending_actions(self):
        self.pending_actions = []

    def entity_string(self):
        return ' '.join(['ore', self.name, str(self.position.x),
                         str(self.position.y), str(self.rate)])

class Blacksmith:
    def __init__(self, name, position, imgs, resource_limit, rate,
                 resource_distance=1):
        self.name = name
        self.position = position
        self.imgs = imgs
        self.current_img = 0
        self.resource_limit = resource_limit
        self.resource_count = 0
        self.rate = rate
        self.resource_distance = resource_distance
        self.pending_actions = []

    def set_position(self, point):
        self.position = point

    def get_position(self):
        return self.position

    def get_images(self):
        return self.imgs

    def get_image(self):
        return self.imgs[self.current_img]

    def get_rate(self):
        return self.rate

    def set_resource_count(self, n):
        self.resource_count = n

    def get_resource_count(self):
        return self.resource_count

    def get_resource_limit(self):
        return self.resource_limit

    def get_resource_distance(self):
         return self.resource_distance

    def get_name(self):
        return self.name

    def next_image(self):
        self.current_img = (self.current_img + 1) % len(self.imgs)

    def remove_pending_action(self, action):
        self.pending_actions.remove(action)

    def add_pending_action(self, action):
        self.pending_actions.append(action)

    def get_pending_actions(self):
        return self.pending_actions

    def clear_pending_actions(self):
        self.pending_actions = []

    def entity_string(self):
        return ' '.join(['blacksmith', self.name, str(self.position.x),
                         str(self.position.y), str(self.resource_limit),
                         str(self.rate), str(self.resource_distance)])

class Obstacle:
    def __init__(self, name, position, imgs):
        self.name = name
        self.position = position
        self.imgs = imgs
        self.current_img = 0

    def set_position(self, point):
        self.position = point

    def get_position(self):
        return self.position

    def get_images(self):
        return self.imgs

    def get_image(self):
        return self.imgs[self.current_img]

    def get_name(self):
        return self.name

    def next_image(self):
        self.current_img = (self.current_img + 1) % len(self.imgs)

    def remove_pending_action(self, action):
        pass

    def add_pending_action(self, action):
        pass

    def get_pending_actions(self):
        return []

    def clear_pending_actions(self):
        pass

    def entity_string(self):
        return ' '.join(['obstacle', self.name, str(self.position.x),
                         str(self.position.y)])

class OreBlob:
    def __init__(self, name, position, rate, imgs, animation_rate):
        self.name = name
        self.position = position
        self.rate = rate
        self.imgs = imgs
        self.current_img = 0
        self.animation_rate = animation_rate
        self.pending_actions = []

    def set_position(self, point):
        self.position = point

    def get_position(self):
        return self.position

    def get_images(self):
        return self.imgs

    def get_image(self):
        return self.imgs[self.current_img]

    def get_rate(self):
        return self.rate

    def get_name(self):
        return self.name

    def get_animation_rate(self):
        return self.animation_rate

    def next_image(self):
        self.current_img = (self.current_img + 1) % len(self.imgs)

    def remove_pending_action(self, action):
        self.pending_actions.remove(action)

    def add_pending_action(self, action):
        self.pending_actions.append(action)

    def get_pending_actions(self):
        return self.pending_actions

    def clear_pending_actions(self):
        self.pending_actions = []

    def entity_string(self):
        return 'unknown'

    def create_action(self, world, i_store):
        def action(current_ticks):
            self.remove_pending_action(action)

            entity_pt = self.get_position()
            vein = world.find_nearest(entity_pt, Vein)
            (tiles, found) = actions.blob_to_vein(world, self, vein)

            next_time = current_ticks + self.get_rate()
            if found:
                quake = actions.create_quake(world, tiles[0], current_ticks, i_store)
                world.add_entity(quake)
                next_time = current_ticks + self.get_rate() * 2

            world.schedule_action(self,
                            self.create_action(world, i_store),
                            next_time)

            return tiles

        return action

    def create_animation_action(self, world, repeat_count):
        def action(current_ticks):
            self.remove_pending_action(action)

            self.next_image()

            if repeat_count != 1:
                world.schedule_action(self,
                                self.create_animation_action(world, max(repeat_count - 1, 0)),
                                current_ticks + self.get_animation_rate())

            return [self.get_position()]

        return action

class Quake:
    def __init__(self, name, position, imgs, animation_rate):
        self.name = name
        self.position = position
        self.imgs = imgs
        self.current_img = 0
        self.animation_rate = animation_rate
        self.pending_actions = []

    def set_position(self, point):
        self.position = point

    def get_position(self):
        return self.position

    def get_images(self):
        return self.imgs

    def get_image(self):
        return self.imgs[self.current_img]

    def get_name(self):
        return self.name

    def get_animation_rate(self):
        return self.animation_rate

    def next_image(self):
        self.current_img = (self.current_img + 1) % len(self.imgs)

    def remove_pending_action(self, action):
        self.pending_actions.remove(action)

    def add_pending_action(self, action):
        self.pending_actions.append(action)

    def get_pending_actions(self):
        return self.pending_actions

    def clear_pending_actions(self):
        self.pending_actions = []

    def entity_string(self):
        return 'unknown'

    def create_animation_action(self, world, repeat_count):
        def action(current_ticks):
            self.remove_pending_action(action)

            self.next_image()

            if repeat_count != 1:
                world.schedule_action(self,
                                self.create_animation_action(world, max(repeat_count - 1, 0)),
                                current_ticks + self.get_animation_rate())

            return [self.get_position()]

        return action
