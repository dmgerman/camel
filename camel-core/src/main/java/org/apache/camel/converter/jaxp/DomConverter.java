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

begin_comment
comment|/**  * Converts from some DOM types to Java types  *  * @version $Revision$  */
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
DECL|method|DomConverter ()
specifier|private
name|DomConverter
parameter_list|()
block|{
comment|// Utility Class
block|}
annotation|@
name|Converter
DECL|method|toString (NodeList nodeList)
specifier|public
specifier|static
name|String
name|toString
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
return|return
name|buffer
operator|.
name|toString
argument_list|()
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

