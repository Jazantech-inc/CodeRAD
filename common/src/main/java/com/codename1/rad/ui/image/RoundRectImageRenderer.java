/*
 * Copyright 2020 shannah.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codename1.rad.ui.image;

import com.codename1.rad.models.PropertySelector;
import com.codename1.ui.Image;

/**
 *
 * @author shannah
 */
public class RoundRectImageRenderer implements PropertyImageRenderer {
    private int width;
    private int height;
    private float cornerRadiusMM;
    
    public RoundRectImageRenderer(int width, int height, float cornerRadiusMM) {
        this.width = width;
        this.height = height;
        this.cornerRadiusMM = cornerRadiusMM;
    }
    @Override
    public Image createImage(PropertySelector selector) {
        if (selector.isEmpty()) {
            return null;
        }
        return selector.createImageToFile(ImageUtil.createPlaceholder(width, height), ImageUtil.createRoundRectMaskAdapter(cornerRadiusMM, width, height));
    }
    
}
