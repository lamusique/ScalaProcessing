package com.nekopiano.scala.sandbox.spec.slick

import slick.driver.H2Driver.api._
import slick.collection.heterogeneous.{ HList, HCons, HNil }
import slick.collection.heterogeneous.syntax._

/**
  * Created on 28/09/2016.
  */
class SlickHList extends App {

  // http://stackoverflow.com/questions/37198118/too-many-elements-for-tuple-27-allowed-22

  class Users(tag: Tag) extends Table[Long :: String :: HNil](tag, "users") {
    def id    = column[Long]( "id", O.PrimaryKey, O.AutoInc )
    def email = column[String]("email")

    def * = id :: email :: HNil
  }

  lazy val users = TableQuery[Users]
}

import java.sql.{Blob, Timestamp}

import slick.collection.heterogeneous.HNil
import slick.collection.heterogeneous.syntax._
//import slick.driver.MySQLDriver.api._

class Anomaly(tag:Tag) extends Table[Int :: String :: Int :: Timestamp :: Timestamp :: Int :: Int :: Float :: Int :: String
  :: String :: Int ::Blob :: Timestamp :: Int ::Int ::String ::Int ::Int ::String ::Int ::String :: Int ::Int ::Int ::
  Float :: Int :: HNil ](tag, "Anomaly") {

  def id = column[Int]("id")
  def serviceName = column[String]("ServiceName")
  def serviceId = column[Int]("ServiceId")
  def timeUpdated = column[Timestamp]("TimeUpdated")
  def timestamp = column[Timestamp]("Timestamp")
  def anomalyCategoryId = column[Int]("AnomalyCategoryId")
  def userGroup = column[Int]("UserGroup")
  def riskValue = column[Float]("RiskValue")
  def activityTypeId = column[Int]("ActivityTypeId")
  def destinationHost = column[String]("DestinationHost")
  def userName = column[String]("UserName")
  def tenantId = column[Int]("TenantId")
  def information = column[Blob]("Information")
  def timeCreated = column[Timestamp]("TimeCreated")
  def userId = column[Int]("UserId")
  def anomalyType = column[Int]("AnomalyType")
  def anomalyValue = column[String]("AnomalyValue")
  def measure = column[Int]("Measure")
  def userAction = column[Int]("UserAction")
  def uniqueIdentifier = column[String]("UniqueIdentifier")
  def similarCount = column[Int]("SimilarCount")
  def trainingValue = column[String]("TrainingValue")
  def state = column[Int]("State")
  def riskLevel = column[Int]("RiskLevel")
  def userRiskLevel = column[Int]("UserRiskLevel")
  def userRiskScore = column[Float]("UserRiskScore")
  def response = column[Int]("Response")

  def *  = id :: serviceName :: serviceId :: timeUpdated :: timestamp :: anomalyCategoryId :: userGroup ::
    riskValue :: activityTypeId :: destinationHost :: userName :: tenantId :: information :: timeCreated :: userId :: anomalyType :: anomalyValue ::
    measure :: userAction :: uniqueIdentifier :: similarCount :: trainingValue :: state :: riskLevel :: userRiskLevel :: userRiskScore :: response :: HNil
}

class AnomalyB(tag:Tag) extends Table[Int :: String :: Int :: Timestamp :: Timestamp :: Int :: Int :: Float :: Int :: String
  :: String :: Int ::Blob :: Timestamp :: Int ::Int ::String ::Int ::Int ::String ::Int ::String :: Int ::Int ::Int ::
  Float :: Int :: HNil ](tag, "Anomaly") {

  def id = column[Int]("id")
  def serviceName = column[String]("ServiceName")
  def serviceId = column[Int]("ServiceId")
  def timeUpdated = column[Timestamp]("TimeUpdated")
  def timestamp = column[Timestamp]("Timestamp")
  def anomalyCategoryId = column[Int]("AnomalyCategoryId")
  def userGroup = column[Int]("UserGroup")
  def riskValue = column[Float]("RiskValue")
  def activityTypeId = column[Int]("ActivityTypeId")
  def destinationHost = column[String]("DestinationHost")
  def userName = column[String]("UserName")
  def tenantId = column[Int]("TenantId")
  def information = column[Blob]("Information")
  def timeCreated = column[Timestamp]("TimeCreated")
  def userId = column[Int]("UserId")
  def anomalyType = column[Int]("AnomalyType")
  def anomalyValue = column[String]("AnomalyValue")
  def measure = column[Int]("Measure")
  def userAction = column[Int]("UserAction")
  def uniqueIdentifier = column[String]("UniqueIdentifier")
  def similarCount = column[Int]("SimilarCount")
  def trainingValue = column[String]("TrainingValue")
  def state = column[Int]("State")
  def riskLevel = column[Int]("RiskLevel")
  def userRiskLevel = column[Int]("UserRiskLevel")
  def userRiskScore = column[Float]("UserRiskScore")
  def response = column[Int]("Response")

  def *  = id :: serviceName :: serviceId :: timeUpdated :: timestamp :: anomalyCategoryId :: userGroup ::
    riskValue :: activityTypeId :: destinationHost :: userName :: tenantId :: information :: timeCreated :: userId :: anomalyType :: anomalyValue ::
    measure :: userAction :: uniqueIdentifier :: similarCount :: trainingValue :: state :: riskLevel :: userRiskLevel :: userRiskScore :: response :: HNil
}
