begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.bean.validator
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|bean
operator|.
name|validator
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Collections
operator|.
name|singletonList
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|validation
operator|.
name|ValidationProviderResolver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|hibernate
operator|.
name|validator
operator|.
name|HibernateValidator
import|;
end_import

begin_comment
comment|/**  * OSGi-friendly implementation of {@code javax.validation.ValidationProviderResolver} returning  * {@code org.hibernate.validator.HibernateValidator} instance.  */
end_comment

begin_class
DECL|class|HibernateValidationProviderResolver
specifier|public
class|class
name|HibernateValidationProviderResolver
implements|implements
name|ValidationProviderResolver
block|{
annotation|@
name|Override
DECL|method|getValidationProviders ()
specifier|public
name|List
name|getValidationProviders
parameter_list|()
block|{
return|return
name|singletonList
argument_list|(
operator|new
name|HibernateValidator
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

