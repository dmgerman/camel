begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.converter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
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
name|util
operator|.
name|StopWatch
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|PropertyEditorTypeConverterIssueTest
specifier|public
class|class
name|PropertyEditorTypeConverterIssueTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|testPropertyEditorTypeConverter ()
specifier|public
name|void
name|testPropertyEditorTypeConverter
parameter_list|()
throws|throws
name|Exception
block|{
comment|// test that converters a custom object (MyBean) to a String which causes
comment|// PropertyEditorTypeConverter to be used. And this test times how fast
comment|// this is. As we want to optimize PropertyEditorTypeConverter to be faster
name|MyBean
name|bean
init|=
operator|new
name|MyBean
argument_list|()
decl_stmt|;
name|bean
operator|.
name|setBar
argument_list|(
literal|"Hello"
argument_list|)
expr_stmt|;
name|StopWatch
name|watch
init|=
operator|new
name|StopWatch
argument_list|()
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
literal|500
condition|;
name|i
operator|++
control|)
block|{
name|String
name|s
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|bean
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
name|s
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
name|log
operator|.
name|info
argument_list|(
literal|"Time taken: "
operator|+
name|watch
operator|.
name|taken
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

