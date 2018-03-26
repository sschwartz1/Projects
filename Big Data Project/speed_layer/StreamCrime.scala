import kafka.serializer.StringDecoder

import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka._
import org.apache.spark.SparkConf
import com.fasterxml.jackson.databind.{ DeserializationFeature, ObjectMapper }
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.TableName
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.ConnectionFactory
import org.apache.hadoop.hbase.client.Get
import org.apache.hadoop.hbase.client.Increment
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.client.Put

object StreamCrime {
  val mapper = new ObjectMapper()
  mapper.registerModule(DefaultScalaModule)
  val hbaseConf: Configuration = HBaseConfiguration.create()
  hbaseConf.set("hbase.zookeeper.property.clientPort", "2181")
  
  // Use the following two lines if you are building for the cluster 
  hbaseConf.set("hbase.zookeeper.quorum","mpcs530132017test-hgm1-1-20170924181440.c.mpcs53013-2017.internal,mpcs530132017test-hgm2-2-20170924181505.c.mpcs53013-2017.internal,mpcs530132017test-hgm3-3-20170924181529.c.mpcs53013-2017.internal")
  hbaseConf.set("zookeeper.znode.parent", "/hbase-unsecure")
  
  // Use the following line if you are building for the VM
  //hbaseConf.set("hbase.zookeeper.quorum", "localhost")
  
  val hbaseConnection = ConnectionFactory.createConnection(hbaseConf)
  val crimeOverviewPerArea = hbaseConnection.getTable(TableName.valueOf("sschwartz1_crime_overview_main_speed"))
  
  def incrementCrimeByKey(kcr : KafkaCrimeRecord) : String = {
   
    val put = new Put(Bytes.toBytes(kcr.communityAreaNumber + "-" + kcr.crimeDate))
    // Speed layer will use most current temp for that day
    put.addColumn(Bytes.toBytes("crime"), Bytes.toBytes("Average_Day_Temp"), Bytes.toBytes(kcr.temp))
    crimeOverviewPerArea.put(put)
    
    val inc = new Increment(Bytes.toBytes(kcr.communityAreaNumber + "-" + kcr.crimeDate))
    if(kcr.crimeType == "HOMICIDE") {
      inc.addColumn(Bytes.toBytes("crime"), Bytes.toBytes("HOMICIDE"), 1)
    }
    if(kcr.crimeType == "ROBBERY") {
      inc.addColumn(Bytes.toBytes("crime"), Bytes.toBytes("ROBBERY"), 1)
    }
    if(kcr.crimeType == "BATTERY_ASSAULT") {
      inc.addColumn(Bytes.toBytes("crime"), Bytes.toBytes("BATTERY_ASSAULT"), 1)
    }
    if(kcr.crimeType == "BURGLARY_THEFT_MOTORVEHICLETHEFT") {
      inc.addColumn(Bytes.toBytes("crime"), Bytes.toBytes("BURGLARY_THEFT_MOTORVEHICLETHEFT"), 1)
    }
    if(kcr.crimeType == "ARSON") {
      inc.addColumn(Bytes.toBytes("crime"), Bytes.toBytes("ARSON"), 1)
    }
    if(kcr.crimeType == "DECEPTIVE_PRACTICE") {
      inc.addColumn(Bytes.toBytes("crime"), Bytes.toBytes("DECEPTIVE_PRACTICE"), 1)
    }
    if(kcr.crimeType == "CRIMINAL_DAMAGE_TRESPASS") {
      inc.addColumn(Bytes.toBytes("crime"), Bytes.toBytes("CRIMINAL_DAMAGE_TRESPASS"), 1)
    }
    if(kcr.crimeType == "WEAPONS_VIOLATION") {
      inc.addColumn(Bytes.toBytes("crime"), Bytes.toBytes("WEAPONS_VIOLATION"), 1)
    }
    if(kcr.crimeType == "SEX_OFFENSE") {
      inc.addColumn(Bytes.toBytes("crime"), Bytes.toBytes("SEX_OFFENSE"), 1)
    }
    if(kcr.crimeType == "OFFENSE_INVOLVING_CHILDREN") {
      inc.addColumn(Bytes.toBytes("crime"), Bytes.toBytes("OFFENSE_INVOLVING_CHILDREN"), 1)
    }
    if(kcr.crimeType == "NARCOTICS") {
      inc.addColumn(Bytes.toBytes("crime"), Bytes.toBytes("NARCOTICS"), 1)
    }
    if(kcr.crimeType == "OTHER") {
      inc.addColumn(Bytes.toBytes("crime"), Bytes.toBytes("OTHER"), 1)
    }
    crimeOverviewPerArea.increment(inc)
    return "Updated speed layer for for community area # " + kcr.communityAreaNumber + " on " + kcr.crimeDate
}
  
  def main(args: Array[String]) {
    if (args.length < 1) {
      System.err.println(s"""
        |Usage: StreamFlights <brokers> 
        |  <brokers> is a list of one or more Kafka brokers
        | 
        """.stripMargin)
      System.exit(1)
    }
    
    val Array(brokers) = args

    // Create context with 2 second batch interval
    val sparkConf = new SparkConf().setAppName("StreamCrime")
    val ssc = new StreamingContext(sparkConf, Seconds(2))

    // Create direct kafka stream with brokers and topics
    val topicsSet = Set[String]("sschwartz1_crime")
    val kafkaParams = Map[String, String]("metadata.broker.list" -> brokers)
    val messages = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](
      ssc, kafkaParams, topicsSet)

    // Get the lines, split them into words, count the words and print
    val serializedRecords = messages.map(_._2);

    val kcrs = serializedRecords.map(rec => mapper.readValue(rec, classOf[KafkaCrimeRecord]))
    

    // Update speed table    
    val processedCrimes = kcrs.map(incrementCrimeByKey)
    processedCrimes.print()
    // Start the computation
    ssc.start()
    ssc.awaitTermination()
  }

}