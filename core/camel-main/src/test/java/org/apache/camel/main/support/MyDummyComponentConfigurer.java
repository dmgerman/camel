begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.main.support
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|main
operator|.
name|support
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
name|spi
operator|.
name|GeneratedPropertyConfigurer
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
name|component
operator|.
name|PropertyConfigurerSupport
import|;
end_import

begin_class
DECL|class|MyDummyComponentConfigurer
specifier|public
class|class
name|MyDummyComponentConfigurer
extends|extends
name|PropertyConfigurerSupport
implements|implements
name|GeneratedPropertyConfigurer
block|{
annotation|@
name|Override
DECL|method|configure (CamelContext camelContext, Object component, String name, Object value, boolean ignoreCase)
specifier|public
name|boolean
name|configure
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Object
name|component
parameter_list|,
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|,
name|boolean
name|ignoreCase
parameter_list|)
block|{
if|if
condition|(
name|ignoreCase
condition|)
block|{
return|return
name|doConfigureIgnoreCase
argument_list|(
name|camelContext
argument_list|,
name|component
argument_list|,
name|name
argument_list|,
name|value
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|doConfigure
argument_list|(
name|camelContext
argument_list|,
name|component
argument_list|,
name|name
argument_list|,
name|value
argument_list|)
return|;
block|}
block|}
DECL|method|doConfigure (CamelContext camelContext, Object component, String name, Object value)
specifier|private
specifier|static
name|boolean
name|doConfigure
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Object
name|component
parameter_list|,
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
switch|switch
condition|(
name|name
condition|)
block|{
case|case
literal|"configuration"
case|:
operator|(
operator|(
name|MyDummyComponent
operator|)
name|component
operator|)
operator|.
name|setConfiguration
argument_list|(
name|property
argument_list|(
name|camelContext
argument_list|,
name|MyDummyConfiguration
operator|.
name|class
argument_list|,
name|value
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
default|default:
return|return
literal|false
return|;
block|}
block|}
DECL|method|doConfigureIgnoreCase (CamelContext camelContext, Object component, String name, Object value)
specifier|private
specifier|static
name|boolean
name|doConfigureIgnoreCase
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Object
name|component
parameter_list|,
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
switch|switch
condition|(
name|name
operator|.
name|toLowerCase
argument_list|()
condition|)
block|{
case|case
literal|"configuration"
case|:
operator|(
operator|(
name|MyDummyComponent
operator|)
name|component
operator|)
operator|.
name|setConfiguration
argument_list|(
name|property
argument_list|(
name|camelContext
argument_list|,
name|MyDummyConfiguration
operator|.
name|class
argument_list|,
name|value
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
default|default:
return|return
literal|false
return|;
block|}
block|}
block|}
end_class

end_unit

