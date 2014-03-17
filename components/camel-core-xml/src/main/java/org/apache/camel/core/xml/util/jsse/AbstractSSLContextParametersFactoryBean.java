begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.core.xml.util.jsse
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|core
operator|.
name|xml
operator|.
name|util
operator|.
name|jsse
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAttribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlTransient
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
name|jsse
operator|.
name|SSLContextParameters
import|;
end_import

begin_class
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
annotation|@
name|XmlTransient
DECL|class|AbstractSSLContextParametersFactoryBean
specifier|public
specifier|abstract
class|class
name|AbstractSSLContextParametersFactoryBean
extends|extends
name|AbstractBaseSSLContextParametersFactoryBean
argument_list|<
name|SSLContextParameters
argument_list|>
block|{
annotation|@
name|XmlAttribute
DECL|field|provider
specifier|private
name|String
name|provider
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|secureSocketProtocol
specifier|private
name|String
name|secureSocketProtocol
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|certAlias
specifier|private
name|String
name|certAlias
decl_stmt|;
annotation|@
name|Override
DECL|method|createInstance ()
specifier|protected
name|SSLContextParameters
name|createInstance
parameter_list|()
throws|throws
name|Exception
block|{
name|SSLContextParameters
name|newInstance
init|=
operator|new
name|SSLContextParameters
argument_list|()
decl_stmt|;
if|if
condition|(
name|getKeyManagers
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|getKeyManagers
argument_list|()
operator|.
name|setCamelContext
argument_list|(
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
name|newInstance
operator|.
name|setKeyManagers
argument_list|(
name|getKeyManagers
argument_list|()
operator|.
name|getObject
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getTrustManagers
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|getTrustManagers
argument_list|()
operator|.
name|setCamelContext
argument_list|(
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
name|newInstance
operator|.
name|setTrustManagers
argument_list|(
name|getTrustManagers
argument_list|()
operator|.
name|getObject
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getSecureRandom
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|getSecureRandom
argument_list|()
operator|.
name|setCamelContext
argument_list|(
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
name|newInstance
operator|.
name|setSecureRandom
argument_list|(
name|getSecureRandom
argument_list|()
operator|.
name|getObject
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getClientParameters
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|getClientParameters
argument_list|()
operator|.
name|setCamelContext
argument_list|(
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
name|newInstance
operator|.
name|setClientParameters
argument_list|(
name|getClientParameters
argument_list|()
operator|.
name|getObject
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getServerParameters
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|getServerParameters
argument_list|()
operator|.
name|setCamelContext
argument_list|(
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
name|newInstance
operator|.
name|setServerParameters
argument_list|(
name|getServerParameters
argument_list|()
operator|.
name|getObject
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|newInstance
operator|.
name|setProvider
argument_list|(
name|provider
argument_list|)
expr_stmt|;
name|newInstance
operator|.
name|setSecureSocketProtocol
argument_list|(
name|secureSocketProtocol
argument_list|)
expr_stmt|;
name|newInstance
operator|.
name|setCertAlias
argument_list|(
name|certAlias
argument_list|)
expr_stmt|;
name|newInstance
operator|.
name|setCamelContext
argument_list|(
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|newInstance
return|;
block|}
annotation|@
name|Override
DECL|method|getObjectType ()
specifier|public
name|Class
argument_list|<
name|?
extends|extends
name|SSLContextParameters
argument_list|>
name|getObjectType
parameter_list|()
block|{
return|return
name|SSLContextParameters
operator|.
name|class
return|;
block|}
DECL|method|getProvider ()
specifier|public
name|String
name|getProvider
parameter_list|()
block|{
return|return
name|provider
return|;
block|}
DECL|method|setProvider (String provider)
specifier|public
name|void
name|setProvider
parameter_list|(
name|String
name|provider
parameter_list|)
block|{
name|this
operator|.
name|provider
operator|=
name|provider
expr_stmt|;
block|}
DECL|method|getSecureSocketProtocol ()
specifier|public
name|String
name|getSecureSocketProtocol
parameter_list|()
block|{
return|return
name|secureSocketProtocol
return|;
block|}
DECL|method|setSecureSocketProtocol (String secureSocketProtocol)
specifier|public
name|void
name|setSecureSocketProtocol
parameter_list|(
name|String
name|secureSocketProtocol
parameter_list|)
block|{
name|this
operator|.
name|secureSocketProtocol
operator|=
name|secureSocketProtocol
expr_stmt|;
block|}
DECL|method|getCertAlias ()
specifier|public
name|String
name|getCertAlias
parameter_list|()
block|{
return|return
name|certAlias
return|;
block|}
DECL|method|setCertAlias (String certAlias)
specifier|public
name|void
name|setCertAlias
parameter_list|(
name|String
name|certAlias
parameter_list|)
block|{
name|this
operator|.
name|certAlias
operator|=
name|certAlias
expr_stmt|;
block|}
DECL|method|getKeyManagers ()
specifier|protected
specifier|abstract
name|AbstractKeyManagersParametersFactoryBean
name|getKeyManagers
parameter_list|()
function_decl|;
DECL|method|getTrustManagers ()
specifier|protected
specifier|abstract
name|AbstractTrustManagersParametersFactoryBean
name|getTrustManagers
parameter_list|()
function_decl|;
DECL|method|getSecureRandom ()
specifier|protected
specifier|abstract
name|AbstractSecureRandomParametersFactoryBean
name|getSecureRandom
parameter_list|()
function_decl|;
DECL|method|getClientParameters ()
specifier|protected
specifier|abstract
name|AbstractSSLContextClientParametersFactoryBean
name|getClientParameters
parameter_list|()
function_decl|;
DECL|method|getServerParameters ()
specifier|protected
specifier|abstract
name|AbstractSSLContextServerParametersFactoryBean
name|getServerParameters
parameter_list|()
function_decl|;
block|}
end_class

end_unit

