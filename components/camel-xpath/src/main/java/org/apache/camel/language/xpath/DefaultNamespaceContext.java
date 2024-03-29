begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.language.xpath
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
operator|.
name|xpath
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
name|HashSet
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
operator|.
name|Entry
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
name|namespace
operator|.
name|NamespaceContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathFactory
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
name|NamespaceAware
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
name|CastUtils
import|;
end_import

begin_comment
comment|/**  * An implementation of {@link NamespaceContext} which uses a simple Map where  * the keys are the prefixes and the values are the URIs  */
end_comment

begin_class
DECL|class|DefaultNamespaceContext
specifier|public
class|class
name|DefaultNamespaceContext
implements|implements
name|NamespaceContext
implements|,
name|NamespaceAware
block|{
DECL|field|map
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
decl_stmt|;
DECL|field|parent
specifier|private
specifier|final
name|NamespaceContext
name|parent
decl_stmt|;
DECL|method|DefaultNamespaceContext ()
specifier|public
name|DefaultNamespaceContext
parameter_list|()
block|{
name|this
argument_list|(
name|XPathFactory
operator|.
name|newInstance
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|DefaultNamespaceContext (XPathFactory factory)
specifier|public
name|DefaultNamespaceContext
parameter_list|(
name|XPathFactory
name|factory
parameter_list|)
block|{
name|this
operator|.
name|parent
operator|=
name|factory
operator|.
name|newXPath
argument_list|()
operator|.
name|getNamespaceContext
argument_list|()
expr_stmt|;
name|this
operator|.
name|map
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
DECL|method|DefaultNamespaceContext (NamespaceContext parent, Map<String, String> map)
specifier|public
name|DefaultNamespaceContext
parameter_list|(
name|NamespaceContext
name|parent
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
parameter_list|)
block|{
name|this
operator|.
name|parent
operator|=
name|parent
expr_stmt|;
name|this
operator|.
name|map
operator|=
name|map
expr_stmt|;
block|}
comment|/**      * A helper method to make it easy to create newly populated instances      */
DECL|method|add (String prefix, String uri)
specifier|public
name|DefaultNamespaceContext
name|add
parameter_list|(
name|String
name|prefix
parameter_list|,
name|String
name|uri
parameter_list|)
block|{
name|map
operator|.
name|put
argument_list|(
name|prefix
argument_list|,
name|uri
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|getNamespaceURI (String prefix)
specifier|public
name|String
name|getNamespaceURI
parameter_list|(
name|String
name|prefix
parameter_list|)
block|{
name|String
name|answer
init|=
name|map
operator|.
name|get
argument_list|(
name|prefix
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
operator|&&
name|parent
operator|!=
literal|null
condition|)
block|{
return|return
name|parent
operator|.
name|getNamespaceURI
argument_list|(
name|prefix
argument_list|)
return|;
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|getPrefix (String namespaceURI)
specifier|public
name|String
name|getPrefix
parameter_list|(
name|String
name|namespaceURI
parameter_list|)
block|{
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|namespaceURI
operator|.
name|equals
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|entry
operator|.
name|getKey
argument_list|()
return|;
block|}
block|}
if|if
condition|(
name|parent
operator|!=
literal|null
condition|)
block|{
return|return
name|parent
operator|.
name|getPrefix
argument_list|(
name|namespaceURI
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|getPrefixes (String namespaceURI)
specifier|public
name|Iterator
argument_list|<
name|String
argument_list|>
name|getPrefixes
parameter_list|(
name|String
name|namespaceURI
parameter_list|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|set
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|namespaceURI
operator|.
name|equals
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
condition|)
block|{
name|set
operator|.
name|add
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|parent
operator|!=
literal|null
condition|)
block|{
name|Iterator
argument_list|<
name|String
argument_list|>
name|iter
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|parent
operator|.
name|getPrefixes
argument_list|(
name|namespaceURI
argument_list|)
argument_list|)
decl_stmt|;
while|while
condition|(
name|iter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|set
operator|.
name|add
argument_list|(
name|iter
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|set
operator|.
name|iterator
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|setNamespaces (Map<String, String> namespaces)
specifier|public
name|void
name|setNamespaces
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaces
parameter_list|)
block|{
name|map
operator|.
name|putAll
argument_list|(
name|namespaces
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getNamespaces ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getNamespaces
parameter_list|()
block|{
return|return
name|map
return|;
block|}
comment|/**      * toString() implementation that outputs the namespace mappings with the following format: "[me: {prefix -> value}, {prefix -> value}], [parent: {prefix -> value}, {prefix -> value}].      * Recurses up the parent's chain.      */
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"[me: "
argument_list|)
decl_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|nsEntry
range|:
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"{"
operator|+
name|nsEntry
operator|.
name|getKey
argument_list|()
operator|+
literal|" -> "
operator|+
name|nsEntry
operator|.
name|getValue
argument_list|()
operator|+
literal|"},"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|map
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// remove the last comma
name|sb
operator|.
name|deleteCharAt
argument_list|(
name|sb
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|"]"
argument_list|)
expr_stmt|;
comment|// Get the parent's namespace mappings
if|if
condition|(
name|parent
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|", [parent: "
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|parent
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"]"
argument_list|)
expr_stmt|;
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

