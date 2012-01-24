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
name|twitter4j
operator|.
name|Twitter
import|;
end_import

begin_class
DECL|class|TwitterEndpointEvent
specifier|public
class|class
name|TwitterEndpointEvent
extends|extends
name|DirectEndpoint
implements|implements
name|TwitterEndpoint
block|{
DECL|field|twitter
specifier|private
name|Twitter
name|twitter
decl_stmt|;
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
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
block|{
name|twitter
operator|=
name|properties
operator|.
name|getTwitterInstance
argument_list|()
expr_stmt|;
block|}
DECL|method|getTwitter ()
specifier|public
name|Twitter
name|getTwitter
parameter_list|()
block|{
return|return
name|twitter
return|;
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
block|}
end_class

end_unit

