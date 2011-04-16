begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.xmpp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|xmpp
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
name|ServiceHelper
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

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|XmppComponent
specifier|public
class|class
name|XmppComponent
extends|extends
name|DefaultComponent
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|XmppComponent
operator|.
name|class
argument_list|)
decl_stmt|;
comment|//keep a cache of endpoints so they can be properly cleaned up
DECL|field|endpointCache
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|XmppEndpoint
argument_list|>
name|endpointCache
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|XmppEndpoint
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
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
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|endpointCache
operator|.
name|containsKey
argument_list|(
name|uri
argument_list|)
condition|)
block|{
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
literal|"Using cached endpoint for URI "
operator|+
name|uri
argument_list|)
expr_stmt|;
block|}
return|return
name|endpointCache
operator|.
name|get
argument_list|(
name|uri
argument_list|)
return|;
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
literal|"Creating new endpoint for URI "
operator|+
name|uri
argument_list|)
expr_stmt|;
block|}
name|XmppEndpoint
name|endpoint
init|=
operator|new
name|XmppEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|URI
name|u
init|=
operator|new
name|URI
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|setHost
argument_list|(
name|u
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setPort
argument_list|(
name|u
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|u
operator|.
name|getUserInfo
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|setUser
argument_list|(
name|u
operator|.
name|getUserInfo
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|String
name|remainingPath
init|=
name|u
operator|.
name|getPath
argument_list|()
decl_stmt|;
if|if
condition|(
name|remainingPath
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|remainingPath
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|remainingPath
operator|=
name|remainingPath
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
comment|// assume its a participant
if|if
condition|(
name|remainingPath
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|endpoint
operator|.
name|setParticipant
argument_list|(
name|remainingPath
argument_list|)
expr_stmt|;
block|}
block|}
name|endpointCache
operator|.
name|put
argument_list|(
name|uri
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
return|return
name|endpoint
return|;
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
name|ServiceHelper
operator|.
name|stopServices
argument_list|(
name|endpointCache
operator|.
name|values
argument_list|()
argument_list|)
expr_stmt|;
name|endpointCache
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

