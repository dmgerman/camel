begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jetty
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jetty
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
name|java
operator|.
name|util
operator|.
name|List
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
name|http
operator|.
name|HttpConsumer
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
name|http
operator|.
name|HttpEndpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mortbay
operator|.
name|jetty
operator|.
name|Handler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mortbay
operator|.
name|jetty
operator|.
name|client
operator|.
name|HttpClient
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|JettyHttpEndpoint
specifier|public
class|class
name|JettyHttpEndpoint
extends|extends
name|HttpEndpoint
block|{
DECL|field|sessionSupport
specifier|private
name|boolean
name|sessionSupport
decl_stmt|;
DECL|field|handlers
specifier|private
name|List
argument_list|<
name|Handler
argument_list|>
name|handlers
decl_stmt|;
DECL|field|client
specifier|private
name|HttpClient
name|client
decl_stmt|;
DECL|field|jettyBinding
specifier|private
name|JettyHttpBinding
name|jettyBinding
decl_stmt|;
DECL|method|JettyHttpEndpoint (JettyHttpComponent component, String uri, URI httpURL)
specifier|public
name|JettyHttpEndpoint
parameter_list|(
name|JettyHttpComponent
name|component
parameter_list|,
name|String
name|uri
parameter_list|,
name|URI
name|httpURL
parameter_list|)
throws|throws
name|URISyntaxException
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|,
name|httpURL
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getComponent ()
specifier|public
name|JettyHttpComponent
name|getComponent
parameter_list|()
block|{
return|return
operator|(
name|JettyHttpComponent
operator|)
name|super
operator|.
name|getComponent
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|JettyHttpProducer
name|answer
init|=
operator|new
name|JettyHttpProducer
argument_list|(
name|this
argument_list|,
name|getClient
argument_list|()
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setBinding
argument_list|(
name|getJettyBinding
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
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
throws|throws
name|Exception
block|{
return|return
operator|new
name|HttpConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
return|;
block|}
DECL|method|setSessionSupport (boolean support)
specifier|public
name|void
name|setSessionSupport
parameter_list|(
name|boolean
name|support
parameter_list|)
block|{
name|sessionSupport
operator|=
name|support
expr_stmt|;
block|}
DECL|method|isSessionSupport ()
specifier|public
name|boolean
name|isSessionSupport
parameter_list|()
block|{
return|return
name|sessionSupport
return|;
block|}
DECL|method|getHandlers ()
specifier|public
name|List
argument_list|<
name|Handler
argument_list|>
name|getHandlers
parameter_list|()
block|{
return|return
name|handlers
return|;
block|}
DECL|method|setHandlers (List<Handler> handlers)
specifier|public
name|void
name|setHandlers
parameter_list|(
name|List
argument_list|<
name|Handler
argument_list|>
name|handlers
parameter_list|)
block|{
name|this
operator|.
name|handlers
operator|=
name|handlers
expr_stmt|;
block|}
DECL|method|getClient ()
specifier|public
name|HttpClient
name|getClient
parameter_list|()
block|{
if|if
condition|(
name|client
operator|==
literal|null
condition|)
block|{
return|return
name|getComponent
argument_list|()
operator|.
name|getHttpClient
argument_list|()
return|;
block|}
return|return
name|client
return|;
block|}
DECL|method|setClient (HttpClient client)
specifier|public
name|void
name|setClient
parameter_list|(
name|HttpClient
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
DECL|method|getJettyBinding ()
specifier|public
specifier|synchronized
name|JettyHttpBinding
name|getJettyBinding
parameter_list|()
block|{
if|if
condition|(
name|jettyBinding
operator|==
literal|null
condition|)
block|{
name|jettyBinding
operator|=
operator|new
name|DefaultJettyHttpBinding
argument_list|()
expr_stmt|;
name|jettyBinding
operator|.
name|setHeaderFilterStrategy
argument_list|(
name|getHeaderFilterStrategy
argument_list|()
argument_list|)
expr_stmt|;
name|jettyBinding
operator|.
name|setThrowExceptionOnFailure
argument_list|(
name|isThrowExceptionOnFailure
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|jettyBinding
return|;
block|}
DECL|method|setJettyBinding (JettyHttpBinding jettyBinding)
specifier|public
name|void
name|setJettyBinding
parameter_list|(
name|JettyHttpBinding
name|jettyBinding
parameter_list|)
block|{
name|this
operator|.
name|jettyBinding
operator|=
name|jettyBinding
expr_stmt|;
block|}
block|}
end_class

end_unit

