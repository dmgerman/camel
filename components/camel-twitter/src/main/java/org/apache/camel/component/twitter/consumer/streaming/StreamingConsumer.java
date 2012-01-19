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
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

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
name|Twitter4JConsumer
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
name|StatusDeletionNotice
import|;
end_import

begin_import
import|import
name|twitter4j
operator|.
name|StatusListener
import|;
end_import

begin_import
import|import
name|twitter4j
operator|.
name|TwitterException
import|;
end_import

begin_comment
comment|/**  * Super class providing consuming capabilities for the streaming API.  *   */
end_comment

begin_class
DECL|class|StreamingConsumer
specifier|public
class|class
name|StreamingConsumer
extends|extends
name|Twitter4JConsumer
implements|implements
name|StatusListener
block|{
DECL|field|te
name|TwitterEndpoint
name|te
decl_stmt|;
DECL|field|receivedStatuses
specifier|private
specifier|final
name|List
argument_list|<
name|Status
argument_list|>
name|receivedStatuses
init|=
operator|new
name|ArrayList
argument_list|<
name|Status
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|clear
specifier|private
specifier|volatile
name|boolean
name|clear
decl_stmt|;
DECL|method|StreamingConsumer (TwitterEndpoint te)
specifier|public
name|StreamingConsumer
parameter_list|(
name|TwitterEndpoint
name|te
parameter_list|)
block|{
name|this
operator|.
name|te
operator|=
name|te
expr_stmt|;
block|}
DECL|method|pollConsume ()
specifier|public
name|List
argument_list|<
name|Status
argument_list|>
name|pollConsume
parameter_list|()
throws|throws
name|TwitterException
block|{
name|clear
operator|=
literal|true
expr_stmt|;
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
operator|new
name|ArrayList
argument_list|<
name|Status
argument_list|>
argument_list|(
name|receivedStatuses
argument_list|)
argument_list|)
return|;
block|}
DECL|method|directConsume ()
specifier|public
name|List
argument_list|<
name|Status
argument_list|>
name|directConsume
parameter_list|()
throws|throws
name|TwitterException
block|{
comment|// not used
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|onException (Exception ex)
specifier|public
name|void
name|onException
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{     }
annotation|@
name|Override
DECL|method|onStatus (Status status)
specifier|public
name|void
name|onStatus
parameter_list|(
name|Status
name|status
parameter_list|)
block|{
if|if
condition|(
name|clear
condition|)
block|{
name|receivedStatuses
operator|.
name|clear
argument_list|()
expr_stmt|;
name|clear
operator|=
literal|false
expr_stmt|;
block|}
name|receivedStatuses
operator|.
name|add
argument_list|(
name|status
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onDeletionNotice (StatusDeletionNotice statusDeletionNotice)
specifier|public
name|void
name|onDeletionNotice
parameter_list|(
name|StatusDeletionNotice
name|statusDeletionNotice
parameter_list|)
block|{     }
annotation|@
name|Override
DECL|method|onTrackLimitationNotice (int numberOfLimitedStatuses)
specifier|public
name|void
name|onTrackLimitationNotice
parameter_list|(
name|int
name|numberOfLimitedStatuses
parameter_list|)
block|{     }
annotation|@
name|Override
DECL|method|onScrubGeo (long userId, long upToStatusId)
specifier|public
name|void
name|onScrubGeo
parameter_list|(
name|long
name|userId
parameter_list|,
name|long
name|upToStatusId
parameter_list|)
block|{     }
block|}
end_class

end_unit

