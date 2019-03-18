begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.braintree
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|braintree
package|;
end_package

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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|extension
operator|.
name|verifier
operator|.
name|DefaultComponentVerifierExtension
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
name|extension
operator|.
name|verifier
operator|.
name|OptionsGroup
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
name|extension
operator|.
name|verifier
operator|.
name|ResultBuilder
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
name|extension
operator|.
name|verifier
operator|.
name|ResultErrorHelper
import|;
end_import

begin_class
DECL|class|BraintreeComponentVerifierExtension
specifier|public
class|class
name|BraintreeComponentVerifierExtension
extends|extends
name|DefaultComponentVerifierExtension
block|{
DECL|method|BraintreeComponentVerifierExtension ()
name|BraintreeComponentVerifierExtension
parameter_list|()
block|{
name|super
argument_list|(
literal|"braintree"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|verifyParameters (Map<String, Object> parameters)
specifier|protected
name|Result
name|verifyParameters
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
block|{
comment|// Validate mandatory component options, needed to be done here as these
comment|// options are not properly marked as mandatory in the catalog.
name|ResultBuilder
name|builder
init|=
name|ResultBuilder
operator|.
name|withStatusAndScope
argument_list|(
name|Result
operator|.
name|Status
operator|.
name|OK
argument_list|,
name|Scope
operator|.
name|PARAMETERS
argument_list|)
operator|.
name|errors
argument_list|(
name|ResultErrorHelper
operator|.
name|requiresAny
argument_list|(
name|parameters
argument_list|,
name|OptionsGroup
operator|.
name|withName
argument_list|(
name|AuthenticationType
operator|.
name|PUBLIC_PRIVATE_KEYS
argument_list|)
operator|.
name|options
argument_list|(
literal|"environment"
argument_list|,
literal|"merchantId"
argument_list|,
literal|"publicKey"
argument_list|,
literal|"privateKey"
argument_list|,
literal|"!accessToken"
argument_list|)
argument_list|,
name|OptionsGroup
operator|.
name|withName
argument_list|(
name|AuthenticationType
operator|.
name|ACCESS_TOKEN
argument_list|)
operator|.
name|options
argument_list|(
literal|"!environment"
argument_list|,
literal|"!merchantId"
argument_list|,
literal|"!publicKey"
argument_list|,
literal|"!privateKey"
argument_list|,
literal|"accessToken"
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
comment|// Validate using the catalog
name|super
operator|.
name|verifyParametersAgainstCatalog
argument_list|(
name|builder
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
name|builder
operator|.
name|build
argument_list|()
return|;
block|}
block|}
end_class

end_unit

