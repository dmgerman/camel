begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jira
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jira
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
name|component
operator|.
name|jira
operator|.
name|consumer
operator|.
name|NewCommentConsumer
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
name|jira
operator|.
name|consumer
operator|.
name|NewIssueConsumer
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
name|jira
operator|.
name|producer
operator|.
name|NewIssueProducer
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

begin_comment
comment|/**  * The endpoint encapsulates portions of the JIRA API, relying on the jira-rest-java-client SDK.  * Available endpoint URIs include:  *   * CONSUMERS  * jira://newIssue (new tickets)  * jira://newComment (new comments on tickets)  *   * The endpoints will respond with jira-rest-java-client POJOs (Issue, Comment, etc.)  *   * Note: Rather than webhooks, this endpoint relies on simple polling.  Reasons include:  * - concerned about reliability/stability if this somehow relied on an exposed, embedded server (Jetty?)  * - the types of payloads we're polling aren't typically large (plus, paging is available in the API)  * - need to support apps running somewhere not publicly accessible where a webhook would fail  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"jira"
argument_list|,
name|label
operator|=
literal|"api,reporting"
argument_list|)
DECL|class|JIRAEndpoint
specifier|public
class|class
name|JIRAEndpoint
extends|extends
name|DefaultEndpoint
block|{
annotation|@
name|UriPath
DECL|field|type
specifier|private
name|JIRAType
name|type
decl_stmt|;
annotation|@
name|UriParam
DECL|field|serverUrl
specifier|private
name|String
name|serverUrl
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
DECL|field|jql
specifier|private
name|String
name|jql
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"6000"
argument_list|)
DECL|field|delay
specifier|private
name|int
name|delay
init|=
literal|6000
decl_stmt|;
DECL|method|JIRAEndpoint (String uri, JIRAComponent component)
specifier|public
name|JIRAEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|JIRAComponent
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|type
operator|==
name|JIRAType
operator|.
name|NEWISSUE
condition|)
block|{
return|return
operator|new
name|NewIssueProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Producer does not support type: "
operator|+
name|type
argument_list|)
throw|;
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
if|if
condition|(
name|type
operator|==
name|JIRAType
operator|.
name|NEWCOMMENT
condition|)
block|{
return|return
operator|new
name|NewCommentConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|type
operator|==
name|JIRAType
operator|.
name|NEWISSUE
condition|)
block|{
return|return
operator|new
name|NewIssueConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
return|;
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Consumer does not support type: "
operator|+
name|type
argument_list|)
throw|;
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
DECL|method|getType ()
specifier|public
name|JIRAType
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
DECL|method|setType (JIRAType type)
specifier|public
name|void
name|setType
parameter_list|(
name|JIRAType
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
DECL|method|getServerUrl ()
specifier|public
name|String
name|getServerUrl
parameter_list|()
block|{
return|return
name|serverUrl
return|;
block|}
DECL|method|setServerUrl (String serverUrl)
specifier|public
name|void
name|setServerUrl
parameter_list|(
name|String
name|serverUrl
parameter_list|)
block|{
name|this
operator|.
name|serverUrl
operator|=
name|serverUrl
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
DECL|method|getJql ()
specifier|public
name|String
name|getJql
parameter_list|()
block|{
return|return
name|jql
return|;
block|}
DECL|method|setJql (String jql)
specifier|public
name|void
name|setJql
parameter_list|(
name|String
name|jql
parameter_list|)
block|{
name|this
operator|.
name|jql
operator|=
name|jql
expr_stmt|;
block|}
DECL|method|getDelay ()
specifier|public
name|int
name|getDelay
parameter_list|()
block|{
return|return
name|delay
return|;
block|}
DECL|method|setDelay (int delay)
specifier|public
name|void
name|setDelay
parameter_list|(
name|int
name|delay
parameter_list|)
block|{
name|this
operator|.
name|delay
operator|=
name|delay
expr_stmt|;
block|}
block|}
end_class

end_unit

