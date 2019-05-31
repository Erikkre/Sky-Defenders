// Copyright (c) 2019 Erik Kredatus. All rights reserved.
package com.kredatus.flockblockers.Helpers;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.utils.ShortArray;

public class CustomShapeRenderer extends ShapeRenderer {
    EarClippingTriangulator ear = new EarClippingTriangulator();

    public void polygon(float[] vertices, int offset, int count)
    {
            ShortArray arrRes = ear.computeTriangles(vertices);

            for (int i = 0; i < arrRes.size - 2; i = i + 3)
            {
                float x1 = vertices[arrRes.get(i) * 2];
                float y1 = vertices[(arrRes.get(i) * 2) + 1];

                float x2 = vertices[(arrRes.get(i + 1)) * 2];
                float y2 = vertices[(arrRes.get(i + 1) * 2) + 1];

                float x3 = vertices[arrRes.get(i + 2) * 2];
                float y3 = vertices[(arrRes.get(i + 2) * 2) + 1];

                this.triangle(x1, y1, x2, y2, x3, y3);
            }

    }
}
