begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.rabbitmq.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|rabbitmq
operator|.
name|springboot
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Generated
import|;
end_import

begin_import
import|import
name|com
operator|.
name|rabbitmq
operator|.
name|client
operator|.
name|ConnectionFactory
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
name|spring
operator|.
name|boot
operator|.
name|ComponentConfigurationPropertiesCommon
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|NestedConfigurationProperty
import|;
end_import

begin_comment
comment|/**  * The rabbitmq component allows you produce and consume messages from RabbitMQ  * instances.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.SpringBootAutoConfigurationMojo"
argument_list|)
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.rabbitmq"
argument_list|)
DECL|class|RabbitMQComponentConfiguration
specifier|public
class|class
name|RabbitMQComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * The hostname of the running rabbitmq instance or cluster.      */
DECL|field|hostname
specifier|private
name|String
name|hostname
decl_stmt|;
comment|/**      * Port number for the host with the running rabbitmq instance or cluster.      */
DECL|field|portNumber
specifier|private
name|Integer
name|portNumber
init|=
literal|5672
decl_stmt|;
comment|/**      * Username in case of authenticated access      */
DECL|field|username
specifier|private
name|String
name|username
init|=
literal|"guest"
decl_stmt|;
comment|/**      * Password for authenticated access      */
DECL|field|password
specifier|private
name|String
name|password
init|=
literal|"guest"
decl_stmt|;
comment|/**      * To use a custom RabbitMQ connection factory. When this option is set, all      * connection options (connectionTimeout, requestedChannelMax...) set on URI      * are not used      */
annotation|@
name|NestedConfigurationProperty
DECL|field|connectionFactory
specifier|private
name|ConnectionFactory
name|connectionFactory
decl_stmt|;
comment|/**      * Whether to auto-detect looking up RabbitMQ connection factory from the      * registry. When enabled and a single instance of the connection factory is      * found then it will be used. An explicit connection factory can be      * configured on the component or endpoint level which takes precedence.      */
DECL|field|autoDetectConnectionFactory
specifier|private
name|Boolean
name|autoDetectConnectionFactory
init|=
literal|true
decl_stmt|;
comment|/**      * Whether the component should resolve property placeholders on itself when      * starting. Only properties which are of String type can use property      * placeholders.      */
DECL|field|resolvePropertyPlaceholders
specifier|private
name|Boolean
name|resolvePropertyPlaceholders
init|=
literal|true
decl_stmt|;
DECL|method|getHostname ()
specifier|public
name|String
name|getHostname
parameter_list|()
block|{
return|return
name|hostname
return|;
block|}
DECL|method|setHostname (String hostname)
specifier|public
name|void
name|setHostname
parameter_list|(
name|String
name|hostname
parameter_list|)
block|{
name|this
operator|.
name|hostname
operator|=
name|hostname
expr_stmt|;
block|}
DECL|method|getPortNumber ()
specifier|public
name|Integer
name|getPortNumber
parameter_list|()
block|{
return|return
name|portNumber
return|;
block|}
DECL|method|setPortNumber (Integer portNumber)
specifier|public
name|void
name|setPortNumber
parameter_list|(
name|Integer
name|portNumber
parameter_list|)
block|{
name|this
operator|.
name|portNumber
operator|=
name|portNumber
expr_stmt|;
block|}
DECL|method|getUsername ()
specifier|public
name|String
name|getUsername
parameter_list|()
block|{
return|return
name|username
return|;
block|}
DECL|method|setUsername (String username)
specifier|public
name|void
name|setUsername
parameter_list|(
name|String
name|username
parameter_list|)
block|{
name|this
operator|.
name|username
operator|=
name|username
expr_stmt|;
block|}
DECL|method|getPassword ()
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|password
return|;
block|}
DECL|method|setPassword (String password)
specifier|public
name|void
name|setPassword
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|this
operator|.
name|password
operator|=
name|password
expr_stmt|;
block|}
DECL|method|getConnectionFactory ()
specifier|public
name|ConnectionFactory
name|getConnectionFactory
parameter_list|()
block|{
return|return
name|connectionFactory
return|;
block|}
DECL|method|setConnectionFactory (ConnectionFactory connectionFactory)
specifier|public
name|void
name|setConnectionFactory
parameter_list|(
name|ConnectionFactory
name|connectionFactory
parameter_list|)
block|{
name|this
operator|.
name|connectionFactory
operator|=
name|connectionFactory
expr_stmt|;
block|}
DECL|method|getAutoDetectConnectionFactory ()
specifier|public
name|Boolean
name|getAutoDetectConnectionFactory
parameter_list|()
block|{
return|return
name|autoDetectConnectionFactory
return|;
block|}
DECL|method|setAutoDetectConnectionFactory ( Boolean autoDetectConnectionFactory)
specifier|public
name|void
name|setAutoDetectConnectionFactory
parameter_list|(
name|Boolean
name|autoDetectConnectionFactory
parameter_list|)
block|{
name|this
operator|.
name|autoDetectConnectionFactory
operator|=
name|autoDetectConnectionFactory
expr_stmt|;
block|}
DECL|method|getResolvePropertyPlaceholders ()
specifier|public
name|Boolean
name|getResolvePropertyPlaceholders
parameter_list|()
block|{
return|return
name|resolvePropertyPlaceholders
return|;
block|}
DECL|method|setResolvePropertyPlaceholders ( Boolean resolvePropertyPlaceholders)
specifier|public
name|void
name|setResolvePropertyPlaceholders
parameter_list|(
name|Boolean
name|resolvePropertyPlaceholders
parameter_list|)
block|{
name|this
operator|.
name|resolvePropertyPlaceholders
operator|=
name|resolvePropertyPlaceholders
expr_stmt|;
block|}
block|}
end_class

end_unit

