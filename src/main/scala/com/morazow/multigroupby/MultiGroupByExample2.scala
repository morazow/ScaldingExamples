package com.morazow.multigroupby

import com.twitter.scalding.Job
import com.twitter.scalding.Tsv
import com.twitter.scalding.Args
import cascading.tuple.Fields
import com.liveramp.cascading_ext.assembly.MultiGroupBy
import com.morazow.JobRunner

class MultiGroupByExample2(args: Args) extends Job(args) {

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

  val MyJob =
    new MultiGroupBy(
      Array(UserAges, Purchases),
      Array(new Fields("USERID"), new Fields("USERID")),
      new Fields("USERID"),
      new MyMultiBufferOp(new Fields("STATE", "AGE", "COUNT"))
    )
    .discard('USERID)
    .write(Tsv(outputPath))
}

object MultiGroupByExample2 extends JobRunner {
  override val className = "com.morazow.multigroupby.MultiGroupByExample2"
  override val localModeArgs = Array(
    "--local",
    "--purchases", "data/multiGroupBy/purchases.tsv",
    "--userAges",  "data/multiGroupBy/user_ages.tsv",
    "--output",    "output/multi_groupBy2.tsv"
  )
}
