package com.morazow

import com.twitter.scalding.Tool
import org.apache.hadoop.util.ToolRunner
import org.apache.hadoop.conf.Configuration

trait JobRunner {

  val className: String
  val localModeArgs: Array[String]

  def main(args: Array[String]) {
    if (args.length != 0) {
      // run on cluster, hadoop mode
      ToolRunner.run(new Configuration, new Tool, className +: args)
    } else {
      // run locally
      ToolRunner.run(new Configuration, new Tool, className +: localModeArgs)
    }
  }

}
