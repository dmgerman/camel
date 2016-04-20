begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.cdi.rule
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|cdi
operator|.
name|rule
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
name|hamcrest
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|org
operator|.
name|hamcrest
operator|.
name|Matchers
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|rules
operator|.
name|RuleChain
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|rules
operator|.
name|TestRule
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|Description
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|model
operator|.
name|Statement
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|allOf
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|anyOf
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|Matchers
operator|.
name|hasItem
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertThat
import|;
end_import

begin_class
DECL|class|ExpectedDeploymentException
specifier|public
specifier|final
class|class
name|ExpectedDeploymentException
implements|implements
name|TestRule
block|{
DECL|field|exceptions
specifier|private
specifier|final
name|List
argument_list|<
name|Matcher
argument_list|<
name|Throwable
argument_list|>
argument_list|>
name|exceptions
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|messages
specifier|private
specifier|final
name|List
argument_list|<
name|Matcher
argument_list|<
name|String
argument_list|>
argument_list|>
name|messages
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|log
specifier|private
specifier|final
name|LogVerifier
name|log
init|=
operator|new
name|LogVerifier
argument_list|()
decl_stmt|;
DECL|field|chain
specifier|private
specifier|final
name|TestRule
name|chain
decl_stmt|;
DECL|method|ExpectedDeploymentException ()
specifier|private
name|ExpectedDeploymentException
parameter_list|()
block|{
name|chain
operator|=
name|RuleChain
operator|.
name|outerRule
argument_list|(
name|log
argument_list|)
operator|.
name|around
argument_list|(
parameter_list|(
name|base
parameter_list|,
name|description
parameter_list|)
lambda|->
operator|new
name|Statement
argument_list|()
block|{
block|@Override                 public void evaluate(
argument_list|)
throws|throws
name|Throwable
block|{
try|try
block|{
name|base
operator|.
name|evaluate
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|exception
parameter_list|)
block|{
name|assertThat
argument_list|(
name|exception
argument_list|,
name|allOf
argument_list|(
name|pecs
argument_list|(
name|exceptions
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
comment|// OpenWebBeans logs the deployment exception details
comment|// TODO: OpenWebBeans only log the root cause of exception thrown in producer methods
comment|//assertThat(log.getMessages(), containsInRelativeOrder(pecs(messages)))
name|assertThat
argument_list|(
name|log
operator|.
name|getMessages
argument_list|()
argument_list|,
name|anyOf
argument_list|(
name|hasItems
argument_list|(
name|messages
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AssertionError
name|error
parameter_list|)
block|{
comment|// Weld stores the deployment exception details in the exception message
name|assertThat
argument_list|(
name|exception
operator|.
name|getMessage
argument_list|()
argument_list|,
name|allOf
argument_list|(
name|pecs
argument_list|(
name|messages
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

begin_empty_stmt
unit|)
empty_stmt|;
end_empty_stmt

begin_function
unit|}      public
DECL|method|none ()
specifier|static
name|ExpectedDeploymentException
name|none
parameter_list|()
block|{
return|return
operator|new
name|ExpectedDeploymentException
argument_list|()
return|;
block|}
end_function

begin_function
DECL|method|expect (Class<? extends Throwable> type)
specifier|public
name|ExpectedDeploymentException
name|expect
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|Throwable
argument_list|>
name|type
parameter_list|)
block|{
name|exceptions
operator|.
name|add
argument_list|(
name|Matchers
operator|.
name|instanceOf
argument_list|(
name|type
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
end_function

begin_function
DECL|method|expectMessage (Matcher<String> matcher)
specifier|public
name|ExpectedDeploymentException
name|expectMessage
parameter_list|(
name|Matcher
argument_list|<
name|String
argument_list|>
name|matcher
parameter_list|)
block|{
name|messages
operator|.
name|add
argument_list|(
name|matcher
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
end_function

begin_function
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|,
literal|"rawtypes"
block|}
argument_list|)
DECL|method|pecs (List<Matcher<T>> matchers)
specifier|private
parameter_list|<
name|T
parameter_list|>
name|List
argument_list|<
name|Matcher
argument_list|<
name|?
super|super
name|T
argument_list|>
argument_list|>
name|pecs
parameter_list|(
name|List
argument_list|<
name|Matcher
argument_list|<
name|T
argument_list|>
argument_list|>
name|matchers
parameter_list|)
block|{
return|return
operator|new
name|ArrayList
argument_list|<>
argument_list|(
operator|(
name|List
operator|)
name|matchers
argument_list|)
return|;
block|}
end_function

begin_function
DECL|method|hasItems (List<Matcher<T>> matchers)
specifier|private
parameter_list|<
name|T
parameter_list|>
name|Matcher
argument_list|<
name|Iterable
argument_list|<
name|?
super|super
name|T
argument_list|>
argument_list|>
index|[]
name|hasItems
parameter_list|(
name|List
argument_list|<
name|Matcher
argument_list|<
name|T
argument_list|>
argument_list|>
name|matchers
parameter_list|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Matcher
argument_list|<
name|Iterable
argument_list|<
name|?
super|super
name|T
argument_list|>
argument_list|>
index|[]
name|items
init|=
operator|new
name|Matcher
index|[
name|matchers
operator|.
name|size
argument_list|()
index|]
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
name|items
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|items
index|[
name|i
index|]
operator|=
name|hasItem
argument_list|(
name|matchers
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|items
return|;
block|}
end_function

begin_function
annotation|@
name|Override
DECL|method|apply (Statement base, Description description)
specifier|public
name|Statement
name|apply
parameter_list|(
name|Statement
name|base
parameter_list|,
name|Description
name|description
parameter_list|)
block|{
return|return
name|chain
operator|.
name|apply
argument_list|(
name|base
argument_list|,
name|description
argument_list|)
return|;
block|}
end_function

unit|}
end_unit

