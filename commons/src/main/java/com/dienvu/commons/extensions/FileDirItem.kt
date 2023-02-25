package com.dienvu.commons.extensions

import android.content.Context
import com.dienvu.commons.models.FileDirItem

fun FileDirItem.isRecycleBinPath(context: Context): Boolean
{
    return path.startsWith(context.recycleBinPath)
}
