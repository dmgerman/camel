begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.freemarker
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|freemarker
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

begin_comment
comment|/**  * Freemarker unit test  */
end_comment

begin_class
DECL|class|FreemarkerTest
specifier|public
class|class
name|FreemarkerTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testFreemarkerLetter ()
specifier|public
name|void
name|testFreemarkerLetter
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|DataHandler
name|dataHandler
init|=
operator|new
name|DataHandler
argument_list|(
literal|"my attachment"
argument_list|,
literal|"text/plain"
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
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
annotation|@
name|Override
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
name|getIn
argument_list|()
operator|.
name|addAttachment
argument_list|(
literal|"item"
argument_list|,
name|dataHandler
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Monday"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"name"
argument_list|,
literal|"Christian"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
literal|"item"
argument_list|,
literal|"7"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Dear Christian. You ordered item 7 on Monday."
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Christian"
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|dataHandler
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getAttachment
argument_list|(
literal|"item"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testFreemarkerDataModel ()
specifier|public
name|void
name|testFreemarkerDataModel
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
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
annotation|@
name|Override
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
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"name"
argument_list|,
literal|"Christian"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|variableMap
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headersMap
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|headersMap
operator|.
name|put
argument_list|(
literal|"name"
argument_list|,
literal|"Willem"
argument_list|)
expr_stmt|;
name|variableMap
operator|.
name|put
argument_list|(
literal|"headers"
argument_list|,
name|headersMap
argument_list|)
expr_stmt|;
name|variableMap
operator|.
name|put
argument_list|(
literal|"body"
argument_list|,
literal|"Monday"
argument_list|)
expr_stmt|;
name|variableMap
operator|.
name|put
argument_list|(
literal|"exchange"
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|FreemarkerConstants
operator|.
name|FREEMARKER_DATA_MODLE
argument_list|,
name|variableMap
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
literal|"item"
argument_list|,
literal|"7"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Dear Willem. You ordered item 7 on Monday."
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Christian"
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
comment|// START SNIPPET: example
name|from
argument_list|(
literal|"direct:a"
argument_list|)
operator|.
name|to
argument_list|(
literal|"freemarker:org/apache/camel/component/freemarker/example.ftl"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: example
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

