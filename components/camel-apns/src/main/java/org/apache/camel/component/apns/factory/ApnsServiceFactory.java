begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.apns.factory
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|apns
operator|.
name|factory
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileNotFoundException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|SSLContext
import|;
end_import

begin_import
import|import
name|com
operator|.
name|notnoop
operator|.
name|apns
operator|.
name|APNS
import|;
end_import

begin_import
import|import
name|com
operator|.
name|notnoop
operator|.
name|apns
operator|.
name|ApnsDelegate
import|;
end_import

begin_import
import|import
name|com
operator|.
name|notnoop
operator|.
name|apns
operator|.
name|ApnsService
import|;
end_import

begin_import
import|import
name|com
operator|.
name|notnoop
operator|.
name|apns
operator|.
name|ApnsServiceBuilder
import|;
end_import

begin_import
import|import
name|com
operator|.
name|notnoop
operator|.
name|apns
operator|.
name|ReconnectPolicy
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
name|CamelContext
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
name|CamelContextAware
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
name|apns
operator|.
name|model
operator|.
name|ConnectionStrategy
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
name|apns
operator|.
name|model
operator|.
name|ReconnectionPolicy
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
name|apns
operator|.
name|util
operator|.
name|AssertUtils
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
name|apns
operator|.
name|util
operator|.
name|ParamUtils
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
name|apns
operator|.
name|util
operator|.
name|ResourceUtils
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

