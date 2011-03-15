/*
 * Copyright 2011 Kazuhiro Sera
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package com.github.seratch.signedrequest4scala

/**
 * HTTP/1.1 Method Definitions
 *
 * @author <a href="mailto:seratch@gmail.com">Kazuhiro Sera</a>
 */
case class HttpMethod(val name: String) {
  override def toString() = name
}

object HttpMethod {
  val GET = HttpMethod("GET")
  val HEAD = HttpMethod("HEAD")
  val POST = HttpMethod("POST")
  val OPTIONS = HttpMethod("OPTIONS")
  val PUT = HttpMethod("PUT")
  val DELETE = HttpMethod("DELETE")
  val TRACE = HttpMethod("TRACE")
}