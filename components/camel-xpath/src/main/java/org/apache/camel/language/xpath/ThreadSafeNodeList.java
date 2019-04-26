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
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
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
name|support
operator|.
name|builder
operator|.
name|xml
operator|.
name|XMLConverterHelper
import|;
end_import

begin_comment
comment|/**  * A simple thread-safe {@link NodeList} that is used by XPathBuilder  * to return thread-safe {@link NodeList} instances as its result.  *<p/>  * This is needed to ensure that end users do not hit any concurrency issues while working  * with xpath expressions using built-in from the JDK or via camel-saxon.  */
end_comment

begin_class
DECL|class|ThreadSafeNodeList
class|class
name|ThreadSafeNodeList
implements|implements
name|NodeList
block|{
DECL|field|list
specifier|private
specifier|final
name|List
argument_list|<
name|Node
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|ThreadSafeNodeList (NodeList source)
specifier|public
name|ThreadSafeNodeList
parameter_list|(
name|NodeList
name|source
parameter_list|)
throws|throws
name|Exception
block|{
name|init
argument_list|(
name|source
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|item (int index)
specifier|public
name|Node
name|item
parameter_list|(
name|int
name|index
parameter_list|)
block|{
return|return
name|list
operator|.
name|get
argument_list|(
name|index
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getLength ()
specifier|public
name|int
name|getLength
parameter_list|()
block|{
return|return
name|list
operator|.
name|size
argument_list|()
return|;
block|}
DECL|method|init (NodeList source)
specifier|private
name|void
name|init
parameter_list|(
name|NodeList
name|source
parameter_list|)
throws|throws
name|Exception
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|source
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|node
init|=
name|source
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|node
operator|!=
literal|null
condition|)
block|{
comment|// import node must not occur concurrent on the same node (must be its owner)
comment|// so we need to synchronize on it
synchronized|synchronized
init|(
name|node
operator|.
name|getOwnerDocument
argument_list|()
init|)
block|{
name|Document
name|doc
init|=
operator|new
name|XMLConverterHelper
argument_list|()
operator|.
name|createDocument
argument_list|()
decl_stmt|;
comment|// import node must not occur concurrent on the same node (must be its owner)
comment|// so we need to synchronize on it
synchronized|synchronized
init|(
name|node
operator|.
name|getOwnerDocument
argument_list|()
init|)
block|{
name|Node
name|clone
init|=
name|doc
operator|.
name|importNode
argument_list|(
name|node
argument_list|,
literal|true
argument_list|)
decl_stmt|;
if|if
condition|(
name|clone
operator|instanceof
name|Text
condition|)
block|{
comment|// basic text node then add as-is
name|list
operator|.
name|add
argument_list|(
name|clone
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// more complex node, then add as child (yes its a bit weird but this is working)
name|doc
operator|.
name|appendChild
argument_list|(
name|clone
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|doc
operator|.
name|getChildNodes
argument_list|()
operator|.
name|item
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
block|}
block|}
end_class

end_unit
