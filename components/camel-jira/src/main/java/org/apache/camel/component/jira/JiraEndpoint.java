begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|com
operator|.
name|atlassian
operator|.
name|jira
operator|.
name|rest
operator|.
name|client
operator|.
name|api
operator|.
name|JiraRestClient
import|;
end_import

begin_import
import|import
name|com
operator|.
name|atlassian
operator|.
name|jira
operator|.
name|rest
operator|.
name|client
operator|.
name|api
operator|.
name|JiraRestClientFactory
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
name|component
operator|.
name|jira
operator|.
name|consumer
operator|.
name|NewCommentsConsumer
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
name|NewIssuesConsumer
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
name|oauth
operator|.
name|JiraOAuthAuthenticationHandler
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
name|oauth
operator|.
name|OAuthAsynchronousJiraRestClientFactory
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
name|AddCommentProducer
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
name|AddIssueProducer
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
name|AttachFileProducer
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
name|DeleteIssueProducer
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
name|TransitionIssueProducer
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
name|UpdateIssueProducer
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
name|WatcherProducer
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
name|Registry
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
name|support
operator|.
name|DefaultEndpoint
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

begin_import
import|import static
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
name|JiraConstants
operator|.
name|JIRA_REST_CLIENT_FACTORY
import|;
end_import

