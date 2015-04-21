import random

import entities
import image_store

BLOB_RATE_SCALE = 4
BLOB_ANIMATION_RATE_SCALE = 50
BLOB_ANIMATION_MIN = 1
BLOB_ANIMATION_MAX = 3

ORE_CORRUPT_MIN = 20000
ORE_CORRUPT_MAX = 30000

QUAKE_STEPS = 10
QUAKE_DURATION = 1100
QUAKE_ANIMATION_RATE = 100

VEIN_SPAWN_DELAY = 500
VEIN_RATE_MIN = 8000
VEIN_RATE_MAX = 17000


def create_blob(world, name, pt, rate, ticks, i_store):
    blob = entities.OreBlob(name, pt, rate,
                            i_store.get_images('blob'),
                            random.randint(BLOB_ANIMATION_MIN, BLOB_ANIMATION_MAX)
                            * BLOB_ANIMATION_RATE_SCALE)
    blob.schedule(world, ticks, i_store)
    return blob

def create_ore(world, name, pt, ticks, i_store):
    ore = entities.Ore(name, pt, i_store.get_images('ore'),
                       random.randint(ORE_CORRUPT_MIN, ORE_CORRUPT_MAX))
    ore.schedule(world, ticks, i_store)

    return ore

def create_quake(world, pt, ticks, i_store):
    quake = entities.Quake("quake", pt,
                           i_store.get_images('quake'), QUAKE_ANIMATION_RATE)
    quake.schedule(world, ticks, i_store)
    return quake

def create_vein(world, name, pt, ticks, i_store):
    vein = entities.Vein("vein" + name,
                         random.randint(VEIN_RATE_MIN, VEIN_RATE_MAX),
                         pt, i_store.get_images('vein'))
    return vein
