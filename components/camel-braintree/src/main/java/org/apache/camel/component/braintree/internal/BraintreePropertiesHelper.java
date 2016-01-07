begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.braintree.internal
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
operator|.
name|internal
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
name|BraintreeConfiguration
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
name|ApiMethodPropertiesHelper
import|;
end_import

begin_comment
comment|/**  * Singleton {@link ApiMethodPropertiesHelper} for Braintree component.  */
end_comment

begin_class
DECL|class|BraintreePropertiesHelper
specifier|public
specifier|final
class|class
name|BraintreePropertiesHelper
extends|extends
name|ApiMethodPropertiesHelper
argument_list|<
name|BraintreeConfiguration
argument_list|>
block|{
DECL|field|helper
specifier|private
specifier|static
name|BraintreePropertiesHelper
name|helper
decl_stmt|;
DECL|method|BraintreePropertiesHelper ()
specifier|private
name|BraintreePropertiesHelper
parameter_list|()
block|{
name|super
argument_list|(
name|BraintreeConfiguration
operator|.
name|class
argument_list|,
name|BraintreeConstants
operator|.
name|PROPERTY_PREFIX
argument_list|)
expr_stmt|;
block|}
DECL|method|getHelper ()
specifier|public
specifier|static
specifier|synchronized
name|BraintreePropertiesHelper
name|getHelper
parameter_list|()
block|{
if|if
condition|(
name|helper
operator|==
literal|null
condition|)
block|{
name|helper
operator|=
operator|new
name|BraintreePropertiesHelper
argument_list|()
expr_stmt|;
block|}
return|return
name|helper
return|;
block|}
block|}
end_class

end_unit

