begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.twitter.consumer.streaming
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|twitter
operator|.
name|consumer
operator|.
name|streaming
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|twitter
operator|.
name|TwitterEndpoint
import|;
end_import

begin_import
import|import
name|twitter4j
operator|.
name|DirectMessage
import|;
end_import

begin_import
import|import
name|twitter4j
operator|.
name|StallWarning
import|;
end_import

begin_import
import|import
name|twitter4j
operator|.
name|Status
import|;
end_import

begin_import
import|import
name|twitter4j
operator|.
name|User
import|;
end_import

begin_import
import|import
name|twitter4j
operator|.
name|UserList
import|;
end_import

begin_import
import|import
name|twitter4j
operator|.
name|UserStreamListener
import|;
end_import

begin_class
DECL|class|UserStreamingConsumer
specifier|public
class|class
name|UserStreamingConsumer
extends|extends
name|StreamingConsumer
implements|implements
name|UserStreamListener
block|{
DECL|method|UserStreamingConsumer (TwitterEndpoint te)
specifier|public
name|UserStreamingConsumer
parameter_list|(
name|TwitterEndpoint
name|te
parameter_list|)
block|{
name|super
argument_list|(
name|te
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|startStreaming ()
specifier|protected
name|void
name|startStreaming
parameter_list|()
block|{
name|twitterStream
operator|.
name|user
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onDeletionNotice (long l, long l2)
specifier|public
name|void
name|onDeletionNotice
parameter_list|(
name|long
name|l
parameter_list|,
name|long
name|l2
parameter_list|)
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|onFriendList (long[] longs)
specifier|public
name|void
name|onFriendList
parameter_list|(
name|long
index|[]
name|longs
parameter_list|)
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|onFavorite (User user, User user2, Status status)
specifier|public
name|void
name|onFavorite
parameter_list|(
name|User
name|user
parameter_list|,
name|User
name|user2
parameter_list|,
name|Status
name|status
parameter_list|)
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|onUnfavorite (User user, User user2, Status status)
specifier|public
name|void
name|onUnfavorite
parameter_list|(
name|User
name|user
parameter_list|,
name|User
name|user2
parameter_list|,
name|Status
name|status
parameter_list|)
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|onFollow (User user, User user2)
specifier|public
name|void
name|onFollow
parameter_list|(
name|User
name|user
parameter_list|,
name|User
name|user2
parameter_list|)
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|onUnfollow (User user, User user2)
specifier|public
name|void
name|onUnfollow
parameter_list|(
name|User
name|user
parameter_list|,
name|User
name|user2
parameter_list|)
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|onDirectMessage (DirectMessage directMessage)
specifier|public
name|void
name|onDirectMessage
parameter_list|(
name|DirectMessage
name|directMessage
parameter_list|)
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|onUserListMemberAddition (User user, User user2, UserList userList)
specifier|public
name|void
name|onUserListMemberAddition
parameter_list|(
name|User
name|user
parameter_list|,
name|User
name|user2
parameter_list|,
name|UserList
name|userList
parameter_list|)
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|onUserListMemberDeletion (User user, User user2, UserList userList)
specifier|public
name|void
name|onUserListMemberDeletion
parameter_list|(
name|User
name|user
parameter_list|,
name|User
name|user2
parameter_list|,
name|UserList
name|userList
parameter_list|)
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|onUserListSubscription (User user, User user2, UserList userList)
specifier|public
name|void
name|onUserListSubscription
parameter_list|(
name|User
name|user
parameter_list|,
name|User
name|user2
parameter_list|,
name|UserList
name|userList
parameter_list|)
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|onUserListUnsubscription (User user, User user2, UserList userList)
specifier|public
name|void
name|onUserListUnsubscription
parameter_list|(
name|User
name|user
parameter_list|,
name|User
name|user2
parameter_list|,
name|UserList
name|userList
parameter_list|)
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|onUserListCreation (User user, UserList userList)
specifier|public
name|void
name|onUserListCreation
parameter_list|(
name|User
name|user
parameter_list|,
name|UserList
name|userList
parameter_list|)
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|onUserListUpdate (User user, UserList userList)
specifier|public
name|void
name|onUserListUpdate
parameter_list|(
name|User
name|user
parameter_list|,
name|UserList
name|userList
parameter_list|)
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|onUserListDeletion (User user, UserList userList)
specifier|public
name|void
name|onUserListDeletion
parameter_list|(
name|User
name|user
parameter_list|,
name|UserList
name|userList
parameter_list|)
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|onUserProfileUpdate (User user)
specifier|public
name|void
name|onUserProfileUpdate
parameter_list|(
name|User
name|user
parameter_list|)
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|onUserSuspension (long l)
specifier|public
name|void
name|onUserSuspension
parameter_list|(
name|long
name|l
parameter_list|)
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|onUserDeletion (long l)
specifier|public
name|void
name|onUserDeletion
parameter_list|(
name|long
name|l
parameter_list|)
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|onBlock (User user, User user2)
specifier|public
name|void
name|onBlock
parameter_list|(
name|User
name|user
parameter_list|,
name|User
name|user2
parameter_list|)
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|onUnblock (User user, User user2)
specifier|public
name|void
name|onUnblock
parameter_list|(
name|User
name|user
parameter_list|,
name|User
name|user2
parameter_list|)
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|onStallWarning (StallWarning stallWarning)
specifier|public
name|void
name|onStallWarning
parameter_list|(
name|StallWarning
name|stallWarning
parameter_list|)
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|onRetweetedRetweet (User source, User target, Status retweetedStatus)
specifier|public
name|void
name|onRetweetedRetweet
parameter_list|(
name|User
name|source
parameter_list|,
name|User
name|target
parameter_list|,
name|Status
name|retweetedStatus
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
block|}
annotation|@
name|Override
DECL|method|onFavoritedRetweet (User source, User target, Status favoritedRetweeet)
specifier|public
name|void
name|onFavoritedRetweet
parameter_list|(
name|User
name|source
parameter_list|,
name|User
name|target
parameter_list|,
name|Status
name|favoritedRetweeet
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
block|}
annotation|@
name|Override
DECL|method|onQuotedTweet (User source, User target, Status quotingTweet)
specifier|public
name|void
name|onQuotedTweet
parameter_list|(
name|User
name|source
parameter_list|,
name|User
name|target
parameter_list|,
name|Status
name|quotingTweet
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
block|}
block|}
end_class

end_unit

