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
name|com
operator|.
name|splunk
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
name|UriParams
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

begin_class
annotation|@
name|UriParams
DECL|class|SplunkConfiguration
specifier|public
class|class
name|SplunkConfiguration
block|{
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"Name has no purpose"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
annotation|@
name|UriParam
DECL|field|scheme
specifier|private
name|String
name|scheme
init|=
name|Service
operator|.
name|DEFAULT_SCHEME
decl_stmt|;
annotation|@
name|UriParam
DECL|field|host
specifier|private
name|String
name|host
init|=
name|Service
operator|.
name|DEFAULT_HOST
decl_stmt|;
annotation|@
name|UriParam
DECL|field|port
specifier|private
name|int
name|port
init|=
name|Service
operator|.
name|DEFAULT_PORT
decl_stmt|;
annotation|@
name|UriParam
DECL|field|app
specifier|private
name|String
name|app
decl_stmt|;
annotation|@
name|UriParam
DECL|field|owner
specifier|private
name|String
name|owner
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
argument_list|(
name|defaultValue
operator|=
literal|"5000"
argument_list|)
DECL|field|connectionTimeout
specifier|private
name|int
name|connectionTimeout
init|=
literal|5000
decl_stmt|;
annotation|@
name|UriParam
DECL|field|useSunHttpsHandler
specifier|private
name|boolean
name|useSunHttpsHandler
decl_stmt|;
annotation|@
name|UriParam
DECL|field|index
specifier|private
name|String
name|index
decl_stmt|;
annotation|@
name|UriParam
DECL|field|sourceType
specifier|private
name|String
name|sourceType
decl_stmt|;
annotation|@
name|UriParam
DECL|field|source
specifier|private
name|String
name|source
decl_stmt|;
annotation|@
name|UriParam
DECL|field|tcpReceiverPort
specifier|private
name|int
name|tcpReceiverPort
decl_stmt|;
comment|// consumer properties
annotation|@
name|UriParam
DECL|field|count
specifier|private
name|int
name|count
decl_stmt|;
annotation|@
name|UriParam
DECL|field|search
specifier|private
name|String
name|search
decl_stmt|;
annotation|@
name|UriParam
DECL|field|savedSearch
specifier|private
name|String
name|savedSearch
decl_stmt|;
annotation|@
name|UriParam
DECL|field|earliestTime
specifier|private
name|String
name|earliestTime
decl_stmt|;
annotation|@
name|UriParam
DECL|field|latestTime
specifier|private
name|String
name|latestTime
decl_stmt|;
annotation|@
name|UriParam
DECL|field|initEarliestTime
specifier|private
name|String
name|initEarliestTime
decl_stmt|;
DECL|field|connectionFactory
specifier|private
name|SplunkConnectionFactory
name|connectionFactory
decl_stmt|;
comment|/**      * Streaming mode sends exchanges as they are received, rather than in a batch      */
annotation|@
name|UriParam
DECL|field|streaming
specifier|private
name|Boolean
name|streaming
decl_stmt|;
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
DECL|method|setName (String name)
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
DECL|method|getInitEarliestTime ()
specifier|public
name|String
name|getInitEarliestTime
parameter_list|()
block|{
return|return
name|initEarliestTime
return|;
block|}
DECL|method|setInitEarliestTime (String initEarliestTime)
specifier|public
name|void
name|setInitEarliestTime
parameter_list|(
name|String
name|initEarliestTime
parameter_list|)
block|{
name|this
operator|.
name|initEarliestTime
operator|=
name|initEarliestTime
expr_stmt|;
block|}
DECL|method|getCount ()
specifier|public
name|int
name|getCount
parameter_list|()
block|{
return|return
name|count
return|;
block|}
DECL|method|setCount (int count)
specifier|public
name|void
name|setCount
parameter_list|(
name|int
name|count
parameter_list|)
block|{
name|this
operator|.
name|count
operator|=
name|count
expr_stmt|;
block|}
DECL|method|getSearch ()
specifier|public
name|String
name|getSearch
parameter_list|()
block|{
return|return
name|search
return|;
block|}
DECL|method|setSearch (String search)
specifier|public
name|void
name|setSearch
parameter_list|(
name|String
name|search
parameter_list|)
block|{
name|this
operator|.
name|search
operator|=
name|search
expr_stmt|;
block|}
DECL|method|getEarliestTime ()
specifier|public
name|String
name|getEarliestTime
parameter_list|()
block|{
return|return
name|earliestTime
return|;
block|}
DECL|method|setEarliestTime (String earliestTime)
specifier|public
name|void
name|setEarliestTime
parameter_list|(
name|String
name|earliestTime
parameter_list|)
block|{
name|this
operator|.
name|earliestTime
operator|=
name|earliestTime
expr_stmt|;
block|}
DECL|method|getLatestTime ()
specifier|public
name|String
name|getLatestTime
parameter_list|()
block|{
return|return
name|latestTime
return|;
block|}
DECL|method|setLatestTime (String latestTime)
specifier|public
name|void
name|setLatestTime
parameter_list|(
name|String
name|latestTime
parameter_list|)
block|{
name|this
operator|.
name|latestTime
operator|=
name|latestTime
expr_stmt|;
block|}
DECL|method|getTcpReceiverPort ()
specifier|public
name|int
name|getTcpReceiverPort
parameter_list|()
block|{
return|return
name|tcpReceiverPort
return|;
block|}
DECL|method|setTcpReceiverPort (int tcpReceiverPort)
specifier|public
name|void
name|setTcpReceiverPort
parameter_list|(
name|int
name|tcpReceiverPort
parameter_list|)
block|{
name|this
operator|.
name|tcpReceiverPort
operator|=
name|tcpReceiverPort
expr_stmt|;
block|}
DECL|method|getSourceType ()
specifier|public
name|String
name|getSourceType
parameter_list|()
block|{
return|return
name|sourceType
return|;
block|}
DECL|method|setSourceType (String sourceType)
specifier|public
name|void
name|setSourceType
parameter_list|(
name|String
name|sourceType
parameter_list|)
block|{
name|this
operator|.
name|sourceType
operator|=
name|sourceType
expr_stmt|;
block|}
DECL|method|getSource ()
specifier|public
name|String
name|getSource
parameter_list|()
block|{
return|return
name|source
return|;
block|}
DECL|method|setSource (String source)
specifier|public
name|void
name|setSource
parameter_list|(
name|String
name|source
parameter_list|)
block|{
name|this
operator|.
name|source
operator|=
name|source
expr_stmt|;
block|}
DECL|method|setIndex (String index)
specifier|public
name|void
name|setIndex
parameter_list|(
name|String
name|index
parameter_list|)
block|{
name|this
operator|.
name|index
operator|=
name|index
expr_stmt|;
block|}
DECL|method|getIndex ()
specifier|public
name|String
name|getIndex
parameter_list|()
block|{
return|return
name|index
return|;
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
DECL|method|getPort ()
specifier|public
name|int
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
DECL|method|setPort (int port)
specifier|public
name|void
name|setPort
parameter_list|(
name|int
name|port
parameter_list|)
block|{
name|this
operator|.
name|port
operator|=
name|port
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
DECL|method|getOwner ()
specifier|public
name|String
name|getOwner
parameter_list|()
block|{
return|return
name|owner
return|;
block|}
DECL|method|setOwner (String owner)
specifier|public
name|void
name|setOwner
parameter_list|(
name|String
name|owner
parameter_list|)
block|{
name|this
operator|.
name|owner
operator|=
name|owner
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
comment|/**      * Returns streaming mode.      *<p>      * Streaming mode sends exchanges as they are received, rather than in a batch.      */
DECL|method|isStreaming ()
specifier|public
name|boolean
name|isStreaming
parameter_list|()
block|{
return|return
name|streaming
operator|!=
literal|null
condition|?
name|streaming
else|:
literal|false
return|;
block|}
comment|/**      * Sets streaming mode.      *<p>      * Streaming mode sends exchanges as they are received, rather than in a batch.      *        * @param streaming      */
DECL|method|setStreaming (boolean streaming)
specifier|public
name|void
name|setStreaming
parameter_list|(
name|boolean
name|streaming
parameter_list|)
block|{
name|this
operator|.
name|streaming
operator|=
name|streaming
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
DECL|method|setConnectionTimeout (int timeout)
specifier|public
name|void
name|setConnectionTimeout
parameter_list|(
name|int
name|timeout
parameter_list|)
block|{
name|this
operator|.
name|connectionTimeout
operator|=
name|timeout
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
DECL|method|getSavedSearch ()
specifier|public
name|String
name|getSavedSearch
parameter_list|()
block|{
return|return
name|this
operator|.
name|savedSearch
return|;
block|}
DECL|method|setSavedSearch (String savedSearch)
specifier|public
name|void
name|setSavedSearch
parameter_list|(
name|String
name|savedSearch
parameter_list|)
block|{
name|this
operator|.
name|savedSearch
operator|=
name|savedSearch
expr_stmt|;
block|}
DECL|method|getConnectionFactory ()
specifier|public
name|SplunkConnectionFactory
name|getConnectionFactory
parameter_list|()
block|{
return|return
name|connectionFactory
operator|!=
literal|null
condition|?
name|connectionFactory
else|:
name|createDefaultConnectionFactory
argument_list|()
return|;
block|}
DECL|method|setConnectionFactory (SplunkConnectionFactory connectionFactory)
specifier|public
name|void
name|setConnectionFactory
parameter_list|(
name|SplunkConnectionFactory
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
DECL|method|createDefaultConnectionFactory ()
specifier|private
name|SplunkConnectionFactory
name|createDefaultConnectionFactory
parameter_list|()
block|{
name|SplunkConnectionFactory
name|splunkConnectionFactory
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|getHost
argument_list|()
argument_list|)
operator|&&
name|getPort
argument_list|()
operator|>
literal|0
condition|)
block|{
name|splunkConnectionFactory
operator|=
operator|new
name|SplunkConnectionFactory
argument_list|(
name|getHost
argument_list|()
argument_list|,
name|getPort
argument_list|()
argument_list|,
name|getUsername
argument_list|()
argument_list|,
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|splunkConnectionFactory
operator|=
operator|new
name|SplunkConnectionFactory
argument_list|(
name|getUsername
argument_list|()
argument_list|,
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|splunkConnectionFactory
operator|.
name|setApp
argument_list|(
name|getApp
argument_list|()
argument_list|)
expr_stmt|;
name|splunkConnectionFactory
operator|.
name|setConnectionTimeout
argument_list|(
name|getConnectionTimeout
argument_list|()
argument_list|)
expr_stmt|;
name|splunkConnectionFactory
operator|.
name|setScheme
argument_list|(
name|getScheme
argument_list|()
argument_list|)
expr_stmt|;
name|splunkConnectionFactory
operator|.
name|setUseSunHttpsHandler
argument_list|(
name|isUseSunHttpsHandler
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|splunkConnectionFactory
return|;
block|}
block|}
end_class

end_unit

