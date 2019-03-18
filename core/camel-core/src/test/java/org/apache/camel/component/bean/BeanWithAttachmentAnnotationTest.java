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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

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
name|AttachmentObjects
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
name|Attachments
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
name|ExchangePattern
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
name|component
operator|.
name|mock
operator|.
name|MockEndpoint
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
DECL|class|BeanWithAttachmentAnnotationTest
specifier|public
class|class
name|BeanWithAttachmentAnnotationTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testBeanWithOldAnnotationAndExchangeTest ()
specifier|public
name|void
name|testBeanWithOldAnnotationAndExchangeTest
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"attachment"
argument_list|)
expr_stmt|;
name|template
operator|.
name|send
argument_list|(
literal|"direct:inOld"
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
name|exchange
operator|.
name|setPattern
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
expr_stmt|;
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
name|addAttachmentObject
argument_list|(
literal|"attachment"
argument_list|,
operator|new
name|DefaultAttachment
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
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
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
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"attachment"
argument_list|)
expr_stmt|;
name|template
operator|.
name|send
argument_list|(
literal|"direct:in"
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
name|exchange
operator|.
name|setPattern
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
expr_stmt|;
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
name|addAttachmentObject
argument_list|(
literal|"attachment"
argument_list|,
operator|new
name|DefaultAttachment
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
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
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
literal|"processorOld"
argument_list|,
operator|new
name|AttachmentProcessorOld
argument_list|()
argument_list|)
expr_stmt|;
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
literal|"direct:in"
argument_list|)
operator|.
name|to
argument_list|(
literal|"bean:processor"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:inOld"
argument_list|)
operator|.
name|to
argument_list|(
literal|"bean:processorOld"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|AttachmentProcessorOld
specifier|public
specifier|static
class|class
name|AttachmentProcessorOld
block|{
comment|// START SNIPPET: e1
DECL|method|doSomething (@ttachments Map<String, DataHandler> attachments)
specifier|public
name|String
name|doSomething
parameter_list|(
annotation|@
name|Attachments
name|Map
argument_list|<
name|String
argument_list|,
name|DataHandler
argument_list|>
name|attachments
parameter_list|)
block|{
name|assertNotNull
argument_list|(
name|attachments
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The attache size is wrong"
argument_list|,
literal|1
argument_list|,
name|attachments
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|key
init|=
name|attachments
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|attachments
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
name|DataHandler
name|handler
init|=
name|attachments
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|handler
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"The data source should be a instance of FileDataSource"
argument_list|,
name|handler
operator|.
name|getDataSource
argument_list|()
operator|instanceof
name|FileDataSource
argument_list|)
expr_stmt|;
return|return
name|key
return|;
block|}
comment|// END SNIPPET: e1
block|}
DECL|class|AttachmentProcessor
specifier|public
specifier|static
class|class
name|AttachmentProcessor
block|{
comment|// START SNIPPET: e2
DECL|method|doSomething (@ttachmentObjects Map<String, Attachment> attachments)
specifier|public
name|String
name|doSomething
parameter_list|(
annotation|@
name|AttachmentObjects
name|Map
argument_list|<
name|String
argument_list|,
name|Attachment
argument_list|>
name|attachments
parameter_list|)
block|{
name|assertNotNull
argument_list|(
name|attachments
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The attache size is wrong"
argument_list|,
literal|1
argument_list|,
name|attachments
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|key
init|=
name|attachments
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|attachments
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
name|Attachment
name|attachment
init|=
name|attachments
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|attachment
argument_list|)
expr_stmt|;
name|DataHandler
name|handler
init|=
name|attachment
operator|.
name|getDataHandler
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|handler
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"The data source should be a instance of FileDataSource"
argument_list|,
name|handler
operator|.
name|getDataSource
argument_list|()
operator|instanceof
name|FileDataSource
argument_list|)
expr_stmt|;
return|return
name|key
return|;
block|}
comment|// END SNIPPET: e2
block|}
block|}
end_class

end_unit

