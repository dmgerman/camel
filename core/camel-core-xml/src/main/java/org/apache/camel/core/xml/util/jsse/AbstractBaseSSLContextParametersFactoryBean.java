begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|support
operator|.
name|jsse
operator|.
name|BaseSSLContextParameters
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
name|jsse
operator|.
name|CipherSuitesParameters
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
name|jsse
operator|.
name|FilterParameters
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
name|jsse
operator|.
name|SecureSocketProtocolsParameters
import|;
end_import

begin_class
annotation|@
name|XmlTransient
DECL|class|AbstractBaseSSLContextParametersFactoryBean
specifier|public
specifier|abstract
class|class
name|AbstractBaseSSLContextParametersFactoryBean
parameter_list|<
name|T
extends|extends
name|BaseSSLContextParameters
parameter_list|>
extends|extends
name|AbstractJsseUtilFactoryBean
argument_list|<
name|T
argument_list|>
block|{
DECL|field|cipherSuites
specifier|private
name|CipherSuitesParametersDefinition
name|cipherSuites
decl_stmt|;
DECL|field|cipherSuitesFilter
specifier|private
name|FilterParametersDefinition
name|cipherSuitesFilter
decl_stmt|;
DECL|field|secureSocketProtocols
specifier|private
name|SecureSocketProtocolsParametersDefinition
name|secureSocketProtocols
decl_stmt|;
DECL|field|secureSocketProtocolsFilter
specifier|private
name|FilterParametersDefinition
name|secureSocketProtocolsFilter
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|sessionTimeout
specifier|private
name|String
name|sessionTimeout
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|instance
specifier|private
name|T
name|instance
decl_stmt|;
annotation|@
name|Override
DECL|method|getObject ()
specifier|public
specifier|final
name|T
name|getObject
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|this
operator|.
name|isSingleton
argument_list|()
condition|)
block|{
if|if
condition|(
name|instance
operator|==
literal|null
condition|)
block|{
name|instance
operator|=
name|createInstanceInternal
argument_list|()
expr_stmt|;
block|}
return|return
name|instance
return|;
block|}
else|else
block|{
return|return
name|createInstanceInternal
argument_list|()
return|;
block|}
block|}
DECL|method|createInstance ()
specifier|protected
specifier|abstract
name|T
name|createInstance
parameter_list|()
throws|throws
name|Exception
function_decl|;
DECL|method|createInstanceInternal ()
specifier|private
name|T
name|createInstanceInternal
parameter_list|()
throws|throws
name|Exception
block|{
name|T
name|newInstance
init|=
name|createInstance
argument_list|()
decl_stmt|;
name|newInstance
operator|.
name|setCamelContext
argument_list|(
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|cipherSuites
operator|!=
literal|null
condition|)
block|{
name|CipherSuitesParameters
name|cipherSuitesInstance
init|=
operator|new
name|CipherSuitesParameters
argument_list|()
decl_stmt|;
name|cipherSuitesInstance
operator|.
name|setCipherSuite
argument_list|(
name|cipherSuites
operator|.
name|getCipherSuite
argument_list|()
argument_list|)
expr_stmt|;
name|newInstance
operator|.
name|setCipherSuites
argument_list|(
name|cipherSuitesInstance
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|cipherSuitesFilter
operator|!=
literal|null
condition|)
block|{
name|newInstance
operator|.
name|setCipherSuitesFilter
argument_list|(
name|createFilterParameters
argument_list|(
name|cipherSuitesFilter
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|secureSocketProtocols
operator|!=
literal|null
condition|)
block|{
name|SecureSocketProtocolsParameters
name|secureSocketProtocolsInstance
init|=
operator|new
name|SecureSocketProtocolsParameters
argument_list|()
decl_stmt|;
name|secureSocketProtocolsInstance
operator|.
name|setSecureSocketProtocol
argument_list|(
name|secureSocketProtocols
operator|.
name|getSecureSocketProtocol
argument_list|()
argument_list|)
expr_stmt|;
name|newInstance
operator|.
name|setSecureSocketProtocols
argument_list|(
name|secureSocketProtocolsInstance
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|secureSocketProtocolsFilter
operator|!=
literal|null
condition|)
block|{
name|newInstance
operator|.
name|setSecureSocketProtocolsFilter
argument_list|(
name|createFilterParameters
argument_list|(
name|secureSocketProtocolsFilter
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|sessionTimeout
operator|!=
literal|null
condition|)
block|{
name|newInstance
operator|.
name|setSessionTimeout
argument_list|(
name|sessionTimeout
argument_list|)
expr_stmt|;
block|}
return|return
name|newInstance
return|;
block|}
DECL|method|createFilterParameters (FilterParametersDefinition definition)
specifier|private
name|FilterParameters
name|createFilterParameters
parameter_list|(
name|FilterParametersDefinition
name|definition
parameter_list|)
block|{
name|FilterParameters
name|filter
init|=
operator|new
name|FilterParameters
argument_list|()
decl_stmt|;
name|filter
operator|.
name|getInclude
argument_list|()
operator|.
name|addAll
argument_list|(
name|definition
operator|.
name|getInclude
argument_list|()
argument_list|)
expr_stmt|;
name|filter
operator|.
name|getExclude
argument_list|()
operator|.
name|addAll
argument_list|(
name|definition
operator|.
name|getExclude
argument_list|()
argument_list|)
expr_stmt|;
name|filter
operator|.
name|setCamelContext
argument_list|(
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|filter
return|;
block|}
DECL|method|getCipherSuites ()
specifier|public
name|CipherSuitesParametersDefinition
name|getCipherSuites
parameter_list|()
block|{
return|return
name|cipherSuites
return|;
block|}
DECL|method|setCipherSuites (CipherSuitesParametersDefinition cipherSuites)
specifier|public
name|void
name|setCipherSuites
parameter_list|(
name|CipherSuitesParametersDefinition
name|cipherSuites
parameter_list|)
block|{
name|this
operator|.
name|cipherSuites
operator|=
name|cipherSuites
expr_stmt|;
block|}
DECL|method|getCipherSuitesFilter ()
specifier|public
name|FilterParametersDefinition
name|getCipherSuitesFilter
parameter_list|()
block|{
return|return
name|cipherSuitesFilter
return|;
block|}
DECL|method|setCipherSuitesFilter (FilterParametersDefinition cipherSuitesFilter)
specifier|public
name|void
name|setCipherSuitesFilter
parameter_list|(
name|FilterParametersDefinition
name|cipherSuitesFilter
parameter_list|)
block|{
name|this
operator|.
name|cipherSuitesFilter
operator|=
name|cipherSuitesFilter
expr_stmt|;
block|}
DECL|method|getSecureSocketProtocols ()
specifier|public
name|SecureSocketProtocolsParametersDefinition
name|getSecureSocketProtocols
parameter_list|()
block|{
return|return
name|secureSocketProtocols
return|;
block|}
DECL|method|setSecureSocketProtocols (SecureSocketProtocolsParametersDefinition secureSocketProtocols)
specifier|public
name|void
name|setSecureSocketProtocols
parameter_list|(
name|SecureSocketProtocolsParametersDefinition
name|secureSocketProtocols
parameter_list|)
block|{
name|this
operator|.
name|secureSocketProtocols
operator|=
name|secureSocketProtocols
expr_stmt|;
block|}
DECL|method|getSecureSocketProtocolsFilter ()
specifier|public
name|FilterParametersDefinition
name|getSecureSocketProtocolsFilter
parameter_list|()
block|{
return|return
name|secureSocketProtocolsFilter
return|;
block|}
DECL|method|setSecureSocketProtocolsFilter (FilterParametersDefinition secureSocketProtocolsFilter)
specifier|public
name|void
name|setSecureSocketProtocolsFilter
parameter_list|(
name|FilterParametersDefinition
name|secureSocketProtocolsFilter
parameter_list|)
block|{
name|this
operator|.
name|secureSocketProtocolsFilter
operator|=
name|secureSocketProtocolsFilter
expr_stmt|;
block|}
DECL|method|getSessionTimeout ()
specifier|public
name|String
name|getSessionTimeout
parameter_list|()
block|{
return|return
name|sessionTimeout
return|;
block|}
DECL|method|setSessionTimeout (String sessionTimeout)
specifier|public
name|void
name|setSessionTimeout
parameter_list|(
name|String
name|sessionTimeout
parameter_list|)
block|{
name|this
operator|.
name|sessionTimeout
operator|=
name|sessionTimeout
expr_stmt|;
block|}
block|}
end_class

end_unit

