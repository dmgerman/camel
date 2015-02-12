begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.gae.login
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
name|login
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
comment|/**  * Represents a<a href="http://camel.apache.org/glogin.html">GLogin  * Endpoint</a>.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"glogin"
argument_list|,
name|producerOnly
operator|=
literal|true
argument_list|,
name|label
operator|=
literal|"cloud"
argument_list|)
DECL|class|GLoginEndpoint
specifier|public
class|class
name|GLoginEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|field|outboundBinding
specifier|private
name|OutboundBinding
argument_list|<
name|GLoginEndpoint
argument_list|,
name|GLoginData
argument_list|,
name|GLoginData
argument_list|>
name|outboundBinding
decl_stmt|;
annotation|@
name|UriPath
DECL|field|hostName
specifier|private
name|String
name|hostName
decl_stmt|;
annotation|@
name|UriPath
DECL|field|clientName
specifier|private
name|String
name|clientName
decl_stmt|;
annotation|@
name|UriParam
DECL|field|userName
specifier|private
name|String
name|userName
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
DECL|field|devPort
specifier|private
name|int
name|devPort
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|devAdmin
specifier|private
name|boolean
name|devAdmin
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|devMode
specifier|private
name|boolean
name|devMode
decl_stmt|;
DECL|field|service
specifier|private
name|GLoginService
name|service
decl_stmt|;
comment|/**      * Creates a new GLoginEndpoint.      *       * @param endpointUri the endpoint uri      * @param component component that created this endpoint.      * @param hostName internet hostname of a GAE application, for example      *<code>example.appspot.com</code>, or<code>localhost</code> if      *            the application is running on a local development server.      * @param devPort port for connecting to the local development server.      */
DECL|method|GLoginEndpoint (String endpointUri, Component component, String hostName, int devPort)
specifier|public
name|GLoginEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|,
name|String
name|hostName
parameter_list|,
name|int
name|devPort
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|hostName
operator|=
name|hostName
expr_stmt|;
name|this
operator|.
name|clientName
operator|=
literal|"apache-camel-2.x"
expr_stmt|;
name|this
operator|.
name|devPort
operator|=
name|devPort
expr_stmt|;
name|this
operator|.
name|devAdmin
operator|=
literal|false
expr_stmt|;
block|}
comment|/**      * Returns the component instance that created this endpoint.      */
annotation|@
name|Override
DECL|method|getComponent ()
specifier|public
name|GLoginComponent
name|getComponent
parameter_list|()
block|{
return|return
operator|(
name|GLoginComponent
operator|)
name|super
operator|.
name|getComponent
argument_list|()
return|;
block|}
comment|/**      * Returns the internet hostname of the GAE application where to login.      */
DECL|method|getHostName ()
specifier|public
name|String
name|getHostName
parameter_list|()
block|{
return|return
name|hostName
return|;
block|}
DECL|method|getOutboundBinding ()
specifier|public
name|OutboundBinding
argument_list|<
name|GLoginEndpoint
argument_list|,
name|GLoginData
argument_list|,
name|GLoginData
argument_list|>
name|getOutboundBinding
parameter_list|()
block|{
return|return
name|outboundBinding
return|;
block|}
comment|/**      * Sets the outbound binding for<code>glogin</code> endpoints. Default binding      * is {@link GLoginBinding}.      */
DECL|method|setOutboundBinding (OutboundBinding<GLoginEndpoint, GLoginData, GLoginData> outboundBinding)
specifier|public
name|void
name|setOutboundBinding
parameter_list|(
name|OutboundBinding
argument_list|<
name|GLoginEndpoint
argument_list|,
name|GLoginData
argument_list|,
name|GLoginData
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
DECL|method|getClientName ()
specifier|public
name|String
name|getClientName
parameter_list|()
block|{
return|return
name|clientName
return|;
block|}
comment|/**      * Sets the client name used for authentication. The default name is      *<code>apache-camel-2.x</code>.      */
DECL|method|setClientName (String clientName)
specifier|public
name|void
name|setClientName
parameter_list|(
name|String
name|clientName
parameter_list|)
block|{
name|this
operator|.
name|clientName
operator|=
name|clientName
expr_stmt|;
block|}
DECL|method|getUserName ()
specifier|public
name|String
name|getUserName
parameter_list|()
block|{
return|return
name|userName
return|;
block|}
comment|/**      * Sets the login username (a Google mail address).      */
DECL|method|setUserName (String userName)
specifier|public
name|void
name|setUserName
parameter_list|(
name|String
name|userName
parameter_list|)
block|{
name|this
operator|.
name|userName
operator|=
name|userName
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
comment|/**      * Sets the login password.      */
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
comment|/**      * Returns the port for connecting to a development server. Only used      * if {@link #devMode} is<code>true</code>. Default is 8080.      */
DECL|method|getDevPort ()
specifier|public
name|int
name|getDevPort
parameter_list|()
block|{
return|return
name|devPort
return|;
block|}
DECL|method|isDevAdmin ()
specifier|public
name|boolean
name|isDevAdmin
parameter_list|()
block|{
return|return
name|devAdmin
return|;
block|}
comment|/**      * Set to<code>true</code> for logging in as admin to a development server.      * Only used if {@link #devMode} is<code>true</code>. Default is      *<code>false</code>.      */
DECL|method|setDevAdmin (boolean devAdmin)
specifier|public
name|void
name|setDevAdmin
parameter_list|(
name|boolean
name|devAdmin
parameter_list|)
block|{
name|this
operator|.
name|devAdmin
operator|=
name|devAdmin
expr_stmt|;
block|}
DECL|method|isDevMode ()
specifier|public
name|boolean
name|isDevMode
parameter_list|()
block|{
return|return
name|devMode
return|;
block|}
comment|/**      * Set to<code>true</code> for connecting to a development server.      */
DECL|method|setDevMode (boolean devMode)
specifier|public
name|void
name|setDevMode
parameter_list|(
name|boolean
name|devMode
parameter_list|)
block|{
name|this
operator|.
name|devMode
operator|=
name|devMode
expr_stmt|;
block|}
DECL|method|getService ()
specifier|public
name|GLoginService
name|getService
parameter_list|()
block|{
return|return
name|service
return|;
block|}
comment|/**      * Sets the service that makes the remote calls to Google services or the      * local development server. Testing code should inject a mock service here      * (using serviceRef in endpoint URI).      */
DECL|method|setService (GLoginService service)
specifier|public
name|void
name|setService
parameter_list|(
name|GLoginService
name|service
parameter_list|)
block|{
name|this
operator|.
name|service
operator|=
name|service
expr_stmt|;
block|}
comment|/**      * Creates a {@link GLoginProducer}.      */
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
name|GLoginProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
comment|/**      * throws {@link UnsupportedOperationException}      */
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"consumption from glogin endpoint not supported"
argument_list|)
throw|;
block|}
comment|/**      * Returns<code>true</code>.      */
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
block|}
end_class

end_unit

