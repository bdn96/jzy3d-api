package org.jzy3d.plot3d.primitives;

import java.util.List;

import javax.media.opengl.GL;

import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.transform.Transform;

public class ConcurrentLineStrip extends LineStrip {

    public ConcurrentLineStrip() {
        super(100);
    }

    public ConcurrentLineStrip(int n) {
        super(n);
    }

    public ConcurrentLineStrip(List<Coord3d> coords) {
        super(coords);
    }

    public ConcurrentLineStrip(Point c1, Point c2) {
        super(c1, c2);
    }

    public void drawLine(GL gl) {
        gl.glLineWidth(width);
        // gl.glEnable(GL.GL_POLYGON_OFFSET_FILL);
        // gl.glPolygonOffset(1.0f, 1.0f);
        if (gl.isGL2()) {
            synchronized (points) {
                drawLineGL2(gl);
            }
        } else {
            synchronized (points) {
                drawLineGLES2();
            }
        }
    }

    public void drawPoints(GL gl) {
        if (showPoints) {
            if (gl.isGL2()) {
                synchronized (points) {
                    drawPointsGL2(gl);
                }
            } else {
                synchronized (points) {
                    drawPointsGLES2();
                }
            }
        }
    }

    /* */

    public void applyGeometryTransform(Transform transform) {
        synchronized (points) {
            for (Point p : points) {
                p.xyz = transform.compute(p.xyz);
            }
        }
        updateBounds();
    }

    public void updateBounds() {
        bbox.reset();
        synchronized (points) {
            for (Point p : points)
                bbox.add(p);
        }
    }

    public void add(Point point) {
        synchronized (points) {
            points.add(point);
        }
        bbox.add(point);
    }

    public void clear() {
        synchronized (points) {
            points.clear();
        }
        updateBounds();
    }

    public Point getLastPoint() {
        synchronized (points) {
            return super.getLastPoint();
        }
    }
}
