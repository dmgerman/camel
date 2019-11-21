begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.activemq
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|activemq
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Constructor
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
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
name|activemq
operator|.
name|ActiveMQConnectionFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|activemq
operator|.
name|Service
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
name|jms
operator|.
name|JmsConfiguration
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
name|springframework
operator|.
name|jms
operator|.
name|connection
operator|.
name|CachingConnectionFactory
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
name|connection
operator|.
name|DelegatingConnectionFactory
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
name|connection
operator|.
name|JmsTransactionManager
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
name|connection
operator|.
name|SingleConnectionFactory
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
name|JmsTemplate
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|transaction
operator|.
name|PlatformTransactionManager
import|;
end_import

begin_class
DECL|class|ActiveMQConfiguration
specifier|public
class|class
name|ActiveMQConfiguration
extends|extends
name|JmsConfiguration
block|{
DECL|field|activeMQComponent
specifier|private
name|ActiveMQComponent
name|activeMQComponent
decl_stmt|;
DECL|field|brokerURL
specifier|private
name|String
name|brokerURL
init|=
name|ActiveMQConnectionFactory
operator|.
name|DEFAULT_BROKER_URL
decl_stmt|;
DECL|field|customBrokerURL
specifier|private
specifier|volatile
name|boolean
name|customBrokerURL
decl_stmt|;
DECL|field|useSingleConnection
specifier|private
name|boolean
name|useSingleConnection
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|usePooledConnection
specifier|private
name|boolean
name|usePooledConnection
init|=
literal|true
decl_stmt|;
DECL|field|trustAllPackages
specifier|private
name|boolean
name|trustAllPackages
decl_stmt|;
DECL|method|ActiveMQConfiguration ()
specifier|public
name|ActiveMQConfiguration
parameter_list|()
block|{     }
DECL|method|getBrokerURL ()
specifier|public
name|String
name|getBrokerURL
parameter_list|()
block|{
return|return
name|brokerURL
return|;
block|}
comment|/**      * Sets the broker URL to use to connect to ActiveMQ broker.      */
DECL|method|setBrokerURL (String brokerURL)
specifier|public
name|void
name|setBrokerURL
parameter_list|(
name|String
name|brokerURL
parameter_list|)
block|{
name|this
operator|.
name|brokerURL
operator|=
name|brokerURL
expr_stmt|;
name|this
operator|.
name|customBrokerURL
operator|=
literal|true
expr_stmt|;
block|}
DECL|method|isUseSingleConnection ()
specifier|public
name|boolean
name|isUseSingleConnection
parameter_list|()
block|{
return|return
name|useSingleConnection
return|;
block|}
comment|/**      * @deprecated - use JmsConfiguration#getUsername()      * @see JmsConfiguration#getUsername()      */
annotation|@
name|Deprecated
DECL|method|getUserName ()
specifier|public
name|String
name|getUserName
parameter_list|()
block|{
return|return
name|getUsername
argument_list|()
return|;
block|}
comment|/**      * @deprecated - use JmsConfiguration#setUsername(String)      * @see JmsConfiguration#setUsername(String)      */
annotation|@
name|Deprecated
DECL|method|setUserName (String userName)
specifier|public
name|void
name|setUserName
parameter_list|(
name|String
name|userName
parameter_list|)
block|{
name|setUsername
argument_list|(
name|userName
argument_list|)
expr_stmt|;
block|}
comment|/**      * Enables or disables whether a Spring {@link SingleConnectionFactory} will      * be used so that when messages are sent to ActiveMQ from outside of a      * message consuming thread, pooling will be used rather than the default      * with the Spring {@link JmsTemplate} which will create a new connection,      * session, producer for each message then close them all down again.      *<p/>      * The default value is false and a pooled connection is used by default.      */
DECL|method|setUseSingleConnection (boolean useSingleConnection)
specifier|public
name|void
name|setUseSingleConnection
parameter_list|(
name|boolean
name|useSingleConnection
parameter_list|)
block|{
name|this
operator|.
name|useSingleConnection
operator|=
name|useSingleConnection
expr_stmt|;
block|}
DECL|method|isUsePooledConnection ()
specifier|public
name|boolean
name|isUsePooledConnection
parameter_list|()
block|{
return|return
name|usePooledConnection
return|;
block|}
comment|/**      * Enables or disables whether a PooledConnectionFactory will be used so      * that when messages are sent to ActiveMQ from outside of a message      * consuming thread, pooling will be used rather than the default with the      * Spring {@link JmsTemplate} which will create a new connection, session,      * producer for each message then close them all down again.      *<p/>      * The default value is true.      */
DECL|method|setUsePooledConnection (boolean usePooledConnection)
specifier|public
name|void
name|setUsePooledConnection
parameter_list|(
name|boolean
name|usePooledConnection
parameter_list|)
block|{
name|this
operator|.
name|usePooledConnection
operator|=
name|usePooledConnection
expr_stmt|;
block|}
DECL|method|isTrustAllPackages ()
specifier|public
name|boolean
name|isTrustAllPackages
parameter_list|()
block|{
return|return
name|trustAllPackages
return|;
block|}
comment|/**      * ObjectMessage objects depend on Java serialization of marshal/unmarshal      * object payload. This process is generally considered unsafe as malicious      * payload can exploit the host system. That's why starting with versions      * 5.12.2 and 5.13.0, ActiveMQ enforces users to explicitly whitelist      * packages that can be exchanged using ObjectMessages.<br/>      * This option can be set to<tt>true</tt> to trust all packages (eg      * whitelist is *).      *<p/>      * See more details at: http://activemq.apache.org/objectmessage.html      */
DECL|method|setTrustAllPackages (boolean trustAllPackages)
specifier|public
name|void
name|setTrustAllPackages
parameter_list|(
name|boolean
name|trustAllPackages
parameter_list|)
block|{
name|this
operator|.
name|trustAllPackages
operator|=
name|trustAllPackages
expr_stmt|;
block|}
comment|/**      * Factory method to create a default transaction manager if one is not      * specified      */
annotation|@
name|Override
DECL|method|createTransactionManager ()
specifier|protected
name|PlatformTransactionManager
name|createTransactionManager
parameter_list|()
block|{
name|JmsTransactionManager
name|answer
init|=
operator|new
name|JmsTransactionManager
argument_list|(
name|getOrCreateConnectionFactory
argument_list|()
argument_list|)
decl_stmt|;
name|answer
operator|.
name|afterPropertiesSet
argument_list|()
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|setActiveMQComponent (ActiveMQComponent activeMQComponent)
specifier|protected
name|void
name|setActiveMQComponent
parameter_list|(
name|ActiveMQComponent
name|activeMQComponent
parameter_list|)
block|{
name|this
operator|.
name|activeMQComponent
operator|=
name|activeMQComponent
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setConnectionFactory (ConnectionFactory connectionFactory)
specifier|public
name|void
name|setConnectionFactory
parameter_list|(
name|ConnectionFactory
name|connectionFactory
parameter_list|)
block|{
if|if
condition|(
name|customBrokerURL
condition|)
block|{
comment|// okay a custom broker url was configured which we want to ensure
comment|// the real target connection factory knows about
name|ConnectionFactory
name|target
init|=
name|connectionFactory
decl_stmt|;
if|if
condition|(
name|target
operator|instanceof
name|CachingConnectionFactory
condition|)
block|{
name|CachingConnectionFactory
name|ccf
init|=
operator|(
name|CachingConnectionFactory
operator|)
name|target
decl_stmt|;
name|target
operator|=
name|ccf
operator|.
name|getTargetConnectionFactory
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|target
operator|instanceof
name|DelegatingConnectionFactory
condition|)
block|{
name|DelegatingConnectionFactory
name|dcf
init|=
operator|(
name|DelegatingConnectionFactory
operator|)
name|target
decl_stmt|;
name|target
operator|=
name|dcf
operator|.
name|getTargetConnectionFactory
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|target
operator|instanceof
name|ActiveMQConnectionFactory
condition|)
block|{
name|ActiveMQConnectionFactory
name|acf
init|=
operator|(
name|ActiveMQConnectionFactory
operator|)
name|target
decl_stmt|;
name|acf
operator|.
name|setBrokerURL
argument_list|(
name|brokerURL
argument_list|)
expr_stmt|;
block|}
block|}
name|super
operator|.
name|setConnectionFactory
argument_list|(
name|connectionFactory
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createConnectionFactory ()
specifier|protected
name|ConnectionFactory
name|createConnectionFactory
parameter_list|()
block|{
name|org
operator|.
name|apache
operator|.
name|activemq
operator|.
name|spring
operator|.
name|ActiveMQConnectionFactory
name|answer
init|=
operator|new
name|org
operator|.
name|apache
operator|.
name|activemq
operator|.
name|spring
operator|.
name|ActiveMQConnectionFactory
argument_list|()
decl_stmt|;
name|answer
operator|.
name|setTrustAllPackages
argument_list|(
name|trustAllPackages
argument_list|)
expr_stmt|;
if|if
condition|(
name|getUsername
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setUserName
argument_list|(
name|getUsername
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getPassword
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setPassword
argument_list|(
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|answer
operator|.
name|getBeanName
argument_list|()
operator|==
literal|null
condition|)
block|{
name|answer
operator|.
name|setBeanName
argument_list|(
literal|"Camel"
argument_list|)
expr_stmt|;
block|}
name|answer
operator|.
name|setBrokerURL
argument_list|(
name|getBrokerURL
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|isUseSingleConnection
argument_list|()
condition|)
block|{
name|SingleConnectionFactory
name|scf
init|=
operator|new
name|SingleConnectionFactory
argument_list|(
name|answer
argument_list|)
decl_stmt|;
if|if
condition|(
name|activeMQComponent
operator|!=
literal|null
condition|)
block|{
name|activeMQComponent
operator|.
name|addSingleConnectionFactory
argument_list|(
name|scf
argument_list|)
expr_stmt|;
block|}
return|return
name|scf
return|;
block|}
elseif|else
if|if
condition|(
name|isUsePooledConnection
argument_list|()
condition|)
block|{
name|ConnectionFactory
name|pcf
init|=
name|createPooledConnectionFactory
argument_list|(
name|answer
argument_list|)
decl_stmt|;
if|if
condition|(
name|activeMQComponent
operator|!=
literal|null
condition|)
block|{
name|activeMQComponent
operator|.
name|addPooledConnectionFactoryService
argument_list|(
operator|(
name|Service
operator|)
name|pcf
argument_list|)
expr_stmt|;
block|}
return|return
name|pcf
return|;
block|}
else|else
block|{
return|return
name|answer
return|;
block|}
block|}
DECL|method|createPooledConnectionFactory (ActiveMQConnectionFactory connectionFactory)
specifier|protected
name|ConnectionFactory
name|createPooledConnectionFactory
parameter_list|(
name|ActiveMQConnectionFactory
name|connectionFactory
parameter_list|)
block|{
try|try
block|{
name|Class
name|type
init|=
name|loadClass
argument_list|(
literal|"org.apache.activemq.pool.PooledConnectionFactory"
argument_list|,
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|)
decl_stmt|;
name|Constructor
name|constructor
init|=
name|type
operator|.
name|getConstructor
argument_list|(
name|org
operator|.
name|apache
operator|.
name|activemq
operator|.
name|ActiveMQConnectionFactory
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
operator|(
name|ConnectionFactory
operator|)
name|constructor
operator|.
name|newInstance
argument_list|(
name|connectionFactory
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Failed to instantiate PooledConnectionFactory: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|loadClass (String name, ClassLoader loader)
specifier|public
specifier|static
name|Class
argument_list|<
name|?
argument_list|>
name|loadClass
parameter_list|(
name|String
name|name
parameter_list|,
name|ClassLoader
name|loader
parameter_list|)
throws|throws
name|ClassNotFoundException
block|{
name|ClassLoader
name|contextClassLoader
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
if|if
condition|(
name|contextClassLoader
operator|!=
literal|null
condition|)
block|{
try|try
block|{
return|return
name|contextClassLoader
operator|.
name|loadClass
argument_list|(
name|name
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
try|try
block|{
return|return
name|loader
operator|.
name|loadClass
argument_list|(
name|name
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e1
parameter_list|)
block|{
throw|throw
name|e1
throw|;
block|}
block|}
block|}
else|else
block|{
return|return
name|loader
operator|.
name|loadClass
argument_list|(
name|name
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

