import java.util.*;

public class Agent2 {
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

            pulsars.sort((a, b) -> {
                double sa = a[3] * 1.4 + a[4] * 6.0 - Math.hypot(a[1] - myBaseX, a[2] - myBaseY) * 0.05;
                double sb = b[3] * 1.4 + b[4] * 6.0 - Math.hypot(b[1] - myBaseX, b[2] - myBaseY) * 0.05;
                return Double.compare(sb, sa);
            });

            int trunkId = myDrones.get(0)[0];
            int dir = (myBaseX < width / 2) ? 1 : -1;
            int trunkX = myBaseX + dir * 350;
            int trunkY = myBaseY;

            for (int i = 0; i < myDrones.size(); i++) {
                int[] d = myDrones.get(i);
                if (i == 0) {
                    if (leagueLevel >= 2 && Math.hypot(d[1] - trunkX, d[2] - trunkY) < 25 && d[4] == 0) {
                        System.out.println("OVERCHARGE -1");
                    } else {
                        System.out.println("MOVE " + trunkX + " " + trunkY + " LINK -1");
                    }
                } else {
                    int[] targetP = pulsars.get(Math.min(i - 1, pulsars.size() - 1));
                    double distPToTrunk = Math.hypot(targetP[1] - trunkX, targetP[2] - trunkY);
                    int hx = targetP[1];
                    int hy = targetP[2];
                    if (distPToTrunk > 175) {
                        double r = 175.0 / distPToTrunk;
                        hx = (int) Math.round(targetP[1] + (trunkX - targetP[1]) * r);
                        hy = (int) Math.round(targetP[2] + (trunkY - targetP[2]) * r);
                    }
                    System.out.println("MOVE " + hx + " " + hy + " LINK " + trunkId);
                }
            }
        }
    }
}