begin_class
DECL|class|ApnsServiceFactory
specifier|public
class|class
name|ApnsServiceFactory
implements|implements
name|CamelContextAware
block|{
DECL|field|DEFAULT_POOL_SIZE
specifier|private
specifier|static
specifier|final
name|int
name|DEFAULT_POOL_SIZE
init|=
literal|10
decl_stmt|;
DECL|field|MIN_POOL_SIZE
specifier|private
specifier|static
specifier|final
name|int
name|MIN_POOL_SIZE
init|=
literal|1
decl_stmt|;
DECL|field|MAX_POOL_SIZE
specifier|private
specifier|static
specifier|final
name|int
name|MAX_POOL_SIZE
init|=
literal|30
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|certificatePath
specifier|private
name|String
name|certificatePath
decl_stmt|;
DECL|field|certificatePassword
specifier|private
name|String
name|certificatePassword
decl_stmt|;
DECL|field|connectionStrategy
specifier|private
name|ConnectionStrategy
name|connectionStrategy
decl_stmt|;
DECL|field|reconnectionPolicy
specifier|private
name|ReconnectionPolicy
name|reconnectionPolicy
decl_stmt|;
DECL|field|sslContext
specifier|private
name|SSLContext
name|sslContext
decl_stmt|;
DECL|field|poolSize
specifier|private
name|int
name|poolSize
init|=
name|DEFAULT_POOL_SIZE
decl_stmt|;
DECL|field|gatewayHost
specifier|private
name|String
name|gatewayHost
decl_stmt|;
DECL|field|gatewayPort
specifier|private
name|int
name|gatewayPort
decl_stmt|;
DECL|field|feedbackHost
specifier|private
name|String
name|feedbackHost
decl_stmt|;
DECL|field|feedbackPort
specifier|private
name|int
name|feedbackPort
decl_stmt|;
DECL|field|apnsDelegate
specifier|private
name|ApnsDelegate
name|apnsDelegate
decl_stmt|;
DECL|method|ApnsServiceFactory ()
specifier|public
name|ApnsServiceFactory
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
DECL|method|ApnsServiceFactory (CamelContext camelContext)
specifier|public
name|ApnsServiceFactory
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
block|}
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
block|}
DECL|method|getFeedbackHost ()
specifier|public
name|String
name|getFeedbackHost
parameter_list|()
block|{
return|return
name|feedbackHost
return|;
block|}
DECL|method|setFeedbackHost (String feedbackHost)
specifier|public
name|void
name|setFeedbackHost
parameter_list|(
name|String
name|feedbackHost
parameter_list|)
block|{
name|this
operator|.
name|feedbackHost
operator|=
name|feedbackHost
expr_stmt|;
block|}
DECL|method|getGatewayHost ()
specifier|public
name|String
name|getGatewayHost
parameter_list|()
block|{
return|return
name|gatewayHost
return|;
block|}
DECL|method|setGatewayHost (String gatewayHost)
specifier|public
name|void
name|setGatewayHost
parameter_list|(
name|String
name|gatewayHost
parameter_list|)
block|{
name|this
operator|.
name|gatewayHost
operator|=
name|gatewayHost
expr_stmt|;
block|}
DECL|method|getGatewayPort ()
specifier|public
name|int
name|getGatewayPort
parameter_list|()
block|{
return|return
name|gatewayPort
return|;
block|}
DECL|method|setGatewayPort (int gatewayPort)
specifier|public
name|void
name|setGatewayPort
parameter_list|(
name|int
name|gatewayPort
parameter_list|)
block|{
name|this
operator|.
name|gatewayPort
operator|=
name|gatewayPort
expr_stmt|;
block|}
DECL|method|getFeedbackPort ()
specifier|public
name|int
name|getFeedbackPort
parameter_list|()
block|{
return|return
name|feedbackPort
return|;
block|}
DECL|method|setFeedbackPort (int feedbackPort)
specifier|public
name|void
name|setFeedbackPort
parameter_list|(
name|int
name|feedbackPort
parameter_list|)
block|{
name|this
operator|.
name|feedbackPort
operator|=
name|feedbackPort
expr_stmt|;
block|}
DECL|method|getPoolSize ()
specifier|public
name|int
name|getPoolSize
parameter_list|()
block|{
return|return
name|poolSize
return|;
block|}
DECL|method|setPoolSize (int poolSize)
specifier|public
name|void
name|setPoolSize
parameter_list|(
name|int
name|poolSize
parameter_list|)
block|{
name|this
operator|.
name|poolSize
operator|=
name|poolSize
expr_stmt|;
block|}
DECL|method|getCertificatePath ()
specifier|public
name|String
name|getCertificatePath
parameter_list|()
block|{
return|return
name|certificatePath
return|;
block|}
DECL|method|setCertificatePath (String certificatePath)
specifier|public
name|void
name|setCertificatePath
parameter_list|(
name|String
name|certificatePath
parameter_list|)
block|{
name|this
operator|.
name|certificatePath
operator|=
name|certificatePath
expr_stmt|;
block|}
DECL|method|getCertificatePassword ()
specifier|public
name|String
name|getCertificatePassword
parameter_list|()
block|{
return|return
name|certificatePassword
return|;
block|}
DECL|method|setCertificatePassword (String certificatePassword)
specifier|public
name|void
name|setCertificatePassword
parameter_list|(
name|String
name|certificatePassword
parameter_list|)
block|{
name|this
operator|.
name|certificatePassword
operator|=
name|certificatePassword
expr_stmt|;
block|}
DECL|method|getReconnectionPolicy ()
specifier|public
name|ReconnectionPolicy
name|getReconnectionPolicy
parameter_list|()
block|{
return|return
name|reconnectionPolicy
return|;
block|}
DECL|method|setReconnectionPolicy (ReconnectionPolicy reconnectionPolicy)
specifier|public
name|void
name|setReconnectionPolicy
parameter_list|(
name|ReconnectionPolicy
name|reconnectionPolicy
parameter_list|)
block|{
name|this
operator|.
name|reconnectionPolicy
operator|=
name|reconnectionPolicy
expr_stmt|;
block|}
DECL|method|getConnectionStrategy ()
specifier|public
name|ConnectionStrategy
name|getConnectionStrategy
parameter_list|()
block|{
return|return
name|connectionStrategy
return|;
block|}
DECL|method|setConnectionStrategy (ConnectionStrategy connectionStrategy)
specifier|public
name|void
name|setConnectionStrategy
parameter_list|(
name|ConnectionStrategy
name|connectionStrategy
parameter_list|)
block|{
name|this
operator|.
name|connectionStrategy
operator|=
name|connectionStrategy
expr_stmt|;
block|}
DECL|method|getSslContext ()
specifier|public
name|SSLContext
name|getSslContext
parameter_list|()
block|{
return|return
name|sslContext
return|;
block|}
DECL|method|setSslContext (SSLContext sslContext)
specifier|public
name|void
name|setSslContext
parameter_list|(
name|SSLContext
name|sslContext
parameter_list|)
block|{
name|this
operator|.
name|sslContext
operator|=
name|sslContext
expr_stmt|;
block|}
DECL|method|getApnsDelegate ()
specifier|public
name|ApnsDelegate
name|getApnsDelegate
parameter_list|()
block|{
return|return
name|apnsDelegate
return|;
block|}
DECL|method|getApnsService ()
specifier|public
name|ApnsService
name|getApnsService
parameter_list|()
block|{
name|ApnsServiceBuilder
name|builder
init|=
name|APNS
operator|.
name|newService
argument_list|()
decl_stmt|;
name|configureConnectionStrategy
argument_list|(
name|builder
argument_list|)
expr_stmt|;
name|configureReconnectionPolicy
argument_list|(
name|builder
argument_list|)
expr_stmt|;
name|configureApnsDelegate
argument_list|(
name|builder
argument_list|)
expr_stmt|;
name|configureApnsDestinations
argument_list|(
name|builder
argument_list|)
expr_stmt|;
try|try
block|{
name|configureApnsCertificate
argument_list|(
name|builder
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|FileNotFoundException
name|e
parameter_list|)
block|{
throw|throw
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|ApnsService
name|apnsService
init|=
name|builder
operator|.
name|build
argument_list|()
decl_stmt|;
return|return
name|apnsService
return|;
block|}
DECL|method|configureApnsCertificate (ApnsServiceBuilder builder)
specifier|private
name|void
name|configureApnsCertificate
parameter_list|(
name|ApnsServiceBuilder
name|builder
parameter_list|)
throws|throws
name|FileNotFoundException
block|{
if|if
condition|(
name|getSslContext
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|withSSLContext
argument_list|(
name|getSslContext
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
literal|"camelContext"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notEmpty
argument_list|(
name|getCertificatePath
argument_list|()
argument_list|,
literal|"certificatePath"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notEmpty
argument_list|(
name|getCertificatePassword
argument_list|()
argument_list|,
literal|"certificatePassword"
argument_list|)
expr_stmt|;
name|InputStream
name|certificateInputStream
init|=
literal|null
decl_stmt|;
try|try
block|{
name|certificateInputStream
operator|=
name|camelContext
operator|.
name|getClassResolver
argument_list|()
operator|.
name|loadResourceAsStream
argument_list|(
name|getCertificatePath
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|certificateInputStream
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|FileNotFoundException
argument_list|(
literal|"Cannot load "
operator|+
name|getCertificatePath
argument_list|()
operator|+
literal|" from classpath"
argument_list|)
throw|;
block|}
name|builder
operator|.
name|withCert
argument_list|(
name|certificateInputStream
argument_list|,
name|getCertificatePassword
argument_list|()
argument_list|)
operator|.
name|withProductionDestination
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|ResourceUtils
operator|.
name|close
argument_list|(
name|certificateInputStream
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|configureApnsDestinations (ApnsServiceBuilder builder)
specifier|private
name|void
name|configureApnsDestinations
parameter_list|(
name|ApnsServiceBuilder
name|builder
parameter_list|)
block|{
name|ParamUtils
operator|.
name|checkDestination
argument_list|(
name|getGatewayHost
argument_list|()
argument_list|,
name|getGatewayPort
argument_list|()
argument_list|,
literal|"gateway"
argument_list|)
expr_stmt|;
name|ParamUtils
operator|.
name|checkDestination
argument_list|(
name|getFeedbackHost
argument_list|()
argument_list|,
name|getFeedbackPort
argument_list|()
argument_list|,
literal|"feedback"
argument_list|)
expr_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|getGatewayHost
argument_list|()
argument_list|)
condition|)
block|{
name|builder
operator|.
name|withGatewayDestination
argument_list|(
name|getGatewayHost
argument_list|()
argument_list|,
name|getGatewayPort
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|getFeedbackHost
argument_list|()
argument_list|)
condition|)
block|{
name|builder
operator|.
name|withFeedbackDestination
argument_list|(
name|getFeedbackHost
argument_list|()
argument_list|,
name|getFeedbackPort
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|configureApnsDelegate (ApnsServiceBuilder builder)
specifier|private
name|void
name|configureApnsDelegate
parameter_list|(
name|ApnsServiceBuilder
name|builder
parameter_list|)
block|{
if|if
condition|(
name|apnsDelegate
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|withDelegate
argument_list|(
name|apnsDelegate
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|configureConnectionStrategy (ApnsServiceBuilder builder)
specifier|private
name|void
name|configureConnectionStrategy
parameter_list|(
name|ApnsServiceBuilder
name|builder
parameter_list|)
block|{
if|if
condition|(
name|getConnectionStrategy
argument_list|()
operator|==
name|ConnectionStrategy
operator|.
name|POOL
condition|)
block|{
name|AssertUtils
operator|.
name|isTrue
argument_list|(
name|poolSize
operator|>=
name|MIN_POOL_SIZE
argument_list|,
literal|"Pool size needs to be greater than: "
operator|+
name|MIN_POOL_SIZE
argument_list|)
expr_stmt|;
name|AssertUtils
operator|.
name|isTrue
argument_list|(
name|poolSize
operator|<=
name|MAX_POOL_SIZE
argument_list|,
literal|"Pool size needs to be lower than: "
operator|+
name|MAX_POOL_SIZE
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getConnectionStrategy
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return;
block|}
switch|switch
condition|(
name|getConnectionStrategy
argument_list|()
condition|)
block|{
case|case
name|NON_BLOCKING
case|:
name|builder
operator|.
name|asNonBlocking
argument_list|()
expr_stmt|;
break|break;
case|case
name|QUEUE
case|:
name|builder
operator|.
name|asQueued
argument_list|()
expr_stmt|;
break|break;
case|case
name|POOL
case|:
name|builder
operator|.
name|asPool
argument_list|(
name|getPoolSize
argument_list|()
argument_list|)
expr_stmt|;
break|break;
default|default:
break|break;
block|}
block|}
DECL|method|configureReconnectionPolicy (ApnsServiceBuilder builder)
specifier|private
name|void
name|configureReconnectionPolicy
parameter_list|(
name|ApnsServiceBuilder
name|builder
parameter_list|)
block|{
if|if
condition|(
name|getReconnectionPolicy
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return;
block|}
switch|switch
condition|(
name|getReconnectionPolicy
argument_list|()
condition|)
block|{
case|case
name|EVERY_HALF_HOUR
case|:
name|builder
operator|.
name|withReconnectPolicy
argument_list|(
name|ReconnectPolicy
operator|.
name|Provided
operator|.
name|EVERY_HALF_HOUR
argument_list|)
expr_stmt|;
break|break;
case|case
name|EVERY_NOTIFICATION
case|:
name|builder
operator|.
name|withReconnectPolicy
argument_list|(
name|ReconnectPolicy
operator|.
name|Provided
operator|.
name|EVERY_NOTIFICATION
argument_list|)
expr_stmt|;
break|break;
default|default:
name|builder
operator|.
name|withReconnectPolicy
argument_list|(
name|ReconnectPolicy
operator|.
name|Provided
operator|.
name|NEVER
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
end_class

end_unit

