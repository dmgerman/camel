begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.yammer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|yammer
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
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
name|Processor
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
name|yammer
operator|.
name|model
operator|.
name|Relationships
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
name|impl
operator|.
name|ScheduledPollConsumer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|jackson
operator|.
name|map
operator|.
name|ObjectMapper
import|;
end_import

begin_comment
comment|/**  * A Yammer consumer that periodically polls relationships from Yammer's relationship API.  */
end_comment

begin_class
DECL|class|YammerRelationshipPollingConsumer
specifier|public
class|class
name|YammerRelationshipPollingConsumer
extends|extends
name|ScheduledPollConsumer
block|{
DECL|field|endpoint
specifier|private
specifier|final
name|YammerEndpoint
name|endpoint
decl_stmt|;
DECL|field|apiUrl
specifier|private
specifier|final
name|String
name|apiUrl
decl_stmt|;
DECL|method|YammerRelationshipPollingConsumer (YammerEndpoint endpoint, Processor processor)
specifier|public
name|YammerRelationshipPollingConsumer
parameter_list|(
name|YammerEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|long
name|delay
init|=
name|endpoint
operator|.
name|getConfig
argument_list|()
operator|.
name|getDelay
argument_list|()
decl_stmt|;
name|setDelay
argument_list|(
name|delay
argument_list|)
expr_stmt|;
name|setTimeUnit
argument_list|(
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
name|apiUrl
operator|=
name|getApiUrl
argument_list|()
expr_stmt|;
block|}
DECL|method|getApiUrl ()
specifier|private
name|String
name|getApiUrl
parameter_list|()
throws|throws
name|Exception
block|{
name|StringBuilder
name|url
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|String
name|function
init|=
name|endpoint
operator|.
name|getConfig
argument_list|()
operator|.
name|getFunction
argument_list|()
decl_stmt|;
switch|switch
condition|(
name|YammerFunctionType
operator|.
name|fromUri
argument_list|(
name|function
argument_list|)
condition|)
block|{
case|case
name|RELATIONSHIPS
case|:
name|url
operator|.
name|append
argument_list|(
name|YammerConstants
operator|.
name|YAMMER_BASE_API_URL
argument_list|)
expr_stmt|;
name|url
operator|.
name|append
argument_list|(
name|function
argument_list|)
expr_stmt|;
name|url
operator|.
name|append
argument_list|(
literal|".json"
argument_list|)
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|Exception
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"%s is not a valid Yammer relationship function type."
argument_list|,
name|function
argument_list|)
argument_list|)
throw|;
block|}
return|return
name|url
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|poll ()
specifier|protected
name|int
name|poll
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|()
decl_stmt|;
try|try
block|{
name|String
name|jsonBody
init|=
name|endpoint
operator|.
name|getConfig
argument_list|()
operator|.
name|getRequestor
argument_list|(
name|apiUrl
argument_list|)
operator|.
name|send
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|endpoint
operator|.
name|getConfig
argument_list|()
operator|.
name|isUseJson
argument_list|()
condition|)
block|{
name|ObjectMapper
name|jsonMapper
init|=
operator|new
name|ObjectMapper
argument_list|()
decl_stmt|;
name|Relationships
name|relationships
init|=
name|jsonMapper
operator|.
name|readValue
argument_list|(
name|jsonBody
argument_list|,
name|Relationships
operator|.
name|class
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|relationships
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|jsonBody
argument_list|)
expr_stmt|;
block|}
comment|// send message to next processor in the route
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
return|return
literal|1
return|;
comment|// number of messages polled
block|}
finally|finally
block|{
comment|// log exception if an exception occurred and was not handled
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
literal|"Error processing exchange"
argument_list|,
name|exchange
argument_list|,
name|exchange
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

