package com.morazow.multigroupby

import com.twitter.scalding.Job
import com.twitter.scalding.Tsv
import com.twitter.scalding.Args
import com.morazow.JobRunner

class MultiGroupByExample1(args: Args) extends Job(args) {

  // arguments
  val purchasesPath = args("purchases")
  val userAgesPath  = args("userAges")
  val outputPath    = args("output")

  val Purchases =
    Tsv(purchasesPath, ('USERID, 'TIMESTAMP, 'STATE, 'PURCHASE))
    .read

  val UserAges =
    Tsv(userAgesPath, ('USERID, 'AGE))
    .read

  val MyJob = Purchases
    .groupBy('USERID, 'STATE) { _.size('COUNT) }
    .joinWithSmaller('USERID -> 'USERID, UserAges)
    .groupBy('STATE, 'AGE) { _.sum[Int]('COUNT) }
    .write(Tsv(outputPath))
}

object MultiGroupByExample1 extends JobRunner {
  override val className = "com.morazow.multigroupby.MultiGroupByExample1"
  override val localModeArgs = Array(
    "--local",
    "--purchases", "data/purchases.tsv",
    "--userAges",  "data/user_ages.tsv",
    "--output",    "output/multi_groupBy1.tsv")
}
