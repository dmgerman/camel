begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|braintree
operator|.
name|internal
operator|.
name|BraintreeApiName
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
name|braintree
operator|.
name|internal
operator|.
name|BraintreePropertiesHelper
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
name|AbstractApiProducer
import|;
end_import

begin_comment
comment|/**  * The Braintree producer.  */
end_comment

begin_class
DECL|class|BraintreeProducer
specifier|public
class|class
name|BraintreeProducer
extends|extends
name|AbstractApiProducer
argument_list|<
name|BraintreeApiName
argument_list|,
name|BraintreeConfiguration
argument_list|>
block|{
DECL|method|BraintreeProducer (BraintreeEndpoint endpoint)
specifier|public
name|BraintreeProducer
parameter_list|(
name|BraintreeEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|BraintreePropertiesHelper
operator|.
name|getHelper
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

