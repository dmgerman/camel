begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ibatis
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ibatis
package|;
end_package

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
name|impl
operator|.
name|DefaultProducer
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
name|com
operator|.
name|ibatis
operator|.
name|sqlmap
operator|.
name|client
operator|.
name|SqlMapClient
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|IBatisProducer
specifier|public
class|class
name|IBatisProducer
extends|extends
name|DefaultProducer
argument_list|<
name|Exchange
argument_list|>
block|{
DECL|field|statement
specifier|private
name|String
name|statement
decl_stmt|;
DECL|field|endpoint
specifier|private
name|IBatisEndpoint
name|endpoint
decl_stmt|;
DECL|method|IBatisProducer (IBatisEndpoint endpoint)
specifier|public
name|IBatisProducer
parameter_list|(
name|IBatisEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|statement
operator|=
name|endpoint
operator|.
name|getStatement
argument_list|()
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|IBatisEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|IBatisEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
comment|/**      * Calls insert on the SqlMapClient.      */
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|SqlMapClient
name|client
init|=
name|endpoint
operator|.
name|getSqlMapClient
argument_list|()
decl_stmt|;
name|Object
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
if|if
condition|(
name|body
operator|==
literal|null
condition|)
block|{
comment|// must be a poll so lets do a query
name|Message
name|msg
init|=
name|exchange
operator|.
name|getOut
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|List
name|list
init|=
name|client
operator|.
name|queryForList
argument_list|(
name|statement
argument_list|)
decl_stmt|;
name|msg
operator|.
name|setBody
argument_list|(
name|list
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
literal|"org.apache.camel.ibatis.queryName"
argument_list|,
name|statement
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// lets handle arrays or collections of objects
name|Iterator
name|iter
init|=
name|ObjectHelper
operator|.
name|createIterator
argument_list|(
name|body
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
name|client
operator|.
name|insert
argument_list|(
name|statement
argument_list|,
name|iter
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

