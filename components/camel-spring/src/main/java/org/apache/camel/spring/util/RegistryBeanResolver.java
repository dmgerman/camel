begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|util
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
name|NoSuchBeanException
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
name|spi
operator|.
name|Registry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|expression
operator|.
name|AccessException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|expression
operator|.
name|BeanResolver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|expression
operator|.
name|EvaluationContext
import|;
end_import

begin_comment
comment|/**  * EL bean resolver that operates against a Camel {@link Registry}.  */
end_comment

begin_class
DECL|class|RegistryBeanResolver
specifier|public
specifier|final
class|class
name|RegistryBeanResolver
implements|implements
name|BeanResolver
block|{
DECL|field|registry
specifier|private
specifier|final
name|Registry
name|registry
decl_stmt|;
DECL|method|RegistryBeanResolver (Registry registry)
specifier|public
name|RegistryBeanResolver
parameter_list|(
name|Registry
name|registry
parameter_list|)
block|{
name|this
operator|.
name|registry
operator|=
name|registry
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|resolve (EvaluationContext context, String beanName)
specifier|public
name|Object
name|resolve
parameter_list|(
name|EvaluationContext
name|context
parameter_list|,
name|String
name|beanName
parameter_list|)
throws|throws
name|AccessException
block|{
name|Object
name|bean
init|=
literal|null
decl_stmt|;
try|try
block|{
name|bean
operator|=
name|registry
operator|.
name|lookupByName
argument_list|(
name|beanName
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchBeanException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
if|if
condition|(
name|bean
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|AccessException
argument_list|(
literal|"Could not resolve bean reference against Registry"
argument_list|)
throw|;
block|}
return|return
name|bean
return|;
block|}
block|}
end_class

end_unit

