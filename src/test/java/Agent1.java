import java.util.*;

/**
 * Advanced Dynamic Relay & Flux-Adaptive Agent for Quantum Relay:
 * - Reads Pulsar energy and regeneration rate (flux shifts at turns 21, 41, 61, 81)
 * - Deploys parallel independent 1-hop links when Pulsar is within BASE_LINK + HARVEST (680)
 * - Uses Drone 0 as a forward relay/harvester when targeting far contested Pulsars
 * - Uses OVERCHARGE strategically on depleted-risk/contested high-yield Pulsars & crossing beams
 */
public class Agent1 {
    static final double HARVEST_RANGE = 192.0;
    static final double MAX_LINK_RANGE = 474.0;

    static int[] computeAnchor(int baseX, int baseY, int px, int py) {
        double dist = Math.hypot(px - baseX, py - baseY);
        if (dist <= MAX_LINK_RANGE) {
            return new int[]{px, py};
        }
        double r = Math.min(HARVEST_RANGE, dist) / dist;
        int ax = (int) Math.round(px + (baseX - px) * r);
        int ay = (int) Math.round(py + (baseY - py) * r);
        return new int[]{ax, ay};
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int width = in.nextInt();
        int height = in.nextInt();
        int leagueLevel = 4 - in.nextInt();
        int myBaseX = in.nextInt();
        int myBaseY = in.nextInt();
        int oppBaseX = in.nextInt();
        int oppBaseY = in.nextInt();

        while (true) {
            int turn = in.nextInt();
            int pulsarCount = in.nextInt();
            List<int[]> pulsars = new ArrayList<>();
            for (int i = 0; i < pulsarCount; i++) {
                pulsars.add(new int[]{in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt()});
            }
            int droneCount = in.nextInt();
            List<int[]> myDrones = new ArrayList<>();
            for (int i = 0; i < droneCount; i++) {
                int id = in.nextInt(), owner = in.nextInt(), x = in.nextInt(), y = in.nextInt();
                int link = in.nextInt(), cd = in.nextInt();
                if (owner == 0) myDrones.add(new int[]{id, x, y, link, cd});
            }

            // Rank pulsars dynamically by energy reservoir + regenRate and reachability from Mothership
            pulsars.sort((a, b) -> {
                double da = Math.hypot(a[1] - myBaseX, a[2] - myBaseY);
                double db = Math.hypot(b[1] - myBaseX, b[2] - myBaseY);
                double reachBonusA = (da <= MAX_LINK_RANGE + HARVEST_RANGE + 15) ? 60.0 : -180.0;
                double reachBonusB = (db <= MAX_LINK_RANGE + HARVEST_RANGE + 15) ? 60.0 : -180.0;
                double sa = a[3] * 1.5 + a[4] * 10.0 + reachBonusA - da * 0.08;
                double sb = b[3] * 1.5 + b[4] * 10.0 + reachBonusB - db * 0.08;
                return Double.compare(sb, sa);
            });

            List<int[]> anchorTargets = new ArrayList<>();
            for (int i = 0; i < myDrones.size(); i++) {
                int[] targetP = pulsars.get(i % pulsars.size());
                anchorTargets.add(computeAnchor(myBaseX, myBaseY, targetP[1], targetP[2]));
            }

            for (int i = 0; i < myDrones.size(); i++) {
                int[] d = myDrones.get(i);
                int[] targetP = pulsars.get(i % pulsars.size());
                int[] anchor = anchorTargets.get(i);

                double distToBase = Math.hypot(d[1] - myBaseX, d[2] - myBaseY);
                double distToPulsar = Math.hypot(d[1] - targetP[1], d[2] - targetP[2]);
                double anchorDistToBase = Math.hypot(anchor[0] - myBaseX, anchor[1] - myBaseY);

                if (anchorDistToBase > MAX_LINK_RANGE && i > 0) {
                    // Relay through Drone 0 when pulsar is beyond direct 1-hop range
                    int[] relay = anchorTargets.get(0);
                    double distToRelay = Math.hypot(d[1] - relay[0], d[2] - relay[1]);
                    if (leagueLevel >= 2 && d[4] == 0 && distToRelay <= 478 && distToPulsar <= 198 && targetP[3] >= 8) {
                        System.out.println("OVERCHARGE " + myDrones.get(0)[0] + " MSG OVER!");
                    } else {
                        System.out.println("MOVE " + anchor[0] + " " + anchor[1] + " LINK " + myDrones.get(0)[0]);
                    }
                } else if (distToBase <= 478 && distToPulsar <= 198) {
                    if (leagueLevel >= 2 && d[4] == 0 && targetP[3] >= 7) {
                        System.out.println("OVERCHARGE -1 MSG 12GW!");
                    } else {
                        System.out.println("MOVE " + anchor[0] + " " + anchor[1] + " LINK -1");
                    }
                } else if (i > 0 && distToBase > 478) {
                    System.out.println("MOVE " + anchor[0] + " " + anchor[1] + " LINK " + myDrones.get(0)[0]);
                } else {
                    System.out.println("MOVE " + anchor[0] + " " + anchor[1] + " LINK -1");
                }
            }
        }
    }
}
