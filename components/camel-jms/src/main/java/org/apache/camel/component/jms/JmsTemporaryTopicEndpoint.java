begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jms
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jms
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Destination
import|;
end_import

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
name|javax
operator|.
name|jms
operator|.
name|TemporaryTopic
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jms
operator|.
name|core
operator|.
name|JmsOperations
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jms
operator|.
name|core
operator|.
name|SessionCallback
import|;
end_import

begin_comment
comment|/**  * A<a href="http://activemq.apache.org/jms.html">JMS Endpoint</a>  * for working with a {@link TemporaryTopic}  *  * @version $Revision: 1.1 $  */
end_comment

begin_comment
comment|// TODO need to be really careful to always use the same Connection otherwise the destination goes stale
end_comment

begin_class
DECL|class|JmsTemporaryTopicEndpoint
specifier|public
class|class
name|JmsTemporaryTopicEndpoint
extends|extends
name|JmsEndpoint
implements|implements
name|DestinationEndpoint
block|{
DECL|field|jmsDestination
specifier|private
name|Destination
name|jmsDestination
decl_stmt|;
DECL|method|JmsTemporaryTopicEndpoint (String uri, JmsComponent component, String destination, JmsConfiguration configuration)
specifier|public
name|JmsTemporaryTopicEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|JmsComponent
name|component
parameter_list|,
name|String
name|destination
parameter_list|,
name|JmsConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|,
name|destination
argument_list|,
literal|true
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
block|}
comment|/**      * This endpoint is a singleton so that the temporary destination instances are shared across all      * producers and consumers of the same endpoint URI      *      * @return true      */
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|getJmsDestination (Session session)
specifier|public
specifier|synchronized
name|Destination
name|getJmsDestination
parameter_list|(
name|Session
name|session
parameter_list|)
throws|throws
name|JMSException
block|{
if|if
condition|(
name|jmsDestination
operator|==
literal|null
condition|)
block|{
name|jmsDestination
operator|=
name|createJmsDestination
argument_list|(
name|session
argument_list|)
expr_stmt|;
block|}
return|return
name|jmsDestination
return|;
block|}
DECL|method|createJmsDestination (Session session)
specifier|protected
name|Destination
name|createJmsDestination
parameter_list|(
name|Session
name|session
parameter_list|)
throws|throws
name|JMSException
block|{
return|return
name|session
operator|.
name|createTemporaryTopic
argument_list|()
return|;
block|}
block|}
end_class

end_unit

