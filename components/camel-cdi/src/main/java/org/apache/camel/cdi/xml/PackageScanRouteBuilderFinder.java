begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.cdi.xml
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|cdi
operator|.
name|xml
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
comment|/**  * A helper class which will find all {@link org.apache.camel.builder.RouteBuilder} instances on the classpath.  */
end_comment

begin_class
DECL|class|PackageScanRouteBuilderFinder
specifier|final
class|class
name|PackageScanRouteBuilderFinder
block|{
DECL|field|logger
specifier|private
specifier|final
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|PackageScanRouteBuilderFinder
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|camelContext
specifier|private
specifier|final
name|CamelContext
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
specifier|final
name|PackageScanClassResolver
name|resolver
decl_stmt|;
DECL|method|PackageScanRouteBuilderFinder (CamelContext camelContext, String[] packages, ClassLoader classLoader, PackageScanClassResolver resolver)
name|PackageScanRouteBuilderFinder
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
index|[]
name|packages
parameter_list|,
name|ClassLoader
name|classLoader
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
name|packages
operator|=
name|packages
expr_stmt|;
name|this
operator|.
name|resolver
operator|=
name|resolver
expr_stmt|;
name|resolver
operator|.
name|addClassLoader
argument_list|(
name|classLoader
argument_list|)
expr_stmt|;
block|}
comment|/**      * Appends all the {@link org.apache.camel.builder.RouteBuilder} instances that can be found on the classpath      */
DECL|method|appendBuilders (List<RoutesBuilder> list)
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
argument_list|<
name|?
argument_list|>
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
argument_list|<
name|?
argument_list|>
name|aClass
range|:
name|classes
control|)
block|{
name|logger
operator|.
name|trace
argument_list|(
literal|"Found RouteBuilder class: {}"
argument_list|,
name|aClass
argument_list|)
expr_stmt|;
comment|// certain beans should be ignored
if|if
condition|(
name|shouldIgnoreBean
argument_list|(
name|aClass
argument_list|)
condition|)
block|{
name|logger
operator|.
name|debug
argument_list|(
literal|"Ignoring RouteBuilder class: {}"
argument_list|,
name|aClass
argument_list|)
expr_stmt|;
continue|continue;
block|}
if|if
condition|(
operator|!
name|isValidClass
argument_list|(
name|aClass
argument_list|)
condition|)
block|{
name|logger
operator|.
name|debug
argument_list|(
literal|"Ignoring invalid RouteBuilder class: {}"
argument_list|,
name|aClass
argument_list|)
expr_stmt|;
continue|continue;
block|}
comment|// type is valid so create and instantiate the builder
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|RoutesBuilder
name|builder
init|=
name|instantiateBuilder
argument_list|(
operator|(
name|Class
argument_list|<
name|?
extends|extends
name|RoutesBuilder
argument_list|>
operator|)
name|aClass
argument_list|)
decl_stmt|;
name|logger
operator|.
name|debug
argument_list|(
literal|"Adding instantiated RouteBuilder: {}"
argument_list|,
name|builder
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|builder
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|shouldIgnoreBean (Class<?> type)
specifier|private
name|boolean
name|shouldIgnoreBean
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|beans
init|=
name|camelContext
operator|.
name|getRegistry
argument_list|()
operator|.
name|findByTypeWithName
argument_list|(
name|type
argument_list|)
decl_stmt|;
return|return
operator|!
operator|(
name|beans
operator|==
literal|null
operator|||
name|beans
operator|.
name|isEmpty
argument_list|()
operator|)
return|;
block|}
DECL|method|isValidClass (Class<?> type)
specifier|private
name|boolean
name|isValidClass
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|Modifier
operator|.
name|isPublic
argument_list|(
name|type
operator|.
name|getModifiers
argument_list|()
argument_list|)
operator|&&
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
return|;
block|}
DECL|method|instantiateBuilder (Class<? extends RoutesBuilder> type)
specifier|private
name|RoutesBuilder
name|instantiateBuilder
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|RoutesBuilder
argument_list|>
name|type
parameter_list|)
block|{
return|return
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

