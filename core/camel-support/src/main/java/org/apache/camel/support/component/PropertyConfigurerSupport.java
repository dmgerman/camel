begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.support.component
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|component
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

begin_comment
comment|/**  * Base class used by the camel-apt compiler plugin when it generates source code for fast  * property configurations via {@link org.apache.camel.spi.PropertyConfigurer}.  */
end_comment

begin_class
DECL|class|PropertyConfigurerSupport
specifier|public
specifier|abstract
class|class
name|PropertyConfigurerSupport
block|{
comment|/**      * Converts the property to the expected type      *      * @param camelContext   the camel context      * @param type           the expected type      * @param value          the value      * @return  the value converted to the expected type      */
DECL|method|property (CamelContext camelContext, Class<T> type, Object value)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|property
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
comment|// if the type is not string based and the value is a bean reference, then we need to lookup
comment|// the bean from the registry
if|if
condition|(
name|value
operator|instanceof
name|String
operator|&&
name|String
operator|.
name|class
operator|!=
name|type
condition|)
block|{
comment|// is it a reference parameter
name|String
name|text
init|=
name|value
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|text
operator|.
name|startsWith
argument_list|(
literal|"#"
argument_list|)
condition|)
block|{
name|String
name|ref
init|=
name|text
operator|.
name|startsWith
argument_list|(
literal|"#bean:"
argument_list|)
condition|?
name|text
operator|.
name|substring
argument_list|(
literal|6
argument_list|)
else|:
name|text
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|Object
name|obj
init|=
name|camelContext
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByName
argument_list|(
name|ref
argument_list|)
decl_stmt|;
if|if
condition|(
name|obj
operator|!=
literal|null
condition|)
block|{
name|value
operator|=
name|obj
expr_stmt|;
block|}
block|}
block|}
comment|// special for boolean values with string values as we only want to accept "true" or "false"
if|if
condition|(
operator|(
name|type
operator|==
name|Boolean
operator|.
name|class
operator|||
name|type
operator|==
name|boolean
operator|.
name|class
operator|)
operator|&&
name|value
operator|instanceof
name|String
condition|)
block|{
name|String
name|text
init|=
operator|(
name|String
operator|)
name|value
decl_stmt|;
if|if
condition|(
operator|!
name|text
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"true"
argument_list|)
operator|&&
operator|!
name|text
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"false"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot convert the String value: "
operator|+
name|value
operator|+
literal|" to type: "
operator|+
name|type
operator|+
literal|" as the value is not true or false"
argument_list|)
throw|;
block|}
block|}
return|return
name|camelContext
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|type
argument_list|,
name|value
argument_list|)
return|;
block|}
block|}
end_class

end_unit

