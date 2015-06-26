begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jcr
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jcr
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
name|net
operator|.
name|URISyntaxException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jcr
operator|.
name|Credentials
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jcr
operator|.
name|Repository
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jcr
operator|.
name|SimpleCredentials
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
name|impl
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * A JCR endpoint  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"jcr"
argument_list|,
name|title
operator|=
literal|"JCR"
argument_list|,
name|syntax
operator|=
literal|"jcr:host/base"
argument_list|,
name|consumerClass
operator|=
name|JcrConsumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"cms,database"
argument_list|)
DECL|class|JcrEndpoint
specifier|public
class|class
name|JcrEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|field|credentials
specifier|private
name|Credentials
name|credentials
decl_stmt|;
DECL|field|repository
specifier|private
name|Repository
name|repository
decl_stmt|;
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|host
specifier|private
name|String
name|host
decl_stmt|;
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|base
specifier|private
name|String
name|base
decl_stmt|;
annotation|@
name|UriParam
DECL|field|username
specifier|private
name|String
name|username
decl_stmt|;
annotation|@
name|UriParam
DECL|field|password
specifier|private
name|String
name|password
decl_stmt|;
annotation|@
name|UriParam
DECL|field|eventTypes
specifier|private
name|int
name|eventTypes
decl_stmt|;
annotation|@
name|UriParam
DECL|field|deep
specifier|private
name|boolean
name|deep
decl_stmt|;
annotation|@
name|UriParam
DECL|field|uuids
specifier|private
name|String
name|uuids
decl_stmt|;
annotation|@
name|UriParam
DECL|field|nodeTypeNames
specifier|private
name|String
name|nodeTypeNames
decl_stmt|;
annotation|@
name|UriParam
DECL|field|noLocal
specifier|private
name|boolean
name|noLocal
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"3000"
argument_list|)
DECL|field|sessionLiveCheckIntervalOnStart
specifier|private
name|long
name|sessionLiveCheckIntervalOnStart
init|=
literal|3000L
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"60000"
argument_list|)
DECL|field|sessionLiveCheckInterval
specifier|private
name|long
name|sessionLiveCheckInterval
init|=
literal|60000L
decl_stmt|;
annotation|@
name|UriParam
DECL|field|workspaceName
specifier|private
name|String
name|workspaceName
decl_stmt|;
DECL|method|JcrEndpoint (String endpointUri, JcrComponent component)
specifier|protected
name|JcrEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|JcrComponent
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
try|try
block|{
name|URI
name|uri
init|=
operator|new
name|URI
argument_list|(
name|endpointUri
argument_list|)
decl_stmt|;
if|if
condition|(
name|uri
operator|.
name|getUserInfo
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|String
index|[]
name|creds
init|=
name|uri
operator|.
name|getUserInfo
argument_list|()
operator|.
name|split
argument_list|(
literal|":"
argument_list|)
decl_stmt|;
name|this
operator|.
name|username
operator|=
name|creds
index|[
literal|0
index|]
expr_stmt|;
name|this
operator|.
name|password
operator|=
name|creds
operator|.
name|length
operator|>
literal|1
condition|?
name|creds
index|[
literal|1
index|]
else|:
literal|""
expr_stmt|;
block|}
name|this
operator|.
name|host
operator|=
name|uri
operator|.
name|getHost
argument_list|()
expr_stmt|;
name|this
operator|.
name|base
operator|=
name|uri
operator|.
name|getPath
argument_list|()
operator|.
name|replaceAll
argument_list|(
literal|"^/"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid URI: "
operator|+
name|endpointUri
argument_list|,
name|e
argument_list|)
throw|;
block|}
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
name|JcrConsumer
name|answer
init|=
operator|new
name|JcrConsumer
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
name|JcrProducer
argument_list|(
name|this
argument_list|)
return|;
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
name|ObjectHelper
operator|.
name|notEmpty
argument_list|(
name|host
argument_list|,
literal|"host"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|this
operator|.
name|repository
operator|=
name|getCamelContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByNameAndType
argument_list|(
name|host
argument_list|,
name|Repository
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|repository
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"No JCR repository defined under '"
operator|+
name|host
operator|+
literal|"'"
argument_list|)
throw|;
block|}
if|if
condition|(
name|username
operator|!=
literal|null
operator|&&
name|password
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|credentials
operator|=
operator|new
name|SimpleCredentials
argument_list|(
name|username
argument_list|,
name|password
operator|.
name|toCharArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getHost ()
specifier|public
name|String
name|getHost
parameter_list|()
block|{
return|return
name|host
return|;
block|}
comment|/**      * Name of the {@link javax.jcr.Repository} to lookup from the Camel registry to be used.      */
DECL|method|setHost (String host)
specifier|public
name|void
name|setHost
parameter_list|(
name|String
name|host
parameter_list|)
block|{
name|this
operator|.
name|host
operator|=
name|host
expr_stmt|;
block|}
comment|/**      * Get the {@link Repository}      *       * @return the repository      */
DECL|method|getRepository ()
specifier|protected
name|Repository
name|getRepository
parameter_list|()
block|{
return|return
name|repository
return|;
block|}
comment|/**      * Get the {@link Credentials} for establishing the JCR repository connection      *       * @return the credentials      */
DECL|method|getCredentials ()
specifier|protected
name|Credentials
name|getCredentials
parameter_list|()
block|{
return|return
name|credentials
return|;
block|}
comment|/**      * Get the base node when accessing the repository      *       * @return the base node      */
DECL|method|getBase ()
specifier|protected
name|String
name|getBase
parameter_list|()
block|{
return|return
name|base
return|;
block|}
DECL|method|setBase (String base)
specifier|public
name|void
name|setBase
parameter_list|(
name|String
name|base
parameter_list|)
block|{
name|this
operator|.
name|base
operator|=
name|base
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
comment|/**      * Username for login      */
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
comment|/**      * Password for login      */
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
comment|/**      *<code>eventTypes</code> (a combination of one or more event types encoded      * as a bit mask value such as javax.jcr.observation.Event.NODE_ADDED, javax.jcr.observation.Event.NODE_REMOVED, etc.).      *       * @return eventTypes      * @see {@link javax.jcr.observation.Event}      * @see {@link javax.jcr.observation.ObservationManager#addEventListener(javax.jcr.observation.EventListener, int, String, boolean, String[], String[], boolean)}      */
DECL|method|getEventTypes ()
specifier|public
name|int
name|getEventTypes
parameter_list|()
block|{
return|return
name|eventTypes
return|;
block|}
DECL|method|setEventTypes (int eventTypes)
specifier|public
name|void
name|setEventTypes
parameter_list|(
name|int
name|eventTypes
parameter_list|)
block|{
name|this
operator|.
name|eventTypes
operator|=
name|eventTypes
expr_stmt|;
block|}
comment|/**      * When<code>isDeep</code> is true, events whose associated parent node is at      *<code>absPath</code> or within its subgraph are received.      * @return deep      */
DECL|method|isDeep ()
specifier|public
name|boolean
name|isDeep
parameter_list|()
block|{
return|return
name|deep
return|;
block|}
DECL|method|setDeep (boolean deep)
specifier|public
name|void
name|setDeep
parameter_list|(
name|boolean
name|deep
parameter_list|)
block|{
name|this
operator|.
name|deep
operator|=
name|deep
expr_stmt|;
block|}
comment|/**      * When a comma separated uuid list string is set, only events whose associated parent node has one of      * the identifiers in the comma separated uuid list will be received.      * @return comma separated uuid list string      */
DECL|method|getUuids ()
specifier|public
name|String
name|getUuids
parameter_list|()
block|{
return|return
name|uuids
return|;
block|}
DECL|method|setUuids (String uuids)
specifier|public
name|void
name|setUuids
parameter_list|(
name|String
name|uuids
parameter_list|)
block|{
name|this
operator|.
name|uuids
operator|=
name|uuids
expr_stmt|;
block|}
comment|/**      * When a comma separated<code>nodeTypeName</code> list string is set, only events whose associated parent node has      * one of the node types (or a subtype of one of the node types) in this      * list will be received.      */
DECL|method|getNodeTypeNames ()
specifier|public
name|String
name|getNodeTypeNames
parameter_list|()
block|{
return|return
name|nodeTypeNames
return|;
block|}
DECL|method|setNodeTypeNames (String nodeTypeNames)
specifier|public
name|void
name|setNodeTypeNames
parameter_list|(
name|String
name|nodeTypeNames
parameter_list|)
block|{
name|this
operator|.
name|nodeTypeNames
operator|=
name|nodeTypeNames
expr_stmt|;
block|}
comment|/**      * If<code>noLocal</code> is<code>true</code>, then events      * generated by the session through which the listener was registered are      * ignored. Otherwise, they are not ignored.      * @return noLocal      */
DECL|method|isNoLocal ()
specifier|public
name|boolean
name|isNoLocal
parameter_list|()
block|{
return|return
name|noLocal
return|;
block|}
DECL|method|setNoLocal (boolean noLocal)
specifier|public
name|void
name|setNoLocal
parameter_list|(
name|boolean
name|noLocal
parameter_list|)
block|{
name|this
operator|.
name|noLocal
operator|=
name|noLocal
expr_stmt|;
block|}
comment|/**      * Interval in milliseconds to wait before the first session live checking.      * The default value is 3000 ms.      *       * @return sessionLiveCheckIntervalOnStart      */
DECL|method|getSessionLiveCheckIntervalOnStart ()
specifier|public
name|long
name|getSessionLiveCheckIntervalOnStart
parameter_list|()
block|{
return|return
name|sessionLiveCheckIntervalOnStart
return|;
block|}
DECL|method|setSessionLiveCheckIntervalOnStart (long sessionLiveCheckIntervalOnStart)
specifier|public
name|void
name|setSessionLiveCheckIntervalOnStart
parameter_list|(
name|long
name|sessionLiveCheckIntervalOnStart
parameter_list|)
block|{
if|if
condition|(
name|sessionLiveCheckIntervalOnStart
operator|<=
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"sessionLiveCheckIntervalOnStart must be positive value"
argument_list|)
throw|;
block|}
name|this
operator|.
name|sessionLiveCheckIntervalOnStart
operator|=
name|sessionLiveCheckIntervalOnStart
expr_stmt|;
block|}
comment|/**      * Interval in milliseconds to wait before each session live checking      * The default value is 60000 ms.      */
DECL|method|getSessionLiveCheckInterval ()
specifier|public
name|long
name|getSessionLiveCheckInterval
parameter_list|()
block|{
return|return
name|sessionLiveCheckInterval
return|;
block|}
DECL|method|setSessionLiveCheckInterval (long sessionLiveCheckInterval)
specifier|public
name|void
name|setSessionLiveCheckInterval
parameter_list|(
name|long
name|sessionLiveCheckInterval
parameter_list|)
block|{
if|if
condition|(
name|sessionLiveCheckInterval
operator|<=
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"sessionLiveCheckInterval must be positive value"
argument_list|)
throw|;
block|}
name|this
operator|.
name|sessionLiveCheckInterval
operator|=
name|sessionLiveCheckInterval
expr_stmt|;
block|}
comment|/**      * The workspace to access. If it's not specified then the default one will be used      */
DECL|method|getWorkspaceName ()
specifier|public
name|String
name|getWorkspaceName
parameter_list|()
block|{
return|return
name|workspaceName
return|;
block|}
DECL|method|setWorkspaceName (String workspaceName)
specifier|public
name|void
name|setWorkspaceName
parameter_list|(
name|String
name|workspaceName
parameter_list|)
block|{
name|this
operator|.
name|workspaceName
operator|=
name|workspaceName
expr_stmt|;
block|}
comment|/**      * Gets the destination name which was configured from the endpoint uri.      *      * @return the destination name resolved from the endpoint uri      */
DECL|method|getEndpointConfiguredDestinationName ()
specifier|public
name|String
name|getEndpointConfiguredDestinationName
parameter_list|()
block|{
name|String
name|remainder
init|=
name|ObjectHelper
operator|.
name|after
argument_list|(
name|getEndpointKey
argument_list|()
argument_list|,
literal|"//"
argument_list|)
decl_stmt|;
if|if
condition|(
name|remainder
operator|!=
literal|null
operator|&&
name|remainder
operator|.
name|contains
argument_list|(
literal|"@"
argument_list|)
condition|)
block|{
name|remainder
operator|=
name|remainder
operator|.
name|substring
argument_list|(
name|remainder
operator|.
name|indexOf
argument_list|(
literal|'@'
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|remainder
operator|!=
literal|null
operator|&&
name|remainder
operator|.
name|contains
argument_list|(
literal|"?"
argument_list|)
condition|)
block|{
comment|// remove parameters
name|remainder
operator|=
name|ObjectHelper
operator|.
name|before
argument_list|(
name|remainder
argument_list|,
literal|"?"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|remainder
argument_list|)
condition|)
block|{
return|return
name|remainder
return|;
block|}
return|return
name|remainder
return|;
block|}
block|}
end_class

end_unit

