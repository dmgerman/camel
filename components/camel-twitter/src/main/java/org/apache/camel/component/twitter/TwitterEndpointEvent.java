begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.twitter
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
name|Consumer
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
name|Producer
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
name|direct
operator|.
name|DirectEndpoint
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
name|TwitterConsumerEvent
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
name|data
operator|.
name|EndpointType
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
name|spi
operator|.
name|UriEndpoint
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
name|spi
operator|.
name|UriParam
import|;
end_import

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"twitter"
argument_list|,
name|title
operator|=
literal|"Twitter"
argument_list|,
name|syntax
operator|=
literal|"twitter:type"
argument_list|,
name|consumerClass
operator|=
name|Twitter4JConsumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"api,social"
argument_list|)
DECL|class|TwitterEndpointEvent
specifier|public
class|class
name|TwitterEndpointEvent
extends|extends
name|DirectEndpoint
implements|implements
name|TwitterEndpoint
block|{
annotation|@
name|UriParam
DECL|field|properties
specifier|private
name|TwitterConfiguration
name|properties
decl_stmt|;
DECL|method|TwitterEndpointEvent (String uri, TwitterComponent component, TwitterConfiguration properties)
specifier|public
name|TwitterEndpointEvent
parameter_list|(
name|String
name|uri
parameter_list|,
name|TwitterComponent
name|component
parameter_list|,
name|TwitterConfiguration
name|properties
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|properties
operator|=
name|properties
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|Twitter4JConsumer
name|twitter4jConsumer
init|=
name|Twitter4JFactory
operator|.
name|getConsumer
argument_list|(
name|this
argument_list|,
name|getEndpointUri
argument_list|()
argument_list|)
decl_stmt|;
return|return
operator|new
name|TwitterConsumerEvent
argument_list|(
name|this
argument_list|,
name|processor
argument_list|,
name|twitter4jConsumer
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Producer not supported"
argument_list|)
throw|;
block|}
DECL|method|getProperties ()
specifier|public
name|TwitterConfiguration
name|getProperties
parameter_list|()
block|{
return|return
name|properties
return|;
block|}
DECL|method|setProperties (TwitterConfiguration properties)
specifier|public
name|void
name|setProperties
parameter_list|(
name|TwitterConfiguration
name|properties
parameter_list|)
block|{
name|this
operator|.
name|properties
operator|=
name|properties
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpointType ()
specifier|public
name|EndpointType
name|getEndpointType
parameter_list|()
block|{
return|return
name|EndpointType
operator|.
name|EVENT
return|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
if|if
condition|(
name|properties
operator|.
name|getTwitterStream
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|properties
operator|.
name|getTwitterStream
argument_list|()
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

