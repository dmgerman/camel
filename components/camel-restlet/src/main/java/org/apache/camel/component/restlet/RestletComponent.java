begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.restlet
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|restlet
package|;
end_package

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
name|HashMap
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
name|impl
operator|.
name|DefaultComponent
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
name|UnsafeUriCharactersEncoder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|restlet
operator|.
name|Component
import|;
end_import

begin_import
import|import
name|org
operator|.
name|restlet
operator|.
name|Guard
import|;
end_import

begin_import
import|import
name|org
operator|.
name|restlet
operator|.
name|Restlet
import|;
end_import

begin_import
import|import
name|org
operator|.
name|restlet
operator|.
name|Server
import|;
end_import

begin_import
import|import
name|org
operator|.
name|restlet
operator|.
name|data
operator|.
name|ChallengeScheme
import|;
end_import

begin_import
import|import
name|org
operator|.
name|restlet
operator|.
name|data
operator|.
name|Protocol
import|;
end_import

begin_comment
comment|/**  * A Camel component embedded Restlet that produces and consumes exchanges.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|RestletComponent
specifier|public
class|class
name|RestletComponent
extends|extends
name|DefaultComponent
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|RestletComponent
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|servers
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Server
argument_list|>
name|servers
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Server
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|routers
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|MethodBasedRouter
argument_list|>
name|routers
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|MethodBasedRouter
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|component
specifier|private
specifier|final
name|Component
name|component
init|=
operator|new
name|Component
argument_list|()
decl_stmt|;
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|createEndpoint (String uri, String remaining, Map parameters)
specifier|protected
name|Endpoint
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
name|RestletEndpoint
name|result
init|=
operator|new
name|RestletEndpoint
argument_list|(
name|this
argument_list|,
name|remaining
argument_list|)
decl_stmt|;
name|setProperties
argument_list|(
name|result
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
comment|// construct URI so we can use it to get the splitted information
name|URI
name|u
init|=
operator|new
name|URI
argument_list|(
name|UnsafeUriCharactersEncoder
operator|.
name|encode
argument_list|(
name|remaining
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|protocol
init|=
name|u
operator|.
name|getScheme
argument_list|()
decl_stmt|;
name|String
name|uriPattern
init|=
name|u
operator|.
name|getPath
argument_list|()
decl_stmt|;
if|if
condition|(
name|parameters
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|uriPattern
operator|=
name|uriPattern
operator|+
literal|"?"
operator|+
name|URISupport
operator|.
name|createQueryString
argument_list|(
name|parameters
argument_list|)
expr_stmt|;
block|}
name|int
name|port
init|=
literal|0
decl_stmt|;
name|String
name|host
init|=
name|u
operator|.
name|getHost
argument_list|()
decl_stmt|;
if|if
condition|(
name|u
operator|.
name|getPort
argument_list|()
operator|>
literal|0
condition|)
block|{
name|port
operator|=
name|u
operator|.
name|getPort
argument_list|()
expr_stmt|;
block|}
name|result
operator|.
name|setProtocol
argument_list|(
name|protocol
argument_list|)
expr_stmt|;
name|result
operator|.
name|setUriPattern
argument_list|(
name|uriPattern
argument_list|)
expr_stmt|;
name|result
operator|.
name|setHost
argument_list|(
name|host
argument_list|)
expr_stmt|;
if|if
condition|(
name|port
operator|>
literal|0
condition|)
block|{
name|result
operator|.
name|setPort
argument_list|(
name|port
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|component
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|component
operator|.
name|stop
argument_list|()
expr_stmt|;
comment|// routers map entries are removed as consumer stops and servers map
comment|// is not touch so to keep in sync with component's servers
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|useIntrospectionOnEndpoint ()
specifier|protected
name|boolean
name|useIntrospectionOnEndpoint
parameter_list|()
block|{
comment|// we invoke setProperties ourselves so we can construct "user" uri on
comment|// on the remaining parameters
return|return
literal|false
return|;
block|}
DECL|method|connect (RestletConsumer consumer)
specifier|public
name|void
name|connect
parameter_list|(
name|RestletConsumer
name|consumer
parameter_list|)
throws|throws
name|Exception
block|{
name|RestletEndpoint
name|endpoint
init|=
operator|(
name|RestletEndpoint
operator|)
name|consumer
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|addServerIfNeccessary
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|MethodBasedRouter
name|router
init|=
name|getMethodRouter
argument_list|(
name|endpoint
operator|.
name|getUriPattern
argument_list|()
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|realm
init|=
name|endpoint
operator|.
name|getRestletRealm
argument_list|()
decl_stmt|;
name|Restlet
name|target
init|=
name|consumer
operator|.
name|getRestlet
argument_list|()
decl_stmt|;
if|if
condition|(
name|realm
operator|!=
literal|null
operator|&&
name|realm
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|Guard
name|guard
init|=
operator|new
name|Guard
argument_list|(
name|component
operator|.
name|getContext
argument_list|()
operator|.
name|createChildContext
argument_list|()
argument_list|,
name|ChallengeScheme
operator|.
name|HTTP_BASIC
argument_list|,
literal|"Camel-Restlet Endpoint Realm"
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|realm
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|guard
operator|.
name|getSecrets
argument_list|()
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|toCharArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|guard
operator|.
name|setNext
argument_list|(
name|target
argument_list|)
expr_stmt|;
name|target
operator|=
name|guard
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Target has been set to guard: "
operator|+
name|guard
argument_list|)
expr_stmt|;
block|}
block|}
name|router
operator|.
name|addRoute
argument_list|(
name|endpoint
operator|.
name|getRestletMethod
argument_list|()
argument_list|,
name|target
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|router
operator|.
name|hasBeenAttached
argument_list|()
condition|)
block|{
name|component
operator|.
name|getDefaultHost
argument_list|()
operator|.
name|attach
argument_list|(
name|endpoint
operator|.
name|getUriPattern
argument_list|()
argument_list|,
name|router
argument_list|)
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Attached methodRouter uriPattern: "
operator|+
name|endpoint
operator|.
name|getUriPattern
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Attached restlet uriPattern: "
operator|+
name|endpoint
operator|.
name|getUriPattern
argument_list|()
operator|+
literal|" method: "
operator|+
name|endpoint
operator|.
name|getRestletMethod
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|disconnect (RestletConsumer consumer)
specifier|public
name|void
name|disconnect
parameter_list|(
name|RestletConsumer
name|consumer
parameter_list|)
throws|throws
name|Exception
block|{
name|RestletEndpoint
name|endpoint
init|=
operator|(
name|RestletEndpoint
operator|)
name|consumer
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|MethodBasedRouter
name|router
init|=
name|getMethodRouter
argument_list|(
name|endpoint
operator|.
name|getUriPattern
argument_list|()
argument_list|)
decl_stmt|;
name|router
operator|.
name|removeRoute
argument_list|(
name|endpoint
operator|.
name|getRestletMethod
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Detached restlet uriPattern: "
operator|+
name|endpoint
operator|.
name|getUriPattern
argument_list|()
operator|+
literal|" method: "
operator|+
name|endpoint
operator|.
name|getRestletMethod
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getMethodRouter (String uriPattern)
specifier|private
name|MethodBasedRouter
name|getMethodRouter
parameter_list|(
name|String
name|uriPattern
parameter_list|)
block|{
synchronized|synchronized
init|(
name|routers
init|)
block|{
name|MethodBasedRouter
name|result
init|=
name|routers
operator|.
name|get
argument_list|(
name|uriPattern
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
name|result
operator|=
operator|new
name|MethodBasedRouter
argument_list|(
name|uriPattern
argument_list|)
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Added method based router: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
name|routers
operator|.
name|put
argument_list|(
name|uriPattern
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
block|}
DECL|method|addServerIfNeccessary (RestletEndpoint endpoint)
specifier|private
name|void
name|addServerIfNeccessary
parameter_list|(
name|RestletEndpoint
name|endpoint
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|key
init|=
name|buildKey
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
name|Server
name|server
decl_stmt|;
synchronized|synchronized
init|(
name|servers
init|)
block|{
name|server
operator|=
name|servers
operator|.
name|get
argument_list|(
name|key
argument_list|)
expr_stmt|;
if|if
condition|(
name|server
operator|==
literal|null
condition|)
block|{
name|server
operator|=
name|component
operator|.
name|getServers
argument_list|()
operator|.
name|add
argument_list|(
name|Protocol
operator|.
name|valueOf
argument_list|(
name|endpoint
operator|.
name|getProtocol
argument_list|()
argument_list|)
argument_list|,
name|endpoint
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|servers
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|server
argument_list|)
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Added server: "
operator|+
name|key
argument_list|)
expr_stmt|;
block|}
name|server
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
block|}
DECL|method|buildKey (RestletEndpoint endpoint)
specifier|private
specifier|static
name|String
name|buildKey
parameter_list|(
name|RestletEndpoint
name|endpoint
parameter_list|)
block|{
return|return
name|endpoint
operator|.
name|getHost
argument_list|()
operator|+
literal|":"
operator|+
name|endpoint
operator|.
name|getPort
argument_list|()
return|;
block|}
block|}
end_class

end_unit

