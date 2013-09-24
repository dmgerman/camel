begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.splunk
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|splunk
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Callable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ExecutorService
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Future
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_import
import|import
name|com
operator|.
name|splunk
operator|.
name|Service
import|;
end_import

begin_import
import|import
name|com
operator|.
name|splunk
operator|.
name|ServiceArgs
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
DECL|class|SplunkConnectionFactory
specifier|public
class|class
name|SplunkConnectionFactory
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|SplunkConnectionFactory
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|host
specifier|private
name|String
name|host
decl_stmt|;
DECL|field|port
specifier|private
name|int
name|port
decl_stmt|;
DECL|field|scheme
specifier|private
name|String
name|scheme
decl_stmt|;
DECL|field|app
specifier|private
name|String
name|app
decl_stmt|;
DECL|field|owner
specifier|private
name|String
name|owner
decl_stmt|;
DECL|field|username
specifier|private
name|String
name|username
decl_stmt|;
DECL|field|password
specifier|private
name|String
name|password
decl_stmt|;
DECL|field|connectionTimeout
specifier|private
name|int
name|connectionTimeout
decl_stmt|;
DECL|field|useSunHttpsHandler
specifier|private
name|boolean
name|useSunHttpsHandler
decl_stmt|;
DECL|method|SplunkConnectionFactory (final String host, final int port, final String username, final String password)
specifier|public
name|SplunkConnectionFactory
parameter_list|(
specifier|final
name|String
name|host
parameter_list|,
specifier|final
name|int
name|port
parameter_list|,
specifier|final
name|String
name|username
parameter_list|,
specifier|final
name|String
name|password
parameter_list|)
block|{
name|this
operator|.
name|host
operator|=
name|host
expr_stmt|;
name|this
operator|.
name|port
operator|=
name|port
expr_stmt|;
name|this
operator|.
name|username
operator|=
name|username
expr_stmt|;
name|this
operator|.
name|password
operator|=
name|password
expr_stmt|;
block|}
DECL|method|SplunkConnectionFactory (final String username, final String password)
specifier|public
name|SplunkConnectionFactory
parameter_list|(
specifier|final
name|String
name|username
parameter_list|,
specifier|final
name|String
name|password
parameter_list|)
block|{
name|this
argument_list|(
name|Service
operator|.
name|DEFAULT_HOST
argument_list|,
name|Service
operator|.
name|DEFAULT_PORT
argument_list|,
name|username
argument_list|,
name|password
argument_list|)
expr_stmt|;
block|}
DECL|method|getApp ()
specifier|public
name|String
name|getApp
parameter_list|()
block|{
return|return
name|app
return|;
block|}
DECL|method|setApp (String app)
specifier|public
name|void
name|setApp
parameter_list|(
name|String
name|app
parameter_list|)
block|{
name|this
operator|.
name|app
operator|=
name|app
expr_stmt|;
block|}
DECL|method|getConnectionTimeout ()
specifier|public
name|int
name|getConnectionTimeout
parameter_list|()
block|{
return|return
name|connectionTimeout
return|;
block|}
DECL|method|setConnectionTimeout (int connectionTimeout)
specifier|public
name|void
name|setConnectionTimeout
parameter_list|(
name|int
name|connectionTimeout
parameter_list|)
block|{
name|this
operator|.
name|connectionTimeout
operator|=
name|connectionTimeout
expr_stmt|;
block|}
DECL|method|getScheme ()
specifier|public
name|String
name|getScheme
parameter_list|()
block|{
return|return
name|scheme
return|;
block|}
DECL|method|setScheme (String scheme)
specifier|public
name|void
name|setScheme
parameter_list|(
name|String
name|scheme
parameter_list|)
block|{
name|this
operator|.
name|scheme
operator|=
name|scheme
expr_stmt|;
block|}
DECL|method|isUseSunHttpsHandler ()
specifier|public
name|boolean
name|isUseSunHttpsHandler
parameter_list|()
block|{
return|return
name|useSunHttpsHandler
return|;
block|}
DECL|method|setUseSunHttpsHandler (boolean useSunHttpsHandler)
specifier|public
name|void
name|setUseSunHttpsHandler
parameter_list|(
name|boolean
name|useSunHttpsHandler
parameter_list|)
block|{
name|this
operator|.
name|useSunHttpsHandler
operator|=
name|useSunHttpsHandler
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"restriction"
argument_list|)
DECL|method|createService (CamelContext camelContext)
specifier|public
name|Service
name|createService
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
specifier|final
name|ServiceArgs
name|args
init|=
operator|new
name|ServiceArgs
argument_list|()
decl_stmt|;
if|if
condition|(
name|host
operator|!=
literal|null
condition|)
block|{
name|args
operator|.
name|setHost
argument_list|(
name|host
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|port
operator|>
literal|0
condition|)
block|{
name|args
operator|.
name|setPort
argument_list|(
name|port
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|scheme
operator|!=
literal|null
condition|)
block|{
name|args
operator|.
name|setScheme
argument_list|(
name|scheme
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|app
operator|!=
literal|null
condition|)
block|{
name|args
operator|.
name|setApp
argument_list|(
name|app
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|owner
operator|!=
literal|null
condition|)
block|{
name|args
operator|.
name|setOwner
argument_list|(
name|owner
argument_list|)
expr_stmt|;
block|}
name|args
operator|.
name|setUsername
argument_list|(
name|username
argument_list|)
expr_stmt|;
name|args
operator|.
name|setPassword
argument_list|(
name|password
argument_list|)
expr_stmt|;
comment|// useful in cases where you want to bypass app. servers https handling
comment|// (wls i'm looking at you)
if|if
condition|(
name|isUseSunHttpsHandler
argument_list|()
condition|)
block|{
name|args
operator|.
name|setHTTPSHandler
argument_list|(
operator|new
name|sun
operator|.
name|net
operator|.
name|www
operator|.
name|protocol
operator|.
name|https
operator|.
name|Handler
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|ExecutorService
name|executor
init|=
name|camelContext
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newSingleThreadExecutor
argument_list|(
name|this
argument_list|,
literal|"DefaultSplunkConnectionFactory"
argument_list|)
decl_stmt|;
name|Future
argument_list|<
name|Service
argument_list|>
name|future
init|=
name|executor
operator|.
name|submit
argument_list|(
operator|new
name|Callable
argument_list|<
name|Service
argument_list|>
argument_list|()
block|{
specifier|public
name|Service
name|call
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|Service
operator|.
name|connect
argument_list|(
name|args
argument_list|)
return|;
block|}
block|}
argument_list|)
decl_stmt|;
try|try
block|{
name|Service
name|service
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|connectionTimeout
operator|>
literal|0
condition|)
block|{
name|service
operator|=
name|future
operator|.
name|get
argument_list|(
name|connectionTimeout
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|service
operator|=
name|future
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
name|LOG
operator|.
name|info
argument_list|(
literal|"Successfully connected to Splunk"
argument_list|)
expr_stmt|;
return|return
name|service
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
name|String
operator|.
name|format
argument_list|(
literal|"could not connect to Splunk Server @ %s:%d - %s"
argument_list|,
name|host
argument_list|,
name|port
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
finally|finally
block|{
if|if
condition|(
name|executor
operator|!=
literal|null
condition|)
block|{
name|camelContext
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|shutdownNow
argument_list|(
name|executor
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

