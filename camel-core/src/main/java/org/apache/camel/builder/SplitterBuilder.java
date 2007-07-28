begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
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
name|Expression
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
name|Processor
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
name|processor
operator|.
name|Splitter
import|;
end_import

begin_comment
comment|/**  * A builder for the<a href="http://activemq.apache.org/camel/splitter.html">Splitter</a> pattern  * where an expression is evaluated to iterate through each of the parts of a message and then each part is then send to some endpoint.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|SplitterBuilder
specifier|public
class|class
name|SplitterBuilder
extends|extends
name|FromBuilder
block|{
DECL|field|expression
specifier|private
specifier|final
name|Expression
name|expression
decl_stmt|;
DECL|method|SplitterBuilder (FromBuilder parent, Expression expression)
specifier|public
name|SplitterBuilder
parameter_list|(
name|FromBuilder
name|parent
parameter_list|,
name|Expression
name|expression
parameter_list|)
block|{
name|super
argument_list|(
name|parent
argument_list|)
expr_stmt|;
name|this
operator|.
name|expression
operator|=
name|expression
expr_stmt|;
block|}
DECL|method|createProcessor ()
specifier|public
name|Processor
name|createProcessor
parameter_list|()
throws|throws
name|Exception
block|{
comment|// lets create a single processor for all child predicates
name|Processor
name|destination
init|=
name|super
operator|.
name|createProcessor
argument_list|()
decl_stmt|;
return|return
operator|new
name|Splitter
argument_list|(
name|expression
argument_list|,
name|destination
argument_list|)
return|;
block|}
block|}
end_class

end_unit

