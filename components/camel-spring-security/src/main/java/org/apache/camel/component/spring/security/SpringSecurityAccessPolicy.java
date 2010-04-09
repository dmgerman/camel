begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.spring.security
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|spring
operator|.
name|security
package|;
end_package

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|security
operator|.
name|ConfigAttributeDefinition
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|util
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|util
operator|.
name|StringUtils
import|;
end_import

begin_class
DECL|class|SpringSecurityAccessPolicy
specifier|public
class|class
name|SpringSecurityAccessPolicy
block|{
DECL|field|configAttributeDefinition
specifier|private
specifier|final
name|ConfigAttributeDefinition
name|configAttributeDefinition
decl_stmt|;
DECL|method|SpringSecurityAccessPolicy (String access)
specifier|public
name|SpringSecurityAccessPolicy
parameter_list|(
name|String
name|access
parameter_list|)
block|{
name|Assert
operator|.
name|isTrue
argument_list|(
name|access
operator|!=
literal|null
argument_list|,
literal|"The access attribute must not be null."
argument_list|)
expr_stmt|;
name|String
index|[]
name|accessValues
init|=
name|StringUtils
operator|.
name|trimArrayElements
argument_list|(
name|StringUtils
operator|.
name|commaDelimitedListToStringArray
argument_list|(
name|access
argument_list|)
argument_list|)
decl_stmt|;
name|this
operator|.
name|configAttributeDefinition
operator|=
operator|(
name|accessValues
operator|.
name|length
operator|>
literal|0
operator|)
condition|?
operator|new
name|ConfigAttributeDefinition
argument_list|(
name|accessValues
argument_list|)
else|:
literal|null
expr_stmt|;
block|}
DECL|method|getConfigAttributeDefinition ()
specifier|public
name|ConfigAttributeDefinition
name|getConfigAttributeDefinition
parameter_list|()
block|{
return|return
name|this
operator|.
name|configAttributeDefinition
return|;
block|}
block|}
end_class

end_unit

