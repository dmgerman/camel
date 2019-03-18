begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.support
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|RestProducerFactory
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
name|support
operator|.
name|service
operator|.
name|ServiceHelper
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
name|StringHelper
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
comment|/**  * Helper for creating configured {@link Component}s used by the  * {@link RestProducerFactory} contract.  *   * When {@link RestProducerFactory} contract is used it could instantiate, start  * and register the underlying component. During this process we have no way of  * configuring component properties, most notably the SSL properties.  */
end_comment

begin_class
DECL|class|RestProducerFactoryHelper
specifier|public
specifier|final
class|class
name|RestProducerFactoryHelper
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|RestProducerFactoryHelper
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|RestProducerFactoryHelper ()
specifier|private
name|RestProducerFactoryHelper
parameter_list|()
block|{
comment|// helper class
block|}
DECL|method|setupComponentFor (final String url, final CamelContext camelContext, final Map<String, Object> componentProperties)
specifier|public
specifier|static
name|void
name|setupComponentFor
parameter_list|(
specifier|final
name|String
name|url
parameter_list|,
specifier|final
name|CamelContext
name|camelContext
parameter_list|,
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|componentProperties
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|String
name|scheme
init|=
name|StringHelper
operator|.
name|before
argument_list|(
name|url
argument_list|,
literal|":"
argument_list|)
decl_stmt|;
name|setupComponent
argument_list|(
name|scheme
argument_list|,
name|camelContext
argument_list|,
name|componentProperties
argument_list|)
expr_stmt|;
block|}
DECL|method|setupComponent (final String componentName, final CamelContext camelContext, final Map<String, Object> componentProperties)
specifier|public
specifier|static
name|Component
name|setupComponent
parameter_list|(
specifier|final
name|String
name|componentName
parameter_list|,
specifier|final
name|CamelContext
name|camelContext
parameter_list|,
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|componentProperties
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|componentProperties
operator|==
literal|null
operator|||
name|componentProperties
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|camelContext
operator|.
name|getComponent
argument_list|(
name|componentName
argument_list|)
return|;
block|}
specifier|final
name|Component
name|existing
init|=
name|camelContext
operator|.
name|getComponent
argument_list|(
name|componentName
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|existing
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|componentProperties
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Found existing `{}` component already present in the Camel context. Not setting component"
operator|+
literal|" properties on the existing component. You can either prevent the component creation or"
operator|+
literal|" set the given properties on the component. Component properties given: {}"
argument_list|,
name|componentName
argument_list|,
name|componentProperties
argument_list|)
expr_stmt|;
block|}
return|return
name|existing
return|;
block|}
comment|// component was not added to the context we can configure it
specifier|final
name|Component
name|newlyCreated
init|=
name|camelContext
operator|.
name|getComponent
argument_list|(
name|componentName
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
decl_stmt|;
comment|// need to make a copy of the component properties as
comment|// IntrospectionSupport::setProperties will remove any that are set and
comment|// we might be called multiple times
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|copyOfComponentProperties
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|componentProperties
argument_list|)
decl_stmt|;
name|IntrospectionSupport
operator|.
name|setProperties
argument_list|(
name|camelContext
argument_list|,
name|camelContext
operator|.
name|getTypeConverter
argument_list|()
argument_list|,
name|newlyCreated
argument_list|,
name|copyOfComponentProperties
argument_list|)
expr_stmt|;
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|newlyCreated
argument_list|)
expr_stmt|;
return|return
name|newlyCreated
return|;
block|}
block|}
end_class

end_unit

