package com.zrmiller.core.colors;

import com.zrmiller.core.utility.ZUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * A class for storing color gradients.
 * Keys should be a float between 0 and 1.
 */
public class Gradient {

    // FIXME : Hashmaps are a bit slow for this use case. Could optimize by using an array and multiplying float values by 100.
    private final HashMap<Float, Color> colorMap = new HashMap<>();
    private final HashMap<Float, Color> colorCache = new HashMap<>();
    private final ArrayList<Float> keyList = new ArrayList<>();
    private int keyCount = 0;

    public void addKey(float key, Color color) {
        colorMap.put(key, color);
        keyList.add(key);
        Collections.sort(keyList);
        keyCount++;
        colorCache.clear();
    }

    public void removeKey(float key) {
        if (!keyList.contains(key)) return;
        colorMap.remove(key);
        keyList.remove(key);
        keyCount--;
        colorCache.clear();
    }

    public int getKeyCount() {
        return keyCount;
    }

    public Color resolveColor(float value) {
        if (colorCache.containsKey(value)) return colorCache.get(value);
        // Find the keys on either side of the color
        int lowerKeyIndex = -1;
        int upperKeyIndex = -1;
        for (int i = 0; i < keyList.size(); i++) {
            if (value >= keyList.get(i))
                lowerKeyIndex = i;
            if (value < keyList.get(i)) {
                upperKeyIndex = i;
                break;
            }
        }
        // If only one key is found, return a solid color
        if (lowerKeyIndex == -1) {
            Color color = colorMap.get(keyList.get(upperKeyIndex));
            colorCache.put(value, color);
            return color;
        } else if (upperKeyIndex == -1) {
            Color color = colorMap.get(keyList.get(lowerKeyIndex));
            colorCache.put(value, color);
            return color;
        } else {
            // If the value is between two keys, return an interpolated color
            float lowerValue = keyList.get(lowerKeyIndex);
            float upperValue = keyList.get(upperKeyIndex);
            float range = upperValue - lowerValue;
            float t = (value - lowerValue) / range;
            Color lowerColor = colorMap.get(lowerValue);
            Color upperColor = colorMap.get(upperValue);
            int r = ZUtil.clamp(Math.round(lerp(lowerColor.getRed(), upperColor.getRed(), t)), 0, 255);
            int g = ZUtil.clamp(Math.round(lerp(lowerColor.getGreen(), upperColor.getGreen(), t)), 0, 255);
            int b = ZUtil.clamp(Math.round(lerp(lowerColor.getBlue(), upperColor.getBlue(), t)), 0, 255);
            Color color = new Color(r, g, b);
            colorCache.put(value, color);
            return color;
        }
    }

    private float lerp(float a, float b, float t) {
        return a + t * (b - a);
    }

}
