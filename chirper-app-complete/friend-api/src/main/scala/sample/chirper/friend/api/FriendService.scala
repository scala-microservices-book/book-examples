/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package sample.chirper.friend.api

import akka.{Done, NotUsed}
import com.lightbend.lagom.scaladsl.api.Descriptor
import com.lightbend.lagom.scaladsl.api.Service
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.api.broker.kafka.{KafkaProperties, PartitionKeyStrategy}

import scala.collection.immutable.Seq

object FriendService  {
  val TOPIC_NAME = "friends"
}

/**
 * The friend service.
 */
trait FriendService extends Service {

  /**
   * Service call for getting a user.
   *
   * The ID of this service call is the user name, and the response message is the User object.
   */
  def getUser(id: String): ServiceCall[NotUsed, User] 

  /**
   * Service call for creating a user.
   *
   * The request message is the User to create.
   */
  def createUser(): ServiceCall[CreateUser, Done]

  /**
   * Service call for adding a friend to a user.
   *
   * The ID for this service call is the ID of the user that the friend is being added to.
   * The request message is the ID of the friend being added.
   */
  def addFriend(userId: String): ServiceCall[FriendId, Done]

  /**
   * Service call for getting the followers of a user.
   *
   * The ID for this service call is the Id of the user to get the followers for.
   * The response message is the list of follower IDs.
   */
  def getFollowers(userId: String): ServiceCall[NotUsed, Seq[String]]

  //added wrt message api
  def friendsTopic: Topic[KFriendMessage]

  override def descriptor(): Descriptor = {
    import Service._

    named("friendservice").withCalls(
        pathCall("/api/users/:id", getUser _),
        namedCall("/api/users", createUser),
        pathCall("/api/users/:userId/friends", addFriend _),
        pathCall("/api/users/:id/followers", getFollowers _)
      ).withTopics(
          topic(FriendService.TOPIC_NAME, friendsTopic)
            .addProperty(
              KafkaProperties.partitionKeyStrategy,
              PartitionKeyStrategy[KFriendMessage](_ => "0")
            )
      ).withAutoAcl(true)

  }
}
