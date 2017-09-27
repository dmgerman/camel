begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.docker
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|docker
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
name|Array
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
name|Message
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
name|ObjectHelper
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
name|lang
operator|.
name|BooleanUtils
import|;
end_import

begin_comment
comment|/**  * Utility methods for Docker Component  */
end_comment

begin_class
DECL|class|DockerHelper
specifier|public
specifier|final
class|class
name|DockerHelper
block|{
DECL|field|STRING_DELIMITER
specifier|private
specifier|static
specifier|final
name|String
name|STRING_DELIMITER
init|=
literal|";"
decl_stmt|;
DECL|method|DockerHelper ()
specifier|private
name|DockerHelper
parameter_list|()
block|{
comment|// Helper class
block|}
comment|/**      * Transforms a Docker Component header value to its analogous URI      * parameter      *      * @param name      * @return      */
DECL|method|transformFromHeaderName (String name)
specifier|public
specifier|static
name|String
name|transformFromHeaderName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notEmpty
argument_list|(
name|name
argument_list|,
literal|"name"
argument_list|)
expr_stmt|;
name|StringBuilder
name|formattedName
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|String
name|nameSubstring
init|=
name|name
operator|.
name|substring
argument_list|(
name|DockerConstants
operator|.
name|DOCKER_PREFIX
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|nameSubstring
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|formattedName
operator|.
name|append
argument_list|(
name|nameSubstring
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|)
operator|.
name|toLowerCase
argument_list|()
argument_list|)
expr_stmt|;
name|formattedName
operator|.
name|append
argument_list|(
name|nameSubstring
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|formattedName
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Attempts to locate a given property name within a URI parameter or the      * message header. A found value in a message header takes precedence over a      * URI parameter.      *      * @param name      * @param configuration      * @param message      * @param clazz      * @return      */
DECL|method|getProperty (String name, DockerConfiguration configuration, Message message, Class<T> clazz)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|getProperty
parameter_list|(
name|String
name|name
parameter_list|,
name|DockerConfiguration
name|configuration
parameter_list|,
name|Message
name|message
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|)
block|{
return|return
name|getProperty
argument_list|(
name|name
argument_list|,
name|configuration
argument_list|,
name|message
argument_list|,
name|clazz
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Attempts to locate a given property name within a URI parameter or the      * message header. A found value in a message header takes precedence over a      * URI parameter. Returns a default value if given      *      * @param name      * @param configuration      * @param message      * @param clazz      * @param defaultValue      * @return      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|getProperty (String name, DockerConfiguration configuration, Message message, Class<T> clazz, T defaultValue)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|getProperty
parameter_list|(
name|String
name|name
parameter_list|,
name|DockerConfiguration
name|configuration
parameter_list|,
name|Message
name|message
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|,
name|T
name|defaultValue
parameter_list|)
block|{
comment|// First attempt to locate property from Message Header, then fallback
comment|// to Endpoint property
if|if
condition|(
name|message
operator|!=
literal|null
condition|)
block|{
name|T
name|headerProperty
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|name
argument_list|,
name|clazz
argument_list|)
decl_stmt|;
if|if
condition|(
name|headerProperty
operator|!=
literal|null
condition|)
block|{
return|return
name|headerProperty
return|;
block|}
block|}
name|Object
name|prop
init|=
name|configuration
operator|.
name|getParameters
argument_list|()
operator|.
name|get
argument_list|(
name|transformFromHeaderName
argument_list|(
name|name
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|prop
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|prop
operator|.
name|getClass
argument_list|()
operator|.
name|isAssignableFrom
argument_list|(
name|clazz
argument_list|)
condition|)
block|{
return|return
operator|(
name|T
operator|)
name|prop
return|;
block|}
elseif|else
if|if
condition|(
name|Integer
operator|.
name|class
operator|==
name|clazz
condition|)
block|{
return|return
operator|(
name|T
operator|)
name|Integer
operator|.
name|valueOf
argument_list|(
operator|(
name|String
operator|)
name|prop
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|Boolean
operator|.
name|class
operator|==
name|clazz
condition|)
block|{
return|return
operator|(
name|T
operator|)
name|BooleanUtils
operator|.
name|toBooleanObject
argument_list|(
operator|(
name|String
operator|)
name|prop
argument_list|,
literal|"true"
argument_list|,
literal|"false"
argument_list|,
literal|"null"
argument_list|)
return|;
block|}
block|}
elseif|else
if|if
condition|(
name|defaultValue
operator|!=
literal|null
condition|)
block|{
return|return
name|defaultValue
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Attempts to locate a given property which is an array by name within a      * URI parameter or the message header. A found value in a message header      * takes precedence over a URI parameter.      *      * @param name      * @param message      * @param clazz      * @return      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|getArrayProperty (String name, Message message, Class<T> clazz)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
index|[]
name|getArrayProperty
parameter_list|(
name|String
name|name
parameter_list|,
name|Message
name|message
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|)
block|{
if|if
condition|(
name|message
operator|!=
literal|null
condition|)
block|{
name|Object
name|header
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|header
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|header
operator|.
name|getClass
argument_list|()
operator|.
name|isAssignableFrom
argument_list|(
name|clazz
argument_list|)
condition|)
block|{
name|T
index|[]
name|headerArray
init|=
operator|(
name|T
index|[]
operator|)
name|Array
operator|.
name|newInstance
argument_list|(
name|clazz
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|headerArray
index|[
literal|0
index|]
operator|=
operator|(
name|T
operator|)
name|header
expr_stmt|;
return|return
name|headerArray
return|;
block|}
if|if
condition|(
name|header
operator|.
name|getClass
argument_list|()
operator|.
name|isArray
argument_list|()
condition|)
block|{
if|if
condition|(
name|header
operator|.
name|getClass
argument_list|()
operator|.
name|getComponentType
argument_list|()
operator|.
name|isAssignableFrom
argument_list|(
name|clazz
argument_list|)
operator|||
name|header
operator|.
name|getClass
argument_list|()
operator|.
name|getDeclaringClass
argument_list|()
operator|.
name|isAssignableFrom
argument_list|(
name|clazz
argument_list|)
condition|)
block|{
return|return
operator|(
name|T
index|[]
operator|)
name|header
return|;
block|}
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * @param headerName name of the header      * @param message the Camel message      * @return      */
DECL|method|parseDelimitedStringHeader (String headerName, Message message)
specifier|public
specifier|static
name|String
index|[]
name|parseDelimitedStringHeader
parameter_list|(
name|String
name|headerName
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
name|Object
name|header
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|headerName
argument_list|)
decl_stmt|;
if|if
condition|(
name|header
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|header
operator|instanceof
name|String
condition|)
block|{
return|return
operator|(
operator|(
name|String
operator|)
name|header
operator|)
operator|.
name|split
argument_list|(
name|STRING_DELIMITER
argument_list|)
return|;
block|}
if|if
condition|(
name|header
operator|instanceof
name|String
index|[]
condition|)
block|{
return|return
operator|(
name|String
index|[]
operator|)
name|header
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

