package me.decapre.util

import org.slf4j.{Logger, LoggerFactory}

/** @author Chenyu.Liu
  * @date 2022/1/30
  * @description ${DESCRIPTION}
  */
trait LoggingSupport {
  val logger: Logger = LoggerFactory.getLogger(this.getClass)
}
