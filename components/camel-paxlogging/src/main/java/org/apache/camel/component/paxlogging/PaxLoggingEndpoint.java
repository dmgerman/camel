begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.paxlogging
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|paxlogging
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
name|RuntimeCamelException
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
name|Metadata
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
comment|/**  * The paxlogging component can be used in an OSGi environment to receive PaxLogging events and process them.  */
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
literal|"paxlogging"
argument_list|,
name|title
operator|=
literal|"OSGi PAX Logging"
argument_list|,
name|syntax
operator|=
literal|"paxlogging:appender"
argument_list|,
name|consumerOnly
operator|=
literal|true
argument_list|,
name|label
operator|=
literal|"monitoring"
argument_list|)
DECL|class|PaxLoggingEndpoint
specifier|public
class|class
name|PaxLoggingEndpoint
extends|extends
name|DefaultEndpoint
block|{
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|appender
specifier|private
specifier|final
name|String
name|appender
decl_stmt|;
DECL|method|PaxLoggingEndpoint (String uri, PaxLoggingComponent component, String appender)
specifier|public
name|PaxLoggingEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|PaxLoggingComponent
name|component
parameter_list|,
name|String
name|appender
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
name|appender
operator|=
name|appender
expr_stmt|;
block|}
comment|/**      * Appender is the name of the pax appender that need to be configured in the PaxLogging service configuration.      */
DECL|method|getAppender ()
specifier|public
name|String
name|getAppender
parameter_list|()
block|{
return|return
name|appender
return|;
block|}
comment|/**      * @deprecated use {@link #getAppender()}      */
annotation|@
name|Deprecated
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|getAppender
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getComponent ()
specifier|public
name|PaxLoggingComponent
name|getComponent
parameter_list|()
block|{
return|return
operator|(
name|PaxLoggingComponent
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
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Cannot produce to a PaxLoggingEndpoint: "
operator|+
name|getEndpointUri
argument_list|()
argument_list|)
throw|;
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
name|PaxLoggingConsumer
name|answer
init|=
operator|new
name|PaxLoggingConsumer
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
block|}
end_class

end_unit

