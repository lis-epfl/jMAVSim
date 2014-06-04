package me.drton.jmavsim;

import me.drton.jmavlib.mavlink.MAVLinkMessage;
import me.drton.jmavlib.mavlink.MAVLinkSchema;

/**
 * User: ton Date: 13.02.14 Time: 22:51
 */
public class MAVLinkTargetSystem extends MAVLinkSystem {
    private Target target;
    private long msgIntervalPosition = 200;
    private long msgLastPosition = 0;

    public MAVLinkTargetSystem(MAVLinkSchema schema, int sysId, int componentId, Target target) {
        super(schema, sysId, componentId);
        this.target = target;
    }

    @Override
    public void update(long t) {
        super.update(t);
        if (t - msgLastPosition > msgIntervalPosition) {
            msgLastPosition = t;
            MAVLinkMessage msg_target = new MAVLinkMessage(schema, "GLOBAL_POSITION_INT", sysId, componentId);
            GPSPosition p = target.getGlobalPosition();
            msg_target.set("time_boot_ms", t * 1000);
            msg_target.set("lat", (long) (p.position.lat * 1e7));
            msg_target.set("lon", (long) (p.position.lon * 1e7));
            msg_target.set("alt", (long) (p.position.alt * 1e3));
            msg_target.set("vx", (int) (p.velocity.x * 100));
            msg_target.set("vy", (int) (p.velocity.y * 100));
            msg_target.set("vz", (int) (p.velocity.z * 100));
            sendMessage(msg_target);

            MAVLinkMessage msg_target_time = new MAVLinkMessage(schema, "GLOBAL_POSITION_TIME", sysId, componentId);
            msg_target_time.set("time", t * 1000);
            msg_target_time.set("lat", (long) (p.position.lat * 1e7));
            msg_target_time.set("lon", (long) (p.position.lon * 1e7));
            msg_target_time.set("alt", (float) (p.position.alt));
            msg_target_time.set("vx", (float) p.velocity.x);
            msg_target_time.set("vy", (float) p.velocity.y);
            msg_target_time.set("vz", (float) p.velocity.z);
            sendMessage(msg_target_time);
        }
    }
}
