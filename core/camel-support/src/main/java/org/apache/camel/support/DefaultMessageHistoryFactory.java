begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.support
package|package
name|org
operator|.
name|apache
operator|.
name|camel
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
name|MessageHistory
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
name|NamedNode
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
name|MessageHistoryFactory
import|;
end_import

begin_class
DECL|class|DefaultMessageHistoryFactory
specifier|public
class|class
name|DefaultMessageHistoryFactory
implements|implements
name|MessageHistoryFactory
block|{
DECL|field|copyMessage
specifier|private
name|boolean
name|copyMessage
decl_stmt|;
DECL|field|nodePattern
specifier|private
name|String
name|nodePattern
decl_stmt|;
annotation|@
name|Override
DECL|method|newMessageHistory (String routeId, NamedNode node, long timestamp, Exchange exchange)
specifier|public
name|MessageHistory
name|newMessageHistory
parameter_list|(
name|String
name|routeId
parameter_list|,
name|NamedNode
name|node
parameter_list|,
name|long
name|timestamp
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|nodePattern
operator|!=
literal|null
condition|)
block|{
name|String
name|name
init|=
name|node
operator|.
name|getShortName
argument_list|()
decl_stmt|;
name|String
index|[]
name|parts
init|=
name|nodePattern
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|part
range|:
name|parts
control|)
block|{
name|boolean
name|match
init|=
name|PatternHelper
operator|.
name|matchPattern
argument_list|(
name|name
argument_list|,
name|part
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|match
condition|)
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
name|Message
name|target
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|copyMessage
condition|)
block|{
name|target
operator|=
name|exchange
operator|.
name|getMessage
argument_list|()
operator|.
name|copy
argument_list|()
expr_stmt|;
block|}
return|return
operator|new
name|DefaultMessageHistory
argument_list|(
name|routeId
argument_list|,
name|node
argument_list|,
name|timestamp
argument_list|,
name|target
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|isCopyMessage ()
specifier|public
name|boolean
name|isCopyMessage
parameter_list|()
block|{
return|return
name|copyMessage
return|;
block|}
annotation|@
name|Override
DECL|method|setCopyMessage (boolean copyMessage)
specifier|public
name|void
name|setCopyMessage
parameter_list|(
name|boolean
name|copyMessage
parameter_list|)
block|{
name|this
operator|.
name|copyMessage
operator|=
name|copyMessage
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getNodePattern ()
specifier|public
name|String
name|getNodePattern
parameter_list|()
block|{
return|return
name|nodePattern
return|;
block|}
annotation|@
name|Override
DECL|method|setNodePattern (String nodePattern)
specifier|public
name|void
name|setNodePattern
parameter_list|(
name|String
name|nodePattern
parameter_list|)
block|{
name|this
operator|.
name|nodePattern
operator|=
name|nodePattern
expr_stmt|;
block|}
block|}
end_class

end_unit

