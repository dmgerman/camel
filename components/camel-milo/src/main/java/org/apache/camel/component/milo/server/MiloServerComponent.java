begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.milo.server
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|milo
operator|.
name|server
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Closeable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLDecoder
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|KeyPair
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|cert
operator|.
name|X509Certificate
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|EnumSet
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
name|LinkedList
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
name|Objects
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Supplier
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Collections
operator|.
name|singletonList
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
name|component
operator|.
name|milo
operator|.
name|KeyStoreLoader
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
name|milo
operator|.
name|server
operator|.
name|internal
operator|.
name|CamelNamespace
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
name|annotations
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
name|support
operator|.
name|DefaultComponent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|sdk
operator|.
name|server
operator|.
name|OpcUaServer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|sdk
operator|.
name|server
operator|.
name|api
operator|.
name|config
operator|.
name|OpcUaServerConfig
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|sdk
operator|.
name|server
operator|.
name|api
operator|.
name|config
operator|.
name|OpcUaServerConfigBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|sdk
operator|.
name|server
operator|.
name|identity
operator|.
name|AnonymousIdentityValidator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|sdk
operator|.
name|server
operator|.
name|identity
operator|.
name|IdentityValidator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|sdk
operator|.
name|server
operator|.
name|identity
operator|.
name|UsernameIdentityValidator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|stack
operator|.
name|core
operator|.
name|StatusCodes
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|stack
operator|.
name|core
operator|.
name|UaException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|stack
operator|.
name|core
operator|.
name|application
operator|.
name|CertificateManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|stack
operator|.
name|core
operator|.
name|application
operator|.
name|CertificateValidator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|stack
operator|.
name|core
operator|.
name|application
operator|.
name|DefaultCertificateManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|stack
operator|.
name|core
operator|.
name|application
operator|.
name|DefaultCertificateValidator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|stack
operator|.
name|core
operator|.
name|security
operator|.
name|SecurityPolicy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|stack
operator|.
name|core
operator|.
name|types
operator|.
name|builtin
operator|.
name|LocalizedText
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|stack
operator|.
name|core
operator|.
name|types
operator|.
name|enumerated
operator|.
name|UserTokenType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|stack
operator|.
name|core
operator|.
name|types
operator|.
name|structured
operator|.
name|BuildInfo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|stack
operator|.
name|core
operator|.
name|types
operator|.
name|structured
operator|.
name|UserTokenPolicy
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|sdk
operator|.
name|server
operator|.
name|api
operator|.
name|config
operator|.
name|OpcUaServerConfig
operator|.
name|USER_TOKEN_POLICY_ANONYMOUS
import|;
end_import

begin_comment
comment|/**  * OPC UA Server based component  */
end_comment

