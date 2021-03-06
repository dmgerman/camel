begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util.spring
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|spring
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
name|XmlRootElement
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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlType
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
name|CamelContext
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
name|core
operator|.
name|xml
operator|.
name|util
operator|.
name|jsse
operator|.
name|AbstractSSLContextParametersFactoryBean
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
name|spring
operator|.
name|util
operator|.
name|CamelContextResolverHelper
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
name|SSLContextParameters
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|FactoryBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationContextAware
import|;
end_import

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"sslContextParameters"
argument_list|)
annotation|@
name|XmlType
argument_list|(
name|propOrder
operator|=
block|{}
argument_list|)
DECL|class|SSLContextParametersFactoryBean
specifier|public
class|class
name|SSLContextParametersFactoryBean
extends|extends
name|AbstractSSLContextParametersFactoryBean
implements|implements
name|FactoryBean
argument_list|<
name|SSLContextParameters
argument_list|>
implements|,
name|ApplicationContextAware
block|{
DECL|field|keyManagers
specifier|private
name|KeyManagersParametersFactoryBean
name|keyManagers
decl_stmt|;
DECL|field|trustManagers
specifier|private
name|TrustManagersParametersFactoryBean
name|trustManagers
decl_stmt|;
DECL|field|secureRandom
specifier|private
name|SecureRandomParametersFactoryBean
name|secureRandom
decl_stmt|;
DECL|field|clientParameters
specifier|private
name|SSLContextClientParametersFactoryBean
name|clientParameters
decl_stmt|;
DECL|field|serverParameters
specifier|private
name|SSLContextServerParametersFactoryBean
name|serverParameters
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|applicationContext
specifier|private
name|ApplicationContext
name|applicationContext
decl_stmt|;
annotation|@
name|Override
DECL|method|getKeyManagers ()
specifier|public
name|KeyManagersParametersFactoryBean
name|getKeyManagers
parameter_list|()
block|{
return|return
name|keyManagers
return|;
block|}
DECL|method|setKeyManagers (KeyManagersParametersFactoryBean keyManagers)
specifier|public
name|void
name|setKeyManagers
parameter_list|(
name|KeyManagersParametersFactoryBean
name|keyManagers
parameter_list|)
block|{
name|this
operator|.
name|keyManagers
operator|=
name|keyManagers
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getTrustManagers ()
specifier|public
name|TrustManagersParametersFactoryBean
name|getTrustManagers
parameter_list|()
block|{
return|return
name|trustManagers
return|;
block|}
DECL|method|setTrustManagers (TrustManagersParametersFactoryBean trustManagers)
specifier|public
name|void
name|setTrustManagers
parameter_list|(
name|TrustManagersParametersFactoryBean
name|trustManagers
parameter_list|)
block|{
name|this
operator|.
name|trustManagers
operator|=
name|trustManagers
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getSecureRandom ()
specifier|public
name|SecureRandomParametersFactoryBean
name|getSecureRandom
parameter_list|()
block|{
return|return
name|secureRandom
return|;
block|}
DECL|method|setSecureRandom (SecureRandomParametersFactoryBean secureRandom)
specifier|public
name|void
name|setSecureRandom
parameter_list|(
name|SecureRandomParametersFactoryBean
name|secureRandom
parameter_list|)
block|{
name|this
operator|.
name|secureRandom
operator|=
name|secureRandom
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getClientParameters ()
specifier|public
name|SSLContextClientParametersFactoryBean
name|getClientParameters
parameter_list|()
block|{
return|return
name|clientParameters
return|;
block|}
DECL|method|setClientParameters (SSLContextClientParametersFactoryBean clientParameters)
specifier|public
name|void
name|setClientParameters
parameter_list|(
name|SSLContextClientParametersFactoryBean
name|clientParameters
parameter_list|)
block|{
name|this
operator|.
name|clientParameters
operator|=
name|clientParameters
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getServerParameters ()
specifier|public
name|SSLContextServerParametersFactoryBean
name|getServerParameters
parameter_list|()
block|{
return|return
name|serverParameters
return|;
block|}
DECL|method|setServerParameters (SSLContextServerParametersFactoryBean serverParameters)
specifier|public
name|void
name|setServerParameters
parameter_list|(
name|SSLContextServerParametersFactoryBean
name|serverParameters
parameter_list|)
block|{
name|this
operator|.
name|serverParameters
operator|=
name|serverParameters
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getCamelContextWithId (String camelContextId)
specifier|protected
name|CamelContext
name|getCamelContextWithId
parameter_list|(
name|String
name|camelContextId
parameter_list|)
block|{
return|return
name|CamelContextResolverHelper
operator|.
name|getCamelContextWithId
argument_list|(
name|applicationContext
argument_list|,
name|camelContextId
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|setApplicationContext (ApplicationContext applicationContext)
specifier|public
name|void
name|setApplicationContext
parameter_list|(
name|ApplicationContext
name|applicationContext
parameter_list|)
block|{
name|this
operator|.
name|applicationContext
operator|=
name|applicationContext
expr_stmt|;
block|}
block|}
end_class

end_unit

