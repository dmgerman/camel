begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.nagios
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|nagios
package|;
end_package

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|jsendnsca
operator|.
name|NagiosPassiveCheckSender
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|jsendnsca
operator|.
name|NonBlockingNagiosPassiveCheckSender
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|jsendnsca
operator|.
name|PassiveCheckSender
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
name|Component
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
name|support
operator|.
name|DefaultEndpoint
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * To send passive checks to Nagios using JSendNSCA.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.3.0"
argument_list|,
name|scheme
operator|=
literal|"nagios"
argument_list|,
name|title
operator|=
literal|"Nagios"
argument_list|,
name|syntax
operator|=
literal|"nagios:host:port"
argument_list|,
name|producerOnly
operator|=
literal|true
argument_list|,
name|label
operator|=
literal|"monitoring"
argument_list|)
DECL|class|NagiosEndpoint
specifier|public
class|class
name|NagiosEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|field|sender
specifier|private
name|PassiveCheckSender
name|sender
decl_stmt|;
annotation|@
name|UriParam
DECL|field|configuration
specifier|private
name|NagiosConfiguration
name|configuration
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|sendSync
specifier|private
name|boolean
name|sendSync
init|=
literal|true
decl_stmt|;
DECL|method|NagiosEndpoint ()
specifier|public
name|NagiosEndpoint
parameter_list|()
block|{     }
DECL|method|NagiosEndpoint (String endpointUri, Component component)
specifier|public
name|NagiosEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|configuration
argument_list|,
literal|"configuration"
argument_list|)
expr_stmt|;
return|return
operator|new
name|NagiosProducer
argument_list|(
name|this
argument_list|,
name|getSender
argument_list|()
argument_list|)
return|;
block|}
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Nagios consumer not supported"
argument_list|)
throw|;
block|}
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
DECL|method|getConfiguration ()
specifier|public
name|NagiosConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration (NagiosConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|NagiosConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|isSendSync ()
specifier|public
name|boolean
name|isSendSync
parameter_list|()
block|{
return|return
name|sendSync
return|;
block|}
comment|/**      * Whether or not to use synchronous when sending a passive check.      * Setting it to false will allow Camel to continue routing the message and the passive check message will be send asynchronously.      */
DECL|method|setSendSync (boolean sendSync)
specifier|public
name|void
name|setSendSync
parameter_list|(
name|boolean
name|sendSync
parameter_list|)
block|{
name|this
operator|.
name|sendSync
operator|=
name|sendSync
expr_stmt|;
block|}
DECL|method|getSender ()
specifier|public
specifier|synchronized
name|PassiveCheckSender
name|getSender
parameter_list|()
block|{
if|if
condition|(
name|sender
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|isSendSync
argument_list|()
condition|)
block|{
name|sender
operator|=
operator|new
name|NagiosPassiveCheckSender
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|getNagiosSettings
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// use a non blocking sender
name|sender
operator|=
operator|new
name|NonBlockingNagiosPassiveCheckSender
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|getNagiosSettings
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|sender
return|;
block|}
DECL|method|setSender (PassiveCheckSender sender)
specifier|public
name|void
name|setSender
parameter_list|(
name|PassiveCheckSender
name|sender
parameter_list|)
block|{
name|this
operator|.
name|sender
operator|=
name|sender
expr_stmt|;
block|}
block|}
end_class

end_unit

