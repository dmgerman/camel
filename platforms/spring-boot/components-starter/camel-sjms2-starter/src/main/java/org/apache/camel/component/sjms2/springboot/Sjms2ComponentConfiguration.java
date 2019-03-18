begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sjms2.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sjms2
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

begin_comment
comment|/**  * The sjms2 component (simple jms) allows messages to be sent to (or consumed  * from) a JMS Queue or Topic (uses JMS 2.x API).  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
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
literal|"camel.component.sjms2"
argument_list|)
DECL|class|Sjms2ComponentConfiguration
specifier|public
class|class
name|Sjms2ComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * Whether to enable auto configuration of the sjms2 component. This is      * enabled by default.      */
DECL|field|enabled
specifier|private
name|Boolean
name|enabled
decl_stmt|;
comment|/**      * A ConnectionFactory is required to enable the SjmsComponent. It can be      * set directly or set set as part of a ConnectionResource. The option is a      * javax.jms.ConnectionFactory type.      */
DECL|field|connectionFactory
specifier|private
name|String
name|connectionFactory
decl_stmt|;
comment|/**      * A ConnectionResource is an interface that allows for customization and      * container control of the ConnectionFactory. See Plugable Connection      * Resource Management for further details. The option is a      * org.apache.camel.component.sjms.jms.ConnectionResource type.      */
DECL|field|connectionResource
specifier|private
name|String
name|connectionResource
decl_stmt|;
comment|/**      * The maximum number of connections available to endpoints started under      * this component      */
DECL|field|connectionCount
specifier|private
name|Integer
name|connectionCount
init|=
literal|1
decl_stmt|;
comment|/**      * Pluggable strategy for encoding and decoding JMS keys so they can be      * compliant with the JMS specification. Camel provides one implementation      * out of the box: default. The default strategy will safely marshal dots      * and hyphens (. and -). Can be used for JMS brokers which do not care      * whether JMS header keys contain illegal characters. You can provide your      * own implementation of the      * org.apache.camel.component.jms.JmsKeyFormatStrategy and refer to it using      * the # notation. The option is a      * org.apache.camel.component.sjms.jms.JmsKeyFormatStrategy type.      */
DECL|field|jmsKeyFormatStrategy
specifier|private
name|String
name|jmsKeyFormatStrategy
decl_stmt|;
comment|/**      * To configure which kind of commit strategy to use. Camel provides two      * implementations out of the box, default and batch. The option is a      * org.apache.camel.component.sjms.TransactionCommitStrategy type.      */
DECL|field|transactionCommitStrategy
specifier|private
name|String
name|transactionCommitStrategy
decl_stmt|;
comment|/**      * To use a custom DestinationCreationStrategy. The option is a      * org.apache.camel.component.sjms.jms.DestinationCreationStrategy type.      */
DECL|field|destinationCreationStrategy
specifier|private
name|String
name|destinationCreationStrategy
decl_stmt|;
comment|/**      * To use a custom TimedTaskManager. The option is a      * org.apache.camel.component.sjms.taskmanager.TimedTaskManager type.      */
DECL|field|timedTaskManager
specifier|private
name|String
name|timedTaskManager
decl_stmt|;
comment|/**      * To use the given MessageCreatedStrategy which are invoked when Camel      * creates new instances of javax.jms.Message objects when Camel is sending      * a JMS message. The option is a      * org.apache.camel.component.sjms.jms.MessageCreatedStrategy type.      */
DECL|field|messageCreatedStrategy
specifier|private
name|String
name|messageCreatedStrategy
decl_stmt|;
comment|/**      * When using the default      * org.apache.camel.component.sjms.jms.ConnectionFactoryResource then should      * each javax.jms.Connection be tested (calling start) before returned from      * the pool.      */
DECL|field|connectionTestOnBorrow
specifier|private
name|Boolean
name|connectionTestOnBorrow
init|=
literal|true
decl_stmt|;
comment|/**      * The username to use when creating javax.jms.Connection when using the      * default org.apache.camel.component.sjms.jms.ConnectionFactoryResource.      */
DECL|field|connectionUsername
specifier|private
name|String
name|connectionUsername
decl_stmt|;
comment|/**      * The password to use when creating javax.jms.Connection when using the      * default org.apache.camel.component.sjms.jms.ConnectionFactoryResource.      */
DECL|field|connectionPassword
specifier|private
name|String
name|connectionPassword
decl_stmt|;
comment|/**      * The client ID to use when creating javax.jms.Connection when using the      * default org.apache.camel.component.sjms.jms.ConnectionFactoryResource.      */
DECL|field|connectionClientId
specifier|private
name|String
name|connectionClientId
decl_stmt|;
comment|/**      * The max wait time in millis to block and wait on free connection when the      * pool is exhausted when using the default      * org.apache.camel.component.sjms.jms.ConnectionFactoryResource.      */
DECL|field|connectionMaxWait
specifier|private
name|Long
name|connectionMaxWait
init|=
literal|5000L
decl_stmt|;
comment|/**      * To use a custom org.apache.camel.spi.HeaderFilterStrategy to filter      * header to and from Camel message. The option is a      * org.apache.camel.spi.HeaderFilterStrategy type.      */
DECL|field|headerFilterStrategy
specifier|private
name|String
name|headerFilterStrategy
decl_stmt|;
comment|/**      * Whether the component should resolve property placeholders on itself when      * starting. Only properties which are of String type can use property      * placeholders.      */
DECL|field|resolvePropertyPlaceholders
specifier|private
name|Boolean
name|resolvePropertyPlaceholders
init|=
literal|true
decl_stmt|;
DECL|method|getConnectionFactory ()
specifier|public
name|String
name|getConnectionFactory
parameter_list|()
block|{
return|return
name|connectionFactory
return|;
block|}
DECL|method|setConnectionFactory (String connectionFactory)
specifier|public
name|void
name|setConnectionFactory
parameter_list|(
name|String
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
DECL|method|getConnectionResource ()
specifier|public
name|String
name|getConnectionResource
parameter_list|()
block|{
return|return
name|connectionResource
return|;
block|}
DECL|method|setConnectionResource (String connectionResource)
specifier|public
name|void
name|setConnectionResource
parameter_list|(
name|String
name|connectionResource
parameter_list|)
block|{
name|this
operator|.
name|connectionResource
operator|=
name|connectionResource
expr_stmt|;
block|}
DECL|method|getConnectionCount ()
specifier|public
name|Integer
name|getConnectionCount
parameter_list|()
block|{
return|return
name|connectionCount
return|;
block|}
DECL|method|setConnectionCount (Integer connectionCount)
specifier|public
name|void
name|setConnectionCount
parameter_list|(
name|Integer
name|connectionCount
parameter_list|)
block|{
name|this
operator|.
name|connectionCount
operator|=
name|connectionCount
expr_stmt|;
block|}
DECL|method|getJmsKeyFormatStrategy ()
specifier|public
name|String
name|getJmsKeyFormatStrategy
parameter_list|()
block|{
return|return
name|jmsKeyFormatStrategy
return|;
block|}
DECL|method|setJmsKeyFormatStrategy (String jmsKeyFormatStrategy)
specifier|public
name|void
name|setJmsKeyFormatStrategy
parameter_list|(
name|String
name|jmsKeyFormatStrategy
parameter_list|)
block|{
name|this
operator|.
name|jmsKeyFormatStrategy
operator|=
name|jmsKeyFormatStrategy
expr_stmt|;
block|}
DECL|method|getTransactionCommitStrategy ()
specifier|public
name|String
name|getTransactionCommitStrategy
parameter_list|()
block|{
return|return
name|transactionCommitStrategy
return|;
block|}
DECL|method|setTransactionCommitStrategy (String transactionCommitStrategy)
specifier|public
name|void
name|setTransactionCommitStrategy
parameter_list|(
name|String
name|transactionCommitStrategy
parameter_list|)
block|{
name|this
operator|.
name|transactionCommitStrategy
operator|=
name|transactionCommitStrategy
expr_stmt|;
block|}
DECL|method|getDestinationCreationStrategy ()
specifier|public
name|String
name|getDestinationCreationStrategy
parameter_list|()
block|{
return|return
name|destinationCreationStrategy
return|;
block|}
DECL|method|setDestinationCreationStrategy ( String destinationCreationStrategy)
specifier|public
name|void
name|setDestinationCreationStrategy
parameter_list|(
name|String
name|destinationCreationStrategy
parameter_list|)
block|{
name|this
operator|.
name|destinationCreationStrategy
operator|=
name|destinationCreationStrategy
expr_stmt|;
block|}
DECL|method|getTimedTaskManager ()
specifier|public
name|String
name|getTimedTaskManager
parameter_list|()
block|{
return|return
name|timedTaskManager
return|;
block|}
DECL|method|setTimedTaskManager (String timedTaskManager)
specifier|public
name|void
name|setTimedTaskManager
parameter_list|(
name|String
name|timedTaskManager
parameter_list|)
block|{
name|this
operator|.
name|timedTaskManager
operator|=
name|timedTaskManager
expr_stmt|;
block|}
DECL|method|getMessageCreatedStrategy ()
specifier|public
name|String
name|getMessageCreatedStrategy
parameter_list|()
block|{
return|return
name|messageCreatedStrategy
return|;
block|}
DECL|method|setMessageCreatedStrategy (String messageCreatedStrategy)
specifier|public
name|void
name|setMessageCreatedStrategy
parameter_list|(
name|String
name|messageCreatedStrategy
parameter_list|)
block|{
name|this
operator|.
name|messageCreatedStrategy
operator|=
name|messageCreatedStrategy
expr_stmt|;
block|}
DECL|method|getConnectionTestOnBorrow ()
specifier|public
name|Boolean
name|getConnectionTestOnBorrow
parameter_list|()
block|{
return|return
name|connectionTestOnBorrow
return|;
block|}
DECL|method|setConnectionTestOnBorrow (Boolean connectionTestOnBorrow)
specifier|public
name|void
name|setConnectionTestOnBorrow
parameter_list|(
name|Boolean
name|connectionTestOnBorrow
parameter_list|)
block|{
name|this
operator|.
name|connectionTestOnBorrow
operator|=
name|connectionTestOnBorrow
expr_stmt|;
block|}
DECL|method|getConnectionUsername ()
specifier|public
name|String
name|getConnectionUsername
parameter_list|()
block|{
return|return
name|connectionUsername
return|;
block|}
DECL|method|setConnectionUsername (String connectionUsername)
specifier|public
name|void
name|setConnectionUsername
parameter_list|(
name|String
name|connectionUsername
parameter_list|)
block|{
name|this
operator|.
name|connectionUsername
operator|=
name|connectionUsername
expr_stmt|;
block|}
DECL|method|getConnectionPassword ()
specifier|public
name|String
name|getConnectionPassword
parameter_list|()
block|{
return|return
name|connectionPassword
return|;
block|}
DECL|method|setConnectionPassword (String connectionPassword)
specifier|public
name|void
name|setConnectionPassword
parameter_list|(
name|String
name|connectionPassword
parameter_list|)
block|{
name|this
operator|.
name|connectionPassword
operator|=
name|connectionPassword
expr_stmt|;
block|}
DECL|method|getConnectionClientId ()
specifier|public
name|String
name|getConnectionClientId
parameter_list|()
block|{
return|return
name|connectionClientId
return|;
block|}
DECL|method|setConnectionClientId (String connectionClientId)
specifier|public
name|void
name|setConnectionClientId
parameter_list|(
name|String
name|connectionClientId
parameter_list|)
block|{
name|this
operator|.
name|connectionClientId
operator|=
name|connectionClientId
expr_stmt|;
block|}
DECL|method|getConnectionMaxWait ()
specifier|public
name|Long
name|getConnectionMaxWait
parameter_list|()
block|{
return|return
name|connectionMaxWait
return|;
block|}
DECL|method|setConnectionMaxWait (Long connectionMaxWait)
specifier|public
name|void
name|setConnectionMaxWait
parameter_list|(
name|Long
name|connectionMaxWait
parameter_list|)
block|{
name|this
operator|.
name|connectionMaxWait
operator|=
name|connectionMaxWait
expr_stmt|;
block|}
DECL|method|getHeaderFilterStrategy ()
specifier|public
name|String
name|getHeaderFilterStrategy
parameter_list|()
block|{
return|return
name|headerFilterStrategy
return|;
block|}
DECL|method|setHeaderFilterStrategy (String headerFilterStrategy)
specifier|public
name|void
name|setHeaderFilterStrategy
parameter_list|(
name|String
name|headerFilterStrategy
parameter_list|)
block|{
name|this
operator|.
name|headerFilterStrategy
operator|=
name|headerFilterStrategy
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

