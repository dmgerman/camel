begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.spi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|spi
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
name|Component
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
name|ComponentResolver
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
name|NoSuchBeanDefinitionException
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
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
operator|.
name|notNull
import|;
end_import

begin_comment
comment|/**  * An implementation of {@link ComponentResolver} which tries to find a Camel  * {@link Component} in the Spring {@link ApplicationContext} first; if its not  * there it defaults to the auto-discovery mechanism.  *   * @version $Revision$  */
end_comment

begin_class
DECL|class|SpringComponentResolver
specifier|public
class|class
name|SpringComponentResolver
implements|implements
name|ComponentResolver
block|{
DECL|field|applicationContext
specifier|private
specifier|final
name|ApplicationContext
name|applicationContext
decl_stmt|;
DECL|field|nextResolver
specifier|private
specifier|final
name|ComponentResolver
name|nextResolver
decl_stmt|;
DECL|method|SpringComponentResolver (ApplicationContext applicationContext, ComponentResolver nextResolver)
specifier|public
name|SpringComponentResolver
parameter_list|(
name|ApplicationContext
name|applicationContext
parameter_list|,
name|ComponentResolver
name|nextResolver
parameter_list|)
block|{
name|notNull
argument_list|(
name|applicationContext
argument_list|,
literal|"applicationContext"
argument_list|)
expr_stmt|;
name|this
operator|.
name|applicationContext
operator|=
name|applicationContext
expr_stmt|;
name|this
operator|.
name|nextResolver
operator|=
name|nextResolver
expr_stmt|;
block|}
DECL|method|resolveComponent (String name, CamelContext context)
specifier|public
name|Component
name|resolveComponent
parameter_list|(
name|String
name|name
parameter_list|,
name|CamelContext
name|context
parameter_list|)
throws|throws
name|Exception
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
name|applicationContext
operator|.
name|getBean
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchBeanDefinitionException
name|e
parameter_list|)
block|{
comment|// ignore its not an error
block|}
if|if
condition|(
name|bean
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|bean
operator|instanceof
name|Component
condition|)
block|{
return|return
operator|(
name|Component
operator|)
name|bean
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Bean with name: "
operator|+
name|name
operator|+
literal|" in spring context is not a Component: "
operator|+
name|bean
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|nextResolver
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|nextResolver
operator|.
name|resolveComponent
argument_list|(
name|name
argument_list|,
name|context
argument_list|)
return|;
block|}
block|}
end_class

end_unit

