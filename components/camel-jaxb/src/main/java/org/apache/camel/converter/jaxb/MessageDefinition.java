begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.converter.jaxb
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
operator|.
name|jaxb
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAnyElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlMixed
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlTransient
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

begin_comment
comment|/**  * Represents a JAXB2 representation of a Camel {@link Message} -<b>Important</b>: work in progress!  *  * @version $Revision$  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"message"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|value
operator|=
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|MessageDefinition
specifier|public
class|class
name|MessageDefinition
block|{
comment|// TODO: XmlElementRef to the abstract class HeaderType does not work (CAMEL-583)
comment|//@XmlElementRef(type = HeaderType.class)
annotation|@
name|XmlAnyElement
argument_list|(
name|lax
operator|=
literal|true
argument_list|)
annotation|@
name|XmlMixed
DECL|field|headers
name|List
argument_list|<
name|HeaderDefinition
argument_list|>
name|headers
init|=
operator|new
name|ArrayList
argument_list|<
name|HeaderDefinition
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|XmlAnyElement
argument_list|(
name|lax
operator|=
literal|true
argument_list|)
annotation|@
name|XmlMixed
DECL|field|content
specifier|private
name|List
name|content
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|body
specifier|private
name|Object
name|body
decl_stmt|;
DECL|method|getBody ()
specifier|public
name|Object
name|getBody
parameter_list|()
block|{
if|if
condition|(
name|body
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|content
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|content
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
return|return
name|content
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|content
return|;
block|}
block|}
block|}
return|return
name|body
return|;
block|}
DECL|method|setBody (Object body)
specifier|public
name|void
name|setBody
parameter_list|(
name|Object
name|body
parameter_list|)
block|{
name|this
operator|.
name|body
operator|=
name|body
expr_stmt|;
if|if
condition|(
name|body
operator|instanceof
name|List
condition|)
block|{
name|content
operator|=
operator|(
name|List
operator|)
name|body
expr_stmt|;
block|}
else|else
block|{
name|content
operator|=
operator|new
name|ArrayList
argument_list|()
expr_stmt|;
name|content
operator|.
name|add
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getHeaders ()
specifier|public
name|List
argument_list|<
name|HeaderDefinition
argument_list|>
name|getHeaders
parameter_list|()
block|{
return|return
name|headers
return|;
block|}
DECL|method|setHeaders (List<HeaderDefinition> headers)
specifier|public
name|void
name|setHeaders
parameter_list|(
name|List
argument_list|<
name|HeaderDefinition
argument_list|>
name|headers
parameter_list|)
block|{
name|this
operator|.
name|headers
operator|=
name|headers
expr_stmt|;
block|}
DECL|method|getHeaderMap ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getHeaderMap
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|answer
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|HeaderDefinition
name|header
range|:
name|headers
control|)
block|{
name|answer
operator|.
name|put
argument_list|(
name|header
operator|.
name|getName
argument_list|()
argument_list|,
name|header
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Copies the headers and body of this object from the given Camel message      *      * @param message the Camel message to read the headers and body from      */
DECL|method|copyFrom (Message message)
specifier|public
name|void
name|copyFrom
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|headers
operator|.
name|clear
argument_list|()
expr_stmt|;
name|Set
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|entries
init|=
name|message
operator|.
name|getHeaders
argument_list|()
operator|.
name|entrySet
argument_list|()
decl_stmt|;
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
name|entries
control|)
block|{
name|Object
name|value
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|headers
operator|.
name|add
argument_list|(
name|createHeader
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|setBody
argument_list|(
name|message
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Copies the headers and body of this object to the given Camel message      *      * @param message the camel message to overwrite its headers and body      */
DECL|method|copyTo (Message message)
specifier|public
name|void
name|copyTo
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|message
operator|.
name|setHeaders
argument_list|(
name|getHeaderMap
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|setBody
argument_list|(
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|createHeader (String key, Object value)
specifier|protected
name|HeaderDefinition
name|createHeader
parameter_list|(
name|String
name|key
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|instanceof
name|String
condition|)
block|{
return|return
operator|new
name|StringHeader
argument_list|(
name|key
argument_list|,
operator|(
name|String
operator|)
name|value
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|Integer
condition|)
block|{
return|return
operator|new
name|IntegerHeader
argument_list|(
name|key
argument_list|,
operator|(
name|Integer
operator|)
name|value
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|Long
condition|)
block|{
return|return
operator|new
name|LongHeader
argument_list|(
name|key
argument_list|,
operator|(
name|Long
operator|)
name|value
argument_list|)
return|;
block|}
else|else
block|{
comment|// lets convert to a String
return|return
operator|new
name|StringHeader
argument_list|(
name|key
argument_list|,
name|value
operator|.
name|toString
argument_list|()
argument_list|)
return|;
comment|//return new ObjectHeader(key, value);
block|}
block|}
block|}
end_class

end_unit

