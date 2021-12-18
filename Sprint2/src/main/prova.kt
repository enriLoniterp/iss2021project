package main


import transportTrolley.TransportTrolleyAdapter
import fan.FanAdapter

fun main(args : Array<String>) {
   val stepRequest   = "msg(step,request, python,basicrobot,step(1400), 1)"
   println("ciao")
   val fanAd = FanAdapter()
   fanAd.sendCommand("ON")

}