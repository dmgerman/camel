begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jmx
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jmx
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashSet
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|Notification
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|NotificationFilter
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
name|Exchange
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
name|jmx
operator|.
name|beans
operator|.
name|ISimpleMXBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertEquals
import|;
end_import

begin_comment
comment|/**  * Tests that the NotificationFilter is applied if configured  */
end_comment

begin_class
DECL|class|JMXNotificationFilterTest
specifier|public
class|class
name|JMXNotificationFilterTest
extends|extends
name|SimpleBeanFixture
block|{
comment|/**      * we'll track the rejected messages so we know what got filtered      */
DECL|field|mRejected
specifier|private
name|LinkedHashSet
argument_list|<
name|Notification
argument_list|>
name|mRejected
init|=
operator|new
name|LinkedHashSet
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|testNotificationFilter ()
specifier|public
name|void
name|testNotificationFilter
parameter_list|()
throws|throws
name|Exception
block|{
name|ISimpleMXBean
name|bean
init|=
name|getSimpleMXBean
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|mRejected
operator|.
name|size
argument_list|()
argument_list|,
literal|"no notifications should have been filtered at this point"
argument_list|)
expr_stmt|;
comment|// we should only get 5 messages, which is 1/2 the number of times we touched the object.
comment|// The 1/2 is due to the behavior of the test NotificationFilter implemented below
name|getMockFixture
argument_list|()
operator|.
name|getMockEndpoint
argument_list|()
operator|.
name|setExpectedMessageCount
argument_list|(
literal|5
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|10
condition|;
name|i
operator|++
control|)
block|{
name|bean
operator|.
name|touch
argument_list|()
expr_stmt|;
block|}
name|getMockFixture
argument_list|()
operator|.
name|waitForMessages
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|mRejected
operator|.
name|size
argument_list|()
argument_list|,
literal|"5 notifications should have been filtered"
argument_list|)
expr_stmt|;
comment|// assert that all of the rejected ones are odd and accepted ones even
for|for
control|(
name|Notification
name|rejected
range|:
name|mRejected
control|)
block|{
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|rejected
operator|.
name|getSequenceNumber
argument_list|()
operator|%
literal|2
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Exchange
name|received
range|:
name|getMockFixture
argument_list|()
operator|.
name|getMockEndpoint
argument_list|()
operator|.
name|getReceivedExchanges
argument_list|()
control|)
block|{
name|Notification
name|n
init|=
operator|(
name|Notification
operator|)
name|received
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|n
operator|.
name|getSequenceNumber
argument_list|()
operator|%
literal|2
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|buildFromURI ()
specifier|protected
name|JMXUriBuilder
name|buildFromURI
parameter_list|()
block|{
comment|// use the raw format so we can we can get the Notification and assert on the sequence
return|return
name|super
operator|.
name|buildFromURI
argument_list|()
operator|.
name|withNotificationFilter
argument_list|(
literal|"#myFilter"
argument_list|)
operator|.
name|withFormat
argument_list|(
literal|"raw"
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|initRegistry ()
specifier|protected
name|void
name|initRegistry
parameter_list|()
block|{
name|super
operator|.
name|initRegistry
argument_list|()
expr_stmt|;
comment|// initialize the registry with our filter
name|getRegistry
argument_list|()
operator|.
name|bind
argument_list|(
literal|"myFilter"
argument_list|,
operator|new
name|NotificationFilter
argument_list|()
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
specifier|public
name|boolean
name|isNotificationEnabled
parameter_list|(
name|Notification
name|aNotification
parameter_list|)
block|{
comment|// only accept even notifications
name|boolean
name|enabled
init|=
name|aNotification
operator|.
name|getSequenceNumber
argument_list|()
operator|%
literal|2
operator|==
literal|0
decl_stmt|;
if|if
condition|(
operator|!
name|enabled
condition|)
block|{
name|mRejected
operator|.
name|add
argument_list|(
name|aNotification
argument_list|)
expr_stmt|;
block|}
return|return
name|enabled
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