begin_comment
comment|/**  * The jira component interacts with the JIRA issue tracker.  *<p>  * The endpoint encapsulates portions of the JIRA API, relying on the jira-rest-java-client SDK. Available endpoint URIs include:  *<p>  * CONSUMERS jira://newIssues (retrieve only new issues after the route is started) jira://newComments (retrieve only new comments after the route is started)  *<p>  * PRODUCERS jira://addIssue (add an issue) jira://addComment (add a comment on a given issue) jira://attach (add an attachment on a given issue) jira://deleteIssue (delete a given issue)  * jira://updateIssue (update fields of a given issue) jira://transitionIssue (transition a status of a given issue) jira://watchers (add/remove watchers of a given issue)  *<p>  * The endpoints will respond with jira-rest-java-client POJOs (Issue, Comment, etc.)  *<p>  * Note: Rather than webhooks, this endpoint relies on simple polling.  Reasons include: - concerned about reliability/stability if this somehow relied on an exposed, embedded server (Jetty?) - the  * types of payloads we're polling aren't typically large (plus, paging is available in the API) - need to support apps running somewhere not publicly accessible where a webhook would fail  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"3.0"
argument_list|,
name|scheme
operator|=
literal|"jira"
argument_list|,
name|title
operator|=
literal|"Jira"
argument_list|,
name|syntax
operator|=
literal|"jira:type"
argument_list|,
name|label
operator|=
literal|"api,reporting"
argument_list|)
DECL|class|JiraEndpoint
specifier|public
class|class
name|JiraEndpoint
extends|extends
name|DefaultEndpoint
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
name|JiraEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|type
specifier|private
name|JiraType
name|type
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|jql
specifier|private
name|String
name|jql
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"50"
argument_list|)
DECL|field|maxResults
specifier|private
name|Integer
name|maxResults
init|=
literal|50
decl_stmt|;
annotation|@
name|UriParam
DECL|field|configuration
specifier|private
name|JiraConfiguration
name|configuration
decl_stmt|;
DECL|field|client
specifier|private
name|JiraRestClient
name|client
decl_stmt|;
DECL|method|JiraEndpoint (String uri, JiraComponent component, JiraConfiguration configuration)
specifier|public
name|JiraEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|JiraComponent
name|component
parameter_list|,
name|JiraConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|public
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
name|Registry
name|registry
init|=
name|getCamelContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
decl_stmt|;
name|JiraRestClientFactory
name|factory
init|=
name|registry
operator|.
name|lookupByNameAndType
argument_list|(
name|JIRA_REST_CLIENT_FACTORY
argument_list|,
name|JiraRestClientFactory
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|factory
operator|==
literal|null
condition|)
block|{
name|factory
operator|=
operator|new
name|OAuthAsynchronousJiraRestClientFactory
argument_list|()
expr_stmt|;
block|}
specifier|final
name|URI
name|jiraServerUri
init|=
name|URI
operator|.
name|create
argument_list|(
name|configuration
operator|.
name|getJiraUrl
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|configuration
operator|.
name|getUsername
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Jira Basic authentication with username/password."
argument_list|)
expr_stmt|;
name|client
operator|=
name|factory
operator|.
name|createWithBasicHttpAuthentication
argument_list|(
name|jiraServerUri
argument_list|,
name|configuration
operator|.
name|getUsername
argument_list|()
argument_list|,
name|configuration
operator|.
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Jira OAuth authentication."
argument_list|)
expr_stmt|;
name|JiraOAuthAuthenticationHandler
name|oAuthHandler
init|=
operator|new
name|JiraOAuthAuthenticationHandler
argument_list|(
name|configuration
operator|.
name|getConsumerKey
argument_list|()
argument_list|,
name|configuration
operator|.
name|getVerificationCode
argument_list|()
argument_list|,
name|configuration
operator|.
name|getPrivateKey
argument_list|()
argument_list|,
name|configuration
operator|.
name|getAccessToken
argument_list|()
argument_list|,
name|configuration
operator|.
name|getJiraUrl
argument_list|()
argument_list|)
decl_stmt|;
name|client
operator|=
name|factory
operator|.
name|create
argument_list|(
name|jiraServerUri
argument_list|,
name|oAuthHandler
argument_list|)
expr_stmt|;
block|}
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
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
if|if
condition|(
name|client
operator|!=
literal|null
condition|)
block|{
name|client
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
block|{
switch|switch
condition|(
name|type
condition|)
block|{
case|case
name|ADDISSUE
case|:
return|return
operator|new
name|AddIssueProducer
argument_list|(
name|this
argument_list|)
return|;
case|case
name|ATTACH
case|:
return|return
operator|new
name|AttachFileProducer
argument_list|(
name|this
argument_list|)
return|;
case|case
name|ADDCOMMENT
case|:
return|return
operator|new
name|AddCommentProducer
argument_list|(
name|this
argument_list|)
return|;
case|case
name|WATCHERS
case|:
return|return
operator|new
name|WatcherProducer
argument_list|(
name|this
argument_list|)
return|;
case|case
name|DELETEISSUE
case|:
return|return
operator|new
name|DeleteIssueProducer
argument_list|(
name|this
argument_list|)
return|;
case|case
name|UPDATEISSUE
case|:
return|return
operator|new
name|UpdateIssueProducer
argument_list|(
name|this
argument_list|)
return|;
case|case
name|TRANSITIONISSUE
case|:
return|return
operator|new
name|TransitionIssueProducer
argument_list|(
name|this
argument_list|)
return|;
default|default:
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
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
block|{
if|if
condition|(
name|type
operator|==
name|JiraType
operator|.
name|NEWCOMMENTS
condition|)
block|{
return|return
operator|new
name|NewCommentsConsumer
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
name|JiraType
operator|.
name|NEWISSUES
condition|)
block|{
return|return
operator|new
name|NewIssuesConsumer
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
annotation|@
name|Override
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
name|JiraType
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
comment|/**      * Operation to perform. Consumers: NewIssues, NewComments. Producers: AddIssue, AttachFile, DeleteIssue, TransitionIssue, UpdateIssue, Watchers. See this class javadoc description for more      * information.      */
DECL|method|setType (JiraType type)
specifier|public
name|void
name|setType
parameter_list|(
name|JiraType
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
comment|/**      * JQL is the query language from JIRA which allows you to retrieve the data you want. For example<tt>jql=project=MyProject</tt> Where MyProject is the product key in Jira. It is important to use      * the RAW() and set the JQL inside it to prevent camel parsing it, example: RAW(project in (MYP, COM) AND resolution = Unresolved)      */
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
name|configuration
operator|.
name|getDelay
argument_list|()
return|;
block|}
DECL|method|getClient ()
specifier|public
name|JiraRestClient
name|getClient
parameter_list|()
block|{
return|return
name|client
return|;
block|}
DECL|method|setClient (JiraRestClient client)
specifier|public
name|void
name|setClient
parameter_list|(
name|JiraRestClient
name|client
parameter_list|)
block|{
name|this
operator|.
name|client
operator|=
name|client
expr_stmt|;
block|}
DECL|method|getMaxResults ()
specifier|public
name|Integer
name|getMaxResults
parameter_list|()
block|{
return|return
name|maxResults
return|;
block|}
comment|/**      * Max number of issues to search for      */
DECL|method|setMaxResults (Integer maxResults)
specifier|public
name|void
name|setMaxResults
parameter_list|(
name|Integer
name|maxResults
parameter_list|)
block|{
name|this
operator|.
name|maxResults
operator|=
name|maxResults
expr_stmt|;
block|}
block|}
end_class

end_unit