begin_class
annotation|@
name|Component
argument_list|(
literal|"milo-server"
argument_list|)
DECL|class|MiloServerComponent
specifier|public
class|class
name|MiloServerComponent
extends|extends
name|DefaultComponent
block|{
DECL|field|DEFAULT_NAMESPACE_URI
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_NAMESPACE_URI
init|=
literal|"urn:org:apache:camel"
decl_stmt|;
DECL|field|URL_CHARSET
specifier|private
specifier|static
specifier|final
name|String
name|URL_CHARSET
init|=
literal|"UTF-8"
decl_stmt|;
DECL|field|DEFAULT_SERVER_CONFIG
specifier|private
specifier|static
specifier|final
name|OpcUaServerConfig
name|DEFAULT_SERVER_CONFIG
decl_stmt|;
static|static
block|{
specifier|final
name|OpcUaServerConfigBuilder
name|cfg
init|=
name|OpcUaServerConfig
operator|.
name|builder
argument_list|()
decl_stmt|;
name|cfg
operator|.
name|setCertificateManager
argument_list|(
operator|new
name|DefaultCertificateManager
argument_list|()
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setCertificateValidator
argument_list|(
name|DenyAllCertificateValidator
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setSecurityPolicies
argument_list|(
name|EnumSet
operator|.
name|allOf
argument_list|(
name|SecurityPolicy
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setApplicationName
argument_list|(
name|LocalizedText
operator|.
name|english
argument_list|(
literal|"Apache Camel Milo Server"
argument_list|)
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setApplicationUri
argument_list|(
literal|"urn:org:apache:camel:milo:server"
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setProductUri
argument_list|(
literal|"urn:org:apache:camel:milo"
argument_list|)
expr_stmt|;
if|if
condition|(
name|Boolean
operator|.
name|getBoolean
argument_list|(
literal|"org.apache.camel.milo.server.default.enableAnonymous"
argument_list|)
condition|)
block|{
name|cfg
operator|.
name|setUserTokenPolicies
argument_list|(
name|singletonList
argument_list|(
name|USER_TOKEN_POLICY_ANONYMOUS
argument_list|)
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setIdentityValidator
argument_list|(
name|AnonymousIdentityValidator
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
block|}
name|DEFAULT_SERVER_CONFIG
operator|=
name|cfg
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
DECL|class|DenyAllCertificateValidator
specifier|private
specifier|static
specifier|final
class|class
name|DenyAllCertificateValidator
implements|implements
name|CertificateValidator
block|{
DECL|field|INSTANCE
specifier|public
specifier|static
specifier|final
name|CertificateValidator
name|INSTANCE
init|=
operator|new
name|DenyAllCertificateValidator
argument_list|()
decl_stmt|;
DECL|method|DenyAllCertificateValidator ()
specifier|private
name|DenyAllCertificateValidator
parameter_list|()
block|{         }
annotation|@
name|Override
DECL|method|validate (final X509Certificate certificate)
specifier|public
name|void
name|validate
parameter_list|(
specifier|final
name|X509Certificate
name|certificate
parameter_list|)
throws|throws
name|UaException
block|{
throw|throw
operator|new
name|UaException
argument_list|(
name|StatusCodes
operator|.
name|Bad_CertificateUseNotAllowed
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|verifyTrustChain (List<X509Certificate> certificateChain)
specifier|public
name|void
name|verifyTrustChain
parameter_list|(
name|List
argument_list|<
name|X509Certificate
argument_list|>
name|certificateChain
parameter_list|)
throws|throws
name|UaException
block|{
throw|throw
operator|new
name|UaException
argument_list|(
name|StatusCodes
operator|.
name|Bad_CertificateUseNotAllowed
argument_list|)
throw|;
block|}
block|}
DECL|field|namespaceUri
specifier|private
name|String
name|namespaceUri
init|=
name|DEFAULT_NAMESPACE_URI
decl_stmt|;
DECL|field|serverConfig
specifier|private
specifier|final
name|OpcUaServerConfigBuilder
name|serverConfig
decl_stmt|;
DECL|field|server
specifier|private
name|OpcUaServer
name|server
decl_stmt|;
DECL|field|namespace
specifier|private
name|CamelNamespace
name|namespace
decl_stmt|;
DECL|field|endpoints
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|MiloServerEndpoint
argument_list|>
name|endpoints
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|enableAnonymousAuthentication
specifier|private
name|Boolean
name|enableAnonymousAuthentication
decl_stmt|;
DECL|field|userMap
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|userMap
decl_stmt|;
DECL|field|usernameSecurityPolicyUri
specifier|private
name|String
name|usernameSecurityPolicyUri
init|=
name|OpcUaServerConfig
operator|.
name|USER_TOKEN_POLICY_USERNAME
operator|.
name|getSecurityPolicyUri
argument_list|()
decl_stmt|;
DECL|field|bindAddresses
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|bindAddresses
decl_stmt|;
DECL|field|certificateValidator
specifier|private
name|Supplier
argument_list|<
name|CertificateValidator
argument_list|>
name|certificateValidator
decl_stmt|;
DECL|field|runOnStop
specifier|private
specifier|final
name|List
argument_list|<
name|Runnable
argument_list|>
name|runOnStop
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|MiloServerComponent ()
specifier|public
name|MiloServerComponent
parameter_list|()
block|{
name|this
argument_list|(
name|DEFAULT_SERVER_CONFIG
argument_list|)
expr_stmt|;
block|}
DECL|method|MiloServerComponent (final OpcUaServerConfig serverConfig)
specifier|public
name|MiloServerComponent
parameter_list|(
specifier|final
name|OpcUaServerConfig
name|serverConfig
parameter_list|)
block|{
name|this
operator|.
name|serverConfig
operator|=
name|OpcUaServerConfig
operator|.
name|copy
argument_list|(
name|serverConfig
operator|!=
literal|null
condition|?
name|serverConfig
else|:
name|DEFAULT_SERVER_CONFIG
argument_list|)
expr_stmt|;
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
name|this
operator|.
name|server
operator|=
operator|new
name|OpcUaServer
argument_list|(
name|buildServerConfig
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|namespace
operator|=
name|this
operator|.
name|server
operator|.
name|getNamespaceManager
argument_list|()
operator|.
name|registerAndAdd
argument_list|(
name|this
operator|.
name|namespaceUri
argument_list|,
name|index
lambda|->
operator|new
name|CamelNamespace
argument_list|(
name|index
argument_list|,
name|this
operator|.
name|namespaceUri
argument_list|,
name|this
operator|.
name|server
argument_list|)
argument_list|)
expr_stmt|;
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|this
operator|.
name|server
operator|.
name|startup
argument_list|()
expr_stmt|;
block|}
comment|/**      * Build the final server configuration, apply all complex configuration      *      * @return the new server configuration, never returns {@code null}      */
DECL|method|buildServerConfig ()
specifier|private
name|OpcUaServerConfig
name|buildServerConfig
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|userMap
operator|!=
literal|null
operator|||
name|this
operator|.
name|enableAnonymousAuthentication
operator|!=
literal|null
condition|)
block|{
comment|// set identity validator
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|userMap
init|=
name|this
operator|.
name|userMap
operator|!=
literal|null
condition|?
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|this
operator|.
name|userMap
argument_list|)
else|:
name|Collections
operator|.
name|emptyMap
argument_list|()
decl_stmt|;
specifier|final
name|boolean
name|allowAnonymous
init|=
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|this
operator|.
name|enableAnonymousAuthentication
argument_list|)
decl_stmt|;
specifier|final
name|IdentityValidator
name|identityValidator
init|=
operator|new
name|UsernameIdentityValidator
argument_list|(
name|allowAnonymous
argument_list|,
name|challenge
lambda|->
block|{
specifier|final
name|String
name|pwd
init|=
name|userMap
operator|.
name|get
argument_list|(
name|challenge
operator|.
name|getUsername
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|pwd
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|pwd
operator|.
name|equals
argument_list|(
name|challenge
operator|.
name|getPassword
argument_list|()
argument_list|)
return|;
block|}
argument_list|)
decl_stmt|;
name|this
operator|.
name|serverConfig
operator|.
name|setIdentityValidator
argument_list|(
name|identityValidator
argument_list|)
expr_stmt|;
comment|// add token policies
specifier|final
name|List
argument_list|<
name|UserTokenPolicy
argument_list|>
name|tokenPolicies
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|allowAnonymous
condition|)
block|{
name|tokenPolicies
operator|.
name|add
argument_list|(
name|OpcUaServerConfig
operator|.
name|USER_TOKEN_POLICY_ANONYMOUS
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|userMap
operator|!=
literal|null
condition|)
block|{
name|tokenPolicies
operator|.
name|add
argument_list|(
name|getUsernamePolicy
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|serverConfig
operator|.
name|setUserTokenPolicies
argument_list|(
name|tokenPolicies
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|this
operator|.
name|bindAddresses
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|serverConfig
operator|.
name|setBindAddresses
argument_list|(
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|this
operator|.
name|bindAddresses
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|this
operator|.
name|certificateValidator
operator|!=
literal|null
condition|)
block|{
specifier|final
name|CertificateValidator
name|validator
init|=
name|this
operator|.
name|certificateValidator
operator|.
name|get
argument_list|()
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Using validator: {}"
argument_list|,
name|validator
argument_list|)
expr_stmt|;
if|if
condition|(
name|validator
operator|instanceof
name|Closeable
condition|)
block|{
name|runOnStop
argument_list|(
parameter_list|()
lambda|->
block|{
try|try
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Closing: {}"
argument_list|,
name|validator
argument_list|)
expr_stmt|;
operator|(
operator|(
name|Closeable
operator|)
name|validator
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
specifier|final
name|IOException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Failed to close"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|serverConfig
operator|.
name|setCertificateValidator
argument_list|(
name|validator
argument_list|)
expr_stmt|;
block|}
comment|// build final configuration
return|return
name|this
operator|.
name|serverConfig
operator|.
name|build
argument_list|()
return|;
block|}
comment|/**      * Get the user token policy for using with username authentication      *       * @return the user token policy to use for username authentication      */
DECL|method|getUsernamePolicy ()
specifier|private
name|UserTokenPolicy
name|getUsernamePolicy
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|usernameSecurityPolicyUri
operator|==
literal|null
operator|||
name|this
operator|.
name|usernameSecurityPolicyUri
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|OpcUaServerConfig
operator|.
name|USER_TOKEN_POLICY_USERNAME
return|;
block|}
return|return
operator|new
name|UserTokenPolicy
argument_list|(
literal|"username"
argument_list|,
name|UserTokenType
operator|.
name|UserName
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|this
operator|.
name|usernameSecurityPolicyUri
argument_list|)
return|;
block|}
DECL|method|runOnStop (final Runnable runnable)
specifier|private
name|void
name|runOnStop
parameter_list|(
specifier|final
name|Runnable
name|runnable
parameter_list|)
block|{
name|this
operator|.
name|runOnStop
operator|.
name|add
argument_list|(
name|runnable
argument_list|)
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
name|this
operator|.
name|server
operator|.
name|shutdown
argument_list|()
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
name|this
operator|.
name|runOnStop
operator|.
name|forEach
argument_list|(
name|runnable
lambda|->
block|{
try|try
block|{
name|runnable
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
specifier|final
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Failed to run on stop"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|this
operator|.
name|runOnStop
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createEndpoint (final String uri, final String remaining, final Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
specifier|final
name|String
name|uri
parameter_list|,
specifier|final
name|String
name|remaining
parameter_list|,
specifier|final
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
synchronized|synchronized
init|(
name|this
init|)
block|{
if|if
condition|(
name|remaining
operator|==
literal|null
operator|||
name|remaining
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|MiloServerEndpoint
name|endpoint
init|=
name|this
operator|.
name|endpoints
operator|.
name|get
argument_list|(
name|remaining
argument_list|)
decl_stmt|;
if|if
condition|(
name|endpoint
operator|==
literal|null
condition|)
block|{
name|endpoint
operator|=
operator|new
name|MiloServerEndpoint
argument_list|(
name|uri
argument_list|,
name|remaining
argument_list|,
name|this
operator|.
name|namespace
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|setProperties
argument_list|(
name|endpoint
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoints
operator|.
name|put
argument_list|(
name|remaining
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
block|}
return|return
name|endpoint
return|;
block|}
block|}
comment|/**      * The URI of the namespace, defaults to<code>urn:org:apache:camel</code>      */
DECL|method|setNamespaceUri (final String namespaceUri)
specifier|public
name|void
name|setNamespaceUri
parameter_list|(
specifier|final
name|String
name|namespaceUri
parameter_list|)
block|{
name|this
operator|.
name|namespaceUri
operator|=
name|namespaceUri
expr_stmt|;
block|}
comment|/**      * The application name      */
DECL|method|setApplicationName (final String applicationName)
specifier|public
name|void
name|setApplicationName
parameter_list|(
specifier|final
name|String
name|applicationName
parameter_list|)
block|{
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|applicationName
argument_list|)
expr_stmt|;
name|this
operator|.
name|serverConfig
operator|.
name|setApplicationName
argument_list|(
name|LocalizedText
operator|.
name|english
argument_list|(
name|applicationName
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * The application URI      */
DECL|method|setApplicationUri (final String applicationUri)
specifier|public
name|void
name|setApplicationUri
parameter_list|(
specifier|final
name|String
name|applicationUri
parameter_list|)
block|{
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|applicationUri
argument_list|)
expr_stmt|;
name|this
operator|.
name|serverConfig
operator|.
name|setApplicationUri
argument_list|(
name|applicationUri
argument_list|)
expr_stmt|;
block|}
comment|/**      * The product URI      */
DECL|method|setProductUri (final String productUri)
specifier|public
name|void
name|setProductUri
parameter_list|(
specifier|final
name|String
name|productUri
parameter_list|)
block|{
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|productUri
argument_list|)
expr_stmt|;
name|this
operator|.
name|serverConfig
operator|.
name|setProductUri
argument_list|(
name|productUri
argument_list|)
expr_stmt|;
block|}
comment|/**      * The TCP port the server binds to      */
DECL|method|setBindPort (final int port)
specifier|public
name|void
name|setBindPort
parameter_list|(
specifier|final
name|int
name|port
parameter_list|)
block|{
name|this
operator|.
name|serverConfig
operator|.
name|setBindPort
argument_list|(
name|port
argument_list|)
expr_stmt|;
block|}
comment|/**      * Set whether strict endpoint URLs are enforced      */
DECL|method|setStrictEndpointUrlsEnabled (final boolean strictEndpointUrlsEnforced)
specifier|public
name|void
name|setStrictEndpointUrlsEnabled
parameter_list|(
specifier|final
name|boolean
name|strictEndpointUrlsEnforced
parameter_list|)
block|{
name|this
operator|.
name|serverConfig
operator|.
name|setStrictEndpointUrlsEnabled
argument_list|(
name|strictEndpointUrlsEnforced
argument_list|)
expr_stmt|;
block|}
comment|/**      * Server name      */
DECL|method|setServerName (final String serverName)
specifier|public
name|void
name|setServerName
parameter_list|(
specifier|final
name|String
name|serverName
parameter_list|)
block|{
name|this
operator|.
name|serverConfig
operator|.
name|setServerName
argument_list|(
name|serverName
argument_list|)
expr_stmt|;
block|}
comment|/**      * Server hostname      */
DECL|method|setHostname (final String hostname)
specifier|public
name|void
name|setHostname
parameter_list|(
specifier|final
name|String
name|hostname
parameter_list|)
block|{
name|this
operator|.
name|serverConfig
operator|.
name|setServerName
argument_list|(
name|hostname
argument_list|)
expr_stmt|;
block|}
comment|/**      * Security policies      */
DECL|method|setSecurityPolicies (final Set<SecurityPolicy> securityPolicies)
specifier|public
name|void
name|setSecurityPolicies
parameter_list|(
specifier|final
name|Set
argument_list|<
name|SecurityPolicy
argument_list|>
name|securityPolicies
parameter_list|)
block|{
if|if
condition|(
name|securityPolicies
operator|==
literal|null
operator|||
name|securityPolicies
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|this
operator|.
name|serverConfig
operator|.
name|setSecurityPolicies
argument_list|(
name|EnumSet
operator|.
name|noneOf
argument_list|(
name|SecurityPolicy
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|serverConfig
operator|.
name|setSecurityPolicies
argument_list|(
name|EnumSet
operator|.
name|copyOf
argument_list|(
name|securityPolicies
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Security policies by URI or name      */
DECL|method|setSecurityPoliciesById (final Collection<String> securityPolicies)
specifier|public
name|void
name|setSecurityPoliciesById
parameter_list|(
specifier|final
name|Collection
argument_list|<
name|String
argument_list|>
name|securityPolicies
parameter_list|)
block|{
specifier|final
name|EnumSet
argument_list|<
name|SecurityPolicy
argument_list|>
name|policies
init|=
name|EnumSet
operator|.
name|noneOf
argument_list|(
name|SecurityPolicy
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|securityPolicies
operator|!=
literal|null
condition|)
block|{
for|for
control|(
specifier|final
name|String
name|policyName
range|:
name|securityPolicies
control|)
block|{
specifier|final
name|SecurityPolicy
name|policy
init|=
name|SecurityPolicy
operator|.
name|fromUriSafe
argument_list|(
name|policyName
argument_list|)
operator|.
name|orElseGet
argument_list|(
parameter_list|()
lambda|->
name|SecurityPolicy
operator|.
name|valueOf
argument_list|(
name|policyName
argument_list|)
argument_list|)
decl_stmt|;
name|policies
operator|.
name|add
argument_list|(
name|policy
argument_list|)
expr_stmt|;
block|}
block|}
name|this
operator|.
name|serverConfig
operator|.
name|setSecurityPolicies
argument_list|(
name|policies
argument_list|)
expr_stmt|;
block|}
comment|/**      * Security policies by URI or name      */
DECL|method|setSecurityPoliciesById (final String... ids)
specifier|public
name|void
name|setSecurityPoliciesById
parameter_list|(
specifier|final
name|String
modifier|...
name|ids
parameter_list|)
block|{
if|if
condition|(
name|ids
operator|!=
literal|null
condition|)
block|{
name|setSecurityPoliciesById
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|ids
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|setSecurityPoliciesById
argument_list|(
operator|(
name|Collection
argument_list|<
name|String
argument_list|>
operator|)
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Set user password combinations in the form of "user1:pwd1,user2:pwd2"      *<p>      * Usernames and passwords will be URL decoded      *</p>      */
DECL|method|setUserAuthenticationCredentials (final String userAuthenticationCredentials)
specifier|public
name|void
name|setUserAuthenticationCredentials
parameter_list|(
specifier|final
name|String
name|userAuthenticationCredentials
parameter_list|)
block|{
if|if
condition|(
name|userAuthenticationCredentials
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|userMap
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
for|for
control|(
specifier|final
name|String
name|creds
range|:
name|userAuthenticationCredentials
operator|.
name|split
argument_list|(
literal|","
argument_list|)
control|)
block|{
specifier|final
name|String
index|[]
name|toks
init|=
name|creds
operator|.
name|split
argument_list|(
literal|":"
argument_list|,
literal|2
argument_list|)
decl_stmt|;
if|if
condition|(
name|toks
operator|.
name|length
operator|==
literal|2
condition|)
block|{
try|try
block|{
name|this
operator|.
name|userMap
operator|.
name|put
argument_list|(
name|URLDecoder
operator|.
name|decode
argument_list|(
name|toks
index|[
literal|0
index|]
argument_list|,
name|URL_CHARSET
argument_list|)
argument_list|,
name|URLDecoder
operator|.
name|decode
argument_list|(
name|toks
index|[
literal|1
index|]
argument_list|,
name|URL_CHARSET
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
specifier|final
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Failed to decode user map entry"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
else|else
block|{
name|this
operator|.
name|userMap
operator|=
literal|null
expr_stmt|;
block|}
block|}
comment|/**      * Enable anonymous authentication, disabled by default      */
DECL|method|setEnableAnonymousAuthentication (final boolean enableAnonymousAuthentication)
specifier|public
name|void
name|setEnableAnonymousAuthentication
parameter_list|(
specifier|final
name|boolean
name|enableAnonymousAuthentication
parameter_list|)
block|{
name|this
operator|.
name|enableAnonymousAuthentication
operator|=
name|enableAnonymousAuthentication
expr_stmt|;
block|}
comment|/**      * Set the {@link UserTokenPolicy} used when      */
DECL|method|setUsernameSecurityPolicyUri (final SecurityPolicy usernameSecurityPolicy)
specifier|public
name|void
name|setUsernameSecurityPolicyUri
parameter_list|(
specifier|final
name|SecurityPolicy
name|usernameSecurityPolicy
parameter_list|)
block|{
name|this
operator|.
name|usernameSecurityPolicyUri
operator|=
name|usernameSecurityPolicy
operator|.
name|getSecurityPolicyUri
argument_list|()
expr_stmt|;
block|}
comment|/**      * Set the {@link UserTokenPolicy} used when      */
DECL|method|setUsernameSecurityPolicyUri (String usernameSecurityPolicyUri)
specifier|public
name|void
name|setUsernameSecurityPolicyUri
parameter_list|(
name|String
name|usernameSecurityPolicyUri
parameter_list|)
block|{
name|this
operator|.
name|usernameSecurityPolicyUri
operator|=
name|usernameSecurityPolicyUri
expr_stmt|;
block|}
comment|/**      * Set the addresses of the local addresses the server should bind to      */
DECL|method|setBindAddresses (final String bindAddresses)
specifier|public
name|void
name|setBindAddresses
parameter_list|(
specifier|final
name|String
name|bindAddresses
parameter_list|)
block|{
if|if
condition|(
name|bindAddresses
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|bindAddresses
operator|=
name|Arrays
operator|.
name|asList
argument_list|(
name|bindAddresses
operator|.
name|split
argument_list|(
literal|","
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|bindAddresses
operator|=
literal|null
expr_stmt|;
block|}
block|}
comment|/**      * Server build info      */
DECL|method|setBuildInfo (final BuildInfo buildInfo)
specifier|public
name|void
name|setBuildInfo
parameter_list|(
specifier|final
name|BuildInfo
name|buildInfo
parameter_list|)
block|{
name|this
operator|.
name|serverConfig
operator|.
name|setBuildInfo
argument_list|(
name|buildInfo
argument_list|)
expr_stmt|;
block|}
comment|/**      * Server certificate      */
DECL|method|setServerCertificate (final KeyStoreLoader.Result result)
specifier|public
name|void
name|setServerCertificate
parameter_list|(
specifier|final
name|KeyStoreLoader
operator|.
name|Result
name|result
parameter_list|)
block|{
comment|/*          * We are not implicitly deactivating the server certificate manager. If          * the key could not be found by the KeyStoreLoader, it will return          * "null" from the load() method. So if someone calls          * setServerCertificate ( loader.load () ); he may, by accident, disable          * the server certificate. If disabling the server certificate is          * desired, do it explicitly.          */
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|result
argument_list|,
literal|"Setting a null is not supported. call setCertificateManager(null) instead.)"
argument_list|)
expr_stmt|;
name|setServerCertificate
argument_list|(
name|result
operator|.
name|getKeyPair
argument_list|()
argument_list|,
name|result
operator|.
name|getCertificate
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Server certificate      */
DECL|method|setServerCertificate (final KeyPair keyPair, final X509Certificate certificate)
specifier|public
name|void
name|setServerCertificate
parameter_list|(
specifier|final
name|KeyPair
name|keyPair
parameter_list|,
specifier|final
name|X509Certificate
name|certificate
parameter_list|)
block|{
name|setCertificateManager
argument_list|(
operator|new
name|DefaultCertificateManager
argument_list|(
name|keyPair
argument_list|,
name|certificate
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Server certificate manager      */
DECL|method|setCertificateManager (final CertificateManager certificateManager)
specifier|public
name|void
name|setCertificateManager
parameter_list|(
specifier|final
name|CertificateManager
name|certificateManager
parameter_list|)
block|{
if|if
condition|(
name|certificateManager
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|serverConfig
operator|.
name|setCertificateManager
argument_list|(
name|certificateManager
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|serverConfig
operator|.
name|setCertificateManager
argument_list|(
operator|new
name|DefaultCertificateManager
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Validator for client certificates      */
DECL|method|setCertificateValidator (final Supplier<CertificateValidator> certificateValidator)
specifier|public
name|void
name|setCertificateValidator
parameter_list|(
specifier|final
name|Supplier
argument_list|<
name|CertificateValidator
argument_list|>
name|certificateValidator
parameter_list|)
block|{
name|this
operator|.
name|certificateValidator
operator|=
name|certificateValidator
expr_stmt|;
block|}
comment|/**      * Validator for client certificates using default file based approach      */
DECL|method|setDefaultCertificateValidator (final File certificatesBaseDir)
specifier|public
name|void
name|setDefaultCertificateValidator
parameter_list|(
specifier|final
name|File
name|certificatesBaseDir
parameter_list|)
block|{
name|this
operator|.
name|certificateValidator
operator|=
parameter_list|()
lambda|->
operator|new
name|DefaultCertificateValidator
argument_list|(
name|certificatesBaseDir
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

