begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.converter.jaxp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
operator|.
name|jaxp
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
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
name|Iterator
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
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|TransformerException
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
name|Attr
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
name|w3c
operator|.
name|dom
operator|.
name|Node
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
name|NodeList
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
name|Text
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
name|Converter
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
name|Exchange
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
name|IOHelper
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

begin_comment
comment|/**  * Converts from some DOM types to Java types  *  * @version   */
end_comment

begin_class
annotation|@
name|Converter
DECL|class|DomConverter
specifier|public
specifier|final
class|class
name|DomConverter
block|{
DECL|field|xml
specifier|private
specifier|final
name|XmlConverter
name|xml
decl_stmt|;
DECL|method|DomConverter ()
specifier|public
name|DomConverter
parameter_list|()
block|{
name|xml
operator|=
operator|new
name|XmlConverter
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Converter
DECL|method|toString (NodeList nodeList, Exchange exchange)
specifier|public
name|String
name|toString
parameter_list|(
name|NodeList
name|nodeList
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|TransformerException
block|{
comment|// converting NodeList to String is more tricky
comment|// sometimes the NodeList is a Node which we can then leverage
comment|// the XML converter to turn into XML incl. tags
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
comment|// use XML converter at first since it preserves tag names
name|boolean
name|found
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|nodeList
operator|instanceof
name|Node
condition|)
block|{
name|Node
name|node
init|=
operator|(
name|Node
operator|)
name|nodeList
decl_stmt|;
name|String
name|s
init|=
name|toString
argument_list|(
name|node
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|s
argument_list|)
condition|)
block|{
name|found
operator|=
literal|true
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// use XML converter at first since it preserves tag names
name|int
name|size
init|=
name|nodeList
operator|.
name|getLength
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|size
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|node
init|=
name|nodeList
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|String
name|s
init|=
name|toString
argument_list|(
name|node
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|s
argument_list|)
condition|)
block|{
name|found
operator|=
literal|true
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// and eventually we must fallback to append without tags, such as when you have
comment|// used an xpath to select an attribute or text() or something
if|if
condition|(
operator|!
name|found
condition|)
block|{
name|append
argument_list|(
name|buffer
argument_list|,
name|nodeList
argument_list|)
expr_stmt|;
block|}
return|return
name|buffer
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|toString (Node node, Exchange exchange)
specifier|private
name|String
name|toString
parameter_list|(
name|Node
name|node
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|TransformerException
block|{
name|String
name|s
decl_stmt|;
if|if
condition|(
name|node
operator|instanceof
name|Text
condition|)
block|{
name|Text
name|textnode
init|=
operator|(
name|Text
operator|)
name|node
decl_stmt|;
name|StringBuilder
name|b
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|b
operator|.
name|append
argument_list|(
name|textnode
operator|.
name|getNodeValue
argument_list|()
argument_list|)
expr_stmt|;
name|textnode
operator|=
operator|(
name|Text
operator|)
name|textnode
operator|.
name|getNextSibling
argument_list|()
expr_stmt|;
while|while
condition|(
name|textnode
operator|!=
literal|null
condition|)
block|{
name|b
operator|.
name|append
argument_list|(
name|textnode
operator|.
name|getNodeValue
argument_list|()
argument_list|)
expr_stmt|;
name|textnode
operator|=
operator|(
name|Text
operator|)
name|textnode
operator|.
name|getNextSibling
argument_list|()
expr_stmt|;
block|}
name|s
operator|=
name|b
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|s
operator|=
name|xml
operator|.
name|toString
argument_list|(
name|node
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
return|return
name|s
return|;
block|}
annotation|@
name|Converter
DECL|method|toInteger (NodeList nodeList)
specifier|public
specifier|static
name|Integer
name|toInteger
parameter_list|(
name|NodeList
name|nodeList
parameter_list|)
block|{
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|append
argument_list|(
name|buffer
argument_list|,
name|nodeList
argument_list|)
expr_stmt|;
name|String
name|s
init|=
name|buffer
operator|.
name|toString
argument_list|()
decl_stmt|;
return|return
name|Integer
operator|.
name|valueOf
argument_list|(
name|s
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toLong (NodeList nodeList)
specifier|public
specifier|static
name|Long
name|toLong
parameter_list|(
name|NodeList
name|nodeList
parameter_list|)
block|{
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|append
argument_list|(
name|buffer
argument_list|,
name|nodeList
argument_list|)
expr_stmt|;
name|String
name|s
init|=
name|buffer
operator|.
name|toString
argument_list|()
decl_stmt|;
return|return
name|Long
operator|.
name|valueOf
argument_list|(
name|s
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toList (NodeList nodeList)
specifier|public
specifier|static
name|List
argument_list|<
name|?
argument_list|>
name|toList
parameter_list|(
name|NodeList
name|nodeList
parameter_list|)
block|{
name|List
argument_list|<
name|Object
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|Object
argument_list|>
name|it
init|=
name|ObjectHelper
operator|.
name|createIterator
argument_list|(
name|nodeList
argument_list|)
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|answer
operator|.
name|add
argument_list|(
name|it
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Converter
DECL|method|toInputStream (NodeList nodeList, Exchange exchange)
specifier|public
name|InputStream
name|toInputStream
parameter_list|(
name|NodeList
name|nodeList
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|TransformerException
throws|,
name|UnsupportedEncodingException
block|{
return|return
operator|new
name|ByteArrayInputStream
argument_list|(
name|toByteArray
argument_list|(
name|nodeList
argument_list|,
name|exchange
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toByteArray (NodeList nodeList, Exchange exchange)
specifier|public
name|byte
index|[]
name|toByteArray
parameter_list|(
name|NodeList
name|nodeList
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|TransformerException
throws|,
name|UnsupportedEncodingException
block|{
name|String
name|data
init|=
name|toString
argument_list|(
name|nodeList
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
return|return
name|data
operator|.
name|getBytes
argument_list|(
name|IOHelper
operator|.
name|getCharsetName
argument_list|(
name|exchange
argument_list|)
argument_list|)
return|;
block|}
DECL|method|append (StringBuilder buffer, NodeList nodeList)
specifier|private
specifier|static
name|void
name|append
parameter_list|(
name|StringBuilder
name|buffer
parameter_list|,
name|NodeList
name|nodeList
parameter_list|)
block|{
name|int
name|size
init|=
name|nodeList
operator|.
name|getLength
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|size
condition|;
name|i
operator|++
control|)
block|{
name|append
argument_list|(
name|buffer
argument_list|,
name|nodeList
operator|.
name|item
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|append (StringBuilder buffer, Node node)
specifier|private
specifier|static
name|void
name|append
parameter_list|(
name|StringBuilder
name|buffer
parameter_list|,
name|Node
name|node
parameter_list|)
block|{
if|if
condition|(
name|node
operator|instanceof
name|Text
condition|)
block|{
name|Text
name|text
init|=
operator|(
name|Text
operator|)
name|node
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|text
operator|.
name|getTextContent
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|node
operator|instanceof
name|Attr
condition|)
block|{
name|Attr
name|attribute
init|=
operator|(
name|Attr
operator|)
name|node
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|attribute
operator|.
name|getTextContent
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|node
operator|instanceof
name|Element
condition|)
block|{
name|Element
name|element
init|=
operator|(
name|Element
operator|)
name|node
decl_stmt|;
name|append
argument_list|(
name|buffer
argument_list|,
name|element
operator|.
name|getChildNodes
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

