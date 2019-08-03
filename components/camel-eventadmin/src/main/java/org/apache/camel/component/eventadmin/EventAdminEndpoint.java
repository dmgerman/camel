begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.eventadmin
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|eventadmin
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
name|MultipleConsumersSupport
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
name|UriPath
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
name|DefaultEndpoint
import|;
end_import

begin_comment
comment|/**  * The eventadmin component can be used in an OSGi environment to receive OSGi EventAdmin events and process them.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.6.0"
argument_list|,
name|scheme
operator|=
literal|"eventadmin"
argument_list|,
name|title
operator|=
literal|"OSGi EventAdmin"
argument_list|,
name|syntax
operator|=
literal|"eventadmin:topic"
argument_list|,
name|label
operator|=
literal|"eventbus"
argument_list|)
DECL|class|EventAdminEndpoint
specifier|public
class|class
name|EventAdminEndpoint
extends|extends
name|DefaultEndpoint
implements|implements
name|MultipleConsumersSupport
block|{
annotation|@
name|UriPath
DECL|field|topic
specifier|private
specifier|final
name|String
name|topic
decl_stmt|;
annotation|@
name|UriParam
DECL|field|send
specifier|private
name|boolean
name|send
decl_stmt|;
DECL|method|EventAdminEndpoint (String uri, EventAdminComponent component, String topic)
specifier|public
name|EventAdminEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|EventAdminComponent
name|component
parameter_list|,
name|String
name|topic
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
name|topic
operator|=
name|topic
expr_stmt|;
block|}
comment|/**      * Name of topic to listen or send to      */
DECL|method|getTopic ()
specifier|public
name|String
name|getTopic
parameter_list|()
block|{
return|return
name|topic
return|;
block|}
DECL|method|isSend ()
specifier|public
name|boolean
name|isSend
parameter_list|()
block|{
return|return
name|send
return|;
block|}
comment|/**      * Whether to use 'send' or 'synchronous' deliver.      * Default false (async delivery)      */
DECL|method|setSend (boolean send)
specifier|public
name|void
name|setSend
parameter_list|(
name|boolean
name|send
parameter_list|)
block|{
name|this
operator|.
name|send
operator|=
name|send
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getComponent ()
specifier|public
name|EventAdminComponent
name|getComponent
parameter_list|()
block|{
return|return
operator|(
name|EventAdminComponent
operator|)
name|super
operator|.
name|getComponent
argument_list|()
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
return|return
operator|new
name|EventAdminProducer
argument_list|(
name|this
argument_list|)
return|;
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
name|EventAdminConsumer
name|answer
init|=
operator|new
name|EventAdminConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|isMultipleConsumersSupported ()
specifier|public
name|boolean
name|isMultipleConsumersSupported
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

