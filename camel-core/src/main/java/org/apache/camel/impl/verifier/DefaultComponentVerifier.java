begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.verifier
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|verifier
package|;
end_package

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
name|Optional
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Supplier
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
name|CamelContextAware
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
name|ComponentVerifier
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
name|NoSuchOptionException
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
name|TypeConverter
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
name|CamelContextHelper
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
name|EndpointHelper
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
name|IntrospectionSupport
import|;
end_import

begin_class
DECL|class|DefaultComponentVerifier
specifier|public
class|class
name|DefaultComponentVerifier
implements|implements
name|ComponentVerifier
implements|,
name|CamelContextAware
block|{
DECL|field|INSTANCE
specifier|public
specifier|static
specifier|final
name|ComponentVerifier
name|INSTANCE
init|=
operator|new
name|DefaultComponentVerifier
argument_list|()
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|method|DefaultComponentVerifier ()
specifier|public
name|DefaultComponentVerifier
parameter_list|()
block|{     }
DECL|method|DefaultComponentVerifier (CamelContext camelContext)
specifier|public
name|DefaultComponentVerifier
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
annotation|@
name|Override
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|verify (Scope scope, Map<String, Object> parameters)
specifier|public
name|Result
name|verify
parameter_list|(
name|Scope
name|scope
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
block|{
switch|switch
condition|(
name|scope
condition|)
block|{
case|case
name|PARAMETERS
case|:
return|return
name|verifyParameters
argument_list|(
name|parameters
argument_list|)
return|;
case|case
name|CONNECTIVITY
case|:
return|return
name|verifyConnectivity
argument_list|(
name|parameters
argument_list|)
return|;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unsupported Verifier scope: "
operator|+
name|scope
argument_list|)
throw|;
block|}
block|}
comment|// *************************************
comment|// Implementation
comment|// *************************************
DECL|method|verifyParameters (Map<String, Object> parameters)
specifier|protected
name|Result
name|verifyParameters
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
block|{
return|return
operator|new
name|ResultBuilder
argument_list|()
operator|.
name|scope
argument_list|(
name|Scope
operator|.
name|PARAMETERS
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|verifyConnectivity (Map<String, Object> parameters)
specifier|protected
name|Result
name|verifyConnectivity
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
block|{
return|return
operator|new
name|ResultBuilder
argument_list|()
operator|.
name|scope
argument_list|(
name|Scope
operator|.
name|CONNECTIVITY
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
comment|// *************************************
comment|// Helpers
comment|// *************************************
DECL|method|setProperties (T instance, Map<String, Object> properties)
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|T
name|setProperties
parameter_list|(
name|T
name|instance
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|camelContext
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Camel context is null"
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|properties
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
specifier|final
name|CamelContext
name|context
init|=
name|getCamelContext
argument_list|()
decl_stmt|;
specifier|final
name|TypeConverter
name|converter
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
decl_stmt|;
name|IntrospectionSupport
operator|.
name|setProperties
argument_list|(
name|converter
argument_list|,
name|instance
argument_list|,
name|properties
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|properties
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|entry
operator|.
name|getValue
argument_list|()
operator|instanceof
name|String
condition|)
block|{
name|String
name|value
init|=
operator|(
name|String
operator|)
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|EndpointHelper
operator|.
name|isReferenceParameter
argument_list|(
name|value
argument_list|)
condition|)
block|{
name|IntrospectionSupport
operator|.
name|setProperty
argument_list|(
name|context
argument_list|,
name|converter
argument_list|,
name|instance
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
literal|null
argument_list|,
name|value
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
return|return
name|instance
return|;
block|}
DECL|method|setProperties (T instance, String prefix, Map<String, Object> properties)
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|T
name|setProperties
parameter_list|(
name|T
name|instance
parameter_list|,
name|String
name|prefix
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|setProperties
argument_list|(
name|instance
argument_list|,
name|IntrospectionSupport
operator|.
name|extractProperties
argument_list|(
name|properties
argument_list|,
name|prefix
argument_list|,
literal|false
argument_list|)
argument_list|)
return|;
block|}
DECL|method|getOption (Map<String, Object> parameters, String key, Class<T> type)
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|Optional
argument_list|<
name|T
argument_list|>
name|getOption
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|,
name|String
name|key
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|Object
name|value
init|=
name|parameters
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
return|return
name|Optional
operator|.
name|ofNullable
argument_list|(
name|CamelContextHelper
operator|.
name|convertTo
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|type
argument_list|,
name|value
argument_list|)
argument_list|)
return|;
block|}
return|return
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
DECL|method|getOption (Map<String, Object> parameters, String key, Class<T> type, Supplier<T> defaultSupplier)
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|T
name|getOption
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|,
name|String
name|key
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|Supplier
argument_list|<
name|T
argument_list|>
name|defaultSupplier
parameter_list|)
block|{
return|return
name|getOption
argument_list|(
name|parameters
argument_list|,
name|key
argument_list|,
name|type
argument_list|)
operator|.
name|orElseGet
argument_list|(
name|defaultSupplier
argument_list|)
return|;
block|}
DECL|method|getMandatoryOption (Map<String, Object> parameters, String key, Class<T> type)
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|T
name|getMandatoryOption
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|,
name|String
name|key
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
throws|throws
name|NoSuchOptionException
block|{
return|return
name|getOption
argument_list|(
name|parameters
argument_list|,
name|key
argument_list|,
name|type
argument_list|)
operator|.
name|orElseThrow
argument_list|(
parameter_list|()
lambda|->
operator|new
name|NoSuchOptionException
argument_list|(
name|key
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

