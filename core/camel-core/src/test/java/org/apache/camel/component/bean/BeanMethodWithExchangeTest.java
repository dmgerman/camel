begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.bean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|bean
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|activation
operator|.
name|DataHandler
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|activation
operator|.
name|FileDataSource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|Context
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
name|Attachment
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
name|builder
operator|.
name|RouteBuilder
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
name|DefaultAttachment
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
name|jndi
operator|.
name|JndiContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|BeanMethodWithExchangeTest
specifier|public
class|class
name|BeanMethodWithExchangeTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testBeanWithAnnotationAndExchangeTest ()
specifier|public
name|void
name|testBeanWithAnnotationAndExchangeTest
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|result
init|=
name|template
operator|.
name|request
argument_list|(
literal|"direct:start1"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
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
name|Message
name|m
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|m
operator|.
name|addAttachment
argument_list|(
literal|"attachment"
argument_list|,
operator|new
name|DataHandler
argument_list|(
operator|new
name|FileDataSource
argument_list|(
literal|"src/test/org/apache/camel/component/bean/BeanWithAttachmentAnnotationTest.java"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|getOut
argument_list|()
operator|.
name|getAttachmentObjects
argument_list|()
operator|.
name|containsKey
argument_list|(
literal|"attachment2"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|getOut
argument_list|()
operator|.
name|getAttachments
argument_list|()
operator|.
name|containsKey
argument_list|(
literal|"attachment1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"attachmentValue1"
argument_list|,
name|result
operator|.
name|getOut
argument_list|()
operator|.
name|getAttachmentObjects
argument_list|()
operator|.
name|get
argument_list|(
literal|"attachment1"
argument_list|)
operator|.
name|getHeader
argument_list|(
literal|"attachmentHeader1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|result
operator|.
name|getOut
argument_list|()
operator|.
name|getAttachments
argument_list|()
operator|.
name|containsKey
argument_list|(
literal|"attachment"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|createJndiContext ()
specifier|protected
name|Context
name|createJndiContext
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiContext
name|answer
init|=
operator|new
name|JndiContext
argument_list|()
decl_stmt|;
name|answer
operator|.
name|bind
argument_list|(
literal|"processor"
argument_list|,
operator|new
name|AttachmentProcessor
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:start1"
argument_list|)
operator|.
name|to
argument_list|(
literal|"bean:processor"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|AttachmentProcessor
specifier|public
specifier|static
class|class
name|AttachmentProcessor
block|{
DECL|method|doSomething (Exchange exchange)
specifier|public
name|void
name|doSomething
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Attachment
name|att
init|=
operator|new
name|DefaultAttachment
argument_list|(
operator|new
name|FileDataSource
argument_list|(
literal|"src/test/org/apache/camel/component/bean/BeanMethodWithExchangeTest.java"
argument_list|)
argument_list|)
decl_stmt|;
name|att
operator|.
name|addHeader
argument_list|(
literal|"attachmentHeader1"
argument_list|,
literal|"attachmentValue1"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|addAttachmentObject
argument_list|(
literal|"attachment1"
argument_list|,
name|att
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|addAttachment
argument_list|(
literal|"attachment2"
argument_list|,
operator|new
name|DataHandler
argument_list|(
operator|new
name|FileDataSource
argument_list|(
literal|"src/test/org/apache/camel/component/bean/BeanMethodWithExchangeTest.java"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

