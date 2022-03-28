package com.yqh.tv.screen.tools.exception

import okhttp3.Response
import java.io.IOException

class HttpException(val response: Response) : IOException(response.toString())