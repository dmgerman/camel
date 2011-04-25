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
name|net
operator|.
name|ssl
operator|.
name|KeyManager
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
name|KeyManagersParameters
import|;
end_import

begin_comment
comment|/**  * A representation of configuration options for creating and loading  * {@link KeyManager} instance(s).  */
end_comment

begin_class
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|AbstractKeyManagersParametersFactoryBean
specifier|public
specifier|abstract
class|class
name|AbstractKeyManagersParametersFactoryBean
extends|extends
name|AbstractJsseUtilFactoryBean
argument_list|<
name|KeyManagersParameters
argument_list|>
block|{
annotation|@
name|XmlAttribute
DECL|field|keyPassword
specifier|protected
name|String
name|keyPassword
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|provider
specifier|protected
name|String
name|provider
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|algorithm
specifier|protected
name|String
name|algorithm
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|instance
specifier|private
name|KeyManagersParameters
name|instance
decl_stmt|;
DECL|method|getKeyPassword ()
specifier|public
name|String
name|getKeyPassword
parameter_list|()
block|{
return|return
name|keyPassword
return|;
block|}
DECL|method|setKeyPassword (String value)
specifier|public
name|void
name|setKeyPassword
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|keyPassword
operator|=
name|value
expr_stmt|;
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
DECL|method|setProvider (String value)
specifier|public
name|void
name|setProvider
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|provider
operator|=
name|value
expr_stmt|;
block|}
DECL|method|getAlgorithm ()
specifier|public
name|String
name|getAlgorithm
parameter_list|()
block|{
return|return
name|algorithm
return|;
block|}
DECL|method|setAlgorithm (String value)
specifier|public
name|void
name|setAlgorithm
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|algorithm
operator|=
name|value
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getObject ()
specifier|public
name|KeyManagersParameters
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
name|createInstance
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
name|createInstance
argument_list|()
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|getObjectType ()
specifier|public
name|Class
argument_list|<
name|?
extends|extends
name|KeyManagersParameters
argument_list|>
name|getObjectType
parameter_list|()
block|{
return|return
name|KeyManagersParameters
operator|.
name|class
return|;
block|}
DECL|method|createInstance ()
specifier|protected
name|KeyManagersParameters
name|createInstance
parameter_list|()
throws|throws
name|Exception
block|{
name|KeyManagersParameters
name|newInstance
init|=
operator|new
name|KeyManagersParameters
argument_list|()
decl_stmt|;
name|newInstance
operator|.
name|setAlgorithm
argument_list|(
name|algorithm
argument_list|)
expr_stmt|;
name|newInstance
operator|.
name|setKeyPassword
argument_list|(
name|keyPassword
argument_list|)
expr_stmt|;
if|if
condition|(
name|getKeyStore
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|newInstance
operator|.
name|setKeyStore
argument_list|(
name|getKeyStore
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
return|return
name|newInstance
return|;
block|}
DECL|method|getKeyStore ()
specifier|public
specifier|abstract
name|AbstractKeyStoreParametersFactoryBean
name|getKeyStore
parameter_list|()
function_decl|;
block|}
end_class

end_unit

