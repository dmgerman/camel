begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
package|;
end_package

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
name|validation
operator|.
name|ValidatingProcessor
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

begin_comment
comment|/**  * Unit test of ValidatingProcessor.  */
end_comment

begin_class
DECL|class|ValidatingProcessorFromUrlTest
specifier|public
class|class
name|ValidatingProcessorFromUrlTest
extends|extends
name|ValidatingProcessorTest
block|{
annotation|@
name|Override
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|validating
operator|=
operator|new
name|ValidatingProcessor
argument_list|()
expr_stmt|;
name|URL
name|url
init|=
name|ObjectHelper
operator|.
name|loadResourceAsURL
argument_list|(
literal|"org/apache/camel/processor/ValidatingProcessor.xsd"
argument_list|)
decl_stmt|;
name|validating
operator|.
name|setSchemaUrl
argument_list|(
name|url
argument_list|)
expr_stmt|;
comment|// loading scheme can be forced so lets try it
name|validating
operator|.
name|loadSchema
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|Test
DECL|method|testValidatingOptions ()
specifier|public
name|void
name|testValidatingOptions
parameter_list|()
throws|throws
name|Exception
block|{
name|assertNotNull
argument_list|(
name|validating
operator|.
name|getErrorHandler
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|validating
operator|.
name|getSchema
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|validating
operator|.
name|getSchemaFactory
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|validating
operator|.
name|getSchemaFile
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|validating
operator|.
name|getSchemaLanguage
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|validating
operator|.
name|getSchemaUrl
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|assertNotNull
argument_list|(
name|validating
operator|.
name|getSchemaSource
argument_list|()
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
comment|// expected
block|}
block|}
block|}
end_class

end_unit

