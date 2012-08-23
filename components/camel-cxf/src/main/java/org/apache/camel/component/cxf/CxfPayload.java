begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|AbstractList
import|;
end_import

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
name|javax
operator|.
name|xml
operator|.
name|XMLConstants
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Source
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|dom
operator|.
name|DOMSource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
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
name|RuntimeCamelException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|staxutils
operator|.
name|StaxUtils
import|;
end_import

begin_comment
comment|/**  * CxfMessage body type when {@link DataFormat#PAYLOAD} is used.  *   * @version   */
end_comment

begin_class
DECL|class|CxfPayload
specifier|public
class|class
name|CxfPayload
parameter_list|<
name|T
parameter_list|>
block|{
DECL|field|body
specifier|private
name|List
argument_list|<
name|Source
argument_list|>
name|body
decl_stmt|;
DECL|field|headers
specifier|private
name|List
argument_list|<
name|T
argument_list|>
name|headers
decl_stmt|;
DECL|field|nsMap
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|nsMap
decl_stmt|;
DECL|method|CxfPayload (List<T> headers, List<Source> body, Map<String, String> nsMap)
specifier|public
name|CxfPayload
parameter_list|(
name|List
argument_list|<
name|T
argument_list|>
name|headers
parameter_list|,
name|List
argument_list|<
name|Source
argument_list|>
name|body
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|nsMap
parameter_list|)
block|{
name|this
operator|.
name|headers
operator|=
name|headers
expr_stmt|;
name|this
operator|.
name|body
operator|=
name|body
expr_stmt|;
name|this
operator|.
name|nsMap
operator|=
name|nsMap
expr_stmt|;
block|}
DECL|method|CxfPayload (List<T> headers, List<Element> body)
specifier|public
name|CxfPayload
parameter_list|(
name|List
argument_list|<
name|T
argument_list|>
name|headers
parameter_list|,
name|List
argument_list|<
name|Element
argument_list|>
name|body
parameter_list|)
block|{
name|this
operator|.
name|headers
operator|=
name|headers
expr_stmt|;
name|this
operator|.
name|body
operator|=
operator|new
name|ArrayList
argument_list|<
name|Source
argument_list|>
argument_list|(
name|body
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Element
name|el
range|:
name|body
control|)
block|{
name|this
operator|.
name|body
operator|.
name|add
argument_list|(
operator|new
name|DOMSource
argument_list|(
name|el
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Get the body as a List of DOM elements.       * This will cause the Body to be fully read and parsed.      * @return      */
DECL|method|getBody ()
specifier|public
name|List
argument_list|<
name|Element
argument_list|>
name|getBody
parameter_list|()
block|{
return|return
operator|new
name|AbstractList
argument_list|<
name|Element
argument_list|>
argument_list|()
block|{
specifier|public
name|boolean
name|add
parameter_list|(
name|Element
name|e
parameter_list|)
block|{
return|return
name|body
operator|.
name|add
argument_list|(
operator|new
name|DOMSource
argument_list|(
name|e
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|Element
name|set
parameter_list|(
name|int
name|index
parameter_list|,
name|Element
name|element
parameter_list|)
block|{
name|Source
name|s
init|=
name|body
operator|.
name|set
argument_list|(
name|index
argument_list|,
operator|new
name|DOMSource
argument_list|(
name|element
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
return|return
name|StaxUtils
operator|.
name|read
argument_list|(
name|s
argument_list|)
operator|.
name|getDocumentElement
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Problem converting content to Element"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|add
parameter_list|(
name|int
name|index
parameter_list|,
name|Element
name|element
parameter_list|)
block|{
name|body
operator|.
name|add
argument_list|(
name|index
argument_list|,
operator|new
name|DOMSource
argument_list|(
name|element
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Element
name|remove
parameter_list|(
name|int
name|index
parameter_list|)
block|{
name|Source
name|s
init|=
name|body
operator|.
name|remove
argument_list|(
name|index
argument_list|)
decl_stmt|;
try|try
block|{
return|return
name|StaxUtils
operator|.
name|read
argument_list|(
name|s
argument_list|)
operator|.
name|getDocumentElement
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Problem converting content to Element"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|Element
name|get
parameter_list|(
name|int
name|index
parameter_list|)
block|{
name|Source
name|s
init|=
name|body
operator|.
name|get
argument_list|(
name|index
argument_list|)
decl_stmt|;
try|try
block|{
name|Element
name|el
init|=
name|StaxUtils
operator|.
name|read
argument_list|(
name|s
argument_list|)
operator|.
name|getDocumentElement
argument_list|()
decl_stmt|;
name|addNamespace
argument_list|(
name|el
argument_list|,
name|nsMap
argument_list|)
expr_stmt|;
name|body
operator|.
name|set
argument_list|(
name|index
argument_list|,
operator|new
name|DOMSource
argument_list|(
name|el
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|el
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Problem converting content to Element"
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|body
operator|.
name|size
argument_list|()
return|;
block|}
block|}
return|;
block|}
DECL|method|addNamespace (Element element, Map<String, String> nsMap)
specifier|protected
specifier|static
name|void
name|addNamespace
parameter_list|(
name|Element
name|element
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|nsMap
parameter_list|)
block|{
if|if
condition|(
name|nsMap
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|ns
range|:
name|nsMap
operator|.
name|keySet
argument_list|()
control|)
block|{
name|element
operator|.
name|setAttribute
argument_list|(
name|XMLConstants
operator|.
name|XMLNS_ATTRIBUTE
operator|+
literal|":"
operator|+
name|ns
argument_list|,
name|nsMap
operator|.
name|get
argument_list|(
name|ns
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Gets the body as a List of source objects.   If possible, the Source objects      * will likely be StaxSource or similar that allows streaming.   If you plan to       * modify or use the Source, be careful that the result is still usable by      * the Camel runtime.      * @return      */
DECL|method|getBodySources ()
specifier|public
name|List
argument_list|<
name|Source
argument_list|>
name|getBodySources
parameter_list|()
block|{
return|return
name|body
return|;
block|}
DECL|method|getHeaders ()
specifier|public
name|List
argument_list|<
name|T
argument_list|>
name|getHeaders
parameter_list|()
block|{
return|return
name|headers
return|;
block|}
comment|/**      * Returns the contents as a String      * Important notes:      *   1) This requires the message to be fully loaded breaking the streaming      *   2) For large messages, the result can be a VERY large String and require      *   large amounts of memory.      */
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringBuilder
name|buf
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|" headers: "
operator|+
name|headers
argument_list|)
expr_stmt|;
comment|// go through the list of element and turn it into String
if|if
condition|(
name|body
operator|==
literal|null
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|"body: "
operator|+
name|body
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|buf
operator|.
name|append
argument_list|(
literal|"body: [ "
argument_list|)
expr_stmt|;
for|for
control|(
name|Element
name|src
range|:
name|getBody
argument_list|()
control|)
block|{
name|String
name|elementString
decl_stmt|;
try|try
block|{
name|elementString
operator|=
name|StaxUtils
operator|.
name|toString
argument_list|(
name|src
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|e
parameter_list|)
block|{
name|elementString
operator|=
name|src
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
name|buf
operator|.
name|append
argument_list|(
literal|"["
operator|+
name|elementString
operator|+
literal|"]"
argument_list|)
expr_stmt|;
block|}
name|buf
operator|.
name|append
argument_list|(
literal|"]"
argument_list|)
expr_stmt|;
block|}
return|return
name|buf
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

