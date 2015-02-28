begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.gae.task
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|gae
operator|.
name|task
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Proxy
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
name|net
operator|.
name|URISyntaxException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|appengine
operator|.
name|api
operator|.
name|taskqueue
operator|.
name|Queue
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|appengine
operator|.
name|api
operator|.
name|taskqueue
operator|.
name|TaskOptions
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
name|gae
operator|.
name|bind
operator|.
name|HttpBindingInvocationHandler
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
name|gae
operator|.
name|bind
operator|.
name|InboundBinding
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
name|gae
operator|.
name|bind
operator|.
name|OutboundBinding
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
name|gae
operator|.
name|bind
operator|.
name|OutboundBindingSupport
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
name|HttpBinding
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
name|HttpClientConfigurer
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
name|servlet
operator|.
name|ServletComponent
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
name|servlet
operator|.
name|ServletEndpoint
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
name|commons
operator|.
name|httpclient
operator|.
name|HttpConnectionManager
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
name|httpclient
operator|.
name|params
operator|.
name|HttpClientParams
import|;
end_import

begin_comment
comment|/**  * Represents a<a href="http://camel.apache.org/gtask.html">Google App Engine Task Queueing endpoint</a>.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"gtask"
argument_list|,
name|syntax
operator|=
literal|"gtask:queueName"
argument_list|,
name|producerOnly
operator|=
literal|true
argument_list|,
name|label
operator|=
literal|"cloud"
argument_list|)
DECL|class|GTaskEndpoint
specifier|public
class|class
name|GTaskEndpoint
extends|extends
name|ServletEndpoint
implements|implements
name|OutboundBindingSupport
argument_list|<
name|GTaskEndpoint
argument_list|,
name|TaskOptions
argument_list|,
name|Void
argument_list|>
block|{
DECL|field|outboundBinding
specifier|private
name|OutboundBinding
argument_list|<
name|GTaskEndpoint
argument_list|,
name|TaskOptions
argument_list|,
name|Void
argument_list|>
name|outboundBinding
decl_stmt|;
DECL|field|inboundBinding
specifier|private
name|InboundBinding
argument_list|<
name|GTaskEndpoint
argument_list|,
name|HttpServletRequest
argument_list|,
name|HttpServletResponse
argument_list|>
name|inboundBinding
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
DECL|field|queueName
specifier|private
name|String
name|queueName
decl_stmt|;
DECL|field|queue
specifier|private
name|Queue
name|queue
decl_stmt|;
annotation|@
name|UriParam
DECL|field|workerRoot
specifier|private
name|String
name|workerRoot
decl_stmt|;
DECL|method|GTaskEndpoint (String endpointUri, ServletComponent component, URI httpUri, HttpClientParams params, HttpConnectionManager httpConnectionManager, HttpClientConfigurer clientConfigurer)
specifier|public
name|GTaskEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|ServletComponent
name|component
parameter_list|,
name|URI
name|httpUri
parameter_list|,
name|HttpClientParams
name|params
parameter_list|,
name|HttpConnectionManager
name|httpConnectionManager
parameter_list|,
name|HttpClientConfigurer
name|clientConfigurer
parameter_list|)
throws|throws
name|URISyntaxException
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|,
name|httpUri
argument_list|,
name|params
argument_list|,
name|httpConnectionManager
argument_list|,
name|clientConfigurer
argument_list|)
expr_stmt|;
block|}
DECL|method|getOutboundBinding ()
specifier|public
name|OutboundBinding
argument_list|<
name|GTaskEndpoint
argument_list|,
name|TaskOptions
argument_list|,
name|Void
argument_list|>
name|getOutboundBinding
parameter_list|()
block|{
return|return
name|outboundBinding
return|;
block|}
DECL|method|setOutboundBinding (OutboundBinding<GTaskEndpoint, TaskOptions, Void> outboundBinding)
specifier|public
name|void
name|setOutboundBinding
parameter_list|(
name|OutboundBinding
argument_list|<
name|GTaskEndpoint
argument_list|,
name|TaskOptions
argument_list|,
name|Void
argument_list|>
name|outboundBinding
parameter_list|)
block|{
name|this
operator|.
name|outboundBinding
operator|=
name|outboundBinding
expr_stmt|;
block|}
DECL|method|getInboundBinding ()
specifier|public
name|InboundBinding
argument_list|<
name|GTaskEndpoint
argument_list|,
name|HttpServletRequest
argument_list|,
name|HttpServletResponse
argument_list|>
name|getInboundBinding
parameter_list|()
block|{
return|return
name|inboundBinding
return|;
block|}
DECL|method|setInboundBinding ( InboundBinding<GTaskEndpoint, HttpServletRequest, HttpServletResponse> inboundBinding)
specifier|public
name|void
name|setInboundBinding
parameter_list|(
name|InboundBinding
argument_list|<
name|GTaskEndpoint
argument_list|,
name|HttpServletRequest
argument_list|,
name|HttpServletResponse
argument_list|>
name|inboundBinding
parameter_list|)
block|{
name|this
operator|.
name|inboundBinding
operator|=
name|inboundBinding
expr_stmt|;
block|}
comment|/**      * Proxies the {@link HttpBinding} returned by {@link super#getBinding()}      * with a dynamic proxy. The proxy's invocation handler further delegates to      * {@link InboundBinding#readRequest(org.apache.camel.Endpoint, Exchange, Object)} .      *       * @return proxied {@link HttpBinding}.      */
annotation|@
name|Override
DECL|method|getBinding ()
specifier|public
name|HttpBinding
name|getBinding
parameter_list|()
block|{
return|return
operator|(
name|HttpBinding
operator|)
name|Proxy
operator|.
name|newProxyInstance
argument_list|(
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|,
operator|new
name|Class
index|[]
block|{
name|HttpBinding
operator|.
name|class
block|}
argument_list|,
operator|new
name|HttpBindingInvocationHandler
argument_list|<
name|GTaskEndpoint
argument_list|,
name|HttpServletRequest
argument_list|,
name|HttpServletResponse
argument_list|>
argument_list|(
name|this
argument_list|,
name|super
operator|.
name|getBinding
argument_list|()
argument_list|,
name|getInboundBinding
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * @see #setWorkerRoot(String)      */
DECL|method|getWorkerRoot ()
specifier|public
name|String
name|getWorkerRoot
parameter_list|()
block|{
return|return
name|workerRoot
return|;
block|}
comment|/**      * Sets the web hook path root.       *      * @param workerRoot the assumed web hook path root. The default is<code>worker</code>.      *                   The servlet handling the callback from the task queueing service should have      *                   a<code>/worker/*</code> servlet mapping in this case. If another servlet mapping      *                   is used it must be set here accordingly.      */
DECL|method|setWorkerRoot (String workerRoot)
specifier|public
name|void
name|setWorkerRoot
parameter_list|(
name|String
name|workerRoot
parameter_list|)
block|{
name|this
operator|.
name|workerRoot
operator|=
name|workerRoot
expr_stmt|;
block|}
DECL|method|getQueue ()
specifier|public
name|Queue
name|getQueue
parameter_list|()
block|{
return|return
name|queue
return|;
block|}
DECL|method|setQueue (Queue queue)
specifier|public
name|void
name|setQueue
parameter_list|(
name|Queue
name|queue
parameter_list|)
block|{
name|this
operator|.
name|queue
operator|=
name|queue
expr_stmt|;
block|}
DECL|method|getQueueName ()
specifier|public
name|String
name|getQueueName
parameter_list|()
block|{
return|return
name|queueName
return|;
block|}
DECL|method|setQueueName (String queueName)
specifier|public
name|void
name|setQueueName
parameter_list|(
name|String
name|queueName
parameter_list|)
block|{
name|this
operator|.
name|queueName
operator|=
name|queueName
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
return|return
operator|new
name|GTaskProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
block|}
end_class

end_unit

