import entities
import image_store
import point

RATE_MULTIPLIER = 30

PROPERTY_KEY = 0

MINER_KEY = 'miner'
MINER_NUM_PROPERTIES = 7
MINER_NAME = 1
MINER_LIMIT = 4
MINER_COL = 2
MINER_ROW = 3
MINER_RATE = 5
MINER_ANIMATION_RATE = 6

OBSTACLE_KEY = 'obstacle'
OBSTACLE_NUM_PROPERTIES = 4
OBSTACLE_NAME = 1
OBSTACLE_COL = 2
OBSTACLE_ROW = 3

ORE_KEY = 'ore'
ORE_NUM_PROPERTIES = 5
ORE_NAME = 1
ORE_COL = 2
ORE_ROW = 3
ORE_RATE = 4

SMITH_KEY = 'blacksmith'
SMITH_NUM_PROPERTIES = 7
SMITH_NAME = 1
SMITH_COL = 2
SMITH_ROW = 3
SMITH_LIMIT = 4
SMITH_RATE = 5
SMITH_REACH = 6

VEIN_KEY = 'vein'
VEIN_NUM_PROPERTIES = 6
VEIN_NAME = 1
VEIN_RATE = 4
VEIN_COL = 2
VEIN_ROW = 3
VEIN_REACH = 5

def create_from_properties(properties, i_store):
    key = properties[PROPERTY_KEY]
    if properties:
        if key == MINER_KEY:
            return create_miner(properties, i_store)
        elif key == VEIN_KEY:
            return create_vein(properties, i_store)
        elif key == ORE_KEY:
            return create_ore(properties, i_store)
        elif key == SMITH_KEY:
            return create_blacksmith(properties, i_store)
        elif key == OBSTACLE_KEY:
            return create_obstacle(properties, i_store)

    return None


def create_miner(properties, i_store):
    if len(properties) == MINER_NUM_PROPERTIES:
        miner = entities.MinerNotFull(properties[MINER_NAME],
                 int(properties[MINER_LIMIT]),
                 point.Point(int(properties[MINER_COL]), int(properties[MINER_ROW])),
                 int(properties[MINER_RATE]) // RATE_MULTIPLIER,
                 i_store.get_images(properties[PROPERTY_KEY]),
                 int(properties[MINER_ANIMATION_RATE]))

        return miner

    else:
        return None


def create_vein(properties, i_store):
    if len(properties) == VEIN_NUM_PROPERTIES:
        vein = entities.Vein(properties[VEIN_NAME],
                int(properties[VEIN_RATE])//RATE_MULTIPLIER,
                point.Point(int(properties[VEIN_COL]), int(properties[VEIN_ROW])),
                i_store.get_images(properties[PROPERTY_KEY]),
                int(properties[VEIN_REACH]))

        return vein

    else:
        return None


def create_ore(properties, i_store):
    if len(properties) == ORE_NUM_PROPERTIES:
        ore = entities.Ore(properties[ORE_NAME],
               point.Point(int(properties[ORE_COL]), int(properties[ORE_ROW])),
               i_store.get_images(properties[PROPERTY_KEY]),
               int(properties[ORE_RATE])//RATE_MULTIPLIER)

        return ore

    else:
        return None


def create_blacksmith(properties, i_store):
    if len(properties) == SMITH_NUM_PROPERTIES:
        return entities.Blacksmith(properties[SMITH_NAME],
                point.Point(int(properties[SMITH_COL]), int(properties[SMITH_ROW])),
                i_store.get_images(properties[PROPERTY_KEY]),
                int(properties[SMITH_LIMIT]),
                int(properties[SMITH_RATE])//RATE_MULTIPLIER,
                int(properties[SMITH_REACH]))

        return smith

    else:
        return None


def create_obstacle(properties, i_store):
    if len(properties) == OBSTACLE_NUM_PROPERTIES:
        return entities.Obstacle(properties[OBSTACLE_NAME],
                point.Point(int(properties[OBSTACLE_COL]), int(properties[OBSTACLE_ROW])),
                i_store.get_images(properties[PROPERTY_KEY]))

    else:
        return None
