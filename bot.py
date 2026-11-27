import math
import sys
from dataclasses import dataclass

# Deploy your quantum relay drones across deep space! Route unstable pulsar energy
# back to your Mothership through laser relay chains while cutting enemy beams.

HARVEST_RANGE = 192.0
MAX_LINK_RANGE = 474.0


@dataclass(frozen=True)
class Pos:
    x: int
    y: int

    def distance_to(self, other: "Pos") -> float:
        return math.hypot(self.x - other.x, self.y - other.y)


@dataclass(frozen=True)
class Pulsar:
    id: int
    pos: Pos
    energy: int
    regen_rate: int


@dataclass(frozen=True)
class Drone:
    id: int
    owner: int
    pos: Pos
    link_target: int
    cooldown: int


def compute_anchor(base: Pos, pulsar: Pos) -> Pos:
    dist = base.distance_to(pulsar.pos)
    if dist <= MAX_LINK_RANGE:
        return pulsar.pos
    ratio = min(HARVEST_RANGE, dist) / dist
    return Pos(
        int(round(pulsar.pos.x + (base.x - pulsar.pos.x) * ratio)),
        int(round(pulsar.pos.y + (base.y - pulsar.pos.y) * ratio)),
    )


def score_pulsar(pulsar: Pulsar, base: Pos) -> float:
    dist = base.distance_to(pulsar.pos)
    reach_bonus = 60.0 if dist <= MAX_LINK_RANGE + HARVEST_RANGE + 15.0 else -180.0
    return pulsar.energy * 1.5 + pulsar.regen_rate * 10.0 + reach_bonus - dist * 0.08


def main() -> None:
    _width, _height, league_level = [int(i) for i in input().split()]
    my_base_x, my_base_y, opp_base_x, opp_base_y = [int(i) for i in input().split()]
    my_base = Pos(my_base_x, my_base_y)

    while True:
        _turn = int(input())
        pulsar_count = int(input())
        pulsars: list[Pulsar] = []
        for _ in range(pulsar_count):
            pid, px, py, energy, regen_rate = [int(j) for j in input().split()]
            pulsars.append(Pulsar(pid, Pos(px, py), energy, regen_rate))

        drone_count = int(input())
        my_drones: list[Drone] = []
        for _ in range(drone_count):
            did, owner, x, y, link_target, cooldown = [int(j) for j in input().split()]
            if owner == 0:
                my_drones.append(Drone(did, owner, Pos(x, y), link_target, cooldown))

        ranked = sorted(pulsars, key=lambda p: score_pulsar(p, my_base), reverse=True)
        anchors = [compute_anchor(my_base, ranked[i % len(ranked)]) for i in range(len(my_drones))]

        trunk_id = my_drones[0].id
        for i, d in enumerate(my_drones):
            target_p = ranked[i % len(ranked)]
            anchor = anchors[i]
            dist_to_base = d.pos.distance_to(my_base)
            dist_to_pulsar = d.pos.distance_to(target_p.pos)
            anchor_to_base = anchor.distance_to(my_base)

            if anchor_to_base > MAX_LINK_RANGE and i > 0:
                dist_to_relay = d.pos.distance_to(anchors[0])
                if league_level <= 2 and d.cooldown == 0 and dist_to_relay <= 478.0 and dist_to_pulsar <= 198.0 and target_p.energy >= 8:
                    print(f"OVERCHARGE {trunk_id} MSG OVER!", flush=True)
                else:
                    print(f"MOVE {anchor.x} {anchor.y} LINK {trunk_id}", flush=True)
            elif dist_to_base <= 478.0 and dist_to_pulsar <= 198.0:
                if league_level <= 2 and d.cooldown == 0 and target_p.energy >= 7:
                    print("OVERCHARGE -1 MSG 12GW!", flush=True)
                else:
                    print(f"MOVE {anchor.x} {anchor.y} LINK -1", flush=True)
            elif i > 0 and dist_to_base > 478.0:
                print(f"MOVE {anchor.x} {anchor.y} LINK {trunk_id}", flush=True)
            else:
                print(f"MOVE {anchor.x} {anchor.y} LINK -1", flush=True)


if __name__ == "__main__":
    main()
