begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.validation
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|validation
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|sax
operator|.
name|SAXResult
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|validation
operator|.
name|Schema
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|validation
operator|.
name|Validator
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|validation
operator|.
name|ValidatorHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|Locator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|SAXParseException
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
name|impl
operator|.
name|DefaultExchange
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
comment|/**  * @version   */
end_comment

begin_class
DECL|class|DefaultValidationErrorHandlerTest
specifier|public
class|class
name|DefaultValidationErrorHandlerTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testWarning ()
specifier|public
name|void
name|testWarning
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultValidationErrorHandler
name|eh
init|=
operator|new
name|DefaultValidationErrorHandler
argument_list|()
decl_stmt|;
name|eh
operator|.
name|warning
argument_list|(
operator|new
name|SAXParseException
argument_list|(
literal|"foo"
argument_list|,
name|createLocator
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
comment|// just a warning so should be valid
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|eh
operator|.
name|isValid
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testError ()
specifier|public
name|void
name|testError
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultValidationErrorHandler
name|eh
init|=
operator|new
name|DefaultValidationErrorHandler
argument_list|()
decl_stmt|;
name|eh
operator|.
name|error
argument_list|(
operator|new
name|SAXParseException
argument_list|(
literal|"foo"
argument_list|,
name|createLocator
argument_list|(
literal|3
argument_list|,
literal|5
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|eh
operator|.
name|isValid
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testFatalError ()
specifier|public
name|void
name|testFatalError
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultValidationErrorHandler
name|eh
init|=
operator|new
name|DefaultValidationErrorHandler
argument_list|()
decl_stmt|;
name|eh
operator|.
name|fatalError
argument_list|(
operator|new
name|SAXParseException
argument_list|(
literal|"foo"
argument_list|,
name|createLocator
argument_list|(
literal|5
argument_list|,
literal|8
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|eh
operator|.
name|isValid
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testReset ()
specifier|public
name|void
name|testReset
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultValidationErrorHandler
name|eh
init|=
operator|new
name|DefaultValidationErrorHandler
argument_list|()
decl_stmt|;
name|eh
operator|.
name|fatalError
argument_list|(
operator|new
name|SAXParseException
argument_list|(
literal|"foo"
argument_list|,
name|createLocator
argument_list|(
literal|5
argument_list|,
literal|8
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|eh
operator|.
name|isValid
argument_list|()
argument_list|)
expr_stmt|;
name|eh
operator|.
name|reset
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|eh
operator|.
name|isValid
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testHandleErrors ()
specifier|public
name|void
name|testHandleErrors
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultValidationErrorHandler
name|eh
init|=
operator|new
name|DefaultValidationErrorHandler
argument_list|()
decl_stmt|;
name|eh
operator|.
name|error
argument_list|(
operator|new
name|SAXParseException
argument_list|(
literal|"foo"
argument_list|,
name|createLocator
argument_list|(
literal|3
argument_list|,
literal|5
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|eh
operator|.
name|error
argument_list|(
operator|new
name|SAXParseException
argument_list|(
literal|"bar"
argument_list|,
name|createLocator
argument_list|(
literal|9
argument_list|,
literal|12
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|eh
operator|.
name|fatalError
argument_list|(
operator|new
name|SAXParseException
argument_list|(
literal|"cheese"
argument_list|,
name|createLocator
argument_list|(
literal|13
argument_list|,
literal|17
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|eh
operator|.
name|isValid
argument_list|()
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
try|try
block|{
name|eh
operator|.
name|handleErrors
argument_list|(
name|exchange
argument_list|,
name|createScheme
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
name|SchemaValidationException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|e
operator|.
name|getErrors
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|e
operator|.
name|getFatalErrors
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|e
operator|.
name|getWarnings
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|e
operator|.
name|getSchema
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|e
operator|.
name|getExchange
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"Validation failed for: org.apache.camel.processor.validation.DefaultValidationErrorHandlerTest"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"fatal errors: ["
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"org.xml.sax.SAXParseException: cheese, Line : 13, Column : 17"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"errors: ["
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"org.xml.sax.SAXParseException: foo, Line : 3, Column : 5"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"org.xml.sax.SAXParseException: bar, Line : 9, Column : 12"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Exchange[]"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testHandleErrorsResult ()
specifier|public
name|void
name|testHandleErrorsResult
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultValidationErrorHandler
name|eh
init|=
operator|new
name|DefaultValidationErrorHandler
argument_list|()
decl_stmt|;
name|eh
operator|.
name|error
argument_list|(
operator|new
name|SAXParseException
argument_list|(
literal|"foo"
argument_list|,
name|createLocator
argument_list|(
literal|3
argument_list|,
literal|5
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|eh
operator|.
name|error
argument_list|(
operator|new
name|SAXParseException
argument_list|(
literal|"bar"
argument_list|,
name|createLocator
argument_list|(
literal|9
argument_list|,
literal|12
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|eh
operator|.
name|isValid
argument_list|()
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
try|try
block|{
name|eh
operator|.
name|handleErrors
argument_list|(
name|exchange
argument_list|,
name|createScheme
argument_list|()
argument_list|,
operator|new
name|SAXResult
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
name|SchemaValidationException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|e
operator|.
name|getErrors
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|e
operator|.
name|getFatalErrors
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|e
operator|.
name|getWarnings
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|e
operator|.
name|getSchema
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|e
operator|.
name|getExchange
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"Validation failed for: org.apache.camel.processor.validation.DefaultValidationErrorHandlerTest"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"errors: ["
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"org.xml.sax.SAXParseException: foo, Line : 3, Column : 5"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"org.xml.sax.SAXParseException: bar, Line : 9, Column : 12"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Exchange[]"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createScheme ()
specifier|private
name|Schema
name|createScheme
parameter_list|()
block|{
return|return
operator|new
name|Schema
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Validator
name|newValidator
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|ValidatorHandler
name|newValidatorHandler
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
block|}
return|;
block|}
DECL|method|createLocator (final int line, final int column)
specifier|private
name|Locator
name|createLocator
parameter_list|(
specifier|final
name|int
name|line
parameter_list|,
specifier|final
name|int
name|column
parameter_list|)
block|{
return|return
operator|new
name|Locator
argument_list|()
block|{
specifier|public
name|String
name|getSystemId
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|String
name|getPublicId
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|int
name|getLineNumber
parameter_list|()
block|{
return|return
name|line
return|;
block|}
specifier|public
name|int
name|getColumnNumber
parameter_list|()
block|{
return|return
name|column
return|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

