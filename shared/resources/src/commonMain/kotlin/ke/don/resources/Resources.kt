/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.resources

import gondi.shared.resources.generated.resources.Res
import gondi.shared.resources.generated.resources.app_icon
import gondi.shared.resources.generated.resources.roboto_regular
import gondi.shared.resources.generated.resources.roboto_semibold

object Resources {
    object Font {
        val REGULAR = Res.font.roboto_regular
        val SEMIBOLD = Res.font.roboto_semibold
    }

    object Images {
        val LOGO = Res.drawable.app_icon
    }
}
