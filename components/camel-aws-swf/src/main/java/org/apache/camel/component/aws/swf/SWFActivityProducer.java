begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.swf
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|aws
operator|.
name|swf
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
name|support
operator|.
name|DefaultProducer
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
name|util
operator|.
name|URISupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
DECL|class|SWFActivityProducer
specifier|public
class|class
name|SWFActivityProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|LOGGER
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|SWFActivityProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|camelSWFClient
specifier|private
specifier|final
name|CamelSWFActivityClient
name|camelSWFClient
decl_stmt|;
DECL|field|endpoint
specifier|private
name|SWFEndpoint
name|endpoint
decl_stmt|;
DECL|field|configuration
specifier|private
name|SWFConfiguration
name|configuration
decl_stmt|;
DECL|field|swfActivityProducerToString
specifier|private
specifier|transient
name|String
name|swfActivityProducerToString
decl_stmt|;
DECL|method|SWFActivityProducer (SWFEndpoint endpoint, CamelSWFActivityClient camelSWFActivityClient)
specifier|public
name|SWFActivityProducer
parameter_list|(
name|SWFEndpoint
name|endpoint
parameter_list|,
name|CamelSWFActivityClient
name|camelSWFActivityClient
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
expr_stmt|;
name|this
operator|.
name|camelSWFClient
operator|=
name|camelSWFActivityClient
expr_stmt|;
block|}
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|eventName
init|=
name|getEventName
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|String
name|version
init|=
name|getVersion
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"scheduleActivity : "
operator|+
name|eventName
operator|+
literal|" : "
operator|+
name|version
argument_list|)
expr_stmt|;
name|Object
name|result
init|=
name|camelSWFClient
operator|.
name|scheduleActivity
argument_list|(
name|eventName
argument_list|,
name|version
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|setResult
argument_list|(
name|exchange
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
DECL|method|getEventName (Exchange exchange)
specifier|private
name|String
name|getEventName
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|eventName
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SWFConstants
operator|.
name|EVENT_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|eventName
operator|!=
literal|null
condition|?
name|eventName
else|:
name|configuration
operator|.
name|getEventName
argument_list|()
return|;
block|}
DECL|method|getVersion (Exchange exchange)
specifier|private
name|String
name|getVersion
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|version
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SWFConstants
operator|.
name|VERSION
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|version
operator|!=
literal|null
condition|?
name|version
else|:
name|configuration
operator|.
name|getVersion
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
if|if
condition|(
name|swfActivityProducerToString
operator|==
literal|null
condition|)
block|{
name|swfActivityProducerToString
operator|=
literal|"SWFActivityProducer["
operator|+
name|URISupport
operator|.
name|sanitizeUri
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
operator|+
literal|"]"
expr_stmt|;
block|}
return|return
name|swfActivityProducerToString
return|;
block|}
block|}
end_class

end_unit

