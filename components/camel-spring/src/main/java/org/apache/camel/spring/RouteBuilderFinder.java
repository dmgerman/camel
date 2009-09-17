begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Modifier
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|Set
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
name|RoutesBuilder
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
name|PackageScanClassResolver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
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
name|config
operator|.
name|BeanPostProcessor
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

begin_comment
comment|/**  * A helper class which will find all {@link RouteBuilder} instances on the classpath  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|RouteBuilderFinder
specifier|public
class|class
name|RouteBuilderFinder
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|RouteBuilderFinder
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|camelContext
specifier|private
specifier|final
name|SpringCamelContext
name|camelContext
decl_stmt|;
DECL|field|packages
specifier|private
specifier|final
name|String
index|[]
name|packages
decl_stmt|;
DECL|field|resolver
specifier|private
name|PackageScanClassResolver
name|resolver
decl_stmt|;
DECL|field|applicationContext
specifier|private
name|ApplicationContext
name|applicationContext
decl_stmt|;
DECL|field|beanPostProcessor
specifier|private
name|BeanPostProcessor
name|beanPostProcessor
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|RouteBuilderFinder (SpringCamelContext camelContext, String[] packages, ClassLoader classLoader, BeanPostProcessor postProcessor, PackageScanClassResolver resolver)
specifier|public
name|RouteBuilderFinder
parameter_list|(
name|SpringCamelContext
name|camelContext
parameter_list|,
name|String
index|[]
name|packages
parameter_list|,
name|ClassLoader
name|classLoader
parameter_list|,
name|BeanPostProcessor
name|postProcessor
parameter_list|,
name|PackageScanClassResolver
name|resolver
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
name|this
operator|.
name|applicationContext
operator|=
name|camelContext
operator|.
name|getApplicationContext
argument_list|()
expr_stmt|;
name|this
operator|.
name|packages
operator|=
name|packages
expr_stmt|;
name|this
operator|.
name|beanPostProcessor
operator|=
name|postProcessor
expr_stmt|;
name|this
operator|.
name|resolver
operator|=
name|resolver
expr_stmt|;
comment|// add our provided loader as well
name|resolver
operator|.
name|addClassLoader
argument_list|(
name|classLoader
argument_list|)
expr_stmt|;
block|}
DECL|method|getPackages ()
specifier|public
name|String
index|[]
name|getPackages
parameter_list|()
block|{
return|return
name|packages
return|;
block|}
DECL|method|getApplicationContext ()
specifier|public
name|ApplicationContext
name|getApplicationContext
parameter_list|()
block|{
return|return
name|applicationContext
return|;
block|}
comment|/**      * Appends all the {@link RouteBuilder} instances that can be found on the classpath      */
DECL|method|appendBuilders (List<RoutesBuilder> list)
specifier|public
name|void
name|appendBuilders
parameter_list|(
name|List
argument_list|<
name|RoutesBuilder
argument_list|>
name|list
parameter_list|)
throws|throws
name|IllegalAccessException
throws|,
name|InstantiationException
block|{
name|Set
argument_list|<
name|Class
argument_list|>
name|classes
init|=
name|resolver
operator|.
name|findImplementations
argument_list|(
name|RoutesBuilder
operator|.
name|class
argument_list|,
name|packages
argument_list|)
decl_stmt|;
for|for
control|(
name|Class
name|aClass
range|:
name|classes
control|)
block|{
if|if
condition|(
name|shouldIgnoreBean
argument_list|(
name|aClass
argument_list|)
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|isValidClass
argument_list|(
name|aClass
argument_list|)
condition|)
block|{
name|RoutesBuilder
name|builder
init|=
name|instantiateBuilder
argument_list|(
name|aClass
argument_list|)
decl_stmt|;
if|if
condition|(
name|beanPostProcessor
operator|!=
literal|null
condition|)
block|{
comment|// Inject the annotated resource
name|beanPostProcessor
operator|.
name|postProcessBeforeInitialization
argument_list|(
name|builder
argument_list|,
name|builder
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|list
operator|.
name|add
argument_list|(
name|builder
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|destroy ()
specifier|public
name|void
name|destroy
parameter_list|()
throws|throws
name|Exception
block|{     }
comment|/**      * Lets ignore beans that are not explicitly configured in the spring.xml      */
DECL|method|shouldIgnoreBean (Class type)
specifier|protected
name|boolean
name|shouldIgnoreBean
parameter_list|(
name|Class
name|type
parameter_list|)
block|{
name|Map
name|beans
init|=
name|applicationContext
operator|.
name|getBeansOfType
argument_list|(
name|type
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
decl_stmt|;
if|if
condition|(
name|beans
operator|==
literal|null
operator|||
name|beans
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
comment|/**      * Returns true if the object is non-abstract and supports a zero argument constructor      */
DECL|method|isValidClass (Class type)
specifier|protected
name|boolean
name|isValidClass
parameter_list|(
name|Class
name|type
parameter_list|)
block|{
if|if
condition|(
operator|!
name|Modifier
operator|.
name|isAbstract
argument_list|(
name|type
operator|.
name|getModifiers
argument_list|()
argument_list|)
operator|&&
operator|!
name|type
operator|.
name|isInterface
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|instantiateBuilder (Class type)
specifier|protected
name|RoutesBuilder
name|instantiateBuilder
parameter_list|(
name|Class
name|type
parameter_list|)
throws|throws
name|IllegalAccessException
throws|,
name|InstantiationException
block|{
return|return
operator|(
name|RoutesBuilder
operator|)
name|camelContext
operator|.
name|getInjector
argument_list|()
operator|.
name|newInstance
argument_list|(
name|type
argument_list|)
return|;
block|}
block|}
end_class

end_unit

