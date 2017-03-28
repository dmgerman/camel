begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sjms.support
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sjms
operator|.
name|support
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|JMSException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Session
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|activemq
operator|.
name|ActiveMQConnection
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|activemq
operator|.
name|management
operator|.
name|JMSStatsImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|activemq
operator|.
name|transport
operator|.
name|Transport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|activemq
operator|.
name|util
operator|.
name|IdGenerator
import|;
end_import

begin_class
DECL|class|MockConnection
specifier|public
class|class
name|MockConnection
extends|extends
name|ActiveMQConnection
block|{
DECL|field|returnBadSessionNTimes
specifier|private
name|int
name|returnBadSessionNTimes
decl_stmt|;
DECL|method|MockConnection (final Transport transport, IdGenerator clientIdGenerator, IdGenerator connectionIdGenerator, JMSStatsImpl factoryStats, int returnBadSessionNTimes)
specifier|protected
name|MockConnection
parameter_list|(
specifier|final
name|Transport
name|transport
parameter_list|,
name|IdGenerator
name|clientIdGenerator
parameter_list|,
name|IdGenerator
name|connectionIdGenerator
parameter_list|,
name|JMSStatsImpl
name|factoryStats
parameter_list|,
name|int
name|returnBadSessionNTimes
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|transport
argument_list|,
name|clientIdGenerator
argument_list|,
name|connectionIdGenerator
argument_list|,
name|factoryStats
argument_list|)
expr_stmt|;
name|this
operator|.
name|returnBadSessionNTimes
operator|=
name|returnBadSessionNTimes
expr_stmt|;
block|}
DECL|method|createSession (boolean transacted, int acknowledgeMode)
specifier|public
name|Session
name|createSession
parameter_list|(
name|boolean
name|transacted
parameter_list|,
name|int
name|acknowledgeMode
parameter_list|)
throws|throws
name|JMSException
block|{
name|this
operator|.
name|checkClosedOrFailed
argument_list|()
expr_stmt|;
name|this
operator|.
name|ensureConnectionInfoSent
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|transacted
condition|)
block|{
if|if
condition|(
name|acknowledgeMode
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|JMSException
argument_list|(
literal|"acknowledgeMode SESSION_TRANSACTED cannot be used for an non-transacted Session"
argument_list|)
throw|;
block|}
if|if
condition|(
name|acknowledgeMode
argument_list|<
literal|0
operator|||
name|acknowledgeMode
argument_list|>
literal|4
condition|)
block|{
throw|throw
operator|new
name|JMSException
argument_list|(
literal|"invalid acknowledgeMode: "
operator|+
name|acknowledgeMode
operator|+
literal|". Valid values are Session.AUTO_ACKNOWLEDGE (1), Session.CLIENT_ACKNOWLEDGE (2), "
operator|+
literal|"Session.DUPS_OK_ACKNOWLEDGE (3), ActiveMQSession.INDIVIDUAL_ACKNOWLEDGE (4) or for transacted sessions Session.SESSION_TRANSACTED (0)"
argument_list|)
throw|;
block|}
block|}
name|boolean
name|useBadSession
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|returnBadSessionNTimes
operator|>
literal|0
condition|)
block|{
name|useBadSession
operator|=
literal|true
expr_stmt|;
name|returnBadSessionNTimes
operator|=
name|returnBadSessionNTimes
operator|-
literal|1
expr_stmt|;
block|}
return|return
operator|new
name|MockSession
argument_list|(
name|this
argument_list|,
name|this
operator|.
name|getNextSessionId
argument_list|()
argument_list|,
name|transacted
condition|?
literal|0
else|:
name|acknowledgeMode
argument_list|,
name|this
operator|.
name|isDispatchAsync
argument_list|()
argument_list|,
name|this
operator|.
name|isAlwaysSessionAsync
argument_list|()
argument_list|,
name|useBadSession
argument_list|)
return|;
block|}
block|}
end_class

end_unit

