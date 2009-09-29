begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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
name|ContextTestSupport
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

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|ExpressionAdapterTest
specifier|public
class|class
name|ExpressionAdapterTest
extends|extends
name|ContextTestSupport
block|{
DECL|class|MyExpression
specifier|private
class|class
name|MyExpression
extends|extends
name|ExpressionAdapter
block|{
DECL|method|assertionFailureMessage (Exchange exchange)
specifier|protected
name|String
name|assertionFailureMessage
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
literal|"foo"
return|;
block|}
DECL|method|evaluate (Exchange exchange, Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|evaluate
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|String
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"Kabom"
operator|.
name|equals
argument_list|(
name|in
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
operator|(
name|T
operator|)
name|in
return|;
block|}
block|}
DECL|method|testExpressionAdapter ()
specifier|public
name|void
name|testExpressionAdapter
parameter_list|()
throws|throws
name|Exception
block|{
name|MyExpression
name|my
init|=
operator|new
name|MyExpression
argument_list|()
decl_stmt|;
name|Exchange
name|e
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|e
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
name|my
operator|.
name|assertMatches
argument_list|(
literal|"bar"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
DECL|method|testExpressionAdapterFail ()
specifier|public
name|void
name|testExpressionAdapterFail
parameter_list|()
throws|throws
name|Exception
block|{
name|MyExpression
name|my
init|=
operator|new
name|MyExpression
argument_list|()
decl_stmt|;
name|Exchange
name|e
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|e
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Kabom"
argument_list|)
expr_stmt|;
try|try
block|{
name|my
operator|.
name|assertMatches
argument_list|(
literal|"damn"
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AssertionError
name|ae
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|ae
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

