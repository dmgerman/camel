begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.as2
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|as2
package|;
end_package

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|Security
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
name|as2
operator|.
name|internal
operator|.
name|AS2ApiCollection
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
name|as2
operator|.
name|internal
operator|.
name|AS2ApiName
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
name|component
operator|.
name|AbstractApiComponent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|jce
operator|.
name|provider
operator|.
name|BouncyCastleProvider
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

begin_comment
comment|/**  * Represents the component that manages {@link AS2Endpoint}.  */
end_comment

begin_class
DECL|class|AS2Component
specifier|public
class|class
name|AS2Component
extends|extends
name|AbstractApiComponent
argument_list|<
name|AS2ApiName
argument_list|,
name|AS2Configuration
argument_list|,
name|AS2ApiCollection
argument_list|>
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|AS2Component
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|AS2Component ()
specifier|public
name|AS2Component
parameter_list|()
block|{
name|super
argument_list|(
name|AS2Endpoint
operator|.
name|class
argument_list|,
name|AS2ApiName
operator|.
name|class
argument_list|,
name|AS2ApiCollection
operator|.
name|getCollection
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|AS2Component (CamelContext context)
specifier|public
name|AS2Component
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|,
name|AS2Endpoint
operator|.
name|class
argument_list|,
name|AS2ApiName
operator|.
name|class
argument_list|,
name|AS2ApiCollection
operator|.
name|getCollection
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getApiName (String apiNameStr)
specifier|protected
name|AS2ApiName
name|getApiName
parameter_list|(
name|String
name|apiNameStr
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
return|return
name|AS2ApiName
operator|.
name|fromValue
argument_list|(
name|apiNameStr
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String methodName, AS2ApiName apiName, AS2Configuration endpointConfiguration)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|methodName
parameter_list|,
name|AS2ApiName
name|apiName
parameter_list|,
name|AS2Configuration
name|endpointConfiguration
parameter_list|)
block|{
name|AS2Endpoint
name|endpoint
init|=
operator|new
name|AS2Endpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|apiName
argument_list|,
name|methodName
argument_list|,
name|endpointConfiguration
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|setName
argument_list|(
name|methodName
argument_list|)
expr_stmt|;
return|return
name|endpoint
return|;
block|}
comment|/**      * To use the shared configuration      */
annotation|@
name|Override
DECL|method|setConfiguration (AS2Configuration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|AS2Configuration
name|configuration
parameter_list|)
block|{
name|super
operator|.
name|setConfiguration
argument_list|(
name|configuration
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
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
if|if
condition|(
name|Security
operator|.
name|getProvider
argument_list|(
literal|"BC"
argument_list|)
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Adding BouncyCastleProvider as security provider"
argument_list|)
expr_stmt|;
name|Security
operator|.
name|addProvider
argument_list|(
operator|new
name|BouncyCastleProvider
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

