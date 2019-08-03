begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.velocity
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|velocity
package|;
end_package

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
name|InvalidPayloadException
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
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
DECL|class|VelocityTemplateInHeaderTest
specifier|public
class|class
name|VelocityTemplateInHeaderTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testReceivesFooResponse ()
specifier|public
name|void
name|testReceivesFooResponse
parameter_list|()
throws|throws
name|Exception
block|{
name|assertRespondsWith
argument_list|(
literal|"cheese"
argument_list|,
literal|"foo"
argument_list|,
literal|"<hello>foo</hello>"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testReceivesBarResponse ()
specifier|public
name|void
name|testReceivesBarResponse
parameter_list|()
throws|throws
name|Exception
block|{
name|assertRespondsWith
argument_list|(
literal|"cheese"
argument_list|,
literal|"bar"
argument_list|,
literal|"<hello>bar</hello>"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRespectHeaderNamesUpperCase ()
specifier|public
name|void
name|testRespectHeaderNamesUpperCase
parameter_list|()
throws|throws
name|Exception
block|{
name|assertRespondsWith
argument_list|(
literal|"Cheese"
argument_list|,
literal|"bar"
argument_list|,
literal|"<hello>bar</hello>"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRespectHeaderNamesCamelCase ()
specifier|public
name|void
name|testRespectHeaderNamesCamelCase
parameter_list|()
throws|throws
name|Exception
block|{
name|assertRespondsWith
argument_list|(
literal|"CorrelationID"
argument_list|,
literal|"bar"
argument_list|,
literal|"<hello>bar</hello>"
argument_list|)
expr_stmt|;
block|}
DECL|method|assertRespondsWith (final String headerName, final String headerValue, String expectedBody)
specifier|protected
name|void
name|assertRespondsWith
parameter_list|(
specifier|final
name|String
name|headerName
parameter_list|,
specifier|final
name|String
name|headerValue
parameter_list|,
name|String
name|expectedBody
parameter_list|)
throws|throws
name|InvalidPayloadException
block|{
name|Exchange
name|response
init|=
name|template
operator|.
name|request
argument_list|(
literal|"direct:a"
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
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
name|VelocityConstants
operator|.
name|VELOCITY_TEMPLATE
argument_list|,
literal|"<hello>${headers."
operator|+
name|headerName
operator|+
literal|"}</hello>"
argument_list|)
expr_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
name|headerName
argument_list|,
name|headerValue
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertOutMessageBodyEquals
argument_list|(
name|response
argument_list|,
name|expectedBody
argument_list|)
expr_stmt|;
name|Object
name|template
init|=
name|response
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
name|VelocityConstants
operator|.
name|VELOCITY_TEMPLATE
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
literal|"Template header should have been removed"
argument_list|,
name|template
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|entrySet
init|=
name|response
operator|.
name|getOut
argument_list|()
operator|.
name|getHeaders
argument_list|()
operator|.
name|entrySet
argument_list|()
decl_stmt|;
name|boolean
name|keyFound
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|entrySet
control|)
block|{
if|if
condition|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|equals
argument_list|(
name|headerName
argument_list|)
condition|)
block|{
name|keyFound
operator|=
literal|true
expr_stmt|;
block|}
block|}
name|assertTrue
argument_list|(
literal|"Header should been found"
argument_list|,
name|keyFound
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
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
block|{
name|from
argument_list|(
literal|"direct:a"
argument_list|)
operator|.
name|to
argument_list|(
literal|"velocity://dummy"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

