package com.morazow.templatedtsv

import com.twitter.scalding.Job
import com.twitter.scalding.Args
import com.twitter.scalding.Tsv
import com.twitter.scalding.TemplatedTsv
import cascading.tap.SinkMode

import com.morazow.JobRunner

class TemplatedTsvExample(args: Args) extends Job(args) {

  // arguments
  val purchasesPath = args("purchases")
  val outputPath    = args("output")

  val ioSchema = ('USERID, 'TIMESTAMP, 'STATE, 'PURCHASE)

  val Purchases =
    Tsv(purchasesPath, ioSchema)
    .read
    .map('STATE -> 'STATENAME) { state: Int => "State=" + state }
    .groupBy('STATENAME) { _.pass } // this is optional
    .write(TemplatedTsv(outputPath, "%s", 'STATENAME, false, SinkMode.REPLACE, ioSchema))

}

object TemplatedTsvExample extends JobRunner {
  override val className = "com.morazow.templatedtsv.TemplatedTsvExample"
  override val localModeArgs = Array(
    "--local",
    "--purchases", "data/purchases.tsv",
    "--output",    "output/templatedtsvexample.tsv"
  )
}
