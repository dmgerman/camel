begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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
name|Endpoint
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
name|Exchange
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
name|IntrospectionSupport
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
name|URISupport
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

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|ScheduledExecutorService
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
name|ScheduledThreadPoolExecutor
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
name|ThreadFactory
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|DefaultComponent
specifier|public
class|class
name|DefaultComponent
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
extends|extends
name|ServiceSupport
implements|implements
name|Component
argument_list|<
name|E
argument_list|>
block|{
DECL|field|EMPTY_ARRAY
specifier|protected
specifier|static
name|String
index|[]
name|EMPTY_ARRAY
init|=
block|{}
decl_stmt|;
DECL|field|defaultThreadPoolSize
specifier|private
name|int
name|defaultThreadPoolSize
init|=
literal|5
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|executorService
specifier|private
name|ScheduledExecutorService
name|executorService
decl_stmt|;
DECL|method|DefaultComponent ()
specifier|public
name|DefaultComponent
parameter_list|()
block|{     }
DECL|method|DefaultComponent (CamelContext context)
specifier|public
name|DefaultComponent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|context
expr_stmt|;
block|}
DECL|method|getUriPrefixes ()
specifier|public
name|String
index|[]
name|getUriPrefixes
parameter_list|()
block|{
return|return
name|EMPTY_ARRAY
return|;
block|}
DECL|method|resolveEndpoint (String uri)
specifier|public
name|Endpoint
argument_list|<
name|E
argument_list|>
name|resolveEndpoint
parameter_list|(
name|String
name|uri
parameter_list|)
throws|throws
name|Exception
block|{
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
name|String
name|remaining
init|=
name|matchesPrefixes
argument_list|(
name|uri
argument_list|)
decl_stmt|;
if|if
condition|(
name|remaining
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|remaining
operator|.
name|startsWith
argument_list|(
literal|":"
argument_list|)
condition|)
block|{
name|remaining
operator|=
name|remaining
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
name|URI
name|u
init|=
operator|new
name|URI
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|String
name|path
init|=
name|u
operator|.
name|getHost
argument_list|()
decl_stmt|;
if|if
condition|(
name|path
operator|==
literal|null
condition|)
block|{
name|path
operator|=
name|u
operator|.
name|getSchemeSpecificPart
argument_list|()
expr_stmt|;
block|}
name|Map
name|parameters
init|=
name|URISupport
operator|.
name|parseParamters
argument_list|(
name|u
argument_list|)
decl_stmt|;
name|Endpoint
argument_list|<
name|E
argument_list|>
name|endpoint
init|=
name|createEndpoint
argument_list|(
name|uri
argument_list|,
name|path
argument_list|,
name|parameters
argument_list|)
decl_stmt|;
if|if
condition|(
name|endpoint
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|parameters
operator|!=
literal|null
condition|)
block|{
name|IntrospectionSupport
operator|.
name|setProperties
argument_list|(
name|endpoint
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
block|}
return|return
name|endpoint
return|;
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
DECL|method|setCamelContext (CamelContext context)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|context
expr_stmt|;
block|}
DECL|method|getExecutorService ()
specifier|public
name|ScheduledExecutorService
name|getExecutorService
parameter_list|()
block|{
if|if
condition|(
name|executorService
operator|==
literal|null
condition|)
block|{
name|executorService
operator|=
name|createExecutorService
argument_list|()
expr_stmt|;
block|}
return|return
name|executorService
return|;
block|}
DECL|method|setExecutorService (ScheduledExecutorService executorService)
specifier|public
name|void
name|setExecutorService
parameter_list|(
name|ScheduledExecutorService
name|executorService
parameter_list|)
block|{
name|this
operator|.
name|executorService
operator|=
name|executorService
expr_stmt|;
block|}
comment|/**      * A factory method to create a default thread pool and executor      */
DECL|method|createExecutorService ()
specifier|protected
name|ScheduledExecutorService
name|createExecutorService
parameter_list|()
block|{
return|return
operator|new
name|ScheduledThreadPoolExecutor
argument_list|(
name|defaultThreadPoolSize
argument_list|,
operator|new
name|ThreadFactory
argument_list|()
block|{
name|int
name|counter
decl_stmt|;
specifier|public
specifier|synchronized
name|Thread
name|newThread
parameter_list|(
name|Runnable
name|runnable
parameter_list|)
block|{
name|Thread
name|thread
init|=
operator|new
name|Thread
argument_list|(
name|runnable
argument_list|)
decl_stmt|;
name|thread
operator|.
name|setName
argument_list|(
literal|"Thread"
operator|+
operator|(
operator|++
name|counter
operator|)
operator|+
literal|" "
operator|+
name|DefaultComponent
operator|.
name|this
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|thread
return|;
block|}
block|}
argument_list|)
return|;
block|}
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{     }
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|executorService
operator|!=
literal|null
condition|)
block|{
name|executorService
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Returns true if the uri matches one of the available prefixes from {@link #getUriPrefixes()}      *      * @param uri the URI      * @return true if the URI matches one of the available prefixes      */
DECL|method|matchesPrefixes (String uri)
specifier|protected
name|String
name|matchesPrefixes
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|String
index|[]
name|prefixes
init|=
name|getUriPrefixes
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|prefix
range|:
name|prefixes
control|)
block|{
if|if
condition|(
name|uri
operator|.
name|startsWith
argument_list|(
name|prefix
argument_list|)
condition|)
block|{
return|return
name|uri
operator|.
name|substring
argument_list|(
name|prefix
operator|.
name|length
argument_list|()
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * A factory method allowing derived components to create a new endpoint from the given URI,      * remaining path and optional parameters      *      * @param uri        the full URI of the endpoint      * @param remaining  the remaining part of the URI without the query parameters or component prefix      * @param parameters the optional parameters passed in      * @return a newly created endpoint or null if the endpoint cannot be created based on the inputs      */
DECL|method|createEndpoint (String uri, String remaining, Map parameters)
specifier|protected
name|Endpoint
argument_list|<
name|E
argument_list|>
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

