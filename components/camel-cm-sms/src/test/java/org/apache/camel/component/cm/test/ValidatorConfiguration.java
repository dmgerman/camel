begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cm.test
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cm
operator|.
name|test
package|;
end_package

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|annotation
operator|.
name|Bean
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
name|annotation
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|validation
operator|.
name|beanvalidation
operator|.
name|LocalValidatorFactoryBean
import|;
end_import

begin_comment
comment|/**  * Builds a SimpleRoute to send a message to CM GW and CM Uri is built based on properties in a file.  */
end_comment

begin_class
annotation|@
name|Configuration
argument_list|(
literal|"smsConfig"
argument_list|)
DECL|class|ValidatorConfiguration
specifier|public
class|class
name|ValidatorConfiguration
block|{
annotation|@
name|Bean
DECL|method|getValidatorFactory ()
specifier|public
name|LocalValidatorFactoryBean
name|getValidatorFactory
parameter_list|()
block|{
specifier|final
name|LocalValidatorFactoryBean
name|localValidatorFactoryBean
init|=
operator|new
name|LocalValidatorFactoryBean
argument_list|()
decl_stmt|;
name|localValidatorFactoryBean
operator|.
name|getValidationPropertyMap
argument_list|()
operator|.
name|put
argument_list|(
literal|"hibernate.validator.fail_fast"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
return|return
name|localValidatorFactoryBean
return|;
block|}
block|}
end_class

end_unit

