package com.phillipcalvin.iconbutton;

/***
 Copyright (c) 2010 CommonsWare, LLC

 Licensed under the Apache License, Version 2.0 (the "License"); you may
 not use this file except in compliance with the License. You may obtain
 a copy of the License at
 http://www.apache.org/licenses/LICENSE-2.0
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 Modified to handle contexts without a package name using a fallback Package.
 This allows widgets accessing resources to be edited in the Eclipse/ADT layout tool.
 Phil Calvin

 */

import android.content.Context;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class SafeParcelHelper {
    private static final int CACHE_SIZE = 101;
    private String id = null;
    private Class arrr = null;
    private Map<String, Integer> cache = null;

    public SafeParcelHelper(String id, Context ctxt, String fallbackPackage) {
        this.id = id.replace('.', '_').replace('-', '_');
        String packageName = ctxt.getPackageName();
        // Package name will be null inside the Eclipse/ADT layout tool
        if (null == packageName) {
            packageName = fallbackPackage;
        }

        try {
            this.arrr = Class.forName(packageName + ".R");
        } catch (Throwable t) {
            throw new RuntimeException("Exception finding R class", t);
        }

        this.cache = Collections.synchronizedMap(new Cache());
    }

    public int[] getStyleableArray(String name) {
        try {
            Class clazz = getResourceClass("styleable");

            if (clazz != null) {
                Field fld = clazz.getDeclaredField(name);

                if (fld != null) {
                    Object o = fld.get(clazz);

                    if (o instanceof int[]) {
                        int[] result = new int[Array.getLength(o)];

                        for (int i = 0; i < Array.getLength(o); i++) {
                            result[i] = Array.getInt(o, i);
                        }

                        return (result);
                    }
                }
            }
        } catch (Throwable t) {
            throw new RuntimeException("Exception finding styleable", t);
        }

        return (new int[0]);
    }

    public int getStyleableId(String component, String attr) {
        return (getIdentifier(component + "_" + attr, "styleable", false));
    }

    public int getLayoutId(String layout) {
        return (getIdentifier(layout, "layout", true));
    }

    public int getItemId(String item) {
        return (getIdentifier(item, "id", false));
    }

    public int getMenuId(String menu) {
        return (getIdentifier(menu, "menu", true));
    }

    public int getDrawableId(String drawable) {
        return (getIdentifier(drawable, "drawable", true));
    }

    public int getIdentifier(String name, String defType) {
        return (getIdentifier(name, defType, true));
    }

    public int getIdentifier(String name, String defType,
                             boolean mungeName) {
        int result = -1;
        StringBuilder cacheKey = new StringBuilder(name);

        cacheKey.append('|');
        cacheKey.append(defType);

        Integer cacheHit = cache.get(cacheKey.toString());

        if (cacheHit != null) {
            return (cacheHit.intValue());
        }

        if (!name.startsWith(id) && mungeName) {
            StringBuilder buf = new StringBuilder(id);

            buf.append('_');
            buf.append(name);

            name = buf.toString();
        }

        try {
            Class clazz = getResourceClass(defType);

            if (clazz != null) {
                Field fld = clazz.getDeclaredField(name);

                if (fld != null) {
                    result = fld.getInt(clazz);
                    cache.put(cacheKey.toString(), result);
                }
            }
        } catch (Throwable t) {
            throw new RuntimeException("Exception finding resource identifier", t);
        }

        return (result);
    }

    private Class getResourceClass(String defType) {
        for (Class clazz : arrr.getClasses()) {
            if (defType.equals(clazz.getSimpleName())) {
                return (clazz);
            }
        }

        return (null);
    }

    public class Cache extends LinkedHashMap<String, Integer> {
        public Cache() {
            super(CACHE_SIZE, 1.1f, true);
        }

        protected boolean removeEldestEntry(Entry eldest) {
            return (size() > CACHE_SIZE);
        }
    }
}